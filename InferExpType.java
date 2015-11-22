import CPP.Absyn.*;
import CPP.PrettyPrinter;

public class InferExpType implements Exp.Visitor<Type,Env> {
	
		// check an expression of form Exp Operator Exp
		// where both expressions are the same type of either int or double
		public Type arithmetic(Exp e1, Exp e2, Env env, String errMsg) {
			Type t1 = e1.accept(this, env);
			Type t2 = e2.accept(this, env);
			if (TypeCode.typeCode(t1) == TypeCode.CInt &&
				TypeCode.typeCode(t2) == TypeCode.CInt) {
				return new Type_int();
			}
			else if (TypeCode.typeCode(t1) == TypeCode.CDouble &&
					 TypeCode.typeCode(t2) == TypeCode.CDouble) {
				return new Type_double();
			}
			else {
				throw new TypeException(errMsg);
			}
		}
		
		public Type comparison(Exp e1, Exp e2, Env env, String errMsg) {
			Type t1 = e1.accept(this, env);
			Type t2 = e2.accept(this, env);
			if (TypeCode.typeCode(t1) == TypeCode.CInt &&
				TypeCode.typeCode(t2) == TypeCode.CInt) {
				return new Type_int();
			}
			else if (TypeCode.typeCode(t1) == TypeCode.CDouble &&
					 TypeCode.typeCode(t2) == TypeCode.CDouble) {
				return new Type_double();
			}
			else if (TypeCode.typeCode(t1) == TypeCode.CBool &&
					TypeCode.typeCode(t2) == TypeCode.CBool) {
					return new Type_bool();
			}
			else {
				throw new TypeException(errMsg);
			}
		}
		
		public Type logical(Exp e1, Exp e2, Env env, String errMsg) {
			Type t1 = e1.accept(this, env);
			Type t2 = e2.accept(this, env);
			if (TypeCode.typeCode(t1) == TypeCode.CBool &&
					TypeCode.typeCode(t2) == TypeCode.CBool) {
					return new Type_bool();
			}
			else {
				throw new TypeException(errMsg);
			}
		}
		
	
		public Type visit(ETimes p, Env env) {
			return arithmetic(p.exp1_, p.exp2_, env, "Operands to * must be int or double.");
		}
	    public Type visit(ETrue p, Env env) {
	    	return new Type_bool();
	    }
	    public Type visit(EFalse p, Env env){ 
	    	return new Type_bool();
	    }
	    public Type visit(EInt p, Env env){ 
	    	return new Type_int();
	    }
	    public Type visit(EDouble p, Env env){ 
	    	return new Type_double();
	    }
	    public Type visit(EId p, Env env){
	    	// can return null if the var isn't there
	    	Type t = env.lookupVar(p.id_); 
	    	return t;
	    }
	    public Type visit(EApp p, Env env) {
	    	// visit each expression in the call
	    	LinkedList<Type> argList = new LinkedList<Type>();
	    	for (Exp e : p.listexp_) {
	    		argList.add(e.accept(this, env));
	    	}
	    	if (env.checkFunArgs(p.id_, argList) == false) {
	    		throw new TypeError("Invalid function call arguments");
	    	}
	    	
	    	return lookupFun(p.id_);
	    }
	    
	    public Type intDoubleExp(Exp exp, Env env) {
	    	Type t = exp_.accept(this, env);
	    	TypeCode tc = TypeCode.typeCode(t);
	    	if (tc != new Type_int() || tc != new Type_double()) {
	    		// would be nice to be able to print the expression
	    		// probably would require another visitor, though :(
	    		// would be a visitor returning strings...for each exp thing
	    		throw new TypeError("Type must be int or double for expression: " + PrettyPrinter.print(exp));
	    	}
	    	return t;
	    }
	    
	    public Type visit(EPostIncr p, Env env) {
	    	return intDoubleExp(p.exp_, env);
	    }
	    public Type visit(EPostDecr p, Env env) {
	    	return intDoubleExp(p.exp_, env);
	    }
	    public Type visit(EPreIncr p, Env env){
	    	return intDoubleExp(p.exp_, env);
	    }
	    public Type visit(EPreDecr p, Env env){ 
	    	return intDoubleExp(p.exp_, env);
	    }
	    public Type visit(EDiv p, Env env) {
	    	return arithmetic(p.exp1_, p.exp2_, env, "Operands to / must be int or double.");
	    }
	    public Type visit(EPlus p, Env env){ 
	    	return arithmetic(p.exp1_, p.exp2_, env, "Operands to + must be int or double.");
	    }
	    public Type visit(EMinus p, Env env){ 
	    	return arithmetic(p.exp1_, p.exp2_, env, "Operands to - must be int or double.");
	    }
	    public Type visit(ELt p, Env env){ 
	    	return comparison(p.exp1_, p.exp2_, env, "Operands to < must be bool, int, or double.");
	    }
	    public Type visit(EGt p, Env env){ 
	    	return comparison(p.exp1_, p.exp2_, env, "Operands to > must be bool, int, or double.");
	    }
	    public Type visit(ELtEq p, Env env){
	    	return comparison(p.exp1_, p.exp2_, env, "Operands to <= must be bool, int, or double.");
	    }
	    public Type visit(EGtEq p, Env env){ 
	    	return comparison(p.exp1_, p.exp2_, env, "Operands to >= must be bool, int, or double.");
	    }
	    public Type visit(EEq p, Env env){ 
	    	return comparison(p.exp1_, p.exp2_, env, "Operands to == must be bool, int, or double.");
	    }
	    public Type visit(ENEq p, Env env){ 
	    	return comparison(p.exp1_, p.exp2_, env, "Operands to != must be bool, int, or double.");
	    }
	    public Type visit(EAnd p, Env env){ 
	    	return logical(p.exp1_, p.exp2_, env, "Operands to && must be bool.");
	    }
	    public Type visit(EOr p, Env env){
	    	return logical(p.exp1_, p.exp2_, env, "Operands to || must be bool.");
	    }
	    public Type visit(EAss p, Env env){
	    	Type t1 = p.exp1.accept(this, env);
	    	Type t2 = p.exp2.accept(this, env);
	    	boolean ident = p.exp1.accept(new IdVisitor(), env);
	    	return new Type_int();
	    }
	}