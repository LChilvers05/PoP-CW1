// figure out if this can run as java ChatBot have its own main
public class ChatBot extends Client implements ConnectionDelegate {

  MessageHandler msgHandler = new MessageHandler();

  public ChatBot(String address, int port) {
    println("ChatBot activated");
    clientName = "ChatBot";
    openSocket(address, port);
  }

  @Override
  public void replyToMessage(String msg) {
    try {
      //simulate typing...|
      Thread.sleep(2000);
      serverOut.println(msgHandler.getReply(msg));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void connect() {
    super.connect();
    
    //start new server connection
    BotConnection bot = new BotConnection(socket);
    bot.connectionDelegate = this;
    bot.listen();
  }

  @Override
  public void disconnect() {}
}
