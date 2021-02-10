import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientConnection implements Runnable {

  private Socket clientSocket;

  InputStreamReader clientCharStream;
  BufferedReader clientIn;
  PrintWriter clientOut;

  /**
   * for handling client connections on a separate thread
   * @param clientSocket
   */
  public ClientConnection(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  /**
   * close the client connection
   */
  private void closeClientSocket() {
    try {
      clientCharStream.close();
      clientIn.close();
      clientOut.close();
      clientSocket.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    try {
      //to read data from the client
      clientCharStream = new InputStreamReader(clientSocket.getInputStream());
      clientIn = new BufferedReader(clientCharStream);

      //to send data to the client
      clientOut = new PrintWriter (clientSocket.getOutputStream(), true);

      while(true) {
        //read from client
        String in = clientIn.readLine();
        if (in == null) {
          break;
        }

        //send to client
        //TODO: use script class
        clientOut.println("MESSAGE BACK");
      }

    } catch (IOException e) {
      e.printStackTrace();

    } finally {
      closeClientSocket();
    }
  }
}
