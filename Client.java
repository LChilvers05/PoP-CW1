import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

abstract class Client {

  Socket socket;

  PrintWriter serverOut;

  String clientName;

  /**
   * open socket to server with address and port
   * @param address
   * @param port
   */
  public void openSocket(String address, int port) {
    try {
      socket = new Socket(address, port);

    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * close the server connection
   */
  public void closeSocket() {
    try {
      socket.close();
      
      println("Goodbye " + clientName + ".");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void connect() {
    try {
      //to send data to server
      serverOut = new PrintWriter(socket.getOutputStream(), true);
      //inform server of client name
      serverOut.println(clientName);
    } catch (IOException e) {
      e.printStackTrace();
    }
  };

  //helper functions
  public static void println(String msg) {
    System.out.println(msg);
  }
}
