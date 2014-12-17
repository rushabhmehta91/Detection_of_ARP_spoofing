/*
 * @File: AnomalyDetector.java
 * 
 * @Author: Mayur Sanghavi and Rushbah Mehta
 * 
 * @Version: 1.0, 12/4/2014
 */

import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;

public class AnomalyDetector {

    static boolean detectAnomaly(ARPPacket pack) {
        EthernetPacket ep = (EthernetPacket) pack.datalink;
        if (!pack.getSenderHardwareAddress().equals(ep.getSourceAddress())) {
            return true;
        }
        if (pack.operation == pack.ARP_REPLY) {
            if (!pack.getTargetHardwareAddress().equals(ep.getDestinationAddress())) {
                return true;
            }
        }
        return false;
    }

}
