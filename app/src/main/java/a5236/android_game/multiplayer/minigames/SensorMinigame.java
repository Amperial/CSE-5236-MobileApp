package a5236.android_game.multiplayer.minigames;

import java.util.HashSet;
import java.util.Set;

import a5236.android_game.Player;
import a5236.android_game.multiplayer.GameHost;

/**
 * Created by Jared on 4/9/2018.
 */

public class SensorMinigame implements Minigame {

    private final GameHost host;

    private int points;

    private Set<Player> remainingPlayers;

    public SensorMinigame(GameHost host, int points) {
        this.host = host;
        this.points = points;

        remainingPlayers = new HashSet<>();
        remainingPlayers.addAll(host.players);
    }

    //TODO
    @Override
    public void gameTick() {

    }

    public SensorMinigame(GameHost host) {
        this(host, 100);
    }

    @Override
    public String getName() {
        return "Sensor";
    }

    @Override
    public boolean isFinished() {
        return remainingPlayers.isEmpty();
    }

}
