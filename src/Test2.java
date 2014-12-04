import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.packet.ARPPacket;
import jpcap.packet.DatalinkPacket;
import jpcap.packet.ICMPPacket;
import jpcap.packet.Packet;

import java.io.IOException;
import java.util.Scanner;

public class Test2 {
    private static JpcapCaptor captor;
    private static final Scanner input = new Scanner(System.in);


    public static void main(String args[]) {
        //Obtain the list of network interfaces
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();

        //for each network interface
        for (int i = 0; i < devices.length; i++) {
            //print out its name and description
            System.out.println(i + ": " + devices[i].name + "(" + devices[i].description + ")");
            //print out its datalink name and description
//            System.out.println(" datalink: " + devices[i].datalink_name + "(" + devices[i].datalink_description + ")");
            //print out its MAC address
//            System.out.print(" MAC address:");
//            for (byte b : devices[i].mac_address)
//                System.out.print(Integer.toHexString(b & 0xff) + ":");
//            System.out.println();

//            //print out its IP address, subnet mask and broadcast address
//            for (NetworkInterfaceAddress a : devices[i].addresses)
//                System.out.println(" address:" + a.address + " " + a.subnet + " " + a.broadcast);
        }

            int device = input.nextInt();
             try {
                captor=captor.openDevice(devices[device], 65535, true, 20);
                captor.setFilter("arp or icmp", true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int i=0;
        sendARPRequest(devices[device]);
           while(true){
               Packet p=captor.getPacket();
               if(p!=null) {
                   System.out.println(i + ": " + p);
                   System.out.println();
                   System.out.println();
                   DatalinkPacket d=p.datalink;
                   System.out.println(i++ + ": " + d);
                   System.out.println();
               }
           }

    }

    private static void sendARPRequest(NetworkInterface device) {
        ARPPacket arpRequest=new ARPPacket();
        arpRequest.hardtype=ARPPacket.HARDTYPE_ETHER;
        arpRequest.prototype=ARPPacket.PROTOTYPE_IP;
        arpRequest.hlen=6;
        arpRequest.plen=4;
        arpRequest.operation=ARPPacket.ARP_REQUEST;
        arpRequest.sender_hardaddr=device.mac_address;
//        arpRequest.sender_protoaddr=;
//        arpRequest.target_hardaddr=


    }
}