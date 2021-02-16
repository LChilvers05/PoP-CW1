import java.io.IOException;
import java.net.Socket;
import java.util.List;

class ArgHandler {

  public static String getAddress(List<String> args) {

    try {
      if (args.contains("-cca")) {
        int i = args.indexOf("-cca");
        String address = args.get(i + 1);
        try (Socket test = new Socket(address, 14001)) {
          return address;
        } catch (IOException e) {
          System.out.println("Bad address given, using localhost");
        }
      }
    } catch (IndexOutOfBoundsException e) {
      System.out.println("No address given despite command, using localhost");
    }

    return "localhost";
  }

  public static int getPort(List<String> args) {
    try {
      if (args.contains("-csp")) {
        int i = args.indexOf("-csp");
        int port = Integer.parseInt(args.get(i + 1));
        //TODO: check port
        return port;
      }
    } catch (IndexOutOfBoundsException e) {
      System.out.println("No port given despite command, using 14001");
    } catch (NumberFormatException e) {
      System.out.println("Bad port given, using 14001");
    }

    return 14001;
  }

  public static Client getClient(List<String> args, String address, int port) {

    if (args.contains("-botclient")) {
      return new ChatBot(address, port);
    }

    //TODO: -dodclient


    return new ChatUser(address, port);
  }
}
