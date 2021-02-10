class ArgHandler {

  /**
   * gets the address and/or port to be used on program run
   * @param args from console (-cca address -csp port)
   * @return [address, port]
   */
  public static String[] getAddressAndPort(String[] args) {
    //default
    String[] addressPort = {"localhost", "14001"};
    //check the arguments, if any in format
    //-cca address
    //-csp port
    if (args.length == 2 || args.length == 4) {
      boolean addressFlag = false;
      boolean portFlag = false;
      for (String arg : args) {

        //address specified update default
        if (addressFlag) {
          addressFlag = false;
          addressPort[0] = arg;
        
        //port specified
        } else if (portFlag) {
          portFlag = false;
          try {
            int portCheck = Integer.parseInt(arg);
            addressPort[1] = arg;
          } catch (NumberFormatException e) {
            // e.printStackTrace();
            System.out.println("Error - using default port 14001");
          }
          
        } else if (arg.equals("-csp")) {
          portFlag = true;

        } else if (arg.equals("-cca")) {
          addressFlag = true;
        }
      }
    }
    return addressPort;
  }
}
