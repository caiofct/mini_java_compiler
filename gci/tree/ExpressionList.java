package gci.tree;

/**
 * ExpressionList agora é subclasse de expression para poder usar no visitor de FormalList
 * @author Fabio
 */
//public class ExpressionList extends Expression {
public class ExpressionList {

  public Expression head;
  public ExpressionList tail;
  public ExpressionList(Expression h, ExpressionList t)
  {
      head=h;
      tail=t;
  }

  @SuppressWarnings("empty-statement")
  public void add(Expression exp) {
      ExpressionList varrer;
      if (exp==null)
          return;
      for (varrer=this; (varrer.tail!=null)&&(varrer.head!=null);varrer=varrer.tail);
      if (varrer.head == null) {
          varrer.head = exp;
      }
      else {
          varrer.tail = new ExpressionList(exp,null);
      }
  }

  @SuppressWarnings("empty-statement")
  public Expression get(int i) {
      ExpressionList varrer;
      int j=0;
      for (varrer=this;(j<i)&&(varrer!=null);j++,varrer=varrer.tail);
      if (varrer==null)
          return null;
      return varrer.head;
  }

  @SuppressWarnings("empty-statement")
  public int size() {
      ExpressionList varrer;
      int j=0;
      for (varrer=this; varrer!=null;j++,varrer=varrer.tail);
      return j;
  }
  
}



