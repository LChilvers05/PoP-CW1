import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class ServerSideConnection {

  protected Socket clientSocket;

  protected BufferedReader clientIn;
  protected PrintWriter clientOut;

  ClientsDelegate clientDelegate;

  protected String clientID;

  protected boolean isConnected = true;

  public ServerSideConnection(Socket clientSocket, BufferedReader clientIn) {
    this.clientSocket = clientSocket;
    this.clientIn = clientIn;
  }

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

  abstract public void sendChatMessage(String sender);

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
