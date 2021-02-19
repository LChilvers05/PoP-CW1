import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

/**client operated by a user */
class UserClient extends Client implements Closer {

  BufferedReader userInput;

  public UserClient(String address, int port) {
    //to read the user input from console
    userInput = new BufferedReader(new InputStreamReader(System.in));
    println("> Enter chat name");
    try {
      //name to make up clientID
      ID = UUID.randomUUID().toString() + "," + userInput.readLine();
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
    if (socket != null) {
      super.connect();
      try {
        serverOut.println("CHAT");
        serverOut.println(ID);
        //start new user connection thread to read messages
        UserConnection server = new UserConnection(socket, ID);
        //delegation pattern so connection thread talks to UserClient
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
}