import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

class ChatConnection extends ServerSideConnection implements Runnable {

  ChatQueue chatQueue;

  private Boolean isDoDClient;
  private Boolean isPlayingDoD = false;

  private final String EOT = "EOT";
  private final String NL = "_";

  /**
   * for handling client connections on a separate thread
   * @param clientSocket
   */
  public ChatConnection(Socket clientSocket, BufferedReader clientIn, ChatQueue chatQueue) {
    super(clientSocket, clientIn);
    this.chatQueue = chatQueue;
  }

  /**
   * close the client connection
   */
  @Override
  public void closeClientSocket() {
    super.closeClientSocket();
    clientDelegate.forgetChatClient(clientID);
    clientDelegate.forgetDoDPlayer(clientID);
  }

  //format the message
  @Override
  public void sendChatMessage(String sender) {
    String msg = sender + ";" + chatQueue.next();
    //send to client
    clientOut.println(msg);
  }

  public void sendDoDResponse(String sender, String response) {
    String msg = sender + ";" + response;
    //send to player
    clientOut.println(msg);
  }

  public void sendDoDCommand(String sender, String command) {
    String msg = sender + ";" + command;
    // send to DoDClient
    clientOut.println(msg);
  }

  @Override
  public void run() {
    try {
      //to read data from the client
      // clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

      //to send data to the client
      clientOut = new PrintWriter(clientSocket.getOutputStream(), true);

      setClientID(clientIn.readLine());

      while(isConnected) {

        if (isDoDClient) {

          StringBuilder msg = new StringBuilder();
          while(true) {
            String line = clientIn.readLine();
            msg.append(line + NL);
            if (line.contains(EOT)) {
              break;
            }
          }
          handleGameDoD(msg.toString().split(EOT)[0]);

        } else {
          //read from client
          String msg = clientIn.readLine();
          if (msg == null) {
            break;
          }

          if (isPlayingDoD) {
            handleGamePlayer(msg);

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

  private void handleChat(String msg) {
    //requesting to start DoD game
    if (msg.toUpperCase().equals("JOIN")) {
      //transfer client from chat to dod
      clientDelegate.forgetChatClient(clientID);
      clientDelegate.addDoDPlayer(this);
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

  private void handleGameDoD(String msg) {
    String player = getPlayerID(msg);
    String result = getMessage(msg);
    clientDelegate.sendToClient(clientID, player, result);
    if (result.contains("LOSE") || result.contains("WIN")) {
      clientDelegate.addChatClientWithID(player);
      clientDelegate.forgetDoDPlayer(player);
    }
  }

  private void handleGamePlayer(String cmd) {
    clientDelegate.sendToDoDClient(clientID, cmd);
  }

  private String getPlayerID(String response) {
    String[] splitResponse = response.split(";");
    return splitResponse[0];
  }

  private String getMessage(String response) {
    String[] splitResponse = response.split(";");
    return splitResponse[1].toUpperCase();
  }

  public void setIsDoD(Boolean is) {
    isDoDClient = is;
  }
  public void setIsPlayingDoD(Boolean is) {
    isPlayingDoD = is;
  }
  public Boolean getIsPlayingDoD() {
    return isPlayingDoD;
  }
}
