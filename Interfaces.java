interface ClientsDelegate {
  public void addClient(String clientID, ChatConnection client);
  public void forgetClient(String clientID);
  
  public void sendToAllClients(String sender);
  public void sendToClient(String sender, String reciever, String msg);

  public void swapChatPlayer(String clientID);

  public Boolean dodClientExists();

  public void disconnectClients();
}

interface ReplyDelegate {
  public void replyToMessage(String msg);
}

interface Closer {
  public void closeReaders();
}
