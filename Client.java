import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

abstract class Client {

  Socket socket;

  PrintWriter serverOut;

  String ID;

  /**
   * open socket to server with address and port
   * @param address
   * @param port
   */
  public void openSocket(String address, int port) {
    try {
      socket = new Socket(address, port);

    } catch (UnknownHostException e) {
      println("This is not a known server host, closing program.");
    } catch (IOException e) {
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
