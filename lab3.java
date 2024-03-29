import java_cup.runtime.*;
import CPP.*;
import CPP.Absyn.*;
import java.io.*;

public class lab3 {
    public static void main(String args[]) {
        if (args.length != 1) {
            System.err.println("Usage: lab2 <SourceFile>");
            System.exit(1);
        }

        Yylex l = null;
        try {
            String fileName = args[0];
            l = new Yylex(new FileReader(args[0]));
            parser p = new parser(l);
            CPP.Absyn.Program parse_tree = p.pProgram();
// type checker is run in the code gen
//            new TypeChecker().typecheck(parse_tree);
            new CodeGenerator().codeGenerator(parse_tree, fileName);
        } catch (TypeException e) {
            System.out.println("TYPE ERROR");
            System.err.println(e.toString());
            System.exit(1);
        } catch (RuntimeException e) {
            //            System.out.println("RUNTIME ERROR");
            System.err.println(e.toString());
            System.exit(-1);
        } catch (IOException e) {
            System.err.println(e.toString());
            System.exit(1);
        } catch (Throwable e) {
            System.out.println("SYNTAX ERROR");
            System.out.println("At line " + String.valueOf(l.line_num())
                       + ", near \"" + l.buff() + "\" :");
            System.out.println("     " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}

