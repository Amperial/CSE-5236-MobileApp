package a5236.android_game;

public class Player {
    private String id;
    private int rounds_won;
    private int points;

    public Player(String playerId) {
        this.id = playerId;
        this.rounds_won = 0;
        this.points = 0;
    }

    public String getPlayerId() {
        return id;
    }

    public int getRounds_won() {
        return rounds_won;
    }

    public void setRounds_won(int rounds_won) {
        this.rounds_won = rounds_won;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}
