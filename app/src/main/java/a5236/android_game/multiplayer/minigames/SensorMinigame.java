package a5236.android_game.multiplayer.minigames;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import a5236.android_game.Player;
import a5236.android_game.multiplayer.GameHost;

public class SensorMinigame implements Minigame {

    private final GameHost host;

    private int points;

    private boolean started = false;
    private Set<Player> remainingPlayers;
    private Map<Player, Long> playerTimes;

    public SensorMinigame(GameHost host, int points) {
        this.host = host;
        this.points = points;

        remainingPlayers = new HashSet<>();
        remainingPlayers.addAll(host.players);
        playerTimes = new HashMap<>();
    }

    public SensorMinigame(GameHost host) {
        this(host, 100);
    }

    @Override
    public String getName() {
        return "Sensor";
    }

    @Override
    public void gameTick() {
        if (!started) {
            host.sendToPlayers(host.buildSensorMinigamePacket());
            started = true;
        }
    }

    @Override
    public boolean isFinished() {
        return remainingPlayers.isEmpty();
    }

    public void receiveTime(String participantId, long time) {
        Player player = null;
        for (Player remaining : remainingPlayers) {
            if (remaining.getParticipantId().equals(participantId)) {
                player = remaining;
                break;
            }
        }
        if (player != null) {
            playerTimes.put(player, time);
            remainingPlayers.remove(player);

            if (isFinished()) {
                long shortestTime = Collections.min(playerTimes.values());
                for (Map.Entry<Player, Long> entry : playerTimes.entrySet()) {
                    if (entry.getValue() <= shortestTime) {
                        Player winner = entry.getKey();
                        winner.setPoints(winner.getPoints() + points);
                        winner.setRounds_won(winner.getRounds_won() + 1);
                    }
                }
            }
        }
    }
}
