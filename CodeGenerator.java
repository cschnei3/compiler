import CPP.Absyn.*;
import java.util.regex.Pattern;
import java.util.LinkedList;

public class CodeGenerator implements
	Program.Visitor<Object, Object>, 
	Def.Visitor<Object, Object>,
	Arg.Visitor<Object, Object>,
	Stm.Visitor<Object, Object>,
    Exp.Visitor<Object, Object> {
		
    ContextTable ct = new ContextTable();
    String fileName; 

    public void codeGenerator(Program p, String fileName) {
        this.fileName = fileName;
		p.accept(this, ct);
    }
   
    private void stripFileName() {
       fileName = fileName.split(Pattern.quote("."))[0]; 
    }

    private void writeBoilerplate() {
        ct.writeInstr(".class public" + fileName);
        ct.writeInstr(".super java/lang/Object");
        ct.startMethod("<init>()V");
        ct.writeInstr("aload_0");
        ct.writeInstr("invokespecial java/lang/Object/<init>()V");
        ct.writeInstr("return");
        ct.endMethod();
    }

    /* PROGRAM */
    public Object visit(PDefs p, Object unused) {
        writeBoilerplate();

        for (Def d : p.listdef_) {
            d.accept(this, unused);
        }

        return null;
    }
    
    /* FUNCTIONS */
    public Object visit(DFun p, Object unused) { 
        ct.startMethod(p.id_ )                
        return null; 
    }
    
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
