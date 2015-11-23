import java.util.HashMap;
import java.util.LinkedList;

import CPP.Absyn.*;

public class ValueTable {
	public LinkedList<HashMap<String, Value>> values = new LinkedList<HashMap<String, Value>>();
	public HashMap<String, IntrFun> funs = new HashMap<String, IntrFun>();
	
	public String function;
	
	public ValueTable() {
		
	}
	
	public void pushScope() {
		values.addLast(new HashMap<String, Value>());
	}
	
	public void popScope() {
		values.removeLast();
	}
	
	public HashMap<String, Value> getTopScope() {
		return values.getLast();
	}
	
	public void updateVar(String id, Value v) {
		HashMap<String, Value> context = getTopScope();
		context.put(id, v);
	}
	
	public <T> void updateVar (String id, Type ty, T val) {
		//System.out.println("Updating id " + id + " of type " + TypeCode.printTC(ty));
		HashMap<String, Value> context = getTopScope();
		updateVar(id, new Value<T>(ty, val));
	}
	
	public Object getVar(String id) {
		for (int i = values.size() - 1; i >= 0; i--) {
			HashMap<String, Value> scope = values.get(i);
			if (scope.containsKey(id)) {
				return scope.get(id).getValue();
			}
		}
		throw new Error("unreachable code reached");
	}
	
	public Value lookupVar(String id) {
		for (int i = values.size() - 1; i >= 0; i--) {
			HashMap<String, Value> scope = values.get(i);
			if (scope.containsKey(id)) {
				return scope.get(id);
			}
		}
		throw new Error("unreachable code reached");
	}
	
	public IntrFun getFun(String id) {
		if (id.equals("main") && !funs.containsKey(id)) {
			return null;
		}
		return funs.get(id);
	}
	
	public void addFun(String id, LinkedList<String> names, LinkedList<Stm> body) {
//		System.err.println("Adding fun: " + id);
		funs.put(id, new IntrFun(names, body));
	}
	
}