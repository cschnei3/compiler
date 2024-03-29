import CPP.Absyn.*;
import java.util.HashMap;
import java.util.LinkedList;

public class Env {
	public HashMap<String,FunType> signature = new HashMap<String, FunType>();
	public LinkedList<HashMap<String,Type>> contexts = new LinkedList<HashMap<String,Type>>(); 
	
	
    String function;
    FunType cur_fun;
    public int local_vars = 0, stack_size = 0, max_stack = 0;

	public Env() {
		pushScope();
		init_bifs();
	}

    public void pushStack() {
        cur_fun.stack_size++;
        if (cur_fun.stack_size > cur_fun.max_stack) cur_fun.max_stack = cur_fun.stack_size;
    }

    public void popStack() {
        cur_fun.stack_size--;
    }
	
	public String toString() {
		String ret = "";
		for (String s : signature.keySet()) {
			ret += s + "\n";
		}
		return ret;
	}
	
	// utility function
	private LinkedList<Type> one_arg_list(Type t) {
		LinkedList<Type> retVal = new LinkedList<Type>();
		retVal.push(t);
		return retVal;
	}
	// built in functions
	private void init_bifs() {
		addFun("printInt", new Type_void(), one_arg_list(new Type_int()));
		addFun("readInt", new Type_int(), null);
	}
	

	public void popScope() {
		HashMap<String, Type> topContext = getTopScope();
		
		for(String key : topContext.keySet()) {
			//System.err.println("scope id: " + key);
		}
		
		contexts.removeLast();
		//System.err.println("popping scope: contexts size is now" + contexts.size());
	}
	public void pushScope(String funName) {
		
		if(funName != null){
            function = funName;
            cur_fun = lookupFun(function);
        }
		
        pushScope();
	}
	
	public void pushScope() {
		contexts.addLast(new HashMap<String, Type>());
		//System.err.println("pushing scope, contexts size " + contexts.size());
	}
	
	// the innermost scope, ie, the scope with no scopes inside of it
	public HashMap<String, Type> getTopScope() {
		return contexts.get(contexts.size() - 1);
	}
	
    public  boolean addFun(String name, Type retType, LinkedList<Type> arguments){
    	if (name.equals("main")) {
    		//System.err.println("found main");
    		if (arguments.size() > 0) {
    			throw new TypeException("Main function cannot take args");
    		}
    	}
    	if (signature.containsKey(name)) {
    		// function already defined
    		return false;
    	}
    	else {
    		signature.put(name, new FunType(retType, arguments));
    	}
    	
    	return true;
    }
    
    public void checkFunArgs(String id, LinkedList<Type> callArgs) throws TypeException {
    	LinkedList<Type> funArgs = lookupFun(id).args;
    	String errMsg = "Function call argument mismatch";
    	
    	if (funArgs.size() != callArgs.size()) throw new TypeException(errMsg);
    	
    	for(int i = 0; i < funArgs.size(); i++) {
    		Type funType = funArgs.get(i);
    		Type callType = callArgs.get(i);
    		int funTc = TypeCode.typeCode(funType);
    		int callTc = TypeCode.typeCode(callType);
    		if (funTc != callTc) {
    			throw new TypeException(errMsg);
    		}
    	}
    }
    
	public  FunType lookupFun(String id) {
        if (signature.containsKey(id)) {
            return signature.get(id);
        }
        else {
        	throw new TypeException("Function: " + id + " not defined");
        }
	}
 	public  Type lookupReturnVal() {
 		return lookupFun(function).retType;
	}
       
	public Type lookupVar(String id) { 
		// iterate backwards to find the var in the innermost scope
		for (int i = contexts.size() - 1; i >= 0; i--) {
			HashMap<String, Type> scope = contexts.get(i);
            if(scope.containsKey(id)){
		        return scope.get(id);
            }
	    }
        throw new TypeException("Variable: " + id + " not defined");
    }

    // can only ever modify the innermost ("top") scope
	public  void updateVar (String id, Type ty) {
		//System.out.println("Updating id " + id + " of type " + TypeCode.printTC(ty));
		HashMap<String, Type> context = getTopScope();
		
		if (context.containsKey(id)) {
			throw new TypeException("Variable: " + id + " already defined");
		}
		
		context.put(id, ty);
	}
}
