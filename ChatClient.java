import java.util.Arrays;
import java.util.List;

/**Main function that instantiates all types of clients with  */
class ChatClient {
  public static void main(String[] args) {
    //get all things specified on 'java ChatClient -args'
    List<String> listArgs = Arrays.asList(args);
    String address = ArgHandler.getAddress(listArgs);
    int port = ArgHandler.getPort(listArgs);
    //app closes if user entered bad address or port
    if (!address.equals("error") && port != -1) {
      //type of client - user, bot, dod
      Client client = ArgHandler.getClient(listArgs, address, port);
      client.connect();
    }
  }
}
