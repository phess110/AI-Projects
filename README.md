# AI-Projects

- **Project 1: Connect 4**. Implementation of a text-based version of Connect 4. The user can specify the game characteristics ![equation](https://latex.codecogs.com/svg.latex?%28m%2Cn%2Ck%29), where ![equation](https://latex.codecogs.com/svg.latex?m) is the height of the board, ![equation](https://latex.codecogs.com/svg.latex?n) the width, and ![equation](https://latex.codecogs.com/svg.latex?k) the length of a sequence in-a-row required to win. The opponent can be chosen to play randomly or to use minimax, ![equation](https://latex.codecogs.com/svg.latex?%5Calpha%5Cbeta)-pruning, or H-minimax (heuristic depth-limited minimax) to play optimally. Generally, minimax is the slowest of the three techniques, while H-minimax is the fastest (however, H-minimax sacrifices completeness and optimality).

The utility function takes a terminal state *s* at a depth *d* moves from the current state and returns 

![equation](https://latex.codecogs.com/svg.latex?u%28s%2Cd%29%20%3D%20%5Cbegin%7Bcases%7D%2050%20-%20d%20%26%5Ctext%7Bif%20%5Ctextsc%7Boutcome%7D%28%7D%20s%7B%29%7D%20%3D%20%5Ctexttt%7BAIWIN%7D%20%5C%5C%20-50%20&plus;%20d%20%26%5Ctext%7Bif%20%5Ctextsc%7Boutcome%7D%28%7D%20s%7B%29%7D%20%3D%20%5Ctexttt%7BAILOSS%7D%20%5Cend%7Bcases%7D)

The 50 is not, per se, a necessary constant other than the fact that any game is at most 42 steps long, so regardless of the depth, a win is always valued more than a tie, and a tie more than a loss.

A *good sequence* for player *i* is a sequence of *k-2* of player *i*'s tiles on the game board. A *good move* builds on, or creates a good sequence. The heuristic function computes the number of good moves for the human and the AI and returns the difference, so as to incentivize the AI to block the human's good sequences and build its own.

- **Project 2: Constraint Satisfaction Problem Solver**. A *constraint satisfaction problem (CSP)* is a 3-tuple ![equation](https://latex.codecogs.com/svg.latex?%28X%2CD%2CC%29) where 

  -  ![equation](https://latex.codecogs.com/svg.latex?X) is a set of variables.
  -  ![equation](https://latex.codecogs.com/svg.latex?D) is a set of domains, one for each variable.
  -  ![equation](https://latex.codecogs.com/svg.latex?C) is a set of constraints of the form ![equation](https://latex.codecogs.com/svg.latex?%5Clangle%20S%2C%20R%20%5Crangle) where ![equation](https://latex.codecogs.com/svg.latex?S) is a tuple of variables and ![equation](https://latex.codecogs.com/svg.latex?R) is a relation on the variables in ![equation](https://latex.codecogs.com/svg.latex?S).

For example, if ![equation](https://latex.codecogs.com/svg.latex?S%20%3D%20%28X_1%2C%5Cldots%2C%20X_k%29) then ![equation](https://latex.codecogs.com/svg.latex?R%20%5Csubseteq%20D_1%5Ctimes%20%5Cldots%20%5Ctimes%20D_k) and an assignment ![equation](https://latex.codecogs.com/svg.latex?%5C%7BX_1%3Da_1%2CX_2%3Da_2%2C%5Cldots%2CX_k%3Da_k%5C%7D) satisfies ![equation](https://latex.codecogs.com/svg.latex?R) if ![equation](https://latex.codecogs.com/svg.latex?%28a_1%2C%5Cldots%2C%20a_k%29%20%5Cin%20R).

A solution to a CSP is an assignment of the variables in ![equation](https://latex.codecogs.com/svg.latex?X) satsifying every constraint in ![equation](https://latex.codecogs.com/svg.latex?C). The backtracking algorithm solves instances of arbirary CSPs. Included are implementations for map coloring, job scheduling, and the n-queens CSPs. Inference (arc & node consistency) provides significant speedup.

- **Project 3: Bayesian Network \& Bayesian Inference**. Implmentation of exact and approximate inference in a Bayesian network. A *Bayesian network* is a digraph, whose nodes correspond to random variables and whose directed edges indicate conditional dependence. Each node stores a conditional probability table, which gives the probability of the random variable taking on a certain element of its domain, given an assignment to its *parents*, i.e. its in-neighbors. ![equation](https://latex.codecogs.com/svg.latex?%5Ctextstyle%20P%28X_1%3Dx_1%2C%5Cldots%2C%20X_n%3Dx_n%29%20%3D%20%5Cprod_%7Bi%3D1%7D%5En%20P%28x_i%7C%5Ctext%7Bparents%7D%28X_i%29%29)
where ![equation](https://latex.codecogs.com/svg.latex?%5Ctext%7Bparents%7D%28X_i%29) are the values assigned to the parents of ![equation](https://latex.codecogs.com/svg.latex?X_i). Exact inference computes the above equation given a bayesian network and a query variable, conditioned on a partial assignment (called the *evidence* or the *givens*) to other variables in the network. The approximation algorithms are:

   - *Rejection Sampling*: Generates ![equation](https://latex.codecogs.com/svg.latex?N) sample events, choosing a value for each random variable in the network (in topological order). The value for each RV ![equation](https://latex.codecogs.com/svg.latex?X) is chosen according to the conditional probability distribution ![equation](https://latex.codecogs.com/svg.latex?%5Cmathbb%7BP%7D%28X%7C%5Ctext%7Bparents%7D%28X%29%29).  If the generated sample is consistent with the evidence and query variable ![equation](https://latex.codecogs.com/svg.latex?X_0) has value ![equation](https://latex.codecogs.com/svg.latex?x), we count the event towards ![equation](https://latex.codecogs.com/svg.latex?%5Cmathbf%7BN%7D%5Bx%5D). Then ![equation](https://latex.codecogs.com/svg.latex?%5Cmathbb%7BP%7D%28X_0%7Ce%29%20%5Capprox%20%5Ctilde%7B%5Cmathbf%7BN%7D%7D), where ![equation](https://latex.codecogs.com/svg.latex?%5Ctilde%7B%5Cmathbf%7BN%7D%7D) is ![equation](https://latex.codecogs.com/svg.latex?%5Cmathbf%7BN%7D) normalized so that ![equation](https://latex.codecogs.com/svg.latex?%5Ctextstyle%20%5Csum_%7Bi%3D1%7D%5En%20%5Ctilde%7B%5Cmathbf%7BN%7D%7D%5Bx_i%5D%20%3D%201.)
   - *Likelihood Weighted Sampling*: Rejection sampling is inefficient because it may generate inconsistent samples. Likelihood weighted sampling only generates samples consistent with the evidence. However, the value of the generated event is weighted to account for the probability that each evidence variable assumes its required value, in accordance with the respective values of its previously sampled parents.
   - *Gibbs Sampling*: Gibbs sampling is an example of a Markov Chain Monte Carlo algorithm. It works by generating a random event. Then at each step, it perturbs the value of each random variable according to the conditional distribution formed by the *Markov blanket* of that RV.
   
- **Project 4 - Linear Separators & Neural Networks**. 
  - A linear separator separates experimental data into two classes. The hypothesis is defined by a vector of weights ![equation](https://latex.codecogs.com/svg.latex?%5Cmathbf%7Bw%7D): ![equation](https://latex.codecogs.com/svg.latex?h_%7B%5Cmathbf%7Bw%7D%7D%28%5Cmathbf%7Bx%7D%29%20%3D%20Threshold%28%5Cmathbf%7Bw%7D%5Ccdot%5Cmathbf%7Bx%7D%29), where thereshold returns 1 if its argument is greater than or equal to 0, and 0 otherwise. Our goal is to minimize the loss (squared error of the correct data characterization and the characterization of the hypothesis). Applying gradient descent we have the following update rule: ![equation](https://latex.codecogs.com/svg.latex?w_i%20%5Cgets%20w_i%20&plus;%20%5Calpha%20%28y-h_%7B%5Cmathbf%7Bw%7D%7D%28%5Cmathbf%7Bx%7D%29%29%20%5Ctimes%20x_i), where alpha is the step size which may either be constant or decay at a rate ![equation](https://latex.codecogs.com/svg.latex?O%281/t%29). 
  - For a softer threshold, we can use the logistic function ![equation](https://latex.codecogs.com/svg.latex?L%28z%29%20%3D%20%5Cfrac%7B1%7D%7B1&plus;e%5E%7B-z%7D%7D) and the update rule:  ![equation](https://latex.codecogs.com/svg.latex?w_i%20%5Cgets%20w_i%20&plus;%20%5Calpha%20%28y-h_%7B%5Cmathbf%7Bw%7D%7D%28%5Cmathbf%7Bx%7D%29%29h_%7B%5Cmathbf%7Bw%7D%7D%28%5Cmathbf%7Bx%7D%29%281-h_%7B%5Cmathbf%7Bw%7D%7D%28%5Cmathbf%7Bx%7D%29%29%5Ctimes%20x_i).
  
  - A neural network 
