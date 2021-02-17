/**bot that simply replies to messages */
public class BotClient extends Client implements ReplyDelegate {

  /**for scripted reply to messages */
  MessageHandler msgHandler = new MessageHandler();

  public BotClient(String address, int port) {
    println("ChatBot activated");
    clientName = "ChatBot";
    openSocket(address, port);
  }

  /**send scripted messages */
  @Override
  public void replyToMessage(String msg) {
    try {
      //simulate typing...|
      Thread.sleep(2000);
      //send scripted message using MessageHandler
      serverOut.println(msgHandler.getReply(msg));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void connect() {
    super.connect();
    serverOut.println("CHAT");
    //start new server connection (not a thread)
    BotConnection bot = new BotConnection(socket);
    bot.replyDelegate = this;
    bot.listen();
    
    serverOut.println(clientName);
  }
}
