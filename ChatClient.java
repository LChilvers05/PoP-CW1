import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

class ChatClient {

  private Socket socket;
  // private boolean connected = false;

  public ChatClient(String address, int port) {
    openSocket(address, port);
  }

  private void openSocket(String address, int port) {
    try {
      socket = new Socket(address, port);

    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void closeSocket() {
    try {
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void connect() {
    try {
      //to read the user input from console
      BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

      //to send data to server
      PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), true);

      //to read data from server
      BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      while(true) {
        //send request
        String userInputStr = userInput.readLine();
        serverOut.println(userInputStr);

        //print response
        String serverResponse = serverIn.readLine();
        println(serverResponse);
      }

    } catch (IOException e) {
      e.printStackTrace();

    } finally {
      closeSocket();
    }
  }

  public static void main(String[] args) {
    String addArg = ArgHandler.getAddressAndPort(args)[0];
    int portArg = Integer.parseInt(ArgHandler.getAddressAndPort(args)[1]);
    ChatClient chatClient = new ChatClient(addArg, portArg);
    chatClient.connect();
  }
  //helper functions
  public static void println(String msg) {
    System.out.println(msg);
  }
}