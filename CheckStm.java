import CPP.Absyn.*;

public class CheckStm implements 
                                Stm.Visitor<Env,Env>, 
                                Program.Visitor<Env, Env>, 
                                Def.Visitor<Env, Env>, 
                                Arg.Visitor<Env, Env>
{
	public static Type inferExp(Exp exp, Env env) {
		return new Type_int();
	}
	public Env visit(PDefs p, Env env) {
		System.out.println("Visiting your program yoooo");
		
		for (Def x : p.listdef_) {
			//p.listdef_.add(x.accept(this,env));
			x.accept(this, env);
		}
		return env;//return new CPP.Absyn.PDefs(listdef_);
	}
	
	public Env visit(CPP.Absyn.DFun p, Env env) {
		String id = p.id_;
		Type retType = p.type_;
		
		//System.out.println()
		for(Arg a : p.listarg_){
            a.accept(this, env);
        }
		for (Stm a: p.liststm_) {
            a.accept(this, env);	    
        }
		
		return env;
	}
    
    public Env visit(CPP.Absyn.ADecl p, Env e){
        e.addVar(p.id_, p.type_);
        return e;
    }

	public Env visit(SDecls p, Env env) {
		for (String id : p.listid_) {
			env.updateVar(id, p.type_) ;
		}
		return env ;
	}

	public Env visit(SExp s, Env env) {
       s.exp_.accept(this, env);
		return env ;
	}
	
    public Env visit(CPP.Absyn.SInit p, Env env) {
        if(inferExp(p.exp_, env) != p.type_){
            throw new TypeException("Assignment must match initilization");
        }
		env.updateVar(p.id_, p.type_);
        return env;
	}

    public Env visit(CPP.Absyn.SReturn p, Env env) {
        if(env.lookupReturnVal() != inferExp(p.exp_) ){
            throw new TypeException("Return type is wrong");
        }
        return env;
    }
    public Env visit(CPP.Absyn.SWhile p, Env env) {
        p.exp_.accept(this, env);
        env.pushScope();
        p.stm_.accept(this, env);
        env.popScope();
    	return env;
    }
    public Env visit(CPP.Absyn.SBlock p, Env env) {
        env.pushScope();
        p.stm_.accept(this, env);
        env.popScope();
    	return env;
    }
    public Env visit(CPP.Absyn.SIfElse p, Env env) {
    	p.exp_.accept(this, env);
        env.pushScope();
        p.stm_1.accept(this, env);
        env.popScope();
    	env.pushScope();
        p.stm_2.accept(this, env);
        env.popScope();
        return env;
    }
}
