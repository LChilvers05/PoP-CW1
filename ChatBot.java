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
      Thread.sleep(2500);
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

  public static void main(String[] args) {
    String addArg = ArgHandler.getAddressAndPort(args)[0];
    int portArg = Integer.parseInt(ArgHandler.getAddressAndPort(args)[1]);
    ChatBot chatBot = new ChatBot(addArg, portArg);
    chatBot.connect();
  }

  @Override
  public void disconnect() {}
}
