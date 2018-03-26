package a5236.android_game.multiplayer;

import a5236.android_game.multiplayer.packet.Packet;

public class GameHost extends GameController {

    GameHost(MultiplayerClient multiplayerClient) {
        super(multiplayerClient);
    }

    @Override
    public void gameTick() {
        super.gameTick();

        sendPacketToPlayers(buildMessagePacket("Message sent by host"));
    }

    private void sendPacketToPlayers(Packet packet) {
        multiplayerClient.sendToPlayers(packet, callback);
    }

}
