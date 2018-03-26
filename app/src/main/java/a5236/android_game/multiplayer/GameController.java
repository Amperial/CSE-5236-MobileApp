package a5236.android_game.multiplayer;

import android.util.Log;

import a5236.android_game.Player;
import a5236.android_game.multiplayer.packet.Packet;
import a5236.android_game.multiplayer.packet.PacketBuilder;
import a5236.android_game.multiplayer.packet.PacketReader;

import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Participant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class GameController {

    private static final String TAG = "GameController";

    protected MultiplayerClient multiplayerClient;
    protected List<Player> players;

    protected static final RealTimeMultiplayerClient.ReliableMessageSentCallback callback = new RealTimeMultiplayerClient.ReliableMessageSentCallback() {
        @Override
        public void onRealTimeMessageSent(int i, int i1, String s) {
        }
    };

    GameController(MultiplayerClient multiplayerClient) {
        this.multiplayerClient = multiplayerClient;

        players = new ArrayList<>();
        for (Participant participant : multiplayerClient.mParticipants) {
            players.add(new Player(participant.getParticipantId()));
        }
    }

    public void gameTick() {

    }

    public void handlePacket(Packet packet) {
        PacketReader reader = new PacketReader(packet);
        switch (packet.getPacketID()) {
            case 1:
                handleMessagePacket(reader);
                break;
            default:
        }
    }

    protected Packet buildMessagePacket(String message) {
        return new PacketBuilder(Packet.PacketType.Request)
                .withID((short) 1)
                .withString(message)
                .build();
    }

    private void handleMessagePacket(PacketReader reader) {
        try {
            String message = reader.readString();
            Log.d(TAG, "Received message: " + message);
        } catch (IOException ignored) {
        }
    }

}
