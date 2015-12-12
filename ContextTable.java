import CPP.Absyn.*;
import java.util.LinkedList;
import java.util.HashMap;
import java.lang.StringBuilder;

public class ContextTable{
    public LinkedList<HashMap<String, Integer>> vars;
    public int max_var;
    public StringBuilder file;   
    private boolean indented = false;

    public ContextTable() {
        vars = new LinkedList<HashMap<String, Integer>>();
        max_var = 0;
    }

    public void addVar(String str) {
        max_var++;
        for (int i = vars.size() -1; i >= 0; i--) {
            if (vars.hasKey(str)) {
                System.err.println("err not sure if we care");
            }
            else {
                vars.put(str, max_var);
            }
        }
    }
    public void writeInstr(String str){
        System.err.println(str);

        if (indented) str = "\t" + str;

        file.append(str + "\n");
    }

    public void startMethod(String s) {
        writeInstr(".method public " + s);
        indented = true;
    }
    
    public void startMethod(String s, boolean staticc) {
        if (staticc) startMethod("static " + s);
        else startMethod(s);
    }

    public void endMethod() {
        indented = false;
        writeInstr(".end method");
    }
}
