import CPP.Absyn.*;
import java.util.LinkedList;
import java.util.HashMap;
import java.lang.StringBuilder;

public class ContextTable {
    public LinkedList<HashMap<String, Integer>> vars;
    public int max_var;
    public StringBuilder file = new StringBuilder();   
    private boolean indented = false;
    private int label_count;
    public HashMap<String, String> param_types = new HashMap<String, String>();
    public ContextTable() {
        vars = new LinkedList<HashMap<String, Integer>>();
        max_var = 1;
        label_count = 0;
    }

    public String addVar(String str) {
        max_var++;
        vars.getLast().put(str, Integer.valueOf(max_var));
        return Integer.valueOf(max_var).toString();
    }

    public String getVar(String str) {
        for(int i = vars.size() - 1; i >= 0; i--){
            if(vars.get(i).containsKey(str)){
                return Integer.toString(vars.get(i).get(str));
            }
        }
        System.err.println("couldnt find var in getVar");
        return "";
    }

    public void pushScope(){
        vars.addLast(new HashMap<String, Integer>());
    }

    public void popScope(){
        if(vars.size() >0){
            vars.removeLast();
        }
    }

    public void writeInstr(String str){
        System.err.println(str);

        if (indented) str = "\t" + str;

        file.append(str + "\n");
    }

    public void startLabel(String str){
        indented = false;
        writeInstr(str + ":");
        indented = true;
    }


    public void startMethod(String str) {
        writeInstr(".method public static " + str);
        indented = true;
    }

    public void startNonStaticMethod(String str) {
        writeInstr(".method public " + str);
        indented = true; 
    }

    public void endMethod() {
        indented = false;
        writeInstr(".end method");
    }

    public String newLabel(){
        label_count++;
        return "label_" + label_count;
    }
}
