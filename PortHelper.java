class PortHelper {

  /**
   * gets the port to be used on program run
   * @param args from console (-csp port)
   * @return port used
   */
  public static int getPort(String[] args) {
    //check that arguments, if any, is in format -csp portNum
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
    //default
    return 14001;
  }
}
