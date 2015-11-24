import CPP.Absyn.*;
import CPP.PrettyPrinter;

public class InterpretStm implements Stm.Visitor<Value,ValueTable> { 
//    Def.Visitor<ValueTable, ValueTable>, 
//    Arg.Visitor<ValueTable, ValueTable> {

	public Value make_void() {
		return new Value(new Type_void(), null);
	}
	
	public static Value interpretExp(Exp exp, ValueTable vt) {
		return exp.accept(new InterpretExp(), vt);
	}
	
	//public ValueTable visit(CPP.Absyn.DFun p, ValueTable vt) {
	//	
	//}
	//
	//public ValueTable visit(CPP.Absyn.ADecl p, ValueTable vt) {
	//	// ignore
	//	return vt;
	//}
	
	public Value visit(SDecls p, ValueTable vt) {
		for (String id : p.listid_) {
			vt.addVar(id, new Value(new Type_void(), null));	
		}
		
		return make_void();
	}
	
	public Value visit(SExp s, ValueTable vt) {
		return interpretExp(s.exp_, vt);
	}
	
	public Value visit(CPP.Absyn.SInit p, ValueTable vt) {
		Value v = interpretExp(p.exp_, vt);
		//System.out.println(v);
		vt.addVar(p.id_, v);
		return make_void();
	}
	
	public Value visit(CPP.Absyn.SReturn p, ValueTable vt) {
		Value v = interpretExp(p.exp_, vt);
		vt.returning = true;
		return v;
	}
	public Value visit(CPP.Absyn.SWhile p, ValueTable vt) {
		Value v = interpretExp(p.exp_, vt);
		Value ret = make_void();
		while ((boolean) v.getValue()) {
			ret = doStm(p.stm_, vt);
			v = interpretExp(p.exp_, vt);
		}
		return ret;
	}
	
	public Value doStm(Stm stm, ValueTable vt) {
		vt.pushScope();
		Value val = stm.accept(this, vt);	
		vt.popScope();
		return val;
	}
	
	public Value visit(CPP.Absyn.SBlock p, ValueTable vt) {
		vt.pushScope();
		Value ret = make_void();
		for (Stm s : p.liststm_) {
			ret = s.accept(this, vt);	
		}
		vt.popScope();
		return ret;
	}
	
	public Value visit(CPP.Absyn.SIfElse p, ValueTable vt) {
		boolean doIf = (boolean) interpretExp(p.exp_, vt).getValue();
		
		if (doIf) {
			return doStm(p.stm_1, vt);
		}
		else {
			return doStm(p.stm_2, vt);
		}
	}
}