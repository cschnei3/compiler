public class Value<R> {
	R val;
	Type type;
	
	public Value (R v, Type t) {
		val = v;
		type = t;
	}
	
	
	public R getValue() {
		return val;
	}
	public boolean setValue(R r) {
		val = r;
	}
	public Type getType() {
		return type;
	}
}