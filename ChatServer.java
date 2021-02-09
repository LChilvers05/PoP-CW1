import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class ChatServer {

  private ServerSocket socket;

  public ChatServer(int port) {
    try {
      socket = new ServerSocket(port);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void startServer() {
    println("Server started");
    try {
      //accept client connection
      Socket clientSocket = socket.accept();
      println("Connection on: " + socket.getLocalPort() + " ; " + clientSocket.getPort());

      //to read data from the client
      InputStreamReader clientCharStream = new InputStreamReader(clientSocket.getInputStream());
      BufferedReader clientIn = new BufferedReader(clientCharStream);

      //to send data to the client
      PrintWriter clientOut = new PrintWriter (clientSocket.getOutputStream(), true);

      //read and send to client
      while(true) {
        String userInput = clientIn.readLine();
        clientOut.println(userInput);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      stopServer();
    }
  }

  /**
   * closes server socket connection
   */
  private void stopServer() {
    try {
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    ChatServer chatServer = new ChatServer(PortHelper.getPort(args));
  }

  //helper functions
  public static void println(String msg) {
    System.out.println(msg);
  }
}