package gci.tree;

public class MEM extends Expression {
  public Expression exp;
  public MEM(Expression e) {exp=e;}
  
  public ExpressionList kids() {
      return new ExpressionList(exp,null);
  }
  public Expression build(ExpressionList kids) {
    return new MEM(kids.head);
  }
public String print(){
    return "MEM ("+exp.print()+")";
}

}

