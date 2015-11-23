import CPP.Absyn.*;
import java.util.LinkedList;

public class Interpreter implements  
	Program.Visitor<ValueTable, ValueTable>, 
	Def.Visitor<ValueTable, ValueTable>,
	Arg.Visitor<String, ValueTable> {

    public void interpret(Program p) {
		ValueTable vt = new ValueTable();
		
		vt = p.accept(this, vt);
		
		IntrFun main = vt.getFun("main");
		if (main == null) {
			System.err.println("no main found");
			return;
		}
		
		for (Stm s : main.stms) {
			vt = s.accept(new InterpretStm(), vt);
		}		
    }

	public ValueTable visit(PDefs p, ValueTable vt) {
		for (Def x : p.listdef_) {
			x.accept(this, vt);
		}
		return vt;
	}
	
	public ValueTable visit(CPP.Absyn.DFun p, ValueTable vt) {
		String id = p.id_;
		Type retType = p.type_;
		
		LinkedList<String> names = new LinkedList<String>();
		LinkedList<Stm> body = new LinkedList<Stm>();
		
		for (Arg a: p.listarg_) {
			names.add(a.accept(this, vt));
		}
		
		for (Stm s : p.liststm_) {
			body.add(s);
		}
		
		vt.addFun(id, names, body);
		
		return vt;
	}
	
	public String visit(CPP.Absyn.ADecl p, ValueTable vt) {
		return p.id_;
	}
}
