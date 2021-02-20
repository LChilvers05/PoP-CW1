/**
 * Delegation Pattern
 * Used to allow an object or thread to communicate with its owner in a decoupled way
 * Sundell 2018, Delegation in Swift, Swift by Sundell, viewed 20 February 2021,
 * <https://www.swiftbysundell.com/articles/delegation-in-swift/>
 */
/**
 * so connections can communicate with ChatServer
 */
interface ClientsDelegate {
  //tell ChatServer to change clients hashmap
  public void addClient(String clientID, ChatConnection client);
  public void forgetClient(String clientID);
  //used for Chat only
  public void sendToAllClients(String sender);
  //used for DoD only
  public void sendToClient(String sender, String reciever, String msg);
  //swap client player <--> chatter
  public void swapChatPlayer(String clientID);
  //informs if ChatServer is holding a DoDClient
  public Boolean dodClientExists();
  //request all clients to disconnect and forget
  public void disconnectClients();
}

/**
 * client listener tells owner client to reply
 */
interface ReplyDelegate {
  public void replyToMessage(String msg);
}

/**
 * client listener tells owner client to close server readers
 */
interface Closer {
  public void closeReaders();
}
