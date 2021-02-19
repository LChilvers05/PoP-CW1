import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

class ChatServer implements ClientsDelegate {

  private ServerSocket serverSocket;
  private boolean running = false;

  private LinkedList<ChatConnection> chatClients = new LinkedList<>();
  private HashMap<String, ChatConnection> dodPlayers = new HashMap<>();
  private ChatConnection dodClient;
  private ChatQueue chatQueue;

  public ChatServer(int port) {
    chatQueue = new ChatQueue(new LinkedList<String>());
    openSocket(port);
    startConsoleListener();
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
        BufferedReader clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String type = clientIn.readLine();

        //create new client
        ChatConnection client = new ChatConnection(clientSocket, clientIn, chatQueue);
        client.clientDelegate = this;
        if (type.equals("DOD")) {
          client.setIsDoD(true);
          dodClient = client;
        } else {
          client.setIsDoD(false);
          synchronized (chatClients) {
            chatClients.add(client);
          }
        }

        //new thread for this client
        Thread clientThread = new Thread(client);
        clientThread.start();
      }

    } catch (IOException e) {
      println("Server socket closed");
    } finally {
      disconnectClients();
    }
  }
  
  //input to console to stop server with anonymous
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

  //MARK: ClientsDelegate methods

  @Override
  public void forgetChatClient(String clientID) {
    synchronized (chatClients) {
      ChatConnection foundClient = null;
      for (ChatConnection client : chatClients) {
        if (client.getClientID().equals(clientID)) {
          foundClient = client;
          break;
        }
      }
      if (foundClient != null) {
        chatClients.remove(foundClient);
      }
    }
  }

  @Override
  public void sendToAllClients(String sender) {
    // for all client connections
    for (ChatConnection client : chatClients) {
      // write the latest message in chat
      client.sendChatMessage(sender);
    }
    // done with message, release it
    chatQueue.dequeue();
  }

  @Override
  public void disconnectClients() {
    // request all clients to disconnect
    synchronized(chatClients) {
      for (ChatConnection client : chatClients) {
        client.sendDisconnectRequest();
      }
    }
    synchronized(dodPlayers) {
      for (ChatConnection client : dodPlayers.values()) {
        client.sendDisconnectRequest();
      }
    }
    if (dodClient != null) {
      dodClient.sendDisconnectRequest();
    }
  }

  @Override
  public void addChatClient(ChatConnection client) {
    // request all clients to disconnect
    synchronized(chatClients) {
      System.out.println(client.getIsPlayingDoD());
      client.setIsPlayingDoD(false);
      System.out.println(client.getIsPlayingDoD());
      chatClients.add(client);
    }
  }

  @Override
  public void addChatClientWithID(String clientID) {
    addChatClient(getClientForID(clientID));
  }

  @Override
  public void addDoDPlayer(ChatConnection client) {
    synchronized(dodPlayers) {
      client.setIsPlayingDoD(true);
      dodPlayers.put(client.getClientID(), client);
    }
  }

  @Override
  public void forgetDoDPlayer(String clientID) {
    synchronized(dodPlayers) {
      dodPlayers.remove(clientID);
    }
  }

  @Override
  public void sendToClient(String sender, String reciever, String msg) {
    dodPlayers.get(reciever).sendDoDResponse(sender, msg);
  }

  @Override
  public void sendToDoDClient(String sender, String msg) {
    dodClient.sendDoDCommand(sender, msg);
  }

  private ChatConnection getClientForID(String clientID) {
    for (ChatConnection client : chatClients) {
      if (client.getClientID().equals(clientID)) {
        return client;
      }
    }
    return dodPlayers.get(clientID); //null if in neither
  }

  //helper functions
  public static void println(String msg) {
    System.out.println(msg);
  }

  public static void main(String[] args) {
    List<String> listArgs = Arrays.asList(args);
    int port = ArgHandler.getPort(listArgs);
    ChatServer chatServer = new ChatServer(port);
    chatServer.startServer();
  }
}