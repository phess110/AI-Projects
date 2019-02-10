Peter Hess
CSC 242 - Project 1 Connect 4
Feb. 2019

I've included a makefile to compile the program so to compile just enter: make
in the directory containing the program files. Alternatively, enter: javac *.java

To run the program enter: java Connect

Play instructions are printed as the game executes and are fairly self-explanatory. All user input is in the form of integers. There is some input testing.

All forms of play are nearly instantaneous in the 3x3 game. Play appears optimal.

The utility function incentivizes the opponent to win quickly and lose as slowly as possible, while remaining zero sum. This is important in boards where the first player has a winning strategy like 3x4 with 3 in-a-row. If the opponent weren't incentivized to extend the game, he would appear to play randomly because all moves are viewed as equally bad since they all end up as a loss.

For the 6x7 board with 4 in-a-row, the recommended depth is 6. The opponent remains competitive and will certainly win if you give it opportunities.

H-minimax plays relatively quickly when the depth is <= 6 (no more than 5 seconds on the first move, decreasing to around 1 sec. after a handfull of moves). For a depth of 7, the time begins to increase significantly (~20 sec on the first move before dropping off). Minimax and alpha-beta pruning take too long to respond in the beginning on the 6x7 board.