import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * parent of UserClient, BotClient, DoDClient
 */
abstract class Client {

  protected Socket socket;
  protected PrintWriter serverOut;
  //unique identifier for the client
  protected String ID;

  /**
   * open socket to server with address and port
   * @param address address of server
   * @param port port to be used
   */
  public void openSocket(String address, int port) {
    try {
      socket = new Socket(address, port);
    
    } catch (UnknownHostException e) {
      println("This is not a known server host, closing program.");
    } catch (IOException e) {
      //Causes: server not running, using a bad address or port
      println("Could not connect to server, closing program.");
    }
  }

  /**
   * close the server connection
   */
  public void closeSocket() {
    try {
      socket.close();
      println("Goodbye.");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * all clients will have a serverOut
   */
  public void connect() {
    try {
      serverOut = new PrintWriter(socket.getOutputStream(), true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  };

  //helper functions
  public static void println(String msg) {
    System.out.println(msg);
  }
}
