package a5236.android_game.multiplayer;

import android.util.Log;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import a5236.android_game.Player;
import a5236.android_game.multiplayer.minigames.Minigame;
import a5236.android_game.multiplayer.minigames.MultipleChoiceMinigame;
import a5236.android_game.multiplayer.packet.Packet;
import a5236.android_game.multiplayer.packet.PacketReader;

public class GameHost extends GamePlayer {

    private static final String TAG = "GameHost";

    private static final int ROUNDS = 3;

    private int round = 0;
    private boolean roundChosen = false;
    private boolean roundStarted = false;
    private Minigame roundMinigame = null;
    private Set<String> waitingIds = new HashSet<>();

    GameHost(MultiplayerClient multiplayerClient, Player player, List<Player> players) {
        super(multiplayerClient, player, players);

        // Player ready for mini-game
        setPacketHandler(11, new PacketHandler() {
            @Override
            public void handlePacket(PacketReader reader) {
                try {
                    String participantId = reader.readString();
                    waitingIds.remove(participantId);
                    Log.d(TAG, "Received mini-game ready from participantId " + participantId);
                } catch (IOException ignored) {
                }
            }
        });
        // Player answer for multiple choice mini-game question
        setPacketHandler(21, new PacketHandler() {
            @Override
            public void handlePacket(PacketReader reader) {
                try {
                    String participantId = reader.readString();
                    String answer = reader.readString();
                    if (roundMinigame != null && roundMinigame instanceof MultipleChoiceMinigame) {
                        Log.d(TAG, "Received multiple choice answer: " + answer + " from participantId " + participantId);
                        ((MultipleChoiceMinigame) roundMinigame).receiveAnswer(participantId, answer);
                    }
                } catch (IOException ignored) {
                }
            }
        });
        // Player finished with scoreboard
        setPacketHandler(31, new PacketHandler() {
            @Override
            public void handlePacket(PacketReader reader) {
                try {
                    String participantId = reader.readString();
                    waitingIds.remove(participantId);
                    Log.d(TAG, "Received scoreboard continue from participantId " + participantId);
                } catch (IOException ignored) {
                }
            }
        });
    }

    @Override
    public void gameTick() {
        super.gameTick();

        if (!waitingIds.isEmpty()) {
            // Waiting on players to continue for whatever reason
            Log.d(TAG, "Waiting on players to continue");
            return;
        }

        if (roundStarted) { // Round has been started
            if (roundMinigame.isFinished()) {
                Log.d(TAG, "Round finished, showing scoreboard");
                roundChosen = false;
                roundStarted = false;
                roundMinigame = null;

                // Show all players the current scoreboard and wait for them to continue
                waitOnPlayers(buildScoreboardPacket(players));
            } else {
                Log.d(TAG, "Round in progress, ticking minigame");
                roundMinigame.gameTick();
            }
        } else if (roundChosen) { // Round has been chosen but not started
            Log.d(TAG, "Round chosen and ready, now starting");
            roundStarted = true;
        } else { // Round has not been chosen or started
            if (round < ROUNDS) {
                Log.d(TAG, "Choosing next minigame, showing wheel");
                // Increment round number and choose minigame
                round++;
                // TODO: Add more mini-games, ability to select random one
                roundMinigame = new MultipleChoiceMinigame(this);
                roundChosen = true;

                // Show all players the chosen minigame for the next round and wait for them to continue
                waitOnPlayers(buildMinigameWheelPacket(round, roundMinigame.getName()));
            } else {
                Log.d(TAG, "No remaining rounds, game finished");
                sendToPlayers(buildFinishGamePacket());
            }
        }
    }

    // Send a packet to all players to continue to next state and wait for them to continue
    public void waitOnPlayers(Packet packet) {
        sendToPlayers(packet);
        waitingIds.clear();
        for (Player player : players) {
            waitingIds.add(player.getParticipantId());
        }
    }

    // Send a packet to all players
    public void sendToPlayers(Packet packet) {
        multiplayerClient.sendToPlayers(packet, callback);
    }

}
