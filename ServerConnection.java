package Project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * create a new thread for server connection for each chat client
 * handles reading messages sent from server
 */
public class ServerConnection extends Connection implements Runnable {

  public ServerConnection (Socket serverSocket) {
    super(serverSocket);
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
