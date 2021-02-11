import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientConnection implements Runnable {

  Socket clientSocket;

  InputStreamReader clientCharStream;
  BufferedReader clientIn;
  PrintWriter clientOut;

  ChatQueue chatQueue;

  SendToAll sendToAll;

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
      clientCharStream.close();
      clientIn.close();
      clientOut.close();
      clientSocket.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //maybe move into server class
  public void write() {
    try {
      //to send data to the client
      PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
      //send to client
      clientOut.println(chatQueue.next());
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

      while(true) {
        //read from client
        String msg = clientIn.readLine();
        if (msg == null) {
          break;
        }
        synchronized(chatQueue) {
          chatQueue.enqueue(msg);
          //delegate method call
          sendToAll.sendToAll();
        }
      }

    } catch (IOException e) {
      e.printStackTrace();

    } finally {
      closeClientSocket();
    }
  }
}
