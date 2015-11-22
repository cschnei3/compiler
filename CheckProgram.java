import java.util.LinkedList;

import CPP.Absyn.*;

public class CheckProgram implements 
	Program.Visitor<Env,Env>,
	Def.Visitor<Env, Env>,
	Arg.Visitor<Type, Env> {
	public Env visit(PDefs p, Env env) {
		
		for (Def x : p.listdef_) {
			x.accept(this, env);
		}
		return env;
	}
	
	public Env visit(CPP.Absyn.DFun p, Env env) {
		String id = p.id_;
		Type retType = p.type_;
		LinkedList<Type> argTypes = new LinkedList<Type>();
		
		for (Arg a: p.listarg_) {
			argTypes.add(a.accept(this, env));
		}
		
		env.addFun(id, retType, argTypes);
		
		return env;
	}
	
	public Type visit(CPP.Absyn.ADecl p, Env env) {
		return p.type_;
	}
}