import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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

        ArrayList<String> logins = new ArrayList<String>();
        logins.add("miage");
        logins.add("thomas");

        if (logins.contains(str)) {
            output.write("331 login OK \r\n".getBytes());
        }
        else {
            output.write("501 Bad Username \r\n".getBytes());
        }

        output.write("331 login OK \r\n".getBytes());

        str = scanner.nextLine();
        System.out.println(str);

        output.write("230 User logged in \r\n".getBytes());

        str = scanner.nextLine();
        System.out.println(str);
        
        output.write("215 UNIX system type \r\n".getBytes());

        str = scanner.nextLine();
        System.out.println(str);

        output.write("211 System status. \r\n".getBytes());

        str = scanner.nextLine();
        System.out.println(str);

        if (str.equals("QUIT")) {
            scanner.close();
            s2.close();
            serv.close();
        }

        scanner.close();
        s2.close();
        serv.close();
}
}
