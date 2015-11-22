import CPP.Absyn.*;
import java.util.HashMap;
import java.util.LinkedList;

public class TypeChecker {
	
	public void typecheck(Program p) {
		Env env = new Env();
		env = p.accept(new CheckProgram(), env);
		p.accept(new CheckStm(), env);
	}
}
