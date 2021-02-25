import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * listens for game commands and informs DoDClient to handle
 */
class DoDListener extends BotListener {

  public DoDListener(Socket serverSocket, String id) {
    super(serverSocket, id);
  }

  @Override
  public void listen() {
    try {
      //to read from server
      serverIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

      while(true) {
        //game command = clientID; command
        String command = serverIn.readLine();
        if (command != null) {
          //server shut down, diconnect client
          if (command.equals("SERVER_SHUTDOWN")) {
            break;
          }
          //log all commands from everyone
          println(command);
          replyDelegate.replyToMessage(command);
         
        //no server
        } else {
          break;
        }
      }

    } catch (IOException e) {
      e.printStackTrace();

    } finally {
      closeReaders();
    }
  }
}
