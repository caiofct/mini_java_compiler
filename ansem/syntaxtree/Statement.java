//
// Generated by JTB 1.3.2
//

package ansem.syntaxtree;

import gci.tree.IRType;

/**
 * Grammar production:
 * <PRE>
 * f0 -> &lt;LCHAVE&gt; ( Statement() )* &lt;RCHAVE&gt;
 *       | &lt;IF&gt; &lt;LPAR&gt; Exp() &lt;RPAR&gt; Statement() &lt;ELSE&gt; Statement()
 *       | &lt;WHILE&gt; &lt;LPAR&gt; Exp() &lt;RPAR&gt; Statement()
 *       | &lt;SYSTEM&gt; &lt;DOT&gt; &lt;OUT&gt; &lt;DOT&gt; &lt;PRINTLN&gt; &lt;LPAR&gt; Exp() &lt;RPAR&gt; &lt;PVIRGULA&gt;
 *       | &lt;ID&gt; &lt;ASSIGN&gt; Exp() &lt;PVIRGULA&gt;
 *       | &lt;ID&gt; &lt;LCOL&gt; Exp() &lt;RCOL&gt; &lt;ASSIGN&gt; Exp() &lt;PVIRGULA&gt;
 * </PRE>
 */
public class Statement implements Node {
   public NodeChoice f0;

   public Statement(NodeChoice n0) {
      f0 = n0;
   }

   public IRType accept(ansem.visitor.IRVisitor v) throws SemanticException {
      return (IRType) v.visit(this);
   }
   
   public void accept(ansem.visitor.Visitor v) throws SemanticException {
      v.visit(this);
   }
   public <R,A> R accept(ansem.visitor.GJVisitor<R,A> v, A argu) {
      return v.visit(this,argu);
   }
   public <R> R accept(ansem.visitor.GJNoArguVisitor<R> v)  throws SemanticException {
      return v.visit(this);
   }
   public <A> void accept(ansem.visitor.GJVoidVisitor<A> v, A argu) {
      v.visit(this,argu);
   }
}

