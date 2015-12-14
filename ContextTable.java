import CPP.Absyn.*;
import java.util.LinkedList;
import java.util.HashMap;
import java.lang.StringBuilder;

public class ContextTable{
    public LinkedList<HashMap<String, Integer>> vars;
    public int max_var;
    public StringBuilder file;   
    private boolean indented = false;
    private label_count = 0;
    public ContextTable() {
        vars = new LinkedList<HashMap<String, Integer>>();
        max_var = 0;
    }

    public void addVar(String str) {
        max_var++;
        for (int i = vars.size() -1; i >= 0; i--) {
            if (vars.get(i).hasKey(str)) {
                System.err.println("err not sure if we care");
            }
            else {
                vars.put(str, max_var);
            }
        }
    }

    public String getVar(String str){
        for(i = vars.size() - 1; i >= 0; i--){
            if(vars.get(i).hasKey(str)){
                return (String)vars.get(i).get(str);
            }
        }
        throw new Exception("couldnt find var in getVar");
    }

    public void pushScope(){
        vars.addLast(new HashMap<String, Integer>);
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

    public void startLable(String str, boolean label){
        if(label){
            indented = false;
            writeInstr(str + ":");
            indented = true;
       }
    }


    public void startMethod(String str) {
        writeInstr(".method public " + str);
        indented = true;
    }

    public void startMethod(String str, boolean staticc) {
        if (staticc) startMethod("static " + str);
        else startMethod(s);
    }

    public void endMethod() {
        indented = false;
        writeInstr(".end method");
    }

    public String newLabel(){
        return "label_" + label_count;
        label_count++;
    }
}
