package a5236.android_game;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import a5236.android_game.multiplayer.GameHost;
import a5236.android_game.multiplayer.minigames.Minigame;
import a5236.android_game.multiplayer.minigames.MultipleChoiceMinigame;
import a5236.android_game.multiplayer.packet.Packet;

@RunWith(MockitoJUnitRunner.class)
public class MultipleChoiceMinigameTest {

    private Minigame getMultipleChoiceMinigame(List<Player> players) {
        GameHost host = mock(GameHost.class);

        host.players = players;

        final MultipleChoiceMinigame minigame = new MultipleChoiceMinigame(host);

        when(host.buildMultipleChoiceMinigamePacket(any(MC_Question.class))).thenReturn(null);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                // host.sendToPlayers(packet) was called with question info
                // for testing we will immediately reply
                // with correct answer for Player1 and wrong for Player2

                minigame.receiveAnswer("1", minigame.question.getAnswer());
                minigame.receiveAnswer("2", "wrong answer lol");
                return null;
            }
        }).when(host).sendToPlayers(any(Packet.class));

        return minigame;
    }

    @Test
    public void multipleChoiceMinigame_CompleteRun() {
        Player player1 = new Player("1", "Player1");
        Player player2 = new Player("2", "Player2");
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        Minigame minigame = getMultipleChoiceMinigame(players);

        while (!minigame.isFinished()) {
            minigame.gameTick();
        }

        assertEquals(player1.getRounds_won(), 1);
        assertEquals(player1.getPoints(), 100);

        assertEquals(player2.getRounds_won(), 0);
        assertEquals(player2.getPoints(), 0);
    }

}
