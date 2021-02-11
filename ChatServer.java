import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

class ChatServer implements ClientsDelegate {

  private ServerSocket socket;

  private LinkedList<ClientConnection> clients = new LinkedList<>();
  private ChatQueue chatQueue;

  public ChatServer(int port) {
    chatQueue = new ChatQueue(new LinkedList<String>());
    openSocket(port);
    startConsoleListener();
  }

  @Override
  public void forgetClient(String clientID) {
    for (int i = 0; i < clients.size(); i++) {
      if (clients.get(i).getClientID().equals(clientID)) {
        clients.remove(i);
        break;
      }
    }
  }

  @Override
  public void sendToAll(String sender) {
    //for all client connections
    for (ClientConnection client : clients) {
      //write the latest message in chat
      client.write(sender);
    }
    //done with message, release it
    chatQueue.dequeue();
  }

  /**
   * open the server socket connection
   * @param port
   */
  private void openSocket(int port) {
    try {
      socket = new ServerSocket(port);
      println("Server started");

    } catch (IOException e) {
      e.printStackTrace();
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

  public void startServer() {
    try {
      //continuously get client connections
      while(true) {
        //accept client connection
        Socket clientSocket = socket.accept();
        println("Connection on: " + socket.getLocalPort() + " ; " + clientSocket.getPort());

        //create and start new thread for this client
        ClientConnection client = new ClientConnection(clientSocket, chatQueue);
        client.clientDelegate = this;
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
  
  //input to console to stop server with anonymous method
  public void startConsoleListener() {
    Thread stopperThread = new Thread() {
      public void run() {
        try {
          while(true) {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            String cmd = userInput.readLine();
            if (cmd.toUpperCase().equals("EXIT")) {
              println("TODO: STOP THE SERVER");
              break;
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    };
    stopperThread.start();
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