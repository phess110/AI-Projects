# AI-Projects

- **Project 1: Connect 4**. Implementation of a text-based version of Connect 4. The user can specify the game characteristics ![equation](https://latex.codecogs.com/svg.latex?%28m%2Cn%2Ck%29), where ![equation](https://latex.codecogs.com/svg.latex?m) is the height of the board, ![equation](https://latex.codecogs.com/svg.latex?n) the width, and ![equation](https://latex.codecogs.com/svg.latex?k) the length of a sequence in-a-row required to win. The opponent can be chosen to play randomly or to use minimax, ![equation](https://latex.codecogs.com/svg.latex?%5Calpha%5Cbeta)-pruning, or H-minimax (heuristic depth-limited minimax) to play optimally. Generally, minimax is the slowest of the three techniques, while H-minimax is the fastest (however, H-minimax sacrifices completeness and optimality).

The utility function takes a terminal state *s* at a depth *d* moves from the current state and returns 

![equation](https://latex.codecogs.com/svg.latex?u%28s%2Cd%29%20%3D%20%5Cbegin%7Bcases%7D%2050%20-%20d%20%26%5Ctext%7Bif%20%5Ctextsc%7Boutcome%7D%28%7D%20s%7B%29%7D%20%3D%20%5Ctexttt%7BAIWIN%7D%20%5C%5C%20-50%20&plus;%20d%20%26%5Ctext%7Bif%20%5Ctextsc%7Boutcome%7D%28%7D%20s%7B%29%7D%20%3D%20%5Ctexttt%7BAILOSS%7D%20%5Cend%7Bcases%7D)

The 50 is not, per se, a necessary constant other than the fact that any game is at most 42 steps long, so regardless of the depth, a win is always valued more than a tie, and a tie more than a loss.

A *good sequence* for player *i* is a sequence of *k-2* of player *i*'s tiles on the game board. A *good move* builds on, or creates a good sequence. The heuristic function computes the number of good moves for the human and the AI and returns the difference, so as to incentivize the AI to block the human's good sequences and build its own.

- **Project 2:**
