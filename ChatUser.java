import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**client operated by a user */
class ChatUser extends Client implements Closer {

  BufferedReader userInput;

  public ChatUser(String address, int port) {
    //to read the user input from console
    userInput = new BufferedReader(new InputStreamReader(System.in));
    println("> Enter chat name");
    try {
      //name to make up clientID
      clientName = userInput.readLine();
      openSocket(address, port);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * close the console input reader
   */
  @Override
  public void closeReaders() {
    try {
      userInput.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 
   */
  @Override
  public void connect() {
    super.connect();
    try {
      //start new server connection thread to read messages
      ServerConnection server = new ServerConnection(socket);
      //delegation pattern so connection thread talks to ChatUser
      server.closer = this;
      Thread serverThread = new Thread(server);
      serverThread.start();

      //repeatedly get messages from console
      while(true) {
        String userInputStr = userInput.readLine();
        if (userInputStr == null) {
          break;
        }
        //send message
        serverOut.println(userInputStr);
      }

    } catch (IOException e) {
      println("Disconnected.");
    } finally {
      closeSocket();
    }
  }
}