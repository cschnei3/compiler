import CPP.Absyn.*;
import java.util.LinkedList;

public class CodeGenerator implements  
 {

    public void codeGenerator(Program p) {
		ContextTable ct = new ContextTable();
		p.accept(new Generator(), ct);
    }
}
