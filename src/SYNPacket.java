/**
 * Created by Rushabh on 12/4/2014.
 */

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;
import jpcap.packet.*;

public class SYNPacket {

    public static void main(String[] args) throws UnknownHostException, IOException {
        JpcapSender sender = JpcapSender.openDevice(JpcapCaptor.getDeviceList()[3]);
        NetworkInterfaceAddress[] nia = JpcapCaptor.getDeviceList()[0].addresses;
        TCPPacket packet = new TCPPacket(284104, 80, 1, 0, false, false, false, false, true, false, false, false, 0, 0);
        packet.setIPv4Parameter(0, false, false, false, 0, false, true, false, 0, 34567, 64, IPPacket.IPPROTO_TCP, nia[0].address, InetAddress.getByName("www.google.com"));

        packet.data = ("").getBytes();

        EthernetPacket ether = new EthernetPacket();
        ether.frametype = EthernetPacket.ETHERTYPE_IP;
        ether.src_mac = ((NetworkInterface) JpcapCaptor.getDeviceList()[3]).mac_address;
        ether.dst_mac = new byte[]{(byte) 200, (byte) 205, (byte) 114, (byte) 68, (byte) 129, (byte) 162};

        packet.datalink = ether;
        sender.sendPacket(packet);
        System.out.println(packet);
        JpcapCaptor captor = JpcapCaptor.openDevice(JpcapCaptor.getDeviceList()[3], 65535, true, 20);
        captor.setFilter("tcp[0xd]&2=2", true);
        int i = 0;
        while (true) {
            EthernetPacket p = (EthernetPacket) captor.getPacket();

            if (p != null && p.) {
                System.out.println(i++ + ": " + p);
                System.out.println();
                System.out.println();

            }
        }
    }
}