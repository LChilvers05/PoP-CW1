import java.util.List;

class ArgHandler {

  /**
   * process argument to specify address
   * @param args arguments passed in
   * @return address specified, localhost or error
   */
  public static String getAddress(List<String> args) {

    try {
      if (args.contains("-cca")) {
        int i = args.indexOf("-cca");
        String address = args.get(i + 1);
        return address;
      } else {
        return "localhost";
      }
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Closing due to no address given despite command.");
    }

    return "error";
  }

  /**
   * process argument to specify port
   * @param args arguments passed in
   * @return port specified, 14001 or -1 (error)
   */
  public static int getPort(List<String> args, String param) {
    //for server param = -csp
    //for client param = -ccp
    try {
      if (args.contains(param)) {
        int i = args.indexOf(param);
        int port = Integer.parseInt(args.get(i + 1));
        return port;
      } else {
        return 14001;
      }
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Closing due to no port given despite command.");
    } catch (NumberFormatException e) {
      System.out.println("Closing due to bad port given.");
    }

    return -1;
  }

  /**
   * 
   * @param args arguments passed in
   * @param address valid address to instantiate client
   * @param port valid port to instantiate client
   * @return Client object specified
   */
  public static Client getClient(List<String> args, String address, int port) {

    //create bot client
    if (args.contains("-bot")) {
      return new BotClient(address, port);

    //create dod client
    } else if (args.contains("-dod")) {
      return new DoDClient(address, port);
    }
    //default user client
    return new UserClient(address, port);
  }
}
