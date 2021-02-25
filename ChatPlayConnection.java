import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

class ChatPlayConnection extends ServerSideConnection implements Runnable {

  ChatQueue chatQueue;

  //type of client this connection is for
  private Boolean isDoDClient = false;
  private Boolean isPlayingDoD = false;
  //to help understand DoD map information
  private final String EOT = "EOT";
  private final String NL = "NL";

  public ChatPlayConnection(Socket clientSocket, BufferedReader clientIn, PrintWriter clientOut, ChatQueue chatQueue) {
    super(clientSocket, clientIn, clientOut);
    this.chatQueue = chatQueue;
  }

  /**
   * tell ChatServer to forget client
   */
  @Override
  public void closeClientSocket() {
    super.closeClientSocket();
    clientDelegate.forgetClient(clientID);
  }

  /**
   * format the chat message before sending to chat
   */
  public void sendChatMessage(String sender) {
    String msg = sender + ";" + chatQueue.next();
    //send to client
    clientOut.println(msg);
  }

  /**
   * format the DoD message before sending to game
   * @param sender a player or DoDClient
   * @param str player command or DoDClient response
   */
  public void sendDoDMessage(String sender, String str) {
    String msg = sender + ";" + str;
    //send to player or DoDClient
    clientOut.println(msg);
  }

  @Override
  public void run() {
    try {
      //set the clientID for this client connection
      setClientID(clientIn.readLine());
      //tell ChatServer to remember this client
      if (!isDoDClient) {
        clientDelegate.addClient(clientID, this);
      }

      while(isConnected) {

        if (isDoDClient) {
          //need string builder because DoD map is not on one line
          StringBuilder msg = new StringBuilder();
          String line;
          while(true) {
            line = clientIn.readLine();
            if (line == null) {
              break;
            }
            //NL is replaced with \n on client side
            msg.append(line + NL);
            if (line.contains(EOT)) {
              break;
            }
          }
          //disconnected
          if (line == null) {
            break;
          }
          handleGameDoD(msg.toString().split(EOT)[0]);

        //chatter or player
        } else {
          //read from client
          String msg = clientIn.readLine();
          if (msg == null) {
            break;
          }
          if (msg.toUpperCase().equals("EXIT")) {
            break;
          }

          if (isPlayingDoD) {
            handleGamePlayer(msg);
          
          //chatter
          } else {
            handleChat(msg);
          }
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      closeClientSocket();
    }
  }

  /**
   * handle message sent from chatter client
   * @param msg
   */
  private void handleChat(String msg) {
    //requesting to start DoD game
    if (msg.toUpperCase().equals("JOIN")) {
      isPlayingDoD = true;
      //tell DoDClient to create a new game
      handleGamePlayer(msg);
    //inteded for chat room
    } else {
      synchronized(chatQueue) {
        //add to chat queue
        chatQueue.enqueue(msg);
        //request server to send to all clients
        clientDelegate.sendToAllClients(clientID);
      }
    }
  }

  /**
   * handle command sent from player client
   * @param cmd
   */
  private void handleGamePlayer(String cmd) {
    if (clientDelegate.dodClientExists()) {
      //send command to the DoDClient
      clientDelegate.sendToClient(clientID, "DoDClient", cmd);
    } else {
      clientOut.println(clientSocket.getLocalPort() + ",Server;Dungeon of Doom is unavailable right now.");
      isPlayingDoD = false;
    }
  }

  /**
   * handle message sent from DoDClient
   * @param msg
   */
  private void handleGameDoD(String msg) {
    String player = getPlayerID(msg);
    String result = getMessage(msg);
    clientDelegate.sendToClient(clientID, player, result);
    //need to swap player back to chatter
    if (result.contains("LOSE") || result.contains("WIN")) {
      clientDelegate.swapChatPlayer(player);
    }
  }

  private String getPlayerID(String response) {
    String[] splitResponse = response.split(";");
    return splitResponse[0];
  }
  private String getMessage(String response) {
    String[] splitResponse = response.split(";");
    return splitResponse[1];
  }
  public void setIsDoD(Boolean is) {
    isDoDClient = is;
  }
  public Boolean getIsPlayingDoD() {
    return isPlayingDoD;
  }
  public void setIsPlayingDoD(Boolean is) {
    isPlayingDoD = is;
  }
}
