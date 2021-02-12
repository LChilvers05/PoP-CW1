interface ClientsDelegate {
  public void forgetClient(String clientID);
  public void sendToAllClients(String sender);
  public void disconnectClients();
}

interface DisconnectDelegate {
  public void disconnect();
}
