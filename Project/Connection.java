package Project;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

abstract class Connection {

  Socket serverSocket;

  BufferedReader serverIn;

  ConnectionDelegate connectionDelegate;

  public Connection (Socket serverSocket) {
    this.serverSocket = serverSocket;
  }

  public static String getClientName(String clientID) {
    return clientID.split(",")[1];
  }

  public String formatMessage(String response) {
    String[] splitResponse = response.split(";");
    String name = getClientName(splitResponse[0]);
    String msg = splitResponse[1];
    return name + ": " + msg;
  }

  public void disconnect() {
    try {
      serverIn.close();
      connectionDelegate.disconnect();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
