import java.util.Scanner;
import CPP.Absyn.*;

public class InterpretExp implements Exp.Visitor<Value, ValueTable> {
	public Value make_void() {
		return new Value(new Type_void(), null);
	}
	
	public Value visit(EApp p, ValueTable vt) {
    	String id = p.id_;
    	ListExp args = p.listexp_;
    	
    	if (id.equals("printInt")) {
    		Value v = args.get(0).accept(this, vt);
    		
    		if (isInt(v)) 
    			System.out.println((int) v.getValue());
    		
    		if (isBool(v)) 
    			System.out.println((boolean) v.getValue());
    		
    		return new Value(new Type_void(), null);
    	}
    	if (id.equals("printDouble")) {
    		Value v = args.get(0).accept(this, vt);
    		System.out.println((double) v.getValue());
    		return new Value(new Type_void(), null);
    	}
    	if (id.equals("readInt")) {
    		Scanner key = new Scanner(System.in);
    		return new Value(new Type_int(), key.nextInt());
    	}
    	if (id.equals("readDouble")) {
    		Scanner key = new Scanner(System.in);
    		return new Value(new Type_double(), key.nextDouble());
    	}
    	
    	IntrFun fun = vt.getFun(id);
    	vt.pushScope();
    	for (int i = 0; i < args.size(); i++) {
    		Exp e = args.get(i);
    		String name = fun.argNames.get(i);
    		vt.updateVar(name, e.accept(this, vt));
    	}
    	Value retVal = make_void();
    	for (Stm s : fun.stms) {
    		retVal = s.accept(new InterpretStm(), vt);
    		if (retVal != null) {
    			break;
    		}
    	}
    	vt.popScope();
		return retVal;
    }
	
	public boolean isInt(Value v) {
		return TypeCode.typeCode(v.type) == TypeCode.CInt;
	}
	public boolean isDouble(Value v) {
		return TypeCode.typeCode(v.type) == TypeCode.CDouble;
	}
	public boolean isBool(Value v) {
		return TypeCode.typeCode(v.type) == TypeCode.CBool;
	}
	public Value visit(ETimes p, ValueTable vt) {
		Value a = p.exp_1.accept(this, vt);
		Value b = p.exp_2.accept(this, vt);
		if (isInt(a)) {
			return new Value(a.type, (int)a.getValue() * (int)b.getValue());
		}
		else {
			return new Value(a.type, (double) a.getValue() * (double) b.getValue());
		}
	}
    public Value visit(EDiv p, ValueTable vt) {
		Value a = p.exp_1.accept(this, vt);
		Value b = p.exp_2.accept(this, vt);
		if (isInt(a)) {
			return new Value(a.type, (int)a.getValue() / (int)b.getValue());
		}
		else {
			return new Value(a.type, (double) a.getValue() / (double) b.getValue());
		}
    }
    public Value visit(EPlus p, ValueTable vt){
		Value a = p.exp_1.accept(this, vt);
		Value b = p.exp_2.accept(this, vt);
		if (isInt(a)) {
			return new Value(a.type, (int)a.getValue() + (int)b.getValue());
		}
		else {
			return new Value(a.type, (double) a.getValue() + (double) b.getValue());
		}
    }
    public Value visit(EMinus p, ValueTable vt){ 
		Value a = p.exp_1.accept(this, vt);
		Value b = p.exp_2.accept(this, vt);
		if (isInt(a)) {
			return new Value(a.type, (int)a.getValue() - (int)b.getValue());
		}
		else {
			return new Value(a.type, (double) a.getValue() - (double) b.getValue());
		}
    }
    public Value visit(ETrue p, ValueTable vt) {
    	return new Value(new Type_bool(), true);
    }
    public Value visit(EFalse p, ValueTable vt) {
    	return new Value(new Type_bool(), false);
    }
    public Value visit(EInt p, ValueTable vt) {
    	return new Value(new Type_int(), p.integer_);
    }
    public Value visit(EDouble p, ValueTable vt){
    	return new Value(new Type_double(), p.double_);
    }
    public Value visit(EId p, ValueTable vt) {
    	return vt.lookupVar(p.id_);
    }
 
    
    public Value incr(String id, ValueTable vt) {
		Value a = vt.lookupVar(id);
		Value newVal;
		if (isInt(a)) {
			newVal = new Value(a.type, (int)a.getValue() + (int) 1);
		}
		else {
			newVal = new Value(a.type, (double) a.getValue() + (double) 1.0);
		}
		
		vt.updateVar(id, newVal);
		return newVal;
	}
		
	public Value decr(String id, ValueTable vt) {
		Value a = vt.lookupVar(id);
		Value newVal;
		if (isInt(a)) {
			newVal = new Value(a.type, (int)a.getValue() - (int) 1);
		}
		else {
			newVal = new Value(a.type, (double) a.getValue() + (double) 1.0);
		}
		vt.updateVar(id, newVal);
		return newVal;
	}
    
    public Value visit(EPostIncr p, ValueTable vt) {
    	return incr(p.id_, vt);
    }
    public Value visit(EPostDecr p, ValueTable vt) {
    	return decr(p.id_, vt);
    }
    public Value visit(EPreIncr p, ValueTable vt){
    	return incr(p.id_, vt);
    }
    public Value visit(EPreDecr p, ValueTable vt){
    	return decr(p.id_, vt);
    } 
    
