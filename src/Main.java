import java.io.File;
import java.io.FileInputStream;
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

        ServerSocket servDonnee = null;

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

       

        while (!str.equals("QUIT")){

            str = scanner.nextLine();
            System.out.println(str);
            
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
                if (servDonnee != null && !servDonnee.isClosed()){
                    servDonnee.close();
                }
                servDonnee = new ServerSocket(0);
                
                int p1 = servDonnee.getLocalPort()/256;
                int p2 = servDonnee.getLocalPort() % 256;

                //outputDonnee.write("220 connection établie \r\n".getBytes());

                output.write(("227 Entering Passive Mode (127,0,0,1,"+ p1 +","+ p2 +") \r\n").getBytes());
            
            }
            else if (str.substring(0,4).equals("RETR")){
                Socket s = servDonnee.accept();
                File f = new File(str.substring(5)); 

                //InputStream inputData = s.getInputStream();
                OutputStream outputData = s.getOutputStream();
                
                if (f.exists() && f.isFile()){
                    output.write("150 File status okay. \r\n".getBytes());

                    try (InputStream fileInput = new FileInputStream(f)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = fileInput.read(buffer)) != -1) {
                            outputData.write(buffer, 0, bytesRead);
                        }
                        outputData.flush();
                    }
                
                    output.write("226 Closing data connection. \r\n".getBytes());
                    s.close();
                }
                else{
                    output.write("551 File Not Found or invalid \r\n".getBytes());
                }
            }

            else if (str.substring(0,4).equals("LIST")){
                Socket s = servDonnee.accept();
                OutputStream outputData = s.getOutputStream();
                String[] tab = new String[0] ;

                output.write("150 File status okay. \r\n".getBytes());

                try {
                    if (!str.substring(5).contains("*")){
                        System.out.println("ok");
                        File f = new File(str.substring(5)); 
                        tab = f.list();
                        System.out.println(tab);
                        
                    }
                    else {
                        System.out.println("*");
                        output.write("551 File Not Found or invalid \r\n".getBytes());
                    }
                }
                catch(IndexOutOfBoundsException e ){
                    File f = new File("./"); 
                    tab = f.list();    
                } 

                for (int i=0;i<tab.length;i++){
                    System.out.println(tab[i]);
                    outputData.write((tab[i]+"\n").getBytes());
                }
            
                
                output.write("226 Closing data connection. \r\n".getBytes());
                s.close();
            }

           
    }
        scanner.close();
        s2.close();
        serv.close();
        servDonnee.close();
    
}
}

