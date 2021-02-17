import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

class ChatConnection extends ServerSideConnection implements Runnable {

  ChatQueue chatQueue;

  /**
   * for handling client connections on a separate thread
   * @param clientSocket
   */
  public ChatConnection(Socket clientSocket, BufferedReader clientIn, ChatQueue chatQueue) {
    super(clientSocket, clientIn);
    this.chatQueue = chatQueue;
  }

  /**
   * close the client connection
   */
  @Override
  public void closeClientSocket() {
    super.closeClientSocket();
    clientDelegate.forgetClient(clientID);
  }

  //format the message
  @Override
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
      // clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

      //to send data to the client
      clientOut = new PrintWriter(clientSocket.getOutputStream(), true);

      setClientID(clientIn.readLine());

      while(isConnected) {
        //read from client
        String msg = clientIn.readLine();
        if (msg == null) {
          break;
        }
        synchronized(chatQueue) {
          //add to chat queue
          chatQueue.enqueue(msg);
          //request server to send to all clients
          clientDelegate.sendToAllClients(clientID);
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      closeClientSocket();
    }
  }
}
