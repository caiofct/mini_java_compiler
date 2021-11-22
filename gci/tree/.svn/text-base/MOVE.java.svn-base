package gci.tree;

public class MOVE extends Stm {
  public Expression dst, src;
  public MOVE(Expression d, Expression s) {dst=d; src=s;}

  public ExpressionList kids() {
       if (dst instanceof MEM)
	   return new ExpressionList(((MEM)dst).exp, new ExpressionList(src,null));
	else return new ExpressionList(src,null);
  }
  public Stm build(ExpressionList kids) {
    if (dst instanceof MEM)
	   return new MOVE(new MEM(kids.head), kids.tail.head);
	else
       return new MOVE(dst, kids.head);
  }
public String print (){
		return "MOVE ("+dst.print()+") <- ("+src.print()+")";
	}
}

