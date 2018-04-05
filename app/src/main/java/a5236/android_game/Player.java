package a5236.android_game;

import android.os.Parcel;
import android.os.Parcelable;

public class Player{
    private String id;
    private String name;
    private int rounds_won;
    private int points;

    public Player(String playerId, String name) {
        this.id = playerId;
        this.name = name;
        this.rounds_won = 0;
        this.points = 0;
    }

    public String getParticipantId() {
        return id;
    }

    public String getDisplayName() {
        return name;
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
