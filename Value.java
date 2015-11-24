import CPP.Absyn.*;

public class Value<R> {
	R val;
	Type type;
	
	public Value (Type t, R v) {
		val = v;
		type = t;
	}
	
	
	public R getValue() {
	//	System.err.println("returning val " + val);
		return val;
	}
	
	public void setValue(R r) {
		val = r;
		
	}
	public Type getType() {
		return type;
	}
	public String toString() {
		return "type " + type + " value " + val;
	}
}