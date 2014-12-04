import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.HashMap;

/**
 * Created by Rushabh on 11/28/2014.
 */
public class Test {
   static HashMap<String,InetAddress> hostDatabase=new HashMap<String, InetAddress>();
    public static void main(String[] args) {
//        System.out.println(getARPCache());
       printHostDatabase();
    }

    /**
     * Prints host database
     *
     */
    private static void printHostDatabase() {
        for (String key : hostDatabase.keySet()) {
            System.out.println(key + " " + hostDatabase.get(key));
        }
    }

    /**
     * Prints macaddress in bytes to string
     * @param mac
     */
    private static void getMAC(byte[] mac) {
        for (int i = 0; i < mac.length; i++) {
            System.out.format("%02X%s",
                    mac[i], (i < mac.length - 1) ? "-" : "");
        }
    }

    /** returns arp cache table
     *  http://www.java-forums.org/new-java/63347-read-arp-cache.html
     * @return
     */
    public static String getARPCache() {

        String cmd = "arp -a";
        String cmd1="arp -d";
        Runtime run = Runtime.getRuntime();
        String result = "ARP Cache: ";
        int i=0;
        try {
            Process proc1 = run.exec(cmd1);
            Process proc = run.exec(cmd);
            proc.waitFor();
            BufferedReader buf = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = buf.readLine()) != null) {
                result += line + "\n";
                if(i>=3) {
                    String[] parts = line.trim().split("\\s+");
                    if(parts[2].equals("dynamic")){
                        hostDatabase.put(parts[1], InetAddress.getByName(parts[0]));
                    }
                }
                i++;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return (result);
    }
}


