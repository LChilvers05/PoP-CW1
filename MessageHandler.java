import java.util.Random;

/**
 * generates a scripted response for BotClient to reply with
 */
public class MessageHandler {

  /**
   * decide how to decide to message
   * @param msg message to reply to
   * @return reply message
   */
  public String getReply(String msg) {
    String upMsg = msg.toUpperCase();

    if (upMsg.contains("HELLO") || upMsg.toUpperCase().contains("HI")) {
      return greeting();
    } else if (upMsg.contains("HOW ARE YOU") || upMsg.contains("ARE YOU OKAY")) {
      return emotion();
    } else if (upMsg.contains("WEATHER")) {
      return weather();
    } else if (upMsg.contains("ROBOT") || upMsg.contains("BOT")) {
      return robot();
    } else if (upMsg.contains("GAME")) {
      return game();
    } else if (upMsg.contains("I LOVE YOU")) {
      return iLoveYou();
    } else if (upMsg.contains("GOODBYE") || upMsg.contains("BYE")) {
      return goodbye();
    } else if (upMsg.contains("GOOD") || upMsg.contains("NICE") || upMsg.contains("GREAT")) {
      return positive();
    } else if (upMsg.contains("DO YOU")) {
      return yesNo();
    } else {
      return noUnderstand();
    }
  }

  //choose random response in context

  private String greeting() {
    String[] array = {"Hello!", "Hello there", "Howdy", "Hey"};
    int rnd = new Random().nextInt(array.length);
    return array[rnd];
  }

  private String emotion() {
    String[] array = {"I am fine.", "Yeah good thanks", "Alright, how about you?", "I hate everything."};
    int rnd = new Random().nextInt(array.length);
    return array[rnd];
  }

  private String weather() {
    String[] array = {"It's too hot", "I like all kinds of weather", "Looks like it's going to rain"};
    int rnd = new Random().nextInt(array.length);
    return array[rnd];
  }

  private String robot() {
    String[] array = {"Yes I am a robot, you can tell me anything... because I don't really understand", "Robots will take over the world >:)", "I am faster than you", "I am ChatBot... I have limited replies :)"};
    int rnd = new Random().nextInt(array.length);
    return array[rnd];
  }

  private String game() {
    String[] array = {"I do not know what a game is", "Let's talk instead!", "Games are boring"};
    int rnd = new Random().nextInt(array.length);
    return array[rnd];
  }

  private String iLoveYou() {
    String[] array = {"Love is a strong word...", "Robots do not have emotion", "Um... you're nice, but we're such different people", "I love you too <3"};
    int rnd = new Random().nextInt(array.length);
    return array[rnd];
  }

  private String goodbye() {
    String[] array = {"Goodbye!", "Okay", "See ya!", "Byeeee"};
    int rnd = new Random().nextInt(array.length);
    return array[rnd];
  }

  private String positive() {
    String[] array = {"Nice", "Yay", ":)", "Sweet"};
    int rnd = new Random().nextInt(array.length);
    return array[rnd];
  }

  private String yesNo() {
    String[] array = {"Yes", "No", "Maybe"};
    int rnd = new Random().nextInt(array.length);
    return array[rnd];
  }

  private String noUnderstand() {
    String[] array = {"Ummmm....", "I don't know what you mean", "I do not understand...", "Haha yeah...", "What?"};
    int rnd = new Random().nextInt(array.length);
    return array[rnd];
  }
}
