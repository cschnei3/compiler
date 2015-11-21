import CPP.Absyn.*;
import java.util.HashMap;
import java.util.LinkedList;

public class TypeChecker {
	
	public void typecheck(Program p) {
		Env env = new Env();
		
		
		//p.accept(new CheckProgram(), env);
	}
	
	public static class ProgramImpl extends Program {
        
        @Override
		public <R,A> R accept(Program.Visitor<R, A> v, A env) {
            return null;
			//return v.visit( this, env );
		}
	
    }
	
	public static enum TypeCode { CInt, CDouble, CString, CBool, CVoid }
	
	public static TypeCode typeCode (Type ty) {
		return TypeCode.CInt;
	}
	
	public static Type inferExp(Exp exp, Env env) {
		return new Type_int();
	}
	

	// ... checking different statements
}
