package a5236.android_game.multiplayer;

import android.util.Log;

import a5236.android_game.Player;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;

import java.util.ArrayList;
import java.util.List;

public abstract class GameController {

    private static final String TAG = "GameController";

    protected MultiplayerClient multiplayerClient;
    protected List<Player> players;

    GameController(MultiplayerClient multiplayerClient) {
        this.multiplayerClient = multiplayerClient;

        players = new ArrayList<>();
        for (Participant participant : multiplayerClient.mParticipants) {
            players.add(new Player(participant.getParticipantId()));
        }
    }

    public void gameTick() {

    }

    public void handleMessage(RealTimeMessage message) {
        Log.d(TAG, "Received message: " + new String(message.getMessageData()));
    }

}
