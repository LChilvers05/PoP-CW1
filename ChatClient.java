import java.util.Arrays;
import java.util.List;

class ChatClient {
  public static void main(String[] args) {
    List<String> listArgs = Arrays.asList(args);
    String address = ArgHandler.getAddress(listArgs);
    int port = ArgHandler.getPort(listArgs);
    Client client = ArgHandler.getClient(listArgs, address, port);
    client.connect();
  }
}
