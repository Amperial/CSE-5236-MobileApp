package a5236.android_game.multiplayer.packet;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class PacketReader
{
    private final Packet packet;
    private final DataInputStream dataInputStream;

    /**
     * Provides an easy way to read data from a Packet
     * Note: data has to be read in the same order as it was written!
     */
    public PacketReader(final Packet packet)
    {
        this.packet = packet;
        dataInputStream = new DataInputStream(new ByteArrayInputStream(packet.getData()));
    }

    /**
     * See {@link Packet#getPacketID()}
     */
    public short getPacketID()
    {
        return packet.getPacketID();
    }

    /**
     * Reads a byte
     * @return Byte
     * @throws IOException when unable to read
     */
    public synchronized byte readByte() throws IOException
    {
        return dataInputStream.readByte();
    }

    /**
     * Reads byte array into output
     * @return Bytes
     * @throws IOException when not enough data is available
     */
    public synchronized byte[] readBytes() throws IOException
    {
        final int dataLength = dataInputStream.readInt();
        final byte[] data = new byte[dataLength];

        final int dataRead = dataInputStream.read(data, 0, dataLength);
        if (dataRead != dataLength) throw new IOException("Not enough data available");

        return data;
    }

    /**
     * Reads an integer
     * @return Integer
     * @throws IOException when unable to read
     */
    public synchronized int readInt() throws IOException
    {
        return dataInputStream.readInt();
    }

    /**
     * Reads a String
     * @return UTF-8 String
     * @throws IOException when unable to read
     */
    public synchronized String readString() throws IOException
    {
        return new String(readBytes(), "utf-8");
    }

    /**
     * Reads a boolean
     * @return Boolean
     * @throws IOException when unable to read
     */
    public synchronized boolean readBoolean() throws IOException
    {
        return dataInputStream.readBoolean();
    }

    /**
     * Reads a float
     * @return Float
     * @throws IOException when unable to read
     */
    public synchronized float readFloat() throws IOException
    {
        return dataInputStream.readFloat();
    }

    /**
     * Reads a double
     * @return Double
     * @throws IOException when unable to read
     */
    public synchronized double readDouble() throws IOException
    {
        return dataInputStream.readDouble();
    }

    /**
     * Reads a long
     * @return Long
     * @throws IOException when unable to read
     */
    public synchronized long readLong() throws IOException
    {
        return dataInputStream.readLong();
    }

    /**
     * Reads a short
     * @return Short
     * @throws IOException when unable to read
     */
    public synchronized short readShort() throws IOException
    {
        return dataInputStream.readShort();
    }

    /**
     * Returns internal Packet
     * @return Packet
     */
    public Packet getPacket()
    {
        return packet;
    }
}