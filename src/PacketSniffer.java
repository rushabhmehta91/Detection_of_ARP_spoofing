
import net.sourceforge.jpcap.capture.*;
import net.sourceforge.jpcap.net.*;

import java.util.*;


public class PacketSniffer {
    private static String[] devices;
    private static PacketCapture captor;
    private static Packet info;
    private static final Scanner input = new Scanner(System.in);
    private static final String FILTER = "arp";
    private static final int PACKET_COUNT = -1;

    public PacketSniffer()throws Exception
    {
        captor = new PacketCapture();
        int i;
        devices =  captor.lookupDevices();
        System.out.println(devices.length);
        for(i=0; i<devices.length; i++)
        {
            System.out.println(i+": "+devices[i]); // +devices[i].name
            System.out.println();
        }
        System.out.println("device: "+captor.findDevice());
        String device = input.nextLine();
        System.out.println(device);
        captor.open(device, 65535, true, 0);
        captor.setFilter(FILTER, true);
        System.out.println("exiting constructor");
        captor.addPacketListener(new PacketHandler());
        captor.capture(PACKET_COUNT);
        System.out.println("exiting constructor");
    }

    public static void main(String[] args)throws Exception {
        try
        {
            PacketSniffer example = new PacketSniffer();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
    }

class PacketHandler implements PacketListener {
    Queue<Packet> queue = new LinkedList<Packet>();
    int m_counter=0;
    @Override
    public void packetArrived(Packet packet)
    {
        queue.add(packet);
        System.out.println(m_counter++);
        System.out.println(getReadableData(packet));
    }

    private String getReadableData(Packet packet) {
        byte[] b=packet.getData();
        String data="";
        for(int i=0;i<b.length;i++)
            data=data+(char)b[i];

        return data;
    }


}