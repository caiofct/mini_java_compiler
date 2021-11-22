package gci.tree;
import gci.temp.*;

public class LABEL extends Stm { 
  public Label label;
  public LABEL(Label l) {label=l;}

  public ExpressionList kids() {
      return null;
  }
    @Override
  public Stm build(ExpressionList kids) {
    return new LABEL(label);
  }
    public String print(){
		return "LABEL "+label.toString();
	}
}

