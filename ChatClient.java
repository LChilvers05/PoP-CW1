import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class ChatClient extends Client implements ConnectionDelegate {

  BufferedReader userInput;

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
  
  @Override
  public void disconnect() {
    try {
      userInput.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void connect() {
    super.connect();
    try {
      //start new server connection thread to read messages
      ServerConnection server = new ServerConnection(socket);
      server.connectionDelegate = this;
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

  @Override
  public void replyToMessage(String msg) {}
}