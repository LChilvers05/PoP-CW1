import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

/**inherited from to read from the server*/
abstract class Connection {

  Socket serverSocket;

  /**to read from server */
  BufferedReader serverIn;

  /**to communicate with Client from Connection*/
  Closer closer;

  public Connection (Socket serverSocket) {
    this.serverSocket = serverSocket;
  }

  /**
   * gets the name of client from clientID
   * @param clientID uniqueID,name
   * @return name
   */
  public static String getClientName(String clientID) {
    return clientID.split(",")[1];
  }

  /**
   * formatted message (Name: Message) to be printed
   * @param response raw input read from server
   * @return formatted message
   */
  public String formatMessage(String response) {
    String[] splitResponse = response.split(";");
    String name = getClientName(splitResponse[0]);
    String msg = splitResponse[1];
    return name + ": " + msg;
  }

  /**
   * closes server and console readers
   */
  public void closeReaders() {
    try {
      serverIn.close();
      // closer.closeReaders();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
