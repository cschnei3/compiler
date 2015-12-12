import CPP.Absyn.*;
import java.util.LinkedList;

public class CodeGenerator implements
	Program.Visitor<Object, Object>, 
	Def.Visitor<Object, Object>,
	Arg.Visitor<Object, Object>,
	Stm.Visitor<Object, Object>,
    Exp.Visitor<Object, Object> {
		
    ContextTable ct = new ContextTable();
    
    public void codeGenerator(Program p) {
		p.accept(this, ct);
    }
    
    private void compileStm(Stm st, Object arg) {
        st.accept(this, arg);
    }
    
    /* PROGRAM */
    public Object visit(PDefs p, Object unused) {
        return null;
    }
    
    private Object compileExp(Exp e, Object arg) {
        return e.accept(new CodeGenerator(), arg);
    }
    
    /* FUNCTIONS */
    public Object visit(DFun p, Object unused) { return null; }
    
    /* ARGS */
    public Object visit(ADecl p, Object unused) { return null; }
    
    /* STATEMENTS */
    public Object visit(SExp p, Object unused) { return null; }
    public Object visit(SDecls p, Object unused) { return null; }
    public Object visit(SInit p, Object unused) { return null; }
    public Object visit(SReturn p, Object unused) { return null; }
    public Object visit(SWhile p, Object unused) { return null; }
    public Object visit(SBlock p, Object unused) { return null; }
    public Object visit(SIfElse p, Object unused) { return null; }
    
    /* EXPRESSIONS */
    public Object visit(ETrue p, Object unused) { return null; }
    public Object visit(EFalse p, Object unused) { return null; }
    public Object visit(EInt p, Object unused) { return null; }
    public Object visit(EDouble p, Object unused) { return null; }
    public Object visit(EId p, Object unused) { return null; }
    public Object visit(EApp p, Object unused) { return null; }

    public Object visit(EPostIncr p, Object unused) { return null; }
    public Object visit(EPostDecr p, Object unused) { return null; }

    public Object visit(EPreIncr p, Object unused) { return null; }
    public Object visit(EPreDecr p, Object unused) { return null; }

    public Object visit(ETimes p, Object unused) { 
        p.exp_1.accept(this, unused) ;
        p.exp_2.accept(this, unused) ;
        
        ct.writeInstr("imul");
        
        return null ;
    }
    public Object visit(EDiv p, Object unused) { return null; }

    public Object visit(EPlus p, Object unused) { return null; }
    public Object visit(EMinus p, Object unused) { return null; }

    public Object visit(ELt p, Object unused) { return null; }
    public Object visit(EGt p, Object unused) { return null; }
    public Object visit(ELtEq p, Object unused) { return null; }
    public Object visit(EGtEq p, Object unused) { return null; }

    public Object visit(EEq p, Object unused) { return null; }
    public Object visit(ENEq p, Object unused) { return null; }

    public Object visit(EAnd p, Object unused) { return null; }

    public Object visit(EOr p, Object unused) { return null; }

    public Object visit(EAss p, Object unused) { return null; }
}
