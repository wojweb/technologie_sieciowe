import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

class Z2Sender
{
    static final int datagramSize=50;
    static final int sleepTime=500;
    static final int maxPacket=50;
    InetAddress localHost;
    int destinationPort;
    DatagramSocket socket;
    SenderThread sender;
    ReceiverThread receiver;
    ReaderThread reader;
    DatagramQueue datagramQueue;
    ArrayList<Z2Packet> packetsPreparedList;
    Queue<Z2Packet> packetsToSendList;

    public Z2Sender(int myPort, int destPort)
            throws Exception
    {
        localHost=InetAddress.getByName("127.0.0.1");
        destinationPort=destPort;
        socket=new DatagramSocket(myPort);
        sender=new SenderThread();
        receiver=new ReceiverThread();
        reader = new ReaderThread();
        datagramQueue = new DatagramQueue();
        packetsPreparedList = new ArrayList<>();
        packetsToSendList = new LinkedList<>();
    }

    class SenderThread extends Thread {
        public void run() {
            try {

                final int MAX_NUMBER_OF_ATTEMPTS = 40;
                int actual_number_of_attemps = 0;
                Z2Packet unfindedPacket = null;

                while (true) {
                    if (!packetsToSendList.isEmpty()) {

                        Z2Packet p = packetsToSendList.poll();
                        DatagramPacket packet =
                                new DatagramPacket(p.data, p.data.length,
                                        localHost, destinationPort);
                        socket.send(packet);
                        System.err.println("Sending: " + p);
                    } else if(datagramQueue.getNumberOfDatagramProcessed() < packetsPreparedList.size()){
                        if(unfindedPacket == null || !unfindedPacket.equals(packetsPreparedList.get(datagramQueue.getNumberOfDatagramProcessed()))){
                            unfindedPacket = packetsPreparedList.get(datagramQueue.getNumberOfDatagramProcessed());
                            actual_number_of_attemps = 0;
                        }
                        if ((actual_number_of_attemps  % MAX_NUMBER_OF_ATTEMPTS) == 0) {
                            packetsToSendList.add(unfindedPacket);
                            System.err.println("Repeated prepare: " + unfindedPacket);
                        }
                        actual_number_of_attemps++;
                    }
                    sleep(sleepTime);

                }
            } catch (Exception e) {
                System.out.println("Z2Sender.SenderThread.run: " + e);
                e.printStackTrace();
            }

        }
    }
    class ReaderThread extends Thread{

        public void run()
        {
            int i, x;
            try
            {
                for(i=0; (x=System.in.read()) >= 0; i++)
                {
                    Z2Packet p=new Z2Packet(4+1);
                    p.setIntAt(i,0);
                    p.data[4]= (byte) x;

                    System.err.println("Prepared: " + p);
                    packetsToSendList.add(p);
                    packetsPreparedList.add(i, p);
                }
            }
            catch(Exception e)
            {
                System.out.println("Z2Sender.ReaderThread.run: "+e);
                e.printStackTrace();
            }
        }

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
                    while ((p = datagramQueue.getCorrectPacket()) != null) {
                        System.out.println("S:" + p.getIntAt(0) +
                                ": " + (char) p.data[4]);

                    }
                }
            }
            catch(Exception e)
            {
                System.out.println("Z2Sender.ReceiverThread.run: "+e);
                e.printStackTrace();
            }
        }

    }


    public static void main(String[] args)
            throws Exception
    {
        Z2Sender sender=new Z2Sender( Integer.parseInt(args[0]),
                Integer.parseInt(args[1]));
        sender.sender.start();
        sender.receiver.start();
        sender.reader.start();
    }



}