import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static final void main(String[] args) throws IOException{
        int port = 2121;
        ServerSocket serv = new ServerSocket(port);
        Socket s2 = serv.accept();
        System.out.println("Server Ready");

        InputStream input = s2.getInputStream();
        OutputStream output = s2.getOutputStream();

        output.write("220 connection établie \r\n".getBytes());

        Scanner scanner = new Scanner(input);
        String str = scanner.nextLine();

        System.out.println(str);

        Map<String,String> logins = new HashMap<String,String>();
        logins.put("miage","AML");
        logins.put("thomas","CAR");
        logins.put("a","a");
        String username = "";

        if (str.substring(0,4).equals("USER")){
            if (logins.containsKey(str.substring(5))) {
                output.write("331 login OK \r\n".getBytes());
                username = str.substring(5);
            }
            else {
                output.write("430 Bad Username \r\n".getBytes());
            }
        }

        str = scanner.nextLine();
        System.out.println(str);

        if (str.substring(0,4).equals("PASS")){
            if (logins.get(username).equals(str.substring(5))) {
                output.write("230 User logged in \r\n".getBytes());
            }
            else {
                output.write("430 Bad Password \r\n".getBytes());
            }
        }

        str = scanner.nextLine();
        System.out.println(str);

        while (!str.equals("QUIT")){
            
            if (str.equals("SYST")) {
                output.write("215 UNIX system type \r\n".getBytes());
            }
            else if (str.equals("FEAT")){
                output.write("211 System status. \r\n".getBytes());
            }
            else if (str.substring(0,4).equals("TYPE")){
                output.write("150 File status okay. \r\n".getBytes());
            }
            else if (str.equals("EPSV")){
                output.write("150 File status okay. \r\n".getBytes());
            }
            else if (str.equals("PASV")){
                ServerSocket servDonnee = new ServerSocket(0);
                Socket s = servDonnee.accept();
                InputStream inputDonnee = s2.getInputStream();
                OutputStream outputDonnee = s2.getOutputStream();

                int p1 = servDonnee.getLocalPort()/256;
                int p2 = servDonnee.getLocalPort() - p1;

                output.write("227 Entering Passive Mode (127,0,0,1,p1,p2) \r\n".getBytes());

                servDonnee.close();
                s.close();
            }
            else if (str.substring(0,4).equals("RETR")){
                File f = new File(str.substring(5)); 
                if (f.exists()){
                    output.write("150 File status okay. \r\n".getBytes());
                    continue;
                }
                else{
                    output.write("226 Closing data connection. \r\n".getBytes());
                }
            }

            str = scanner.nextLine();
            System.out.println(str);
    }
        scanner.close();
        s2.close();
        serv.close();
}
}

