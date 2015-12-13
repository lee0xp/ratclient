/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;

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
                        p.println("info|" + s.split("\\|")[1]);
                        p.flush();
                    }
                    if (s.startsWith("msg")) {
                        String text = ar[1];
                        String title = ar[2];
                        int i = Integer.parseInt(ar[3]);
                        JOptionPane.showMessageDialog(null, text, title, i);
                    }
                }

            }.start();
        }
    }

}
