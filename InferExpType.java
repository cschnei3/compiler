import CPP.Absyn.*;

public class InferExpType implements Exp.Visitor<Type,Env> {
		public static TypeCode typeCode (Type ty) {
			return TypeCode.CInt;
		}
		
		public Type visit(ETimes p, Env env) {
			Type t1 = p.exp_1.accept(this, env);
			Type t2 = p.exp_2.accept(this, env);
			if (typeCode(t1) == TypeCode.CInt &&
				typeCode(t2) == TypeCode.CInt) {
				return new Type_int();
			}
			else if (typeCode(t1) == TypeCode.CDouble &&
					 typeCode(t2) == TypeCode.CDouble) {
				return new Type_double();
			}
			else {
				throw new TypeException("Operands to * must be int or double.");
			}
		}
	    public Type visit(ETrue p, Env env) {
	    	return new Type_int();
	    }
	    public Type visit(EFalse p, Env env){ 
	    	return new Type_int();
	    }
	    public Type visit(EInt p, Env env){ 
	    	return new Type_int();
	    }
	    public Type visit(EDouble p, Env env){ 
	    	return new Type_int();
	    }
	    public Type visit(EId p, Env env){ 
	    	return new Type_int();
	    }
	    public Type visit(EApp p, Env env){ 
	    	return new Type_int();
	    }
	    public Type visit(EPostIncr p, Env env){ 
	    	return new Type_int();
	    }
	    public Type visit(EPostDecr p, Env env){
	    	return new Type_int();
	    }
	    public Type visit(EPreIncr p, Env env){
	    	return new Type_int();
	    }
	    public Type visit(EPreDecr p, Env env){ 
	    	return new Type_int();
	    }
	    public Type visit(EDiv p, Env env){ 
	    	return new Type_int();
	    }
	    public Type visit(EPlus p, Env env){ 
	    	return new Type_int();
	    }
	    public Type visit(EMinus p, Env env){ 
	    	return new Type_int();
	    }
	    public Type visit(ELt p, Env env){ 
	    	return new Type_int();
	    }
	    public Type visit(EGt p, Env env){ 
	    	return new Type_int();
	    }
	    public Type visit(ELtEq p, Env env){
	    	return new Type_int();
	    }
	    public Type visit(EGtEq p, Env env){ 
	    	return new Type_int();
	    }
	    public Type visit(EEq p, Env env){ 
	    	return new Type_int();
	    }
	    public Type visit(ENEq p, Env env){ 
	    	return new Type_int();
	    }
	    public Type visit(EAnd p, Env env){ 
	    	return new Type_int();
	    }
	    public Type visit(EOr p, Env env){
	    	return new Type_int();
	    }
	    public Type visit(EAss p, Env env){
	    	return new Type_int();
	    }
	}