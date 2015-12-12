import CPP.Absyn.*;
import java.util.LinkedList;
import java.util.HashMap;
import java.lang.StringBuilder;

public class ContextTable{
    public LinkedList<HashMap<String, Integer>> vars;
    public Integer max_var;
    public StringBuilder file;   

    public ContextTable() {
        vars = new LinkedList<HashMap<String, Integer>>();
        max_var = 0;
    }

    public void addVar(String str, int type_code){
        //something w incr max_var
    }
    public writeInstr(String str){
        System.err.println(str);
        file.append(str + "\n");
    }
}
