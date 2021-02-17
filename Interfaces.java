interface ClientsDelegate {
  public void forgetClient(String clientID);
  public void sendToAllClients(String sender);
  public void disconnectClients();
}

interface ReplyDelegate {
  public void replyToMessage(String msg);
}

interface Closer {
  public void closeReaders();
}
