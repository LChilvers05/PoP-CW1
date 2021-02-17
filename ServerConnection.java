import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**listens to server in separate thread and outputs to user */
public class ServerConnection extends Connection implements Runnable {

  public ServerConnection (Socket serverSocket) {
    super(serverSocket);
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
        //server shut down, disconnect client
        if (response.equals("SERVER_SHUTDOWN")) {
          ChatUser.println("Server Stopped.");
          break;
        }
        String msg = formatMessage(response);
        //output formatted message
        ChatUser.println(msg);
      }
  
    } catch (IOException e) {
      e.printStackTrace();

    } finally {
      closeReaders();
    }
  }
}
