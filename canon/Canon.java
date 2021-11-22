package Canon;


class MoveCall extends gci.tree.Stm {
  gci.tree.TEMP dst;
  gci.tree.CALL src;
  MoveCall(gci.tree.TEMP d, gci.tree.CALL s) {dst=d; src=s;}
  public gci.tree.ExpressionList kids() {return src.kids();}
  public gci.tree.Stm build(gci.tree.ExpressionList kids) {
	return new gci.tree.MOVE(dst, src.build(kids));
  }

    @Override
    public String print() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}   
  
class ExpCall extends gci.tree.Stm {
  gci.tree.CALL call;
  ExpCall(gci.tree.CALL c) {call=c;}
  public gci.tree.ExpressionList kids() {return call.kids();}
  public gci.tree.Stm build(gci.tree.ExpressionList kids) {
	return new gci.tree.EXP(call.build(kids));
  }

    @Override
    public String print() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}   
class StmExpressionList {
  gci.tree.Stm stm;
  gci.tree.ExpressionList exps;
  StmExpressionList(gci.tree.Stm s, gci.tree.ExpressionList e) {stm=s; exps=e;}
}

public class Canon {
  
 static boolean isNop(gci.tree.Stm a) {
   return a instanceof gci.tree.EXP
          && ((gci.tree.EXP)a).exp instanceof gci.tree.CONST;
 }

 static gci.tree.Stm seq(gci.tree.Stm a, gci.tree.Stm b) {
    if (isNop(a)) return b;
    else if (isNop(b)) return a;
    else return new gci.tree.SEQ(a,b);
 }


 static boolean commute(gci.tree.Stm a, gci.tree.Expression b) {
    return isNop(a)
        || b instanceof gci.tree.NAME
        || b instanceof gci.tree.CONST;
 }

 static gci.tree.Stm do_stm(gci.tree.SEQ s) {
	return seq(do_stm(s.left), do_stm(s.right));
 }

 static gci.tree.Stm do_stm(gci.tree.MOVE s) {
	if (s.dst instanceof gci.tree.TEMP
	     && s.src instanceof gci.tree.CALL)
		return reorder_stm(new MoveCall((gci.tree.TEMP)s.dst,
						(gci.tree.CALL)s.src));
	else if (s.dst instanceof gci.tree.ESEQ)
	    return do_stm(new gci.tree.SEQ(((gci.tree.ESEQ)s.dst).stm,
					new gci.tree.MOVE(((gci.tree.ESEQ)s.dst).exp,
						  s.src)));
	else return reorder_stm(s);
 }

 static gci.tree.Stm do_stm(gci.tree.EXP s) {
	if (s.exp instanceof gci.tree.CALL)
	       return reorder_stm(new ExpCall((gci.tree.CALL)s.exp));
	else return reorder_stm(s);
 }

 static gci.tree.Stm do_stm(gci.tree.Stm s) {
     if (s instanceof gci.tree.SEQ) return do_stm((gci.tree.SEQ)s);
     else if (s instanceof gci.tree.MOVE) return do_stm((gci.tree.MOVE)s);
     else if (s instanceof gci.tree.EXP) return do_stm((gci.tree.EXP)s);
     else return reorder_stm(s);
 }

 static gci.tree.Stm reorder_stm(gci.tree.Stm s) {
     StmExpressionList x = reorder(s.kids());
     return seq(x.stm, s.build(x.exps));
 }

 static gci.tree.ESEQ do_exp(gci.tree.ESEQ e) {
      gci.tree.Stm stms = do_stm(e.stm);
      gci.tree.ESEQ b = do_exp(e.exp);
      return new gci.tree.ESEQ(seq(stms,b.stm), b.exp);
  }

 static gci.tree.ESEQ do_exp (gci.tree.Expression e) {
       if (e instanceof gci.tree.ESEQ) return do_exp((gci.tree.ESEQ)e);
       else return reorder_exp(e);
 }
         
 static gci.tree.ESEQ reorder_exp (gci.tree.Expression e) {
     StmExpressionList x = reorder(e.kids());
     return new gci.tree.ESEQ(x.stm, e.build(x.exps));
 }

 static StmExpressionList nopNull = new StmExpressionList(new gci.tree.EXP(new gci.tree.CONST(0)),null);

 static StmExpressionList reorder(gci.tree.ExpressionList exps) {
     if (exps==null) return nopNull;
     else {
       gci.tree.Expression a = exps.head;
       if (a instanceof gci.tree.CALL) {
         gci.temp.Temp t = new gci.temp.Temp();
	 gci.tree.Expression e = new gci.tree.ESEQ(new gci.tree.MOVE(new gci.tree.TEMP(t), a),
				    new gci.tree.TEMP(t));
         return reorder(new gci.tree.ExpressionList(e, exps.tail));
       } else {
	 gci.tree.ESEQ aa = do_exp(a);
	 StmExpressionList bb = reorder(exps.tail);
	 if (commute(bb.stm, aa.exp))
	      return new StmExpressionList(seq(aa.stm,bb.stm),
				    new gci.tree.ExpressionList(aa.exp,bb.exps));
	 else {
	   gci.temp.Temp t = new gci.temp.Temp();
	   return new StmExpressionList(
			  seq(aa.stm, 
			    seq(new gci.tree.MOVE(new gci.tree.TEMP(t),aa.exp),
				 bb.stm)),
			  new gci.tree.ExpressionList(new gci.tree.TEMP(t), bb.exps));
	 }
       }
     }
 }
        
 static gci.tree.StmList linear(gci.tree.SEQ s, gci.tree.StmList l) {
      return linear(s.left,linear(s.right,l));
 }
 static gci.tree.StmList linear(gci.tree.Stm s, gci.tree.StmList l) {
    if (s instanceof gci.tree.SEQ) return linear((gci.tree.SEQ)s, l);
    else return new gci.tree.StmList(s,l);
 }

 static public gci.tree.StmList linearize(gci.tree.Stm s) {
    return linear(do_stm(s), null);
 }
}
