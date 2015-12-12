import CPP.Absyn.*;
import java.util.regex.Pattern;
import java.util.LinkedList;

public class CodeGenerator implements
	Program.Visitor<Object, Object>, 
	Def.Visitor<Object, Object>,
	Arg.Visitor<int, Object>,
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
        ct.startMethod("<init>()V", true);
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

    public String jasminType(int typeCode) {
        if (typeCode == TypeCode.CInt) {
            return  "I";
        }
        else if (typeCode == TypeCode.CBool) {
            return  "B";
        }
        else if (typeCode == TypeCode.CVoid) {
            return  "V";
        }
        else {
            return "Invalid type";
        }
    }
    
    /* FUNCTIONS */
    public Object visit(DFun p, Object unused) { 
        // Make the function signature
        String argDescriptor = "(";

        if (p.id_.equals("main")) {
            argDescriptor = "([Ljava/lang/String;)V";
        } 
        else {
            for (Arg a : p.listarg_) {
                int typeCode = a.accept(this, unused);
                argDescriptor += jasminType(typeCode);
            }

            argDescriptor += ")" + jasminType(p.type_.accept(new TypeCode(), unused));
        }

        ct.startMethod(p.id_ + argDescriptor);               

        // Set up the stack

        ct.writeInstr(".limit stack " + p.liststm_.size());
        ct.writeInstr(".limit locals 100");

        for (Stm s : p.liststm_) {
            s.accept(this, unused);
        }

        ct.endMethod();
    
        return null; 
    }
    
    /* ARGS */
    public int visit(ADecl p, Object unused) { 
        ct.addVar(p.id_);
        return null; 
    }
    
    /* STATEMENTS */
    public Object visit(SExp p, Object unused) { 
        p.exp_.accept(this, unused);
        return null; 
    }
    public Object visit(SDecls p, Object unused) { 
        // don't push on to stack (??)
        for (String s : p.listid_) {
            ct.addVar(s);
        }
        return null; 
    }
    public Object visit(SInit p, Object unused) { 
        ct.addVar(p.id_);

        p.accept(p.exp_, unused);

        return null; 
    }
    public Object visit(SReturn p, Object unused) { 
        p.exp_.accept(this, unused);
        ct.writeInstr("ireturn");
        return null; 
    }
    public Object visit(SWhile p, Object unused) { 
             
        return null; 
    }
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
