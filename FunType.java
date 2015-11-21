import java.util.LinkedList;
import CPP.Absyn.*;

public class FunType {
	public LinkedList<Type> args ;
	public Type retType ;
    public FunType(Type  _retType, LinkedList<Type> _args) {
    	args = (LinkedList<Type>)_args.clone();
        retType = _retType;
    }

}
