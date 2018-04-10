package a5236.android_game;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.IOException;

import a5236.android_game.multiplayer.packet.Packet;
import a5236.android_game.multiplayer.packet.PacketBuilder;
import a5236.android_game.multiplayer.packet.PacketCompressor;

public class PacketLibraryTest {

    @Test
    public void packetLibrary_Compression_Test() {
        Packet packet = new PacketBuilder(Packet.PacketType.Request)
                .withID((short) 0)
                .withString("a string")
                .withLong(12345)
                .withBoolean(true)
                .build();

        try {
            Packet compressed = PacketCompressor.compress(packet);
            Packet decompressed = PacketCompressor.decompress(compressed);

            assertEquals(packet, decompressed);
        } catch (IOException ignored) {
        }
    }

    @Test
    public void packetLibrary_Serialization_Test() {
        Packet packet = new PacketBuilder(Packet.PacketType.Request)
                .withID((short) 0)
                .withString("a string")
                .withLong(12345)
                .withBoolean(true)
                .build();

        try {
            byte[] serialized = packet.toByteArray();
            Packet deserialized = Packet.fromByteArray(serialized);

            assertEquals(packet, deserialized);
        } catch (IOException ignored) {
        }
    }
}
