package gci.tree;
import gci.temp.*;

public class JUMP extends Stm {
  public Expression exp;
  public LabelList targets;
  public JUMP(Expression e, LabelList t) {exp=e; targets=t;}
  public JUMP(Label target) {
      this(new NAME(target), new LabelList(target,null));
  }
  public ExpressionList kids() {return new ExpressionList(exp,null);}
  public Stm build(ExpressionList kids) {
    return new JUMP(kids.head,targets);
  }
  	public String print(){
		return "JUMP "+exp.print();
	}
}

