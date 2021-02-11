import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerConnection implements Runnable {

  Socket serverSocket;

  //to read data from server
  BufferedReader serverIn;

  public ServerConnection (Socket serverSocket) {
    this.serverSocket = serverSocket;
  }

  @Override
  public void run() {
    try {

      serverIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

      while(true) {
        //print response
        String serverResponse = serverIn.readLine();
        println(serverResponse);
      }
  
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
  //helper functions
  public static void println(String msg) {
    System.out.println(msg);
  }
}
