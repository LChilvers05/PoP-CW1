import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

class ChatServer implements ClientsDelegate {

  private ServerSocket serverSocket;
  private boolean running = false;

  private LinkedList<ClientConnection> clients = new LinkedList<>();
  private LinkedList<Thread> threads = new LinkedList<>();
  private ChatQueue chatQueue;

  public ChatServer(int port) {
    chatQueue = new ChatQueue(new LinkedList<String>());
    openSocket(port);
    startConsoleListener();
  }

  @Override
  public void forgetClient(String clientID) {
    synchronized (clients) {
      ClientConnection foundClient = null;
      for (ClientConnection client : clients) {
        if (client.getClientID().equals(clientID)) {
          foundClient = client;
          break;
        }
      }
      if (foundClient != null) {
        clients.remove(foundClient);
      }
    }
  }

  @Override
  public void sendToAll(String sender) {
    // for all client connections
    for (ClientConnection client : clients) {
      // write the latest message in chat
      client.write(sender);
    }
    // done with message, release it
    chatQueue.dequeue();
  }

  private void disconnectClients() {
    //TODO: how to disconnect clients
  }

  /**
   * open the server socket connection
   * 
   * @param port
   */
  private void openSocket(int port) {
    try {
      serverSocket = new ServerSocket(port);
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
    try {
      running = false;
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void startServer() {
    try {
      //continuously get client connections
      while(running) {
        //accept client connection
        Socket clientSocket = serverSocket.accept();
        println("Connection on: " + serverSocket.getLocalPort() + " ; " + clientSocket.getPort());

        //create and start new thread for this client
        ClientConnection client = new ClientConnection(clientSocket, chatQueue);
        client.clientDelegate = this;
        Thread clientThread = new Thread(client);
        threads.add(clientThread);
        synchronized (clients) {
          clients.add(client);
        }
        clientThread.start();
      }

    } catch (IOException e) {
      disconnectClients();
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
              stopServer();
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