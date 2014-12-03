import java.io.*;
/**
 * Created by Rushabh on 11/28/2014.
 */
public class Test {
    public static void main(String[] args) {
        System.out.println(getARPCache());
    }

    /** returns arp cache table
     *  http://www.java-forums.org/new-java/63347-read-arp-cache.html
     * @return
     */
    public static String getARPCache() {
        String cmd = "arp -a";
        Runtime run = Runtime.getRuntime();
        String result = "ARP Cache: ";

        try {
            Process proc = run.exec(cmd);
            proc.waitFor();
            BufferedReader buf = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = buf.readLine()) != null) {
                result += line + "\n";
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return (result);
    }
}

