import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class main {

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
        String username = "";

        while (!str.equals("QUIT")){
            if (str.substring(0,4).equals("USER")){
                if (logins.containsKey(str.substring(5))) {
                    output.write("331 login OK \r\n".getBytes());
                    username = str.substring(5);
                }
                else {
                    output.write("501 Bad Username \r\n".getBytes());
                }
            }
            else if (str.substring(0,4).equals("PASS")){
                if (logins.get(username).equals(str.substring(5))) {
                    output.write("230 User logged in \r\n".getBytes());
                }
                else {
                    output.write("501 Bad Password \r\n".getBytes());
                }
            }
            else if (str.equals("SYST")) {
                output.write("215 UNIX system type \r\n".getBytes());
            }
            else if (str.equals("FEAT")){
                output.write("211 System status. \r\n".getBytes());
            }
            str = scanner.nextLine();
            System.out.println(str);
    }
        scanner.close();
        s2.close();
        serv.close();
}
}
