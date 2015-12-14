import CPP.Absyn.*;

public class TypeCode implements Type.Visitor<Integer, Env> {
	// pseudo enum
	public static final Integer 
			CInt = Integer.valueOf(1), 
			CBool = Integer.valueOf(3), 
			CVoid = Integer.valueOf(4);
	
	public static String printTC(Type t) {
		int x = typeCode(t);
		
		switch(x) {
			case 1:
				return "Int";
			case 3:
				return "Bool";
			case 4:
				return "Void";
		}
		
		return "Invalid type code";
	}
	
	public static int typeCode (Type ty) {
		return ty.accept(new TypeCode(), null);
	}
	
	public Integer visit(Type_bool p, Env env) {
		return CBool;
	}
	
	public Integer visit(Type_int p, Env env) {
		return CInt;
	}
	
	public Integer visit(Type_void p, Env env) {
		return CVoid;
	}
}
