import CPP.Absyn.*;
import java.util.regex.Pattern;
import java.util.LinkedList;

public class CodeGenerator implements
	Program.Visitor<Object, Object>, 
	Def.Visitor<Object, Object>,
	Arg.Visitor<Integer, Object>,
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
        ct.pushScope();
        String argDescriptor = "(";

        if (p.id_.equals("main")) {
            argDescriptor = "([Ljava/lang/String;)V";
        } 
        else {
            for (Arg a : p.listarg_) {
                int typeCode = a.accept(this, unused);
                argDescriptor += jasminType(typeCode);
            }

            argDescriptor += ")" + jasminType(p.type_.accept(new TypeCode(), null));
        }

        ct.startMethod(p.id_ + argDescriptor);               

        // Set up the stack

        ct.writeInstr(".limit stack " + p.liststm_.size());
        ct.writeInstr(".limit locals 100");

        for (Stm s : p.liststm_) {
            s.accept(this, unused);
        }

        ct.endMethod();
        ct.popScope();
    
        return null; 
    }
    
    /* ARGS */
    public Integer visit(ADecl p, Object unused) { 
        ct.addVar(p.id_);
        return p.type_.accept(new TypeCode(), null); 
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
        String address = ct.addVar(p.id_);

        p.exp_.accept(this, unused);
        
        ct.writeInstr("istore_" + address);
        ct.writeInstr("iload_" + address);

        return null; 
    }

    public Object visit(SReturn p, Object unused) { 
        p.exp_.accept(this, unused);
        ct.writeInstr("ireturn");
        return null; 
    }

    public Object visit(SWhile p, Object unused) { 
        ct.pushScope();
        
        String start_label = "while_" + ct.newLabel();
        String end_label = "done_" + ct.newLabel();

        ct.startLabel(start_label);
        p.exp_.accept(this, unused);
        ct.writeInstr("ifne " +  end_label);
        p.stm_.accept(this, unused);
        ct.writeInstr("goto " + start_label);
        ct.startLabel(end_label);
        ct.popScope();

        return null; 
    }

    public Object visit(SBlock p, Object unused) { 
        ct.pushScope();
        for(Stm stm : p.liststm_){
            stm.accept(this, unused);
        }
        ct.popScope();
        return null;
    }

    public Object visit(SIfElse p, Object unused) { 
        ct.pushScope();

       String if_label = "if_" + ct.newLabel();
        String end_label = "end_" + ct.newLabel();

        p.exp_.accept(this, unused);
        ct.writeInstr("ifeq " + if_label);
        p.stm_2.accept(this, unused);
        ct.writeInstr("goto " + end_label);
        ct.startLabel(if_label); 
        p.stm_1.accept(this, unused);
        ct.startLabel(end_label);
        return null;
    }
    
    /* EXPRESSIONS */
    public Object visit(ETrue p, Object unused) { 
        ct.writeInstr("iconst_1");
        return null ;
    }

    public Object visit(EFalse p, Object unused) {
        ct.writeInstr("iconst_0");
        return null ;
    }

    public Object visit(EInt p, Object unused) { 
        ct.writeInstr("iconst_" + p.integer_);
        return null ;
    }

    public Object visit(EId p, Object unused) { 
        ct.writeInstr("iload_" + ct.getVar(p.id_));
        return null ;
    }

    public Object visit(EApp p, Object unused) { 
        ct.pushScope();
        
        for(Exp exp : p.listexp_){
            exp.accept(this, unused);
        }
        
        ct.writeInstr("invokestatic " + fileName + "/" +  p.id_);
        
        ct.popScope();
        return null ;
    }

    public Object visit(EPostIncr p, Object unused) { 
        String var_id = ct.getVar(p.id_);
        ct.writeInstr("iload_" + var_id);
        ct.writeInstr("iload_" + var_id);
        ct.writeInstr("iconst_1");
        ct.writeInstr("iadd");
        ct.writeInstr("istore_" + var_id);
        return null;
    }

    public Object visit(EPostDecr p, Object unused){
        String var_id = ct.getVar(p.id_);
        ct.writeInstr("iload_" + var_id);
         ct.writeInstr("iload_" + var_id);
        ct.writeInstr("iconst_1");
        ct.writeInstr("isub");
        ct.writeInstr("istore_" + var_id);
        return null;
    }
 
    public Object visit(EPreIncr p, Object unused) { 
        String var_id = ct.getVar(p.id_);
        ct.writeInstr("iload_" + var_id);
        ct.writeInstr("iconst_1");
        ct.writeInstr("iadd");
        ct.writeInstr("istore_" + var_id);
        ct.writeInstr("iload_" + var_id);
        return null;
    }
     public Object visit(EPreDecr p, Object unused) {
        String var_id = ct.getVar(p.id_);
        ct.writeInstr("iload_" + var_id);
        ct.writeInstr("iconst_1");
        ct.writeInstr("iadd");
        ct.writeInstr("istore_" + var_id);
        ct.writeInstr("iload_" + var_id);
        return null;
    }

    public Object visit(ETimes p, Object unused) { 
        p.exp_1.accept(this, unused) ;
        p.exp_2.accept(this, unused) ;
        
        ct.writeInstr("imul");
        
        return null ;
    }
    public Object visit(EDiv p, Object unused) { 
        p.exp_1.accept(this, unused);
        p.exp_2.accept(this, unused);
        ct.writeInstr("idiv");
        return null ;
    }

    public Object visit(EPlus p, Object unused) { 
        p.exp_1.accept(this, unused);
        p.exp_2.accept(this, unused);
        ct.writeInstr("iadd");
        return null ;
    }
    public Object visit(EMinus p, Object unused) { 
        p.exp_1.accept(this, unused);
        p.exp_2.accept(this, unused);
        ct.writeInstr("isub");
        return null ;
    }

    public void comparison(String instr, Exp e1, Exp e2) {
        String true_label = "true_" + ct.newLabel();
        ct.writeInstr("bipush 1");
        e1.accept(this, null);
        e2.accept(this, null);
        ct.writeInstr(instr + " " + true_label);
        ct.writeInstr("pop");
        ct.writeInstr("bipush 0");
        ct.startLabel(true_label);
    }

    public Object visit(ELt p, Object unused) { 
        comparison("if_icmplt", p.exp_1, p.exp_2);
        return null ;
    }

    public Object visit(EGt p, Object unused) { 
        comparison("if_cmpgt", p.exp_1, p.exp_2);
        return null; 
    }
    public Object visit(ELtEq p, Object unused) { 
        comparison("if_cmple", p.exp_1, p.exp_2);
        return null; 
    }

    public Object visit(EGtEq p, Object unused) { 
        comparison("if_cmpge", p.exp_1, p.exp_2);
        return null; 
    }

    public Object visit(EEq p, Object unused) { 
        comparison("if_cmpeq", p.exp_1, p.exp_2);
        return null; 
    }

    public Object visit(ENEq p, Object unused) { 
        comparison("if_cmpne", p.exp_1, p.exp_2);
        return null; 
    }

    public Object visit(EAnd p, Object unused) { 
        String end_label = "end_" + ct.newLabel();
        // default value to be left on the stack
        ct.writeInstr("bipush 0");
        
        p.exp_1.accept(this, unused);
        ct.writeInstr("bipush 1");
        // if not equal go to end label with 0 on stack
        ct.writeInstr("if_cmpne " + end_label);

        p.exp_2.accept(this, unused);
        ct.writeInstr("bipush 1");
        // same as above with 2nd expression
        ct.writeInstr("if_cmpne " + end_label);
        // both values have compared to true, so replace return value with 1
        ct.writeInstr("pop");
        ct.writeInstr("bipush 1");
        
        ct.startLabel(end_label);
        return null; 
    }

    public Object visit(EOr p, Object unused) { 
        String end_label = "end_" + ct.newLabel();
        // default value to be left on the stack
        ct.writeInstr("bipush 1");
        
        p.exp_1.accept(this, unused);
        ct.writeInstr("bipush 1");
        // if equal go to end label with 1 on stack
        ct.writeInstr("if_cmpeq " + end_label);

        p.exp_2.accept(this, unused);
        ct.writeInstr("bipush 1");
        // same as above with 2nd expression
        ct.writeInstr("if_cmpeq " + end_label);
        // both values have compared to 0, so replace return value with 0
        ct.writeInstr("pop");
        ct.writeInstr("bipush 0");
        
        ct.startLabel(end_label);
        return null; 
    }

    public Object visit(EAss p, Object unused) {
        // accept pushes the value onto the stack
        p.exp_.accept(this, unused);

        String address = ct.addVar(p.id_);

        ct.writeInstr("istore_" + address);
        ct.writeInstr("iload_" + address);
        return null ;
    }
}
