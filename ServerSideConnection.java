import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * parent of 
 */
public abstract class ServerSideConnection {

  protected Socket clientSocket;
  //to read from and send to clients
  protected BufferedReader clientIn;
  protected PrintWriter clientOut;
  //to communicate with ChatServer
  ClientsDelegate clientDelegate;

  protected String clientID;

  protected boolean isConnected = true;

  public ServerSideConnection(Socket clientSocket, BufferedReader clientIn, PrintWriter clientOut) {
    this.clientSocket = clientSocket;
    this.clientIn = clientIn;
    this.clientOut = clientOut;
  }
  
  /**
   * close the client connection
   */
  public void closeClientSocket() {
    try {
      isConnected = false;
      clientIn.close();
      clientOut.close();
      clientSocket.close();
      ChatServer.println(clientSocket.getPort() + " disconnected.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendDisconnectRequest() {
    clientOut.println("SERVER_SHUTDOWN");
  }

  public String getClientID() {
    return clientID;
  }
  public void setClientID(String name) {
    clientID = name;
  }
}
