import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


//NOTES: try closeSocket()
//causes socket exception (this is extra not on spec)
class ChatClient implements DisconnectDelegate {

  private Socket socket;
  // private boolean connected = false;

  BufferedReader userInput;

  private String clientName;

  public ChatClient(String address, int port) {
    //to read the user input from console
    userInput = new BufferedReader(new InputStreamReader(System.in));
    println("> Enter chat name");
    try {
      clientName = userInput.readLine();
      openSocket(address, port);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * open socket to server with address and port
   * @param address
   * @param port
   */
  private void openSocket(String address, int port) {
    try {
      socket = new Socket(address, port);

    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * close the server connection
   */
  private void closeSocket() {
    try {
      socket.close();
      
      println("Goodbye " + clientName);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Override
  public void disconnect() {
    try {
      userInput.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void connect() {
    try {
      //to send data to server
      PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), true);

      //inform server of client name
      serverOut.println(clientName);

      //start new server connection thread to read messages
      ServerConnection server = new ServerConnection(socket, serverOut);
      server.disconnectDelegate = this;
      Thread serverThread = new Thread(server);
      serverThread.start();

      while(true) {
        String userInputStr = userInput.readLine();
        if (userInputStr == null) {
          break;
        }
        //send input to server
        serverOut.println(userInputStr);
      }

    } catch (IOException e) {
      println("Disconnected from Server");
    } finally {
      closeSocket();
    }
  }

  public static void main(String[] args) {
    String addArg = ArgHandler.getAddressAndPort(args)[0];
    int portArg = Integer.parseInt(ArgHandler.getAddressAndPort(args)[1]);
    ChatClient chatClient = new ChatClient(addArg, portArg);
    chatClient.connect();
  }
  //helper functions
  public static void println(String msg) {
    System.out.println(msg);
  }
}