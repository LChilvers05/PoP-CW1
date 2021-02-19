import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

class DoDClient extends Client implements ReplyDelegate {

  private HashMap<String, GameLogic> games = new HashMap<>();

  private final String EOT = "EOT";

  public DoDClient(String address, int port) {
    println("Dungeon of Doom activated.");
    ID = UUID.randomUUID().toString() + ",DODClient";

    openSocket(address, port);
  }

  @Override
  public void connect() {
    super.connect();
    serverOut.println("DOD");
    //give the client connection a unique identifier
    serverOut.println(ID);
    //start new user connection to read game events
    DoDListener dod = new DoDListener(socket, ID);
    //delegation patter so connection thread talks to DoDClient
    dod.replyDelegate = this;
    dod.listen();
  }

  @Override
  public void replyToMessage(String msg) {
    String playerID = getPlayerID(msg);
    String cmd = getCommand(msg);
    //start a new game for playerID
    if (cmd.equals("JOIN")) {
      games.put(playerID, newGame());
      serverOut.println(playerID + ";> Spawned" + EOT);

    //playerID has a game started
    } else {
      if (games.containsKey(playerID)) {

        GameLogic game = games.get(playerID);
        int i = 0;
        //do twice to alternate turns between player and bot
        while (game.gameRunning() && i < 2) {
          //println(game.checkMap()); //for debug
          //bot caught human
          if (Arrays.equals(game.p1.position, game.p2.position)) {
            serverOut.println(playerID + ";LOSE" + EOT);
            game.gameRunning = false;
            games.remove(playerID);
            break;

          } else {
            //swap turns
            game.currentPlayer = (game.currentPlayer == game.p1) ? game.p2 : game.p1;
            game.nextPlayer = (game.nextPlayer == game.p2) ? game.p1 : game.p2;

            //request server to inform player of command result
            String result = playerID + ";\n" + game.currentPlayer.getNextAction(cmd);
            if (game.currentPlayer == game.p1) {
              serverOut.println(result + EOT);
            }
          }
          i++;
        }

      } else {
        serverOut.println(playerID + ";Start a game with JOIN" + EOT);
      }
    }
  }

  private GameLogic newGame() {
    //create game, map and player
    GameLogic game = new GameLogic();
    game.map = new Map(game.gameIntro());
    game.p1 = new HumanPlayer(game, game.randomPosition());
    game.p2 = new BotPlayer(game, game.randomPosition());
    game.gameRunning = true;
    return game;
  }

  private String getPlayerID(String response) {
    String[] splitResponse = response.split(";");
    return splitResponse[0];
  }

  private String getCommand(String response) {
    String[] splitResponse = response.split(";");
    return splitResponse[1].toUpperCase();
  }
}
