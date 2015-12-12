private class ExpCompiler implements Exp.Visitor<Object,Object> {
    public Object visit(Mini.Absyn.EMul p, Object arg) {
        p.exp_1.accept(this, arg) ;
        p.exp_2.accept(this, arg) ;
        if (typeCodeExp(p.exp_1) == TypeCode.INT) {
            System.err.println("imul");
        } else {
            System.err.println("dmul");
        }
        return null ;
    }
}
