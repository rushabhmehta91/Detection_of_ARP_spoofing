import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.HashMap;

/**
 * Created by Rushabh on 11/28/2014.
 */
public class Test {
    static HashMap<InetAddress, String> hostDatabase = new HashMap<InetAddress, String>();
    public static void main(String[] args) {
//        System.out.println(getARPCache());
       printHostDatabase();
    }

    /**
     * Prints host database
     *
     */
    private static void printHostDatabase() {
        for (InetAddress key : hostDatabase.keySet()) {
            System.out.println(key + " " + hostDatabase.get(key));
        }
    }

    /**
     * Prints macaddress in bytes to string
     * @param mac
     *
    private static void getMAC(byte[] mac) {
        for (int i = 0; i < mac.length; i++) {
            System.out.format("%02X%s",
                    mac[i], (i < mac.length - 1) ? "-" : "");
        }
    }*/
    
    public synchronized static String getMAC(byte [] MacAdd){
    	String mac = "";
    	for (int i =0;i<MacAdd.length-1;i++)
            mac = mac + Integer.toHexString(MacAdd[i] & 0xff) + ":";
    	mac = mac + Integer.toHexString(MacAdd[MacAdd.length] & 0xff);
    	return mac;
    }
    
    /**
     * returns arp cache table
     * http://www.java-forums.org/new-java/63347-read-arp-cache.html
     *
     * @return
     */
    public static String getARPCache() {

        String cmd = "arp -a";
        String cmd1 = "arp -d";
        Runtime run = Runtime.getRuntime();
        String result = "ARP Cache: ";
        int i = 0;
        try {
            Process proc1 = run.exec(cmd1);
            Process proc = run.exec(cmd);
            proc.waitFor();
            BufferedReader buf = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = buf.readLine()) != null) {
                result += line + "\n";
                if (i >= 3) {
                    String[] parts = line.trim().split("\\s+");
                    if (parts[2].equals("dynamic")) {
                        hostDatabase.put(InetAddress.getByName(parts[0]), parts[1]);
                    }
                }
                i++;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return (result);
    }

    /**
     * get MAC Address associated with ip address
     */
    String getMAC(InetAddress host) {
        return hostDatabase.get(host);
    }

    /**
     * Compare ip -> mac association in HostDatabase
     * returns true-matched
     * false-not matched
     */
    boolean compareIPMAC(InetAddress ip, String physicalAddress) {
        if (physicalAddress.equals(getMAC(ip))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check do key exist or not
     * returns true - if key exist
     *          false - if key doesnot exist
     */
    boolean checkKeyExistance(InetAddress ip) {
        if (hostDatabase.containsKey(ip)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check do MAC exist or not
     * returns true - if key exist
     *          false - if key doesnot exist
     */
    boolean checkValueExistance(String mac) {
        if (hostDatabase.containsValue(mac)) {
            return true;
        } else {
            return false;
        }
    }
}


