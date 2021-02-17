import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class DoDClient extends Client implements Closer {

  BufferedReader userInput;

  public DoDClient(String address, int port) {
    userInput = new BufferedReader(new InputStreamReader(System.in));
    println("=======================================");
    println("==  Welcome to the Dungeon of Doom!  ==");
    println("=======================================");
    println("Please select map or enter custom_map_file_name.txt :");
    println("SMALL");
    println("MEDIUM");
    println("LARGE");
    println("");

    openSocket(address, port);
  }

  @Override
  public void closeReaders() {
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
      serverOut.println("DOD");
      //start new user conncetion thread to read game events
      UserConnection server = new UserConnection(socket);
      //delegation patter so connection thread talks to DoDClient
      server.closer = this;
      Thread serverThread = new Thread(server);
      serverThread.start();

      //repeatedly get messages from console
      while(true) {
        String userInputStr = userInput.readLine();
        if (userInputStr == null) {
          break;
        }
        //send game cmd
        serverOut.println(userInputStr);
      }
    } catch (IOException e) {
      println("Disconnected.");
    } finally {
      closeSocket();
    }
  }
}
