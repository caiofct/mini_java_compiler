package gci.tree;
import gci.temp.*;

public class CJUMP extends Stm {
  public int relop;
  public Expression left, right;
  public Label iftrue, iffalse;
  public CJUMP(int rel, Expression l, Expression r, Label t, Label f) {
     relop=rel; left=l; right=r; iftrue=t; iffalse=f;
  }
  public final static int EQ=10, NE=11, LT=12, GT=13, LE=14, GE=15,
		   ULT=16, ULE=17, UGT=18, UGE=19;

    @Override
  public ExpressionList kids() {return new ExpressionList(left, new ExpressionList(right,null));}

    @Override
  public Stm build(ExpressionList kids) {
    return new CJUMP(relop,kids.head,kids.tail.head,iftrue,iffalse);
  }

 public static int notRel(int relop) {
    switch (relop) {
        case EQ:  return NE;
        case NE:  return EQ;
    	case LT:  return GE;
        case GE:  return LT;
        case GT:  return LE;
        case LE:  return GT;
        case ULT: return UGE;
        case UGE: return ULT;
        case UGT: return ULE;
        case ULE: return UGT;
        default: throw new Error("bad relop in CJUMP.notRel");
    }
 }
    public String print(){
		return "CJUMP "+relop+" ("+left.print()+") ("+right.print()+") "+iftrue.toString()+" "+iffalse.toString();
	}
}

