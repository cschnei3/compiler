import java.util.HashMap;
import CPP.Absyn.*;
import java.util.LinkedList;

public class Env {
	public HashMap<String,FunType> signature ;
	public LinkedList<HashMap<String,Type>> contexts ;
	
	public static Type lookupVar(String id) { 
		return new Type_int();
	}
	public static FunType lookupFun(String id) {
		return new FunType();
	}
	public static void updateVar (String id, Type ty) {}
}