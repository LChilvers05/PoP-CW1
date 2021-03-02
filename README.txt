SIMPLE CHATTING SYSTEM

Setup
---
Server
To begin, start the chat server with java ChatServer from the console.
Passing parameters:  
-csp to specify the port number.  
Example:  
java ChatServer -csp 14300 starts the server on port 14300.  
java ChatServer starts the server on default port of 14001.  

Clients
Once the server has been started, various types of clients can be connected with java ChatClient.
Passing parameters:  
-cca to specify the server address.  
-ccp to specify which port to use.  
Example:  
java ChatClient -cca 192.168.10.250 -csp 14300 attempts connection at 192.168.10.250 with port 14300.  
java ChatClient defaults connection to 'localhost' address and 14001 port.

-bot to start client as a BotClient  
-dod to start client as a DoDClient  
Example:  
java ChatClient -bot -csp 14003 attempts BotClient connection at 'localhost' port 14003.  
java ChatClient -dod attempts DoDClient connection at 'localhost' port 14001

The Chat Server
---
The chat server will log when a client has connected and disconnected.  
EXIT can be entered from the console to shutdown the server. All clients will be notified and disconnected in the process.

The Chat Room
---
On starting as a user client, there is a prompt to enter a chat name. This chat name partly makes up the clientID; a unique identifier to keep track of who sent what message. All User and Bot clients are initially entered into a chat room to communicate with everyone connected. Simply enter a message in the console to send a message to everyone.
Example:  
Me: Hello everyone! <-- is a message the current user typed in and sent to everyone.  
Adam: Hi there! <-- is a message from Adam who is another chatter client.

Raw messages are sent in the format:  
unique_ID,client_Name;message_To_Client 
to be used appropriately when needed on both the server and client side.


Entering the Dungeon
---
To begin a game of dungeon of doom a DoDClient must be connected to the server.  A user can simply type JOIN as a message to spawn in their own instance of the game.   
From this point, any commands will only be sent to the DoDClient to play the game.  
To return to the chat room, send command QUIT while playing.

Clients can type EXIT or just close the console window to disconnect from the server.

Code
---
This project makes use of the delegation pattern so that objects instantiated from classes such as BotListener and ChatPlayerConnection can communicate with their owners in a decoupled way (Sundell, 2018).
- class ChatServer = Contains the Main method to start the server. It accepts client connections and manages them as they connect, disconnect and request to send messages to each other.
- class ChatClient = Contains the Main method to create a client. Uses ArgHandler as a factory to create the client with the passed parameters.
- class ServerSideConnection = An abstract class which encapsulates methods to disconnect and the unique identifier for the client connected.
- class ChatPlayConnection = Subclass of ServerSideConnection. Implements Runnable so that a new thread is created every time ChatServer accepts a connection. This class represents a client: it reads from it and writes to it. The ClientsDelegate interface is used to go through the ChatServer when it needs to communicate with another client.
- class Client = An abstract class which is purely concerned with everything that is needed to open and close the socket to the server.
- class ClientSideConnection = An abstract class which sets up all that is needed to listen and interpret raw messages from the server.
- class UserListener = A subclass of ClientSideConnection. Implemented as a thread, it receives raw messages from the server, formats it and prints this message to the console for a real person to read.
- class UserClient = A subclass of Client which an actual person will operate. It uses the thread UserListener to receive messages so that the user can write and send messages independently.
- class BotListener = A subclass of ClientSideConnection which receives messages from the server and instructs BotClient to respond using the ReplyDelegate interface.
- class BotClient = Subclass of Client. Uses MessageHandler to reply to messages received by BotListener.
- class DoDListener = Subclass of ClientSideConnection. It listens for commands from players sent by the server and informs DoDClient with the replyDelegate to reply with the command's outcome.
- class DoDClient = Subclass of Client which must be connected for other clients to play DoD. It manages all games and uses GameLogic to generate a response from the players command.
- class MessageHandler = Used by BotClient to encapsulate the logic of returning a scripted response to a message received.
- class ChatQueue = A simple queue data structure which holds messages to be sent to everyone in the chat room.
- class ArgHandler = Handles and validates the parameters entered when running ChatServer and different types of ChatClient.
- interface ClientsDelegate = All the methods to be used by ChatServer which will help manage and access all the clients connected.
- interface ReplyDelegate = Used on the client side by BotClient and DoDClient so that their listeners can inform them to reply to a message just received.
- interface Closer = Used to tell the UserClient to close its console input reader after a server shutdown.

References
---
- Chilvers, L., 2020. Dungeon of Doom, CM10227. University of Bath. Unpublished assignment.
- Sundell, JS., 2018. Delegation in Swift. Swift by Sundell. Available from: https://www.swiftbysundell.com/articles/delegation-in-swift/ [Accessed 20 February 2021]
- Cohen, C., 2014. Converting a 2D matrix into a graph. Stack Overflow. Available from: https://stackoverflow.com/questions/25405165/converting-a-2d-matrix-into-a-graph [Accessed 8 December 2020]