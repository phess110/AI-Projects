# AI-Projects

- **Project 1: Connect 4**. Implementation of a text-based version of Connect 4. The user can specify the game characteristics ![equation](https://latex.codecogs.com/svg.latex?%28m%2Cn%2Ck%29), where ![equation](https://latex.codecogs.com/svg.latex?m) is the width of the board, ![equation](https://latex.codecogs.com/svg.latex?n) the height, and ![equation](https://latex.codecogs.com/svg.latex?k) the length of a sequence in a row required to win. The opponent can be chosen to play randomly or to use minimax, ![equation](https://latex.codecogs.com/svg.latex?%5Calpha%5Cbeta)-pruning, or H-minimax (heuristic depth-limited minimax) to play optimally. Generally, minimax is the slowest of the three techniques, while H-minimax is the fastest (however, H-minimax sacrifices completeness and optimality).

The heuristic function for h-minimax has two components:  ![equation](https://latex.codecogs.com/svg.latex?h_%7Bhuman%7D), the number of empty squares which are part of a ![equation](https://latex.codecogs.com/svg.latex?%28k-1%29)-in-a-row of the human player's pieces and ![equation](https://latex.codecogs.com/svg.latex?h_%7BAI%7D), defined similarly. The heuristic is 

  ![equation](https://latex.codecogs.com/svg.latex?h%28s%29%20%3D%20%5Cfrac%7Bh_%7Bhuman%7D%28s%29%20-%20h_%7BAI%7D%28s%29%7D%7Bh_%7Bhuman%7D%28s%29%20&plus;%20h_%7BAI%7D%28s%29%20&plus;%201%7D.)

- **Project 2:**
