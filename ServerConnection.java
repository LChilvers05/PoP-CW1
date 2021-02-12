import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * create a new thread for server connection for each chat client
 * handles reading messages sent from server
 */
public class ServerConnection implements Runnable {

  Socket serverSocket;

  BufferedReader serverIn;

  DisconnectDelegate disconnectDelegate;

  public ServerConnection (Socket serverSocket, PrintWriter serverOut) {
    this.serverSocket = serverSocket;
  }

  
  public static String getClientName(String clientID) {
    return clientID.split(",")[1];
  }

  private String formatMessage(String response) {
    String[] splitResponse = response.split(";");
    String name = getClientName(splitResponse[0]);
    String msg = splitResponse[1];
    return name + ": " + msg;
  }

  private void disconnect() {
    try {
      serverIn.close();
      disconnectDelegate.disconnect();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    try {

      //to read data from server
      serverIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

      while(true) {
        //response = clientID: message
        String response = serverIn.readLine();
        //server shut down, disconnect client
        if (response.equals("SERVER_SHUTDOWN")) {
          break;
        }
        String msg = formatMessage(response);

        ChatClient.println(msg);
      }
  
    } catch (IOException e) {
      e.printStackTrace();

    } finally {
      disconnect();
    }
  }
}
