import java.util.PriorityQueue;
import java.util.Comparator;

public class DatagramQueue {
    private int NumberOfDatagramProcessed;
    private PriorityQueue<Z2Packet> queue;


    public DatagramQueue() {
        NumberOfDatagramProcessed = 0;
        queue = new PriorityQueue<>(z2PacketComparator);
    }

    public void addPacket(Z2Packet packet){
        queue.add(packet);
    }

    //Klasa gwarantuje zachowanie kolejnosci, funkcja gwarantuje, ze wszystkie elementu zostana wyswietlone i nie beda sie powtarzac

    public Z2Packet getCorrectPacket(){

        while(queue.peek() != null && queue.peek().getIntAt(0) < NumberOfDatagramProcessed)
            queue.poll();

        if(queue.peek() != null && queue.peek().getIntAt(0) == NumberOfDatagramProcessed){
            NumberOfDatagramProcessed++;
            return queue.poll();
        }else
            return null;
    }

    public int getNumberOfDatagramProcessed() {
        return NumberOfDatagramProcessed;
    }

    public int size(){
        return queue.size();
    }

    public static Comparator<Z2Packet> z2PacketComparator = new Comparator<Z2Packet>() {
        @Override
        public int compare(Z2Packet o1, Z2Packet o2) {
            return (int) o1.getIntAt(0) - o2.getIntAt(0);
        }
    };


}
