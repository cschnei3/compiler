import CPP.Absyn.*;
import java.util.HashMap;
import java.util.LinkedList;

public class TypeChecker {
	
	public void typecheck(Program p) {
		Env env = new Env();
		
		
		p.accept(new CheckProgram(), env);
	}
	
	public static class ProgramImpl extends Program {
        
        @Override
		public <R,A> R accept(Program.Visitor<R, A> v, A env) {
			System.out.println("???");
			return null;
			//return v.visit( this, env );
		}
	
    }
}
