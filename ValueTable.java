import java.util.HashMap;
import java.util.LinkedList;

public class ValueTable {
	public LinkedList<HashMap<String, Value<T>>> values = new LinkedList<HashMap<String, Value<T>>>();
	public LinkedList<String, IntrFun> funs = new LinkedList<String, IntrFun>();
	
	public String function;
	
	public ValueTable() {
		
	}
	
	public HashMap<String, Value<T>> getTopScope() {
		return values.getLast();
	}
	
	public <T> void updateVar (String id, Type ty, T val) {
		//System.out.println("Updating id " + id + " of type " + TypeCode.printTC(ty));
		HashMap<String, Type> context = getTopScope();
		
		values.put(id, new Value<T>(ty, val));
	}
	
	public <T> T getVar(String id) {
		for (int i = values.size() - 1; i >= 0; i--) {
			HashMap<String, Value<T>> scope = values.get(i);
			if (scope.containsKey(id)) {
				return scope.get(id).getValue();
			}
		}
	}
	
	public IntrFun getFun(String id) {
		return funs.get(id);
	}
	
	public void addFun(String id, LinkedList<String> names, LinkedList<Stm> body) {
		funs.put(id, new IntrFun(names, body));
	}
	
}