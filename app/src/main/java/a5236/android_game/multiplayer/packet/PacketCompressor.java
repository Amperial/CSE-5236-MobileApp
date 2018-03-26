package a5236.android_game.multiplayer.packet;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class PacketCompressor
{
    private static final int INFLATE_BUFFER_SIZE = 16;

    /**
     * Decompresses given Packet
     * @param packet Compressed Packet
     * @return Decompressed Packet
     * @throws IOException when unable to decompress
     */
    public static Packet decompress(final Packet packet) throws IOException
    {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
        final GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Read from input until everything is inflated
        final byte buffer[] = new byte[INFLATE_BUFFER_SIZE];
        int bytesInflated;
        while ((bytesInflated = gzipInputStream.read(buffer)) >= 0)
        {
            byteArrayOutputStream.write(buffer, 0, bytesInflated);
        }

        return new Packet(
                packet.getPacketType(),
                packet.getPacketID(),
                byteArrayOutputStream.toByteArray()
        );
    }

    /**
     * Compresses given Packet. Note that this can increase the total size when used incorrectly
     * @param packet Packet to compress
     * @return Compressed Packet
     * @throws IOException when unable to compress
     */
    public static Packet compress(final Packet packet) throws IOException
    {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)
        {
            {
                def.setLevel(Deflater.BEST_COMPRESSION);
            }
        };

        // Deflate all data
        gzipOutputStream.write(packet.getData());
        gzipOutputStream.close();

        return new Packet(
                packet.getPacketType(),
                packet.getPacketID(),
                byteArrayOutputStream.toByteArray()
        );
    }
}