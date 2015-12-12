import CPP.Absyn.*;

public class Generator implements
	Program.Visitor<Object, Object>, 
	Def.Visitor<Object, Object>,
	Arg.Visitor<Object, Object>,
    Exp.Visitor<Object, Object> {

    public Object visit(CPP.Absyn.EMul p, Object arg) {
        p.exp_1.accept(this, arg) ;
        p.exp_2.accept(this, arg) ;
        if (typeCodeExp(p.exp_1) == TypeCode.INT) {
            ct.writeInstr("imul");
        } else {
            ct.writeInstr("dmul");
        }
        return null ;
    }

    private void compileStm(Stm st, Object arg) {
        st.accept(new Generator(), arg);
    }

    public Object visit(CPP.Absyn.SDecl p, Object arg) {
        ct.addVar(p.ident_, typeCode(p.type));
        return null;
    }

    private Object compileExp(Exp e, Object arg) {
        return e.accept(new Generator(), arg);
    }
}

