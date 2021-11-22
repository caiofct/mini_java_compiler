package gci.tree;

public class EXP extends Stm {
  public Expression exp;
  public EXP(Expression e) {exp=e;}

  public ExpressionList kids() {
      return new ExpressionList(exp,null);
  }

  public Stm build(ExpressionList kids) {
      return new EXP(kids.head);
  }
  	public String print(){
		return "EXP ("+exp.print()+")";
	}
}

