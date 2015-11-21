import CPP.Absyn.*;

public class CheckProgram implements Program.Visitor<Env,Env> {
	public Env visit(PDefs p, Env env) {
		System.out.println("Visiting your program yoooo");
//		for (Def x : p.listdef_) {
			//p.listdef_.add(x.accept(this,env));
//			x.accept(this, env);
//		}
		return env;//return new CPP.Absyn.PDefs(listdef_);
	}
}