import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * listens to server in separate thread and outputs to user
 */
public class UserListener extends ClientSideConnection implements Runnable {

  //New Line: to properly format DoD map
  private final String NL = "NL";

  public UserListener (Socket serverSocket, String id) {
    super(serverSocket, id);
  }

  @Override
  public void closeReaders() {
    super.closeReaders();
    closer.closeReaders();
  }

  /**
   * reads server input and outputs to console
   */
  @Override
  public void run() {
    try {
      //to read data from server
      serverIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

      while(true) {
        //response = clientID: message
        String response = serverIn.readLine();
        response = response.replaceAll(NL, "\n");
        //server shut down, disconnect client
        if (response.equals("SERVER_SHUTDOWN")) {
          println("Server Stopped.");
          break;
        }
        String msg = formatMessage(response, ID);
        //output formatted message
        println(msg);
      }
  
    } catch (IOException e) {
      e.printStackTrace();

    } finally {
      closeReaders();
    }
  }
}
