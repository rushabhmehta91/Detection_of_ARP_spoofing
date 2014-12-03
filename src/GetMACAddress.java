import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;
import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Arrays;


public class GetMACAddress {

    /**
     *
     * @param ip address containing an IP
     * @return MAC-Address as formatted String
     * @throws IOException
     * @throws IllegalArgumentException
     */
    public static String getMACAdressByIp(Inet4Address ip) throws IOException, IllegalArgumentException {

        byte[] mac = GetMACAddress.getMACAddressByARP(ip);

        StringBuilder formattedMac = new StringBuilder();
        boolean first = true;
        for (byte b : mac) {
            if (first) {
                first = false;
            } else {
                formattedMac.append(":");
            }
            String hexStr = Integer.toHexString(b & 0xff);
            if (hexStr.length() == 1) {
                formattedMac.append("0");
            }
            formattedMac.append(hexStr);
        }

        return formattedMac.toString();
    }

    private static byte[] getMACAddressByARP(Inet4Address ip) throws IOException, IllegalArgumentException {

        NetworkInterface networkDevice = getNetworkDeviceByTargetIP(ip);

        JpcapCaptor captor = JpcapCaptor.openDevice(networkDevice, 2000, false, 3000);
        captor.setFilter("arp", true);
        JpcapSender sender = captor.getJpcapSenderInstance();

        InetAddress srcip = null;
        for (NetworkInterfaceAddress addr : networkDevice.addresses)
            if (addr.address instanceof Inet4Address) {
                srcip = addr.address;
                break;
            }

        byte[] broadcast = new byte[] { (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255 };
        ARPPacket arp = new ARPPacket();
        arp.hardtype = ARPPacket.HARDTYPE_ETHER;
        arp.prototype = ARPPacket.PROTOTYPE_IP;
        arp.operation = ARPPacket.ARP_REQUEST;
        arp.hlen = 6;
        arp.plen = 4;
        arp.sender_hardaddr = networkDevice.mac_address;
        arp.sender_protoaddr = srcip.getAddress();
        arp.target_hardaddr = broadcast;
        arp.target_protoaddr = ip.getAddress();

        EthernetPacket ether = new EthernetPacket();
        ether.frametype = EthernetPacket.ETHERTYPE_ARP;
        ether.src_mac = networkDevice.mac_address;
        ether.dst_mac = broadcast;
        arp.datalink = ether;

        sender.sendPacket(arp);

        while (true) {
            ARPPacket p = (ARPPacket) captor.getPacket();
            if (p == null) {
                throw new IllegalArgumentException(ip + " is not a local address");
            }
            if (Arrays.equals(p.target_protoaddr, srcip.getAddress())) {
                return p.sender_hardaddr;
            }
        }
    }

    private static NetworkInterface getNetworkDeviceByTargetIP(Inet4Address ip) throws IllegalArgumentException {

        NetworkInterface networkDevice = null;
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();

        loop: for (NetworkInterface device : devices) {
            for (NetworkInterfaceAddress addr : device.addresses) {
                if (!(addr.address instanceof Inet4Address)) {
                    continue;
                }
                byte[] bip = ip.getAddress();
                byte[] subnet = addr.subnet.getAddress();
                byte[] bif = addr.address.getAddress();
                for (int i = 0; i < 4; i++) {
                    bip[i] = (byte) (bip[i] & subnet[i]);
                    bif[i] = (byte) (bif[i] & subnet[i]);
                }
                if (Arrays.equals(bip, bif)) {
                    networkDevice = device;
                    break loop;
                }
            }
        }

        if (networkDevice == null) {
            throw new IllegalArgumentException(ip + " is not a local address");
        }
        return networkDevice;
    }
}