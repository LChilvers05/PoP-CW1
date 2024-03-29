import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * listens to server and tells ChatBot to reply
 */
public class BotListener extends ClientSideConnection {
  
  ReplyDelegate replyDelegate;

  public BotListener (Socket serverSocket, String id) {
    super(serverSocket, id);
  }

  /**
   * reads server input and informs ChatBot
   */
  public void listen() {
    try {
      //to read from server
      serverIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

      while(true) {
        //response = clientID; message
        String response = serverIn.readLine();
        if (response != null) {
          //server requests client to shutdown
          if (response.equals("SERVER_SHUTDOWN")) {
            break;
          }
          //so bot doesn't reply to self
          if (!response.split(";")[0].equals(ID)) {
            String msg = formatMessage(response, ID);
            println(msg);
            //get ChatBot to reply to the message
            replyDelegate.replyToMessage(msg);
          }

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
