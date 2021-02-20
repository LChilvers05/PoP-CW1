import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * starts the server and handles all clients
 */
class ChatServer implements ClientsDelegate {

  private ServerSocket serverSocket;
  private boolean running = false;

  //hashmap so direct access to client with id
  private HashMap<String, ChatPlayConnection> clients = new HashMap<>();
  //only ever one dodClient
  private ChatPlayConnection dodClient;
  private ChatQueue chatQueue;

  public ChatServer(int port) {
    chatQueue = new ChatQueue(new LinkedList<String>());
    openSocket(port);
    startConsoleListener();
  }

  /**
   * open the server socket connection
   * @param port port to be used
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

  /**
   * accepts new client connections
   */
  public void startServer() {
    try {
      //continuously get client connections
      while(running) {
        //accept client connection of type
        Socket clientSocket = serverSocket.accept();
        println("Connection on: " + serverSocket.getLocalPort() + " ; " + clientSocket.getPort());
        BufferedReader clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
        createClient(clientSocket, clientIn, clientOut, clientIn.readLine());
      }

    } catch (IOException e) {
      println("Server socket closed");
    } finally {
      disconnectClients();
    }
  }

  /**
   * creates client connections as a thread and manages single DoDClient
   * @param clientSocket  the accepted socket
   * @param clientIn  to read from client
   * @param clientOut to send to client
   * @param type CHAT client or DOD client
   */
  private void createClient(Socket clientSocket, BufferedReader clientIn, PrintWriter clientOut, String type) {
    //create new client
    ChatPlayConnection client = new ChatPlayConnection(clientSocket, clientIn, clientOut, chatQueue);
    client.clientDelegate = this;

    Boolean multipleDoDFlag = false;
    //check for DoD Client
    if (type.equals("DOD")) {
      if (dodClient == null) {
        client.setIsDoD(true);
        dodClient = client;
      } else {
        //trying to add multiple DoDClients
        multipleDoDFlag = true;
      }
    }

    //new thread for this client
    Thread clientThread = new Thread(client);
    clientThread.start();

    //already have a DoDClient, deny request
    if (multipleDoDFlag) { client.sendDisconnectRequest(); }
  }
  
  /** 
   * start thread to read console input
   */
  public void startConsoleListener() {
    Thread stopperThread = new Thread() {
      public void run() {
        try {
          while(true) {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            String cmd = userInput.readLine();
            //only check for 'EXIT' to stop server and then diconnect clients
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

  //ClientsDelegate methods:

  @Override
  public void addClient(String clientID, ChatPlayConnection client) {
    //remember client
    synchronized(clients) {
      clients.put(clientID, client);
    }
  }

  @Override
  public void forgetClient(String clientID) {
    //remove DoDClient if asked to
    if (dodClient != null) {
      if (clientID.equals(dodClient.getClientID())) {
        dodClient = null;
        return;
      }
    }
    //remove client
    synchronized(clients) {
      clients.remove(clientID);
    }
  }


  @Override
  public void sendToAllClients(String sender) {
    //for all client connections
    for (ChatPlayConnection client : clients.values()) {
      // if not playing dod
      if (!client.getIsPlayingDoD()) {
        client.sendChatMessage(sender);
      }
    }
    //done with message, release it
    chatQueue.dequeue();
  }

  @Override
  public void sendToClient(String sender, String reciever, String msg) {
    //only used within a DoD game
    if (reciever.equals("DoDClient")) {
      //if message is from a player send to the DoDClient
      dodClient.sendDoDMessage(sender, msg);
    
    } else {
      //otherwise from DoDClient send to the player
      clients.get(reciever).sendDoDMessage(sender, msg);
    }
  }

  @Override
  public void swapChatPlayer(String clientID) {
    ChatPlayConnection client = clients.get(clientID);
    //swap player <--> chatter
    client.setIsPlayingDoD(!client.getIsPlayingDoD());
  }

  @Override
  public Boolean dodClientExists() {
    return dodClient != null;
  }

  @Override
  public void disconnectClients() {
    // request all clients to disconnect
    synchronized(clients) {
      for (ChatPlayConnection client : clients.values()) {
        client.sendDisconnectRequest();
      }
      //forget all clients
      clients.clear();
    }
    if (dodClient != null) {
      dodClient.sendDisconnectRequest();
      dodClient = null;
    }
  }

  //helper functions
  public static void println(String msg) {
    System.out.println(msg);
  }

  public static void main(String[] args) {
    //use ArgHandler to get specified port
    List<String> listArgs = Arrays.asList(args);
    int port = ArgHandler.getPort(listArgs, "-csp");
    ChatServer chatServer = new ChatServer(port);
    chatServer.startServer();
  }
}