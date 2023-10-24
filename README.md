# Networked-Connect-Four
Author: Rimsha Rizvi

## Description
In this project, we implemented a two-player Connect Four game, where each player is a separate client, and the game is hosted by a server. The server and clients will be on the same machine, with the server choosing a local host port and the clients knowing the local host and port number. After each game, clients have the option to play again or quit. All networking is done using Java Sockets, and the server runs on its thread, handling each client on separate threads.

## How to Play
Connect Four is played on a 7x6 grid, and players take turns dropping checkers into slots on the game board. The checker falls to the bottom of the column, resting on top of other checkers or the bottom row. The first player to get four of their checkers in a vertical, horizontal, or diagonal row wins.

## Implementation Details
1. For the server program GUI:
   Users can choose the port number.
   A button turns on the server.
   The state of the game is displayed, including:
   Number of clients connected.
   Each player's move.
   Whether someone won.
   Whether players are playing again.
2. For the server program logic:
   A game starts only if two clients are connected.
   Clients are notified if they are the only ones connected.
   The server tracks each player's move, notifying clients of wins, ties, and other players' moves.
3. For the client program GUI:
   There are three required scenes:
      Welcome screen with an option to input the port number and IP address to connect to the server.
      The gameplay screen shows whose turn it is, each move made, and the game board.
      Win/tie screen with options to play again or exit the program.

## Copyright and Plagiarism Notice
All content in this repository belongs to the author. If you would like to use this work for educational or other non-commercial purposes, don't hesitate to get in touch with the author for permission.
