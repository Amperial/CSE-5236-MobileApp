package a5236.android_game.multiplayer.packet;

import java.io.*;

public class Packet
{
    private final PacketType packetType;
    private final short packetID;
    private final int dataLength;
    private final byte[] data;

    public enum PacketType
    {
        Request,
        Reply;

        public static final PacketType[] fastValues = values();
    }

    /**
     * Creates a new immutable Packet
     * @param packetType Packet Type
     * @param packetID Packet ID
     * @param data Packet Data
     */
    public Packet(final PacketType packetType, final short packetID, final byte[] data)
    {
        this.packetType = packetType;
        this.packetID = packetID;
        dataLength = data.length;
        this.data = data;
    }

    /**
     * Returns Packet Type
     * @return Packet Type
     */
    public PacketType getPacketType()
    {
        return packetType;
    }

    /**
     * Returns whether Packet is of type Request
     * @return PacketType is Request
     */
    public boolean isRequest()
    {
        return packetType == PacketType.Request;
    }

    /**
     * Returns whether Packet is of type Reply
     * @return PacketType is Reply
     */
    public boolean isReply()
    {
        return packetType == PacketType.Reply;
    }

    /**
     * Returns Packet ID
     * @return Packet ID
     */
    public short getPacketID()
    {
        return packetID;
    }

    /**
     * Returns Data length
     * @return Data length
     */
    public int getDataLength()
    {
        return dataLength;
    }

    /**
     * Returns Packet data
     * @return Data
     */
    public byte[] getData()
    {
        return data;
    }

    /**
     * Writes Packet into DataOutputStream
     * @param out DataOutputStream to write into
     * @throws IOException when unable to write to stream
     */
    public void write(final DataOutputStream out) throws IOException
    {
        // Packet Type
        out.writeByte(packetType.ordinal());

        // Packet ID
        out.writeShort(packetID);

        // Data Length
        out.writeInt(dataLength);

        // Data
        out.write(data);
    }

    public byte[] toByteArray() throws IOException
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        write(dataStream);

        final byte[] data = byteStream.toByteArray();

        dataStream.close();

        return data;
    }

    /**
     * Reads a Packet from raw input data
     * @param in DataInputStream to fromStream from
     * @return Packet created from input
     * @throws IOException when unable to read from stream
     */
    public static Packet fromStream(final DataInputStream in) throws IOException
    {
        // Packet Type
        final Packet.PacketType packetType = Packet.PacketType.fastValues[in.readByte()];

        // Packet ID
        final short packetID = in.readShort();

        // Data Length
        final int dataLength = in.readInt();

        // Data
        final byte[] data = new byte[dataLength];
        in.readFully(data);

        return new Packet(
                packetType,
                packetID,
                data
        );
    }

    public static Packet fromByteArray(final byte[] in) throws IOException
    {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(in);
        DataInputStream dataStream = new DataInputStream(byteStream);

        final Packet packet = fromStream(dataStream);

        dataStream.close();

        return packet;
    }

    @Override
    public String toString()
    {
        return "Type: [" + packetType + "] ID: [" + packetID + "] Data: [" + dataLength + " bytes]";
    }
}