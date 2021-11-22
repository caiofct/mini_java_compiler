package gci.tree;

public class CALL extends Expression {
  public Expression func;
  public ExpressionList args;
  public CALL(Expression f, ExpressionList a) {func=f; args=a;}

    /*public Expression build(ExpressionList kids) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public ExpressionList kids() {
        throw new UnsupportedOperationException("Not yet implemented");
    }*/
  
  public ExpressionList kids() {return new ExpressionList(func,args);}
  
  public Expression build(ExpressionList kids) {
    return new CALL(kids.head,kids.tail);
  }
    public String print(){
        String s = "CALL ("+func.print()+") [ ";
        int i;
        for (i=0;i<=args.size()-2;i++)
            s += "("+args.get(i).print()+"), ";
        s += "("+args.get(i).print()+") ]";
        return s;
    }
}

