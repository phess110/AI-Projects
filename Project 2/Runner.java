import java.util.ArrayList;
import java.util.Scanner;

public class Runner{
	public static void main(String[] args){

		MapProblem mapcolor = new MapProblem();  
/*
		ArrayList<String> assignMap = mapcolor.backtrackingSearch();
		if(assignMap != null){
			mapcolor.printSolution(assignMap);
		}else{
			System.out.println("There is no solution.");
		}
*/
		//ScheduleProblem jobSchedule = new ScheduleProblem();
		ArrayList<Variable<String>> a = mapcolor.getv();

		System.out.println( mapcolor.AC() );


		for(Variable<String> v: a){
			System.out.print(v.toString() + " ");
			ArrayList<String> vals = v.getDomain();

			for(int i = 0; i < vals.size(); i++){
				if(v.contains(i)){
					System.out.print(vals.get(i) + " ");
				}
			}
			System.out.println();
		}

/*
		ArrayList<Integer> assignJob = jobSchedule.backtrackingSearch();
		if(assignJob != null){
			jobSchedule.printSolution(assignJob);
		}else{
			System.out.println("There is no solution.");
		}

		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the board dimension: ");
		int n = sc.nextInt();
		QueenProblem nqueens = new QueenProblem(n); 

*/

/*
		ArrayList<Variable<String>> a = mapcolor.getv();
		ArrayList<Constraint<String>> c = mapcolor.getc();
		for(Variable<String> v: a){
			System.out.print(v.toString() + " : ");
			for(String i: v.getDomain()){
				System.out.print(i + " ");
			}
			System.out.println(mapcolor.countConsUsing(v));
		}

		ArrayList<String> assignment = new ArrayList<String>(a.size());
		for(int i = 0; i < a.size(); i++){
			assignment.add(null);
		}

		assignment.set(5, "green");
		assignment.set(2, "blue");
		assignment.set(3, "green");
		assignment.set(4, "blue");
		assignment.set(1, "green");
		assignment.set(6, "red");
		//System.out.println(mapcolor.isConsistent(a.get(6), "red" , assignment));
		System.out.println(mapcolor.getUnassignedVar(assignment).toString());
		assignment.set(0, "green");
		System.out.println(mapcolor.isComplete(assignment));
*/

		//sc.close();
	}
}