import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class main {

    public static final void main(String[] args) throws IOException{
        int port = 2121;
        ServerSocket serv = new ServerSocket(port);
        Socket s2 = serv.accept();
        System.out.println("Server Ready");

        //PrintStream out = new PrintStream(s2.getOutputStream());

        InputStream input = s2.getInputStream();
        OutputStream output = s2.getOutputStream();

        output.write("220 connection établie \r\n".getBytes());

        Scanner scanner = new Scanner(input);
        String str = scanner.nextLine();

        System.out.println(str);

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

        scanner.close();
        s2.close();
        serv.close();
}
}
