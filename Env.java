import java.util.HashMap;
import CPP.Absyn.*;
import java.util.LinkedList;

public class Env {
	public HashMap<String,FunType> signature ;
	public LinkedList<HashMap<String,Type>> contexts ;
	
	public  Type lookupVar(String id) { 
        for(HashMap<String, Type> scope : contexts){
            if(scope.containsKey(id)){
		        return scope.get(id);
            }
	    }
        //possiably throw error here
        return null;
    }
    //need a way of checking call to make sure it has the right number of variables 
	public  FunType lookupFun(String id) {
        if(signature.containsKey(id)){
            return signature.get(id);
        }
		return null;
	}
    public  boolean addFun(Type name, LinkedList<Type> arguments){
       return true;
    }

	public  void updateVar (String id, Type ty) {}
}
