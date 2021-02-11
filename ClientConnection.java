import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientConnection implements Runnable {

  Socket clientSocket;

  BufferedReader clientIn;
  PrintWriter clientOut;

  ChatQueue chatQueue;
  ClientsDelegate clientDelegate;

  private String clientID;

  /**
   * for handling client connections on a separate thread
   * @param clientSocket
   */
  public ClientConnection(Socket clientSocket, ChatQueue chatQueue) {
    this.clientSocket = clientSocket;
    this.chatQueue = chatQueue;
  }

  /**
   * close the client connection
   */
  private void closeClientSocket() {
    try {
      clientIn.close();
      clientOut.close();
      clientSocket.close();
      clientDelegate.forgetClient(clientID);
      ChatServer.println(clientSocket.getPort() + " disconnected");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //format the message
  public void write(String sender) {
    //do not send to self
    if (!sender.equals(clientID)) {
      String msg = sender + ";" + chatQueue.next();
      // send to client
      clientOut.println(msg);
    }
  }

  @Override
  public void run() {
    try {
      //to read data from the client
      clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

      //to send data to the client
      clientOut = new PrintWriter(clientSocket.getOutputStream(), true);

      setClientID(clientIn.readLine());

      while(true) {
        //read from client
        String msg = clientIn.readLine();
        if (msg == null) { //or request to leave
          break;
        }
        synchronized(chatQueue) {
          //add to chat queue
          chatQueue.enqueue(msg);
          //request server to send to all clients
          clientDelegate.sendToAll(clientID);
        }
      }

    } catch (IOException e) {
      e.printStackTrace();

    } finally {
      closeClientSocket();
    }
  }

  public String getClientID() {
    return clientID;
  }

  public void setClientID(String name) {
    clientID = clientSocket.getPort() + "," + name;
  }
}
