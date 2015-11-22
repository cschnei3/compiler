import CPP.Absyn.*;
import java.util.LinkedList;

public class Interpreter implements  
	Program.Visitor<ValueTable, ValueTable>, 
	Def.Visitor<ValueTable, ValueTable>,
	Arg.Visitor<String, ValueTable> {

    public void interpret(Program p) {
		ValueTable env = new ValueTable();
		env = p.accept(this, env);
		
		//p.accept(new InterpretStm(), env);
		
    }

	public ValueTable visit(PDefs p, ValueTable env) {
		for (Def x : p.listdef_) {
			x.accept(this, env);
		}
		return env;
	}
	
	public ValueTable visit(CPP.Absyn.DFun p, ValueTable env) {
		String id = p.id_;
		Type retType = p.type_;
		
		LinkedList<String> names = new LinkedList<String>();
		LinkedList<Stm> body = new LinkedList<Stm>();
		
		for (Arg a: p.listarg_) {
			names.add(a.accept(this, env));
		}
		
		for (Stm s : p.liststm_) {
			body.add(s);
		}
		
		env.addFun(id, names, body);
		
		return env;
	}
	
	public String visit(CPP.Absyn.ADecl p, ValueTable env) {
		return p.id_;
	}
}