    public Value visit(ELt p, ValueTable vt) {
		Value a = p.exp_1.accept(this, vt);
		Value b = p.exp_2.accept(this, vt);
		
		if (isBool(a)) {
			int val_a = (boolean) a.getValue() ? 1 : 0;
			int val_b = (boolean) b.getValue() ? 1 : 0;
			return new Value(new Type_bool(), val_a < val_b);
		}
		else if (isInt(a)) {
			return new Value(new Type_int(), (int) a.getValue() < (int) b.getValue());
		}
		else if (isDouble(a)) {
			return new Value(new Type_double(), (double) a.getValue() < (double) b.getValue());
		}
		else {
			throw new Error("type checker broken");
		}
    }
    public Value visit(EGt p, ValueTable vt) {
    	Value a = p.exp_1.accept(this, vt);
		Value b = p.exp_2.accept(this, vt);
		
		if (isBool(a)) {
			int val_a = (boolean) a.getValue() ? 1 : 0;
			int val_b = (boolean) b.getValue() ? 1 : 0;
			return new Value(new Type_bool(), val_a > val_b);
		}
		else if (isInt(a)) {
			return new Value(new Type_int(), (int) a.getValue() > (int) b.getValue());
		}
		else if (isDouble(a)) {
			return new Value(new Type_double(), (double) a.getValue() > (double) b.getValue());
		}
		else {
			throw new Error("type checker broken");
		}
    }
    public Value visit(ELtEq p, ValueTable vt) {
    	Value a = p.exp_1.accept(this, vt);
		Value b = p.exp_2.accept(this, vt);
		
		if (isBool(a)) {
			int val_a = (boolean) a.getValue() ? 1 : 0;
			int val_b = (boolean) b.getValue() ? 1 : 0;
			return new Value(new Type_bool(), val_a <= val_b);
		}
		else if (isInt(a)) {
			return new Value(new Type_int(), (int) a.getValue() <= (int) b.getValue());
		}
		else if (isDouble(a)) {
			return new Value(new Type_double(), (double) a.getValue() <= (double) b.getValue());
		}
		else {
			throw new Error("type checker broken");
		}
    }
    public Value visit(EGtEq p, ValueTable vt){ 
    	Value a = p.exp_1.accept(this, vt);
		Value b = p.exp_2.accept(this, vt);
		
		if (isBool(a)) {
			int val_a = (boolean) a.getValue() ? 1 : 0;
			int val_b = (boolean) b.getValue() ? 1 : 0;
			return new Value(new Type_bool(), val_a >= val_b);
		}
		else if (isInt(a)) {
			return new Value(new Type_int(), (int) a.getValue() >= (int) b.getValue());
		}
		else if (isDouble(a)) {
			return new Value(new Type_double(), (double) a.getValue() >= (double) b.getValue());
		}
		else {
			throw new Error("type checker broken");
		}
    }
    public Value visit(EEq p, ValueTable vt) {
    	Value a = p.exp_1.accept(this, vt);
		Value b = p.exp_2.accept(this, vt);
		
		if (isBool(a)) {
			int val_a = (boolean) a.getValue() ? 1 : 0;
			int val_b = (boolean) b.getValue() ? 1 : 0;
			return new Value(new Type_bool(), val_a == val_b);
		}
		else if (isInt(a)) {
			return new Value(new Type_int(), (int) a.getValue() == (int) b.getValue());
		}
		else if (isDouble(a)) {
			return new Value(new Type_double(), (double) a.getValue() == (double) b.getValue());
		}
		else {
			throw new Error("type checker broken");
		}
     }
    public Value visit(ENEq p, ValueTable vt) { 
    	Value a = p.exp_1.accept(this, vt);
		Value b = p.exp_2.accept(this, vt);
		
		if (isBool(a)) {
			int val_a = (boolean) a.getValue() ? 1 : 0;
			int val_b = (boolean) b.getValue() ? 1 : 0;
			return new Value(new Type_bool(), val_a != val_b);
		}
		else if (isInt(a)) {
			return new Value(new Type_int(), (int) a.getValue() != (int) b.getValue());
		}
		else if (isDouble(a)) {
			return new Value(new Type_double(), (double) a.getValue() != (double) b.getValue());
		}
		else {
			throw new Error("type checker broken");
		}
    }
    public Value visit(EAnd p, ValueTable vt) {
    	Value a = p.exp_1.accept(this, vt);
		
		if ((boolean)a.getValue()) {
			Value b = p.exp_2.accept(this, vt);
			return new Value(new Type_bool(), (boolean)b.getValue());
		}
		else {
			return new Value(new Type_bool(), false);
		}
    }
    public Value visit(EOr p, ValueTable vt) {
    	Value a = p.exp_1.accept(this, vt);
    	
    	if ((boolean) a.getValue()) {
    		return new Value(new Type_bool(), true);
    	}
    	else {
    		Value b = p.exp_2.accept(this, vt);
    		return new Value(new Type_bool(), (boolean) b.getValue());
    	}
    }
    public Value visit(EAss p, ValueTable vt) {
    	Value a = p.exp_.accept(this, vt);
    	vt.updateVar(p.id_, a.type, a);
    	return a;
    }
}