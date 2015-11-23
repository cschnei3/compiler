import CPP.Absyn.*;

public class Value<R> {
	R val;
	Type type;
	
	public Value (Type t, R v) {
		val = v;
		type = t;
	}
	
	
	public R getValue() {
		return val;
	}
	
	public void setValue(R r) {
		val = r;
		
	}
	public Type getType() {
		return type;
	}
}