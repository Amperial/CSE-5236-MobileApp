package a5236.android_game.multiplayer;

import a5236.android_game.multiplayer.packet.Packet;

public class GamePlayer extends GameController {

    GamePlayer(MultiplayerClient multiplayerClient) {
        super(multiplayerClient);
    }

    @Override
    public void gameTick() {
        super.gameTick();

        sendPacketToHost(buildMessagePacket("Message sent by player"));
    }

    private void sendPacketToHost(Packet packet) {
        multiplayerClient.sendToHost(packet, callback);
    }

}
