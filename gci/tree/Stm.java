package gci.tree;
abstract public class Stm implements IRType {

    /*public Stm build(ExpressionList exps) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public ExpressionList kids() {
        throw new UnsupportedOperationException("Not yet implemented");
    }*/
	abstract public ExpressionList kids();
	abstract public Stm build(ExpressionList kids);
    abstract public String print();
}

