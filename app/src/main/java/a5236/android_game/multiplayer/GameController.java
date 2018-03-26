package a5236.android_game.multiplayer;

import android.util.Log;

import a5236.android_game.MC_Question;
import a5236.android_game.Player;
import a5236.android_game.multiplayer.packet.Packet;
import a5236.android_game.multiplayer.packet.PacketBuilder;
import a5236.android_game.multiplayer.packet.PacketReader;

import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Participant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GameController {

    private static final String TAG = "GameController";

    protected MultiplayerClient multiplayerClient;
    protected List<Player> players;

    protected static final RealTimeMultiplayerClient.ReliableMessageSentCallback callback = new RealTimeMultiplayerClient.ReliableMessageSentCallback() {
        @Override
        public void onRealTimeMessageSent(int i, int i1, String s) {
        }
    };

    private Map<Short, PacketHandler> packetHandlers = new HashMap<>();

    GameController(MultiplayerClient multiplayerClient) {
        this.multiplayerClient = multiplayerClient;

        players = new ArrayList<>();
        for (Participant participant : multiplayerClient.mParticipants) {
            players.add(new Player(participant.getParticipantId()));
        }

        // Send message
        setPacketHandler(1, new PacketHandler() {
            @Override
            public void handlePacket(PacketReader reader) {
                try {
                    String message = reader.readString();
                    Log.d(TAG, "Received message: " + message);
                } catch (IOException ignored) {
                }
            }
        });
        // Show mini-game wheel fragment
        setPacketHandler(2, new PacketHandler() {
            @Override
            public void handlePacket(PacketReader reader) {
                // Logic to display current round for a few seconds, then show wheel choosing mini-game for the round
                try {
                    int round = (int) reader.readByte();
                    Log.d(TAG, "Showing mini-game wheel for round " + round);
                    // TODO: Show fragment with current round and mini-game wheel
                } catch (IOException ignored) {
                }
            }
        });
        // Show mini-game results / scoreboard fragment
        setPacketHandler(3, new PacketHandler() {
            @Override
            public void handlePacket(PacketReader reader) {
                // TODO: Show scoreboard for mini-game or final results
            }
        });
        // Show multiple choice mini-game
        setPacketHandler(4, new PacketHandler() {
            @Override
            public void handlePacket(PacketReader reader) {
                // Logic to get question and answers to display, and show fragment for player to select answer
                try {
                    String question = reader.readString();
                    String a = reader.readString();
                    String b = reader.readString();
                    String c = reader.readString();
                    String d = reader.readString();
                    String answer = reader.readString();
                    MC_Question mcQuestion = new MC_Question(question, a, b, c, d, answer);
                    Log.d(TAG, "Showing multiple choice mini-game");
                    // TODO: Show fragment with the given question
                } catch (IOException ignored) {
                }
            }
        });
    }

    public interface PacketHandler {
        void handlePacket(PacketReader reader);
    }

    private void setPacketHandler(int id, PacketHandler handler) {
        packetHandlers.put((short) id, handler);
    }

    void handlePacket(Packet packet) {
        PacketReader reader = new PacketReader(packet);
        PacketHandler handler = packetHandlers.get(packet.getPacketID());
        if (handler != null) {
            handler.handlePacket(reader);
        }
    }

    Packet buildMessagePacket(String message) {
        return new PacketBuilder(Packet.PacketType.Request)
                .withID((short) 1)
                .withString(message)
                .build();
    }

    Packet buildMinigameWheelPacket(int round) {
        return new PacketBuilder(Packet.PacketType.Request)
                .withID((short) 2)
                .withByte((byte) round)
                .build();
    }

    Packet buildScoreboardPacket() {
        // TODO: Pass through all players and scores here
        return new PacketBuilder(Packet.PacketType.Request)
                .withID((short) 3)
                .build();
    }

    Packet buildMultipleChoiceMinigamePacket(MC_Question question) {
        return new PacketBuilder(Packet.PacketType.Request)
                .withID((short) 4)
                .withString(question.getQuestion())
                .withString(question.getChoiceA())
                .withString(question.getChoiceB())
                .withString(question.getChoiceC())
                .withString(question.getChoiceD())
                .withString(question.getAnswer())
                .build();
    }

    public void gameTick() {

    }
}
