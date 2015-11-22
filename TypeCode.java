public class TypeCode implements Type.Visitor<TypeCode, Env> {
	// pseudo enum
	public int 
			CInt = 0, 
			CDouble = 1, 
			CString = 2, 
			CBool = 3, 
			CVoid = 4;
	
	public static TypeCode typeCode (Type ty) {
		return CInt;
	}
	
	public TypeCode visit(Type_bool p, Env env) {
		return CBool;
	}
	
	public TypeCode visit(Type_int p, Env env) {
		return CInt;
	}
	
	public TypeCode visit(Type_double p, Env env) {
		return CDouble;
	}
	
	public TypeCode visit(Type_void p, Env env) {
		return CVoid;
	}
}