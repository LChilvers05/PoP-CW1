interface ClientsDelegate {
  public void addChatClient(ChatConnection client);
  public void addDoDPlayer(ChatConnection client);

  public void forgetChatClient(String clientID);
  public void forgetDoDPlayer(String clientID);
  
  public void sendToAllClients(String sender);
  public void sendToClient(String sender, String reciever, String msg);
  public void sendToDoDClient(String sender, String msg);

  public void disconnectClients();
}

interface ReplyDelegate {
  public void replyToMessage(String msg);
}

interface Closer {
  public void closeReaders();
}
