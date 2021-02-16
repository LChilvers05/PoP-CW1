interface ClientsDelegate {
  public void forgetClient(String clientID);
  public void sendToAllClients(String sender);
  public void disconnectClients();
}

interface ConnectionDelegate {
  public void disconnect();
  public void replyToMessage(String msg);
}
