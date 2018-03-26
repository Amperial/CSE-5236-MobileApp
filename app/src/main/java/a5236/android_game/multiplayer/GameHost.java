package a5236.android_game.multiplayer;

import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;

public class GameHost extends GameController {

    GameHost(MultiplayerClient multiplayerClient) {
        super(multiplayerClient);
    }

    @Override
    public void gameTick() {
        super.gameTick();

        sendToPlayers("Message sent by host");
    }

    @Override
    public void handleMessage(RealTimeMessage message) {
        super.handleMessage(message);
    }

    private void sendToPlayers(String message) {
        multiplayerClient.sendToPlayers(message.getBytes(), new RealTimeMultiplayerClient.ReliableMessageSentCallback() {
            @Override
            public void onRealTimeMessageSent(int i, int i1, String s) {
            }
        });
    }

}
