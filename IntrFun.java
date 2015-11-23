import CPP.Absyn.*;
import java.util.LinkedList;

public class IntrFun {
	LinkedList<String> argNames;
	LinkedList<Stm> stms;
	
	public IntrFun(LinkedList<String> args, LinkedList<Stm> ss) {
		stms = ss;
		argNames = args;
	}
}