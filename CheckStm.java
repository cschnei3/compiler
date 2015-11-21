import CPP.Absyn.*;

public class CheckStm implements Stm.Visitor<Env,Env> {
	public static Type inferExp(Exp exp, Env env) {
		return new Type_int();
	}
	
	public Env visit(SDecls p, Env env) {
		for (String id : p.listid_) {
			env.updateVar(id, p.type_) ;
		}

		return env ;
	}
	public Env visit(SExp s, Env env) {
		inferExp(s.exp_, env) ;
		return env ;
	}
	public Env visit(CPP.Absyn.SInit p, Env env) {
		return env;
	}
    public Env visit(CPP.Absyn.SReturn p, Env env) {
    	return env;
    }
    public Env visit(CPP.Absyn.SWhile p, Env env) {
    	return env;
    }
    public Env visit(CPP.Absyn.SBlock p, Env env) {
    	return env;
    }
    public Env visit(CPP.Absyn.SIfElse p, Env env) {
    	return env;
    }
}
