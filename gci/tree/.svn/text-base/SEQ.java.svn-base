package gci.tree;

public class SEQ extends Stm {
  public Stm left, right;
  public SEQ(Stm l, Stm r) { left=l; right=r; }
  public ExpressionList kids() {
      return null;
  }
  public Stm build(ExpressionList kids) {
      return new SEQ(left,right);
  }
  	public String print(){
        String result = null;
        if(left != null)
        {
            result = left.print();
        }
        if(right != null)
        {
            if(result != null)
            {
                result = result + "\n"+right.print();
            }
            else
            {
                result = right.print();
            }

        }
		return result;
	}
}

