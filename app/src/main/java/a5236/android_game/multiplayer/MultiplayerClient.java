package a5236.android_game.multiplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import a5236.android_game.TitleFragment;
import a5236.android_game.multiplayer.packet.Packet;
import a5236.android_game.multiplayer.packet.PacketCompressor;

@SuppressLint("RestrictedApi")
public class MultiplayerClient {

    private static final String TAG = "MultiplayerClient";

    private static final int RC_WAITING_ROOM = 10002;
    private static final int RC_SIGN_IN = 9001;

    // Client used to sign in with Google APIs
    private GoogleSignInClient mGoogleSignInClient = null;

    // The currently signed in account, used to check the account has changed outside of this activity when resuming.
    private GoogleSignInAccount mSignedInAccount = null;
    private String mPlayerId;

    // Client used to interact with the real time multiplayer system.
    private RealTimeMultiplayerClient mRealTimeMultiplayerClient = null;

    // Room ID where the currently active game is taking place; null if we're
    // not playing.
    private String mRoomId = null;

    // Holds the configuration of the current room.
    private RoomConfig mRoomConfig;

    // The participants in the currently active game
    public ArrayList<Participant> mParticipants = null;

    // My participant ID in the currently active game
    private String mMyId = null;

    private final Activity activity;
    private final TitleFragment titleFragment;

