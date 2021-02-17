import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

class DoDClient extends Client implements Closer, ReplyDelegate {

  BufferedReader userInput;

  public DoDClient(String address, int port) {
    ID = UUID.randomUUID().toString() + ",DODClient";
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
    serverOut.println("DOD");
    //start new user connection to read game events
    DoDUserConnection dod = new DoDUserConnection(socket, ID);
    //delegation patter so connection thread talks to DoDClient
    dod.closer = this;
    dod.replyDelegate = this;
    dod.listen();
  }

  @Override
  public void replyToMessage(String msg) {
    try {
      String cmd = userInput.readLine().toUpperCase();
      serverOut.println(cmd);

    } catch (IOException e) {
      e.printStackTrace();

    } catch (NullPointerException e) {
      println("NULL POINTER EXCEPTION");
    } 
    // finally {
    //   closeSocket();
    // }
  }
}
