import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class DoDUserConnection extends BotConnection {

  public DoDUserConnection (Socket serverSocket, String id) {
    super(serverSocket, id);
  }

  @Override
  public void closeReaders() {
    super.closeReaders();
    closer.closeReaders();
  }

  @Override
  public void listen() {
    try {
      serverIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

      while(true) {
       
        String response = serverIn.readLine();
        //server shut down, disconnect client
        if (response.equals("SERVER_SHUTDOWN") || response.equals("WIN") || response.equals("LOSE")) {
          println(response);
          break;
        }
        println(response);
        if (response.equals("> Type a command...")) {
          replyDelegate.replyToMessage(response);
        }
      }

    } catch (IOException e) {
      e.printStackTrace();

    } finally {
      closeReaders();
    }
  }
}