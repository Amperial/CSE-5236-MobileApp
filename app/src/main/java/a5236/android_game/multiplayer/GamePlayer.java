package a5236.android_game.multiplayer;

import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;

public class GamePlayer extends GameController {

    GamePlayer(MultiplayerClient multiplayerClient) {
        super(multiplayerClient);
    }

    @Override
    public void gameTick() {
        super.gameTick();

        sendToHost("Message sent by player");
    }

    @Override
    public void handleMessage(RealTimeMessage message) {
        super.handleMessage(message);
    }

    private void sendToHost(String message) {
        multiplayerClient.sendToHost(message.getBytes(), new RealTimeMultiplayerClient.ReliableMessageSentCallback() {
            @Override
            public void onRealTimeMessageSent(int i, int i1, String s) {
            }
        });
    }

}
