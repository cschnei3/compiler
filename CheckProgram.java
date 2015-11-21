import CPP.Absyn.*;

public class CheckProgram implements Program.Visitor<Env,Env> {
	public Env visit(PDefs p, Env env) {
		for (Def x : p.listdef_) {
			listdef_.add(x.accept(this,arg));
		}
		return new CPP.Absyn.PDefs(listdef_);
	}
}