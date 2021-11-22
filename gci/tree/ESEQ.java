package gci.tree;

public class ESEQ extends Expression {
  public Stm stm;
  public Expression exp;
  public ESEQ(Stm s, Expression e) {stm=s; exp=e;}

  public ExpressionList kids() {
      //throw new Error("kids() not applicable to ESEQ");
      return new ExpressionList(exp,null);
  }

  public Expression build(ExpressionList kids) {
      //throw new Error("build() not applicable to ESEQ");
      return new ESEQ(stm,kids.head);
  }
  	public String print(){
		return stm.print()+"; Return: "+exp.print();
	}
}

