import CPP.Absyn.*;

class Generator{
    private void compileStm(Stm st, Object arg) {
        st.accept(new StmCompiler(), arg);
    }
    private class StmCompiler implements Stm.Visitor<Object,Object> {
        public Object visit(Mini.Absyn.SDecl p, Object arg) {
            ContextTable.addVar(p.ident_, typeCode(p.type));
            return null;
        }
    }

    private Object compileExp(Exp e, Object arg) {
        return e.accept(new ExpCompiler(), arg);
    }
}

