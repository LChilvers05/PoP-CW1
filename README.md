# DUNGEON OF DOOM

## The Game
---
You, a brave fortune-hunter, have been trapped in a rectangular-shaped dungeon attempting to find gold. While running away from an evil Bot, you must hunt down the required amount of gold and exit the dungeon. **If you try to leave without enough gold, or the Bot catches you, you will die.**

## How To Play
---
First, select a dungeon/map to play in the opening menu, or type in the name of a custom map .txt file.
You and the Bot take it in turns to perform an action. Your turn will be prompted by: 
**> Type a command...** \
Commands you can input:
- **HELLO** = tells you how much gold you need before you can leave.
- **GOLD** = tells you how much gold you currently own.
- **MOVE N,S,E,W**  = changes your position on the map.
- **PICKUP** = use when over some gold (G) to increase the amount of gold you own.
- **QUIT** = to be used when over exit (E) tile after you have the required amount of gold. Using this command without satisfying these conditions means you lose the game.
- **LOOK** = displays your surroundings in a 5x5 grid.

Commands can be entered lowercase.
**All input takes up a turn.**

## The Dungeon
---
When you and the Bot use LOOK, you get an understanding of what is around. Here is how the grid returned should be understood:
- **P** = your position. This will always be seen in the centre.
- **B** = the Bot's position. It is highly likely if you see it, it sees you and is chasing you!
- **.** = a blank space which can be moved onto.
- **G** = a space containing gold which can be moved onto.
- **E** = an exit space which can be moved onto.
- **\#** = a wall space which blocks movement here.

## Tactics
---
- It's always good to use LOOK every couple turns or so to make sure you are still going the right way and avoiding the evil Bot. 
- By piecing together the fragments of the dungeon given to you by LOOK, you can navigate without using up a turn to refresh your memory. Remember, every turn you stay still is a potential turn the Bot gets closer!
- The Bot needs to make sure it's still on the right course too, use this to your advantage to get further away as it looks at the map. 
- By running outside of the Bot's surroundings, you will lose it and it will begin searching again rather than chasing.
- It might be better to leave gold and run if the Bot is too close.
- The Bot will always travel the shortest route to the last position it saw you.

## Code
---
- **class GameLogic** = contains main method (program entry point) and main game loop. Methods process each of the commands from both Player and Bot and decides if it was successful or not. Also keeps track of whos turn it is.
- **class Map** = reads in Map file when an instance is made. Keeps track of where gold and exits are in the dungeon and updates as gold is collected.
- **class Player** = an abstract class which both HumanPlayer and BotPlayer inherit from. Player objects hold their positions in the dungeon.
- **class HumanPlayer** = takes input from the console and processes the human command. Responses are returned to be printed to the console. Only the human players collect gold, this current score is stored in the object.
- **class BotPlayer** = commands decided from analysing surroundings with an initial LOOK command. Tries its best to simulate how a human would play the game: moving randomly until its goal is identified. When a target is found a new instance of **Graph** is made to do a breadth first search to chase them with the shortest path.
- **class Graph** = parses the 5x5 grid from LOOK into a graph data structure and contains a breadthFirstSearch to find the shortest path between two x,y positions on this grid.



## References
---
- Cohen 2014, Converting a 2D matrix into a graph, Stack Overflow, viewed 8 December 2020, [<https://stackoverflow.com/questions/25405165/converting-a-2d-matrix-into-a-graph />](https://stackoverflow.com/questions/25405165/converting-a-2d-matrix-into-a-graph)
