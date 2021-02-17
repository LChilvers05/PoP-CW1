import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**listens to server and tells ChatBot to reply*/
public class BotConnection extends ClientSideConnection {

  ReplyDelegate replyDelegate;

  public BotConnection (Socket serverSocket) {
    super(serverSocket);
  }

  /**
   * reads server input and informs ChatBot
   */
  public void listen() {
    try {
      serverIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

      while(true) {
        //response = clientID: message
        String response = serverIn.readLine();
        //server shut down, disconnect client
        if (response.equals("SERVER_SHUTDOWN")) {
          break;
        }
        String msg = formatMessage(response);
        //get ChatBot to reply to the message
        replyDelegate.replyToMessage(msg);
      }
      
    } catch (IOException e) {
      e.printStackTrace();

    } finally {
      closeReaders();
    }
  }
}
