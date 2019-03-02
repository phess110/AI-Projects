Peter Hess
AI Project 2 - Constraint Satisfaction Problems
Feb/March 2019

To compile: javac *.java (in the directory containing the files)

To run: java Runner

When executed, the map coloring and job scheduling problems run automatically and their solutions are printed. 
The solution is found using standard backtracking without inferencing, although AC3 is initially applied.

The user is then asked for the board dimension for the n-queens problem. 

The solution is computed using backtracking with inferencing (arc consistency). This allow the n-queens problem for n <= 40 to be solved in under 1 sec and for n = 50 in around 10 sec. Without inferencing, computation usually takes too long around n = 25.

The abstract implemention of CSPs is provided in CSProb.java. 

	- A CSProb consists of a list of Variables and a list of Constraints.

	- Variables consist of a Domain and name.
		-Domains are an arraylist of possible values for a Variable with
		a bitset indicating whether certain values have been removed by inferencing.

	- Constraints are represented using a lambda function with a list of the variables in the scope of the lambda.

Implementions for the Australia map, Job scheduling, and n-Queens problems, as specified in AIMA, are provided in MapProblem.java, ScheduleProblem.java, and QueenProblem.java, respectively. 
	