    public MultiplayerClient(TitleFragment titleFragment) {
        this.activity = titleFragment.getActivity();
        this.titleFragment = titleFragment;

        mGoogleSignInClient = GoogleSignIn.getClient(activity, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
    }

    private void updateSignedInAccount() {
        mSignedInAccount = GoogleSignIn.getLastSignedInAccount(activity);
    }

    private boolean isSignedIn() {
        return mSignedInAccount != null;
    }

    public interface SignInCallback {
        void onComplete(boolean success);
    }

    private SignInCallback signInCallback;
    public void signIn(SignInCallback callback) {
        // Check if account is already signed in
        if (isSignedIn()) {
            if (callback != null) {
                callback.onComplete(true);
            }
            return;
        }

        // Start activity for sign in intent
        signInCallback = callback;
        titleFragment.startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    private void onSignedIn(GoogleSignInAccount account) {
        if (mSignedInAccount != account) {
            mSignedInAccount = account;

            mRealTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(activity, account);

            PlayersClient playersClient = Games.getPlayersClient(activity, account);
            playersClient.getCurrentPlayer()
                    .addOnSuccessListener(new OnSuccessListener<Player>() {
                        @Override
                        public void onSuccess(Player player) {
                            mPlayerId = player.getPlayerId();
                        }
                    });
        }
    }

    public interface SignOutCallback {
        void onComplete(boolean success);
    }

    public void signOut(final SignOutCallback callback) {
        // Check if account is already signed out
        updateSignedInAccount();
        if (!isSignedIn()) {
            if (callback != null) {
                callback.onComplete(true);
            }
            return;
        }

        // Attempt to sign out with google sign in client
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mSignedInAccount = null;
                }
                if (callback != null) {
                    callback.onComplete(!isSignedIn());
                }
            }
        });
    }

    public interface JoinGameCallback {
        void onComplete(boolean success);
    }

    private JoinGameCallback joinGameCallback;
    public void joinGame(JoinGameCallback callback) {
        // Ensure account is signed in
        if (!isSignedIn()) {
            if (callback != null) {
                callback.onComplete(false);
            }
            return;
        }

        joinGameCallback = callback;

        final int MIN_OPPONENTS = 1, MAX_OPPONENTS = 1;
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_OPPONENTS, MAX_OPPONENTS, 0);

        keepScreenOn();

        mRoomConfig = RoomConfig.builder(mRoomUpdateCallback)
                .setOnMessageReceivedListener(mOnRealTimeMessageReceivedListener)
                .setRoomStatusUpdateCallback(mRoomStatusUpdateCallback)
                .setAutoMatchCriteria(autoMatchCriteria)
                .build();

        mRealTimeMultiplayerClient.create(mRoomConfig);
    }

    public void leaveGame() {
        if (mRoomId != null) {
            mRealTimeMultiplayerClient.leave(mRoomConfig, mRoomId)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mRoomId = null;
                            mRoomConfig = null;
                            controller = null;
                            hostId = null;
                            players = null;
                            Log.d(TAG, "Left game");
                        }
                    });
        }

        stopKeepingScreenOn();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            if (result.isSuccess()) {
                onSignedIn(result.getSignInAccount());

                new AlertDialog.Builder(activity).setMessage("Signed in!")
                        .setNeutralButton(android.R.string.ok, null).show();
            } else {
                String message = result.getStatus().getStatusMessage();
                if (message == null || message.isEmpty()) {
                    // message = getString(R.string.signin_other_error);
                    message = "Error signing in";
                }
                new AlertDialog.Builder(activity).setMessage(message)
                        .setNeutralButton(android.R.string.ok, null).show();
            }
            signInCallback.onComplete(isSignedIn());
            signInCallback = null;
        } else if (requestCode == RC_WAITING_ROOM) {
            if (resultCode == Activity.RESULT_OK) {
                // ready to start playing
                startGame();
            }
        }
    }

    private RoomStatusUpdateCallback mRoomStatusUpdateCallback = new RoomStatusUpdateCallback() {
        @Override
        public void onConnectedToRoom(@Nullable Room room) {
            mParticipants = room.getParticipants();
            mMyId = room.getParticipantId(mPlayerId);

            if (mRoomId == null) {
                mRoomId = room.getRoomId();
            }
        }

        @Override
        public void onDisconnectedFromRoom(@Nullable Room room) {
            mRoomId = null;
            mRoomConfig = null;
        }

        @Override
        public void onPeerDeclined(@Nullable Room room, @NonNull List<String> list) {
            updateRoom(room);
        }

        @Override
        public void onPeerInvitedToRoom(@Nullable Room room, @NonNull List<String> list) {
            updateRoom(room);
        }

        @Override
        public void onP2PDisconnected(@NonNull String s) {
        }

        @Override
        public void onP2PConnected(@NonNull String s) {
        }

        @Override
        public void onPeerJoined(@Nullable Room room, @NonNull List<String> list) {
            updateRoom(room);
        }

        @Override
        public void onPeerLeft(@Nullable Room room, @NonNull List<String> list) {
            updateRoom(room);
        }

        @Override
        public void onRoomAutoMatching(@Nullable Room room) {
            updateRoom(room);
        }

        @Override
        public void onRoomConnecting(@Nullable Room room) {
            updateRoom(room);
        }

        @Override
        public void onPeersConnected(@Nullable Room room, @NonNull List<String> list) {
            updateRoom(room);
        }

        @Override
        public void onPeersDisconnected(@Nullable Room room, @NonNull List<String> list) {
            updateRoom(room);
        }
    };

    private RoomUpdateCallback mRoomUpdateCallback = new RoomUpdateCallback() {
        @Override
        public void onRoomCreated(int i, @Nullable Room room) {
            if (room == null || i != GamesCallbackStatusCodes.OK) {
                if (joinGameCallback != null) {
                    joinGameCallback.onComplete(false);
                    joinGameCallback = null;
                }
                return;
            }

            mRoomId = room.getRoomId();

            showWaitingRoom(room);
        }

        @Override
        public void onRoomConnected(int i, @Nullable Room room) {
            if (room == null || i != GamesCallbackStatusCodes.OK) {
                if (joinGameCallback != null) {
                    joinGameCallback.onComplete(false);
                    joinGameCallback = null;
                }
                return;
            }

            updateRoom(room);
        }

        @Override
        public void onJoinedRoom(int i, @Nullable Room room) {
            if (room == null || i != GamesCallbackStatusCodes.OK) {
                if (joinGameCallback != null) {
                    joinGameCallback.onComplete(false);
                    joinGameCallback = null;
                }
                return;
            }

            showWaitingRoom(room);
        }

        @Override
        public void onLeftRoom(int i, @NonNull String s) {
            // Switch to main screen?
        }
    };

    private OnRealTimeMessageReceivedListener mOnRealTimeMessageReceivedListener = new OnRealTimeMessageReceivedListener() {
        @Override
        public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
            if (controller != null) {
                try {
                    Packet packet = PacketCompressor.decompress(Packet.fromByteArray(realTimeMessage.getMessageData()));
                    controller.handlePacket(packet);
                } catch (IOException ignored) {
                }
            }
        }
    };

    private void updateRoom(Room room) {
        if (room != null) {
            Log.d(TAG, "Room updated");
            mParticipants = room.getParticipants();
        }
    }

    private void showWaitingRoom(Room room) {
        final int MIN_PLAYERS = Integer.MAX_VALUE;
        mRealTimeMultiplayerClient.getWaitingRoomIntent(room, MIN_PLAYERS)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        // show waiting room UI
                        titleFragment.startActivityForResult(intent, RC_WAITING_ROOM);
                    }
                });
    }

    private GamePlayer controller = null;
    // Id of host participant
    public String hostId = null;
    // Player instances of all participants, including host
    public List<a5236.android_game.Player> players = null;

    private void startGame() {
        Log.d(TAG, "Starting game");

        //switchToScreen(R.id.screen_game);

        // Set game controller (participant 0 after sorting ids becomes the hostId)
        // This isn't the best way but it's fine for our basic purposes
        Collections.sort(mParticipants, new Comparator<Participant>() {
            @Override
            public int compare(Participant p1, Participant p2) {
                return p1.getParticipantId().compareTo(p2.getParticipantId());
            }
        });
        Participant host = mParticipants.get(0);
        hostId = host.getParticipantId();
        players = new ArrayList<>();
        a5236.android_game.Player player = null;
        for (Participant participant : mParticipants) {
            a5236.android_game.Player p;
            if(participant.getParticipantId() == host.getParticipantId()) {
                p = new a5236.android_game.Player(participant.getParticipantId(), participant.getDisplayName(), true);
            }
            else{
                p = new a5236.android_game.Player(participant.getParticipantId(), participant.getDisplayName(), false);
            }
            players.add(p);
            if (p.getParticipantId().equals(mMyId)) {
                player = p;
            }
        }
        if (hostId.equals(mMyId)) {
            controller = new GameHost(this, player, players);
        } else {
            controller = new GamePlayer(this, player, players);
        }

        // run the gameTick() method every second to update the game.
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                controller.gameTick();
                h.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void sendToParticipant(byte[] message, String participantId, RealTimeMultiplayerClient.ReliableMessageSentCallback callback) {
        if (participantId.equals(mMyId)) {
            try {
                Packet packet = PacketCompressor.decompress(Packet.fromByteArray(message));
                controller.handlePacket(packet);
            } catch (IOException ignored) {
            }
        } else {
            Task<Integer> task = mRealTimeMultiplayerClient
                    .sendReliableMessage(message, mRoomId, participantId, callback)
                    .addOnCompleteListener(new OnCompleteListener<Integer>() {
                        @Override
                        public void onComplete(@NonNull Task<Integer> task) {
                            // message sent
                        }
                    });
        }
    }

    void sendToParticipant(Packet packet, String participantId, RealTimeMultiplayerClient.ReliableMessageSentCallback callback) {
        try {
            byte[] message = PacketCompressor.compress(packet).toByteArray();
            sendToParticipant(message, participantId, callback);
        } catch (IOException ignored) {
        }
    }

    void sendToHost(Packet packet, RealTimeMultiplayerClient.ReliableMessageSentCallback callback) {
        sendToParticipant(packet, hostId, callback);
    }

    void sendToPlayers(Packet packet, RealTimeMultiplayerClient.ReliableMessageSentCallback callback) {
        try {
            byte[] message = PacketCompressor.compress(packet).toByteArray();
            for (a5236.android_game.Player player : players) {
                sendToParticipant(message, player.getParticipantId(), callback);
            }
        } catch (IOException ignored) {
        }
    }

    private void keepScreenOn() {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void stopKeepingScreenOn() {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

}
