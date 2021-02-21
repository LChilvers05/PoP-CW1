import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

/**
 * inherited from to read from the server
 */
abstract class ClientSideConnection {

  protected Socket serverSocket;

  /**to read from server */
  protected BufferedReader serverIn;

  /**to communicate with Client from ClientSideConnection*/
  Closer closer;
  /**same as Client ID*/
  protected String ID;

  public ClientSideConnection (Socket serverSocket, String id) {
    this.serverSocket = serverSocket;
    this.ID = id;
  }

  /**
   * gets the name of client from clientID
   * @param clientID uniqueID,name
   * @return name
   */
  public static String getClientName(String clientID, String id) {
    if (clientID.equals(id)) {
      return "Me";
    }
    String[] splitID = clientID.split(",");
    return splitID[1];
  }

  /**
   * formatted message (Name: Message) to be printed
   * @param response raw input read from server
   * @return formatted message
   */
  public String formatMessage(String response, String id) {
    String[] splitResponse = response.split(";");
    String name = getClientName(splitResponse[0], id);
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

  public static void println(String msg) {
    System.out.println(msg);
  }
}
