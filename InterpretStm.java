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
		interpretExp(s.exp_, vt);
		return make_void();
	}
	
	public Value visit(CPP.Absyn.SInit p, ValueTable vt) {
		Value v = interpretExp(p.exp_, vt);
		System.out.println(v);
		vt.addVar(p.id_, v);
		return make_void();
	}
	
	public Value visit(CPP.Absyn.SReturn p, ValueTable vt) {
		Value v = interpretExp(p.exp_, vt);
		System.out.println("return: " + v);
		return v;
	}
	public Value visit(CPP.Absyn.SWhile p, ValueTable vt) {
		Value v = interpretExp(p.exp_, vt);
		while ((boolean) v.getValue()) {
			doStm(p.stm_, vt);
			v = interpretExp(p.exp_, vt);
		}
		return make_void();
	}
	
	public Value doStm(Stm stm, ValueTable vt) {
		vt.pushScope();
		stm.accept(this, vt);	
		vt.popScope();
		return make_void();
	}
	
	public Value visit(CPP.Absyn.SBlock p, ValueTable vt) {
		vt.pushScope();
		for (Stm s : p.liststm_) {
			s.accept(this, vt);	
		}
		vt.popScope();
		return make_void();
	}
	
	public Value visit(CPP.Absyn.SIfElse p, ValueTable vt) {
		boolean doIf = (boolean) interpretExp(p.exp_, vt).getValue(); 
		if (doIf) {
			doStm(p.stm_1, vt);
		}
		else {
			doStm(p.stm_2, vt);
		}
		return make_void();
	}
}