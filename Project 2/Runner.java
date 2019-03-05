import java.util.Scanner;

public class Runner{

	public static void main(String[] args){
		MapProblem mapcolor = new MapProblem();  
		mapcolor.run();
		
		ScheduleProblem jobSchedule = new ScheduleProblem();
		jobSchedule.run();

		Scanner sc = new Scanner(System.in);
		System.out.print("\nEnter the board dimension for n-Queens: ");
		int n = sc.nextInt();
		QueenProblem nqueens = new QueenProblem(n); 
		nqueens.runWithInference();
		sc.close();
	}
}