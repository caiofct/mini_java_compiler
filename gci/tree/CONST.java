package gci.tree;

public class CONST extends Expression {
  public int value;
  public CONST(int v) {value=v;}
  public ExpressionList kids() {return null;}
  //public Exp build(ExpList kids) {return this;}
  public Expression build(ExpressionList kids) {
      return new CONST(value);
  }
    public String print(){
        return "CONST "+value;
    }
}

