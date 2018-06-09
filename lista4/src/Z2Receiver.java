import java.net.*;

public class Z2Receiver
{
    static final int datagramSize=50;
    InetAddress localHost;
    int destinationPort;
    DatagramSocket socket;

    ReceiverThread receiver;

    DatagramQueue datagramQueue;

    public Z2Receiver(int myPort, int destPort)
            throws Exception
    {
        localHost=InetAddress.getByName("127.0.0.1");
        destinationPort=destPort;
        socket=new DatagramSocket(myPort);
        receiver=new ReceiverThread();
        datagramQueue = new DatagramQueue();
    }

    class ReceiverThread extends Thread
    {

        public void run()
        {
            try
            {
                while(true)
                {
                    byte[] data=new byte[datagramSize];
                    DatagramPacket packet=
                            new DatagramPacket(data, datagramSize);
                    socket.receive(packet);
                    Z2Packet p=new Z2Packet(packet.getData());
                    System.err.println("Received:"+p.getIntAt(0)
                            +": "+(char) p.data[4]);
                    datagramQueue.addPacket(p);
                    while ((p = datagramQueue.getCorrectPacket()) != null){
                        System.out.println("R:"+p.getIntAt(0)
                                +": "+(char) p.data[4]);
                    }

                    // WYSLANIE POTWIERDZENIA
                    packet.setPort(destinationPort);
                    socket.send(packet);
                }
            }
            catch(Exception e)
            {
                System.out.println("Z2Receiver.ReceiverThread.run: "+e);
                e.printStackTrace();
            }
        }


    }

    public static void main(String[] args)
            throws Exception
    {
        Z2Receiver receiver=new Z2Receiver( Integer.parseInt(args[0]),
                Integer.parseInt(args[1]));
        receiver.receiver.start();
    }


}