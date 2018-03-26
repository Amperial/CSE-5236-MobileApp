package a5236.android_game.multiplayer.minigames;

public interface Minigame {

    String getName();

    void gameTick();

    boolean isFinished();

}
