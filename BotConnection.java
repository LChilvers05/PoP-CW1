package Project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class BotConnection extends Connection {

  public BotConnection (Socket serverSocket) {
    super(serverSocket);
  }

  public void listen() {
    try {
      serverIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

      while(true) {
        //response = clientID: message
        String response = serverIn.readLine();
        //server shut down, disconnect client
        if (response.equals("SERVER_SHUTDOWN")) {
          break;
        }
        String msg = formatMessage(response);
        connectionDelegate.replyToMessage(msg);
      }
      
    } catch (IOException e) {
      e.printStackTrace();

    } finally {
      disconnect();
    }
  }
}
