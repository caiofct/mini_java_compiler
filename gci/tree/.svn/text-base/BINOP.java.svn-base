package gci.tree;

public class BINOP extends Expression {
  public int binop;
  public Expression left, right;
  public BINOP(int b, Expression l, Expression r) {
    binop=b; left=l; right=r;
  }
  /*
   * 0 PLUS
   * 1 MINUS
   * 2 MUL
   * 3 DIV
   * 4 AND
   * 5 OR
   * 6 LSHIFT
   * 7 RSHIFT
   * 8 ARSHIFT
   * 9 XOR
   */
  
  public final static int PLUS=0, MINUS=1, MUL=2, DIV=3, 
		   AND=4,OR=5,LSHIFT=6,RSHIFT=7,ARSHIFT=8,XOR=9;
  public ExpressionList kids() {return new ExpressionList(left, new ExpressionList(right,null));}
  public Expression build(ExpressionList kids) {
    return new BINOP(binop,kids.head,kids.tail.head);
  }
  public String print(){
    return "BINOP "+binop+" ("+left.print()+") ("+right.print()+")";
  }
}

