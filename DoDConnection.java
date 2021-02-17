import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

class DoDConnection extends ServerSideConnection implements Runnable {

  GameLogic logic;

  public DoDConnection(Socket clientSocket) {
    super(clientSocket);

    //create game, map and player
    logic = new GameLogic();
    logic.map = new Map(logic.gameIntro());
    logic.p1 = new HumanPlayer(logic, logic.randomPosition());
    logic.p2 = new BotPlayer(logic, logic.randomPosition());
  }

  @Override
  public void write(String sender) {
    //sender = message from game
    clientOut.println(sender);
  }

  @Override
  public void run() {
    try {
      //to read data from the client
      clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      logic.p1.setUserInput(clientIn);

      //to send data to the client
      clientOut = new PrintWriter(clientSocket.getOutputStream(), true);

      logic.gameRunning = true;
      while(isConnected) {

        while(logic.gameRunning()) {
          //println(logic.checkMap()); //for debug
          //bot caught human
          if (Arrays.equals(logic.p1.position, logic.p2.position)) {
            write("LOSE");
            logic.gameRunning = false;
            break;
          }

          //swap turns
          logic.currentPlayer = (logic.currentPlayer == logic.p1) ? logic.p2 : logic.p1;
          logic.nextPlayer = (logic.nextPlayer == logic.p2) ? logic.p1 : logic.p2;
          //writes the result from the players command inputs
          write(logic.currentPlayer.getNextAction());
        }
        // //read game command from client
        // String cmd = clientIn.readLine();
        // if (cmd == null) {
        //   break;
        // }
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      closeClientSocket();
    }
  }
}
