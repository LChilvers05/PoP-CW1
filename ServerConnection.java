import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * create a new thread for server connection for each chat client
 * handles reading messages sent from server
 */
public class ServerConnection implements Runnable {

  Socket serverSocket;

  BufferedReader serverIn;

  public ServerConnection (Socket serverSocket) {
    this.serverSocket = serverSocket;
  }

  private String formatMessage(String response) {
    String[] splitResponse = response.split(";");
    String name = getClientName(splitResponse[0]);
    String msg = splitResponse[1];
    return name + ": " + msg;
  }

  @Override
  public void run() {
    try {

      //to read data from server
      serverIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

      while(true) {
        //response = clientID: message
        String response = serverIn.readLine();
        String msg = formatMessage(response);

        ChatClient.println(msg);
      }
  
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String getClientName(String clientID) {
    return clientID.split(",")[1];
  }
}
