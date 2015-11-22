import CPP.Absyn.*;

public class TypeCode implements Type.Visitor<Integer, Env> {
	// pseudo enum
	public static final Integer 
			CInt = Integer.valueOf(0), 
			CDouble = Integer.valueOf(1), 
			CString = Integer.valueOf(2), 
			CBool = Integer.valueOf(3), 
			CVoid = Integer.valueOf(4);
	
	public static int typeCode (Type ty) {
		return CInt;
	}
	
	public Integer visit(Type_bool p, Env env) {
		return CBool;
	}
	
	public Integer visit(Type_int p, Env env) {
		return CInt;
	}
	
	public Integer visit(Type_double p, Env env) {
		return CDouble;
	}
	
	public Integer visit(Type_void p, Env env) {
		return CVoid;
	}
}