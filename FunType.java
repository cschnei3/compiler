import java.util.LinkedList;
import CPP.Absyn.*;

public class FunType {
	public LinkedList<Type> args ;
	public Type retVal ;
    FunType(Type  _retVal, LinkedList<Type> _args){
        retVal = _retVal;
        for(Type str : _args){
            args.add(str);
        }
    }

    public static Type findType(String str){
        if(str == "int"){
            
        }
    }
}
