import CPP.Absyn.*;

public class CheckStm implements 
	    Stm.Visitor<Env,Env>, 
	    Program.Visitor<Env, Env>, 
	    Def.Visitor<Env, Env>, 
	    Arg.Visitor<Env, Env>
{
	public static Type inferExp(Exp exp, Env env) {
		return exp.accept(new InferExpType(), env);
	}
	public Env visit(PDefs p, Env env) {
		
		for (Def x : p.listdef_) {
			x.accept(this, env);
		}
		return env;
	}
	
	public Env visit(CPP.Absyn.DFun p, Env env) {
		String id = p.id_;
		Type retType = p.type_;
		
		env.pushScope(id);
		
		for(Arg a : p.listarg_){
            a.accept(this, env);
        }
		for (Stm s: p.liststm_) {
            s.accept(this, env);	    
        }
		
		env.popScope();
		
		return env;
	}
    
    public Env visit(CPP.Absyn.ADecl p, Env env) {
        env.cur_fun.local_vars += 1;
        env.updateVar(p.id_, p.type_);
        return env;
    }

	public Env visit(SDecls p, Env env) {
		for (String id : p.listid_) {
            env.cur_fun.local_vars += 1;
			env.updateVar(id, p.type_) ;
		}
		return env ;
	}

	public Env visit(SExp s, Env env) {
        s.exp_.accept(new InferExpType(), env);
        env.popStack();
	    return env ;
	}
	
	public int tc(Type t) {
		return TypeCode.typeCode(t);
	}
	
    public Env visit(CPP.Absyn.SInit p, Env env) {
    	Type expType = inferExp(p.exp_, env);
    	Type varType = p.type_;

        env.cur_fun.local_vars += 1;

        if(tc(expType) != tc(varType)){
            throw new TypeException("Assignment must match initilization");
        }		
        env.popStack();
		env.updateVar(p.id_, p.type_);
        return env;
	}

    public Env visit(CPP.Absyn.SReturn p, Env env) {
    	Type retType = env.lookupReturnVal();
    	Type expType = inferExp(p.exp_, env);
    	
    	//System.out.println("return expresion type: " + TypeCode.printTC(expType));
    	
        if (tc(retType) != tc(expType)){
            throw new TypeException("Return type is wrong");
        }
        env.popStack();
        return env;
    }
    public Env visit(CPP.Absyn.SWhile p, Env env) {
    	checkBool(p.exp_, env);
    	
        env.pushScope();
        p.stm_.accept(this, env);
        env.popScope();
    	return env;
    }
    public Env visit(CPP.Absyn.SBlock p, Env env) {
        env.pushScope();
        for (Stm s : p.liststm_) {
        	s.accept(this, env);	
        }
        
        env.popScope();
    	return env;
    }
    
    public Env visit(CPP.Absyn.SIfElse p, Env env) {
    	// condition
    	checkBool(p.exp_, env);
    	// if
        env.pushScope();
        p.stm_1.accept(this, env);
        env.popScope();
        // else
    	env.pushScope();
        p.stm_2.accept(this, env);
        env.popScope();
        return env;
    }    
    
    public void checkBool(Exp exp, Env env) {
       	Type condType = inferExp(exp, env);
    	if (tc(condType) != TypeCode.CBool) {
    		throw new TypeException("Condition expression must return a boolean");
    	}
    }
}
