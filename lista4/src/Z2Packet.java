import java.util.Arrays;

public class Z2Packet{
    // PAKIET PRZESYLANY W DATAGRAMIE
    byte[] data;

    public Z2Packet(int size)
// TWORZY PUSTY PAKIET
    {
        data=new byte[size];
    }

    public Z2Packet(byte[] b)
// TWORZY PAKIET ZAWIERAJACY CIAG BAJTOW b
    {
        data=b;
    }

    public void setIntAt(int value, int idx)
// ZAPISUJE LICZBE CALKOWITA value JAKO 4 BAJTY OD POZYCJI idx
    {
        data[idx]=(byte)   ((value>>24)&0xFF);
        data[idx+1]=(byte) ((value>>16)&0xFF);
        data[idx+2]=(byte) ((value>>8)&0xFF);
        data[idx+3]=(byte) ((value)&0xFF);
    }

    public int getIntAt(int idx)
// ODCZYTUJE LICZBE CALKOWITA NA 4 BAJTACH OD POZYCJI idx
    {
        int x;
        x= (((int) data[idx]) & 0xFF)<<24;
        x|= (((int) data[idx+1]) & 0xFF)<<16;
        x|= (((int) data[idx+2]) & 0xFF)<<8;
        x|= (((int) data[idx+3]) & 0xFF);
        return x;
    }

    @Override
    public String toString() {
        return getIntAt(0) + ": " + (char) data[4];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Z2Packet)) return false;

        Z2Packet packet = (Z2Packet) o;

        return Arrays.equals(data, packet.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}