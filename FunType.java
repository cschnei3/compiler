import java.util.LinkedList;
import CPP.Absyn.*;

public class FunType {
	public LinkedList<Type> args ;
	public Type retType ;
    public int stack_size = 0, max_stack = 0, local_vars = 0;
    public FunType(Type  _retType, LinkedList<Type> _args) {
    	if (_args == null) {
    		args = new LinkedList<Type>();
    	}
    	else {
    		args = (LinkedList<Type>)_args.clone();	
    	}
    	
    	retType = _retType;
    }
}
