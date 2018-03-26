package a5236.android_game.multiplayer.packet;

import java.io.*;

public class PacketBuilder
{

    private final ByteArrayOutputStream byteArrayOutputStream;
    private final DataOutputStream dataOutputStream;

    private final Packet.PacketType packetType;
    private short packetID;
    private boolean isBuilt;

    /**
     * Provides an easy way to build a Packet.
     * Note: data has to be written in the same order as it will be fromStream!
     */
    public PacketBuilder(final Packet.PacketType packetType)
    {
        byteArrayOutputStream = new ByteArrayOutputStream();
        dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        isBuilt = false;

        this.packetType = packetType;
        packetID = 0;
    }

    /**
     * @throws IllegalStateException when Packet is already built
     */
    private void checkBuilt()
    {
        if (isBuilt) throw new IllegalStateException("Packet already built");
    }

    /**
     * Adds a custom Packet ID
     * @param packetID Packet ID
     * @throws IllegalStateException see {@link #checkBuilt()}
     */
    public synchronized PacketBuilder withID(final short packetID)
    {
        checkBuilt();
        this.packetID = packetID;
        return this;
    }

    /**
     * Adds a byte
     * @param b Byte
     * @throws IllegalStateException see {@link #checkBuilt()}
     */
    public synchronized PacketBuilder withByte(final byte b)
    {
        checkBuilt();
        try
        {
            dataOutputStream.writeByte(b);
        }
        catch (final IOException e)
        {
        }
        return this;
    }

    /**
     * Adds byte array
     * @param b Byte array
     * @throws IllegalStateException see {@link #checkBuilt()}
     */
    public synchronized PacketBuilder withBytes(final byte[] b)
    {
        checkBuilt();
        try
        {
            dataOutputStream.writeInt(b.length);
            dataOutputStream.write(b);
        }
        catch (final IOException e)
        {
        }
        return this;
    }

    /**
     * Adds an integer
     * @param i Integer
     * @throws IllegalStateException see {@link #checkBuilt()}
     */
    public synchronized PacketBuilder withInt(final int i)
    {
        checkBuilt();
        try
        {
            dataOutputStream.writeInt(i);
        }
        catch (final IOException e)
        {
        }
        return this;
    }

    /**
     * Adds a String
     * @param s UTF-8 String
     * @throws IllegalStateException see {@link #checkBuilt()}
     */
    public synchronized PacketBuilder withString(final String s)
    {
        try
        {
            withBytes(s.getBytes("utf-8"));
        }
        catch (final UnsupportedEncodingException e)
        {
        }

        return this;
    }

    /**
     * Adds a boolean
     * @param b Boolean
     * @throws IllegalStateException see {@link #checkBuilt()}
     */
    public synchronized PacketBuilder withBoolean(final boolean b)
    {
        checkBuilt();
        try
        {
            dataOutputStream.writeBoolean(b);
        }
        catch (final IOException e)
        {
        }
        return this;
    }

    /**
     * Adds a float
     * @param f Float
     * @throws IllegalStateException see {@link #checkBuilt()}
     */
    public synchronized PacketBuilder withFloat(final float f)
    {
        checkBuilt();
        try
        {
            dataOutputStream.writeFloat(f);
        }
        catch (final IOException e)
        {
        }
        return this;
    }

    /**
     * Adds a double
     * @param d Double
     * @throws IllegalStateException see {@link #checkBuilt()}
     */
    public synchronized PacketBuilder withDouble(final double d)
    {
        checkBuilt();
        try
        {
            dataOutputStream.writeDouble(d);
        }
        catch (final IOException e)
        {
        }
        return this;
    }

    /**
     * Adds a long
     * @param l Long
     * @throws IllegalStateException see {@link #checkBuilt()}
     */
    public synchronized PacketBuilder withLong(final long l)
    {
        checkBuilt();
        try
        {
            dataOutputStream.writeLong(l);
        }
        catch (final IOException e)
        {
        }
        return this;
    }

    /**
     * Adds a short
     * @param s Short
     * @throws IllegalStateException see {@link #checkBuilt()}
     */
    public synchronized PacketBuilder withShort(final short s)
    {
        checkBuilt();
        try
        {
            dataOutputStream.writeShort(s);
        }
        catch (final IOException e)
        {
        }
        return this;
    }

    /**
     * Returns current data as a byte array
     * @return Byte array
     */
    public synchronized byte[] getBytes()
    {
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Builds Packet with given data
     * @return Packet
     * @throws IllegalStateException see {@link #checkBuilt()}
     */
    public synchronized Packet build()
    {
        checkBuilt();
        isBuilt = true;

        try
        {
            dataOutputStream.close();
        }
        catch (final IOException e)
        {
        }

        return new Packet(
                packetType,
                packetID,
                byteArrayOutputStream.toByteArray()
        );
    }
}