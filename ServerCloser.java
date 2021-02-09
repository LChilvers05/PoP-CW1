import java.util.Scanner;

class ServerCloser implements Runnable {

  /**
   * simply check for EXIT input to close Server
   */
  @Override
  public void run() {
    Scanner input = new Scanner(System.in);

    //look for EXIT command
    while(true) {
      String command = input.nextLine().toUpperCase();

      if (command.equals("EXIT")) {
        break;
      }
    }

    //close scanner!!
  }
}
