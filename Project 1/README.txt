Peter Hess
CSC 242 - Project 1 Connect 4
Feb. 2019

To compile enter: javac *.java    in the directory containing the source files

To run the program enter: java Connect

You can use: make clean  to remove the class files when finished.

Play instructions are printed as the game executes and are fairly self-explanatory. All user input is in the form of integers. There is  input testing.

All forms of play are nearly instantaneous in the 3x3 game. Play appears optimal.

The utility function incentivizes the opponent to win quickly and lose as slowly as possible, while remaining zero sum. This is important in boards where the first player has a winning strategy like 3x4 with 3 in-a-row. If the opponent weren't incentivized to extend the game, he would appear to play randomly because all moves are viewed as equally bad since they all end up as a loss.

For the 6x7 board with 4 in-a-row, the recommended depth for h-minimax is 8. The opponent remains competitive and will certainly win if you give it opportunities. I've been able to beat the opponent when the depth <= 6. 

H-minimax plays relatively quickly when the depth is <= 8 (no more than 5 seconds in the early moves, decreasing significantly as play goes on). For a depth of 9, the time begins to increase significantly (15-40 sec in the beginning). Additionally, the implementation of h-minimax uses alpha-beta pruning to speed it up so greater depths could be acheived in the same time. Standard minimax and alpha-beta pruning take too long to respond in the beginning on the 6x7 board.
