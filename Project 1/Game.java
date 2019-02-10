import java.util.HashSet;

public interface Game{
	
	HashSet<Action> applicableActions(State s);
	State result(State s, Action a);
	void applyAction(Action a);
	Outcome terminalTest(State s, Action a);
	int utility(Outcome o, int depth);

}