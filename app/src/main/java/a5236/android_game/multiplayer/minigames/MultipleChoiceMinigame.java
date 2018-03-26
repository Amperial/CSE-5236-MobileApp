package a5236.android_game.multiplayer.minigames;

import java.util.HashSet;
import java.util.Set;

import a5236.android_game.MC_Game;
import a5236.android_game.MC_Question;
import a5236.android_game.Player;
import a5236.android_game.multiplayer.GameHost;

public class MultipleChoiceMinigame implements Minigame {

    private final GameHost host;

    private boolean questionAsked = false;
    private int points;
    private MC_Question question;

    private Set<Player> remainingPlayers;

    public MultipleChoiceMinigame(GameHost host, int points) {
        this.host = host;
        this.points = points;

        question = new MC_Game().getRandQuestion();

        remainingPlayers = new HashSet<>();
        remainingPlayers.addAll(host.players);
    }

    public MultipleChoiceMinigame(GameHost host) {
        this(host, 100);
    }

    @Override
    public String getName() {
        return "MultipleChoice";
    }

    @Override
    public void gameTick() {
        if (!questionAsked) {
            host.sendToPlayers(host.buildMultipleChoiceMinigamePacket(question));
            questionAsked = true;
        }
    }

    @Override
    public boolean isFinished() {
        return remainingPlayers.isEmpty();
    }

    public void receiveAnswer(String participantId, String answer) {
        Player player = null;
        for (Player remaining : remainingPlayers) {
            if (remaining.getParticipantId().equals(participantId)) {
                player = remaining;
                break;
            }
        }
        if (player != null) {
            if (question.checkAnswer(answer)) {
                player.setPoints(player.getPoints() + points);
                player.setRounds_won(player.getRounds_won() + 1);
            }

            remainingPlayers.remove(player);
        }
    }

}
