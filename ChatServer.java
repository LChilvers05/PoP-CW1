class ChatServer {





  private int getPort(String[] args) {
    if (args.length == 2) {
      boolean portFlag = false;
      for (String arg : args) {
        if (portFlag) {
          try{
            int port = Integer.parseInt(arg);
            return port;
          } catch (NumberFormatException ex){
            System.out.println("Invalid port argument - using port 14001");
            break;
          }
        } else if (arg.equals("-csp")) {
          portFlag = true;
        }
      }
    }
    return 14001;
  }


  public static void main(String[] args) {
    ChatServer chatServer = new ChatServer();
    int port = chatServer.getPort(args);
  }
}