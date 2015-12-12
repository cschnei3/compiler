import CPP.Absyn.*;
import java.util.LinkedList;

public class CodeGenerator implements  
	Program.Visitor<ContextTable, ContextTable>, 
	Def.Visitor<ContextTable, ContextTable>,
	Arg.Visitor<String, ContextTable> {

    public void codeGenerator(Program p) {
		ContextTable ct = new ContextTable();
		p.accept(new Generator(), ct);
    }
}
