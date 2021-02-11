import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

class ChatServer implements SendToAll {

  private ServerSocket socket;
  private boolean running = false;

  private LinkedList<ClientConnection> clients = new LinkedList<>();
  private ChatQueue chatQueue;

  public ChatServer(int port) {
    chatQueue = new ChatQueue(new LinkedList<String>());

    openSocket(port);
  }

  /**
   * open the server socket connection
   * @param port
   */
  private void openSocket(int port) {
    try {
      socket = new ServerSocket(port);
      running = true;
      println("Server started");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * closes server socket connection
   */
  private void stopServer() {
    running = false;
    try {
      socket.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void startServer() {
    try {
      //continuously get client connections
      while(running) {
        //accept client connection
        Socket clientSocket = socket.accept();
        println("Connection on: " + socket.getLocalPort() + " ; " + clientSocket.getPort());

        //create and start new thread for this client
        ClientConnection client = new ClientConnection(clientSocket, chatQueue);
        client.sendToAll = this;
        Thread clientThread = new Thread(client);
        synchronized (clients) {
          clients.add(client);
        }
        clientThread.start();
      }

    } catch (IOException e) {
      e.printStackTrace();

    } finally {
      //close if stop running requested
      stopServer();
    }
  }

  public void sendToAll(String sender) {
    //for all client connections
    for (ClientConnection client : clients) {
      //write the latest message in chat
      client.write(sender);
    }
    //done with message, release it
    chatQueue.dequeue();
  }

  public static void main(String[] args) {
    int portArg = Integer.parseInt(ArgHandler.getAddressAndPort(args)[1]);
    ChatServer chatServer = new ChatServer(portArg);
    chatServer.startServer();
  }

  //helper functions
  public static void println(String msg) {
    System.out.println(msg);
  }
}