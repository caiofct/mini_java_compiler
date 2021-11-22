//
// Generated by JTB 1.3.2
//

package ansem.syntaxtree;

import gci.tree.IRType;

/**
 * Represents a grammar choice, e.g. ( A | B )
 */
public class NodeChoice implements Node {
   public NodeChoice(Node node) {
      this(node, -1);
   }

   public NodeChoice(Node node, int whichChoice) {
      choice = node;
      which = whichChoice;
   }

   public IRType accept(ansem.visitor.IRVisitor v) throws SemanticException {
      return choice.accept(v);
   }

   public void accept(ansem.visitor.Visitor v) throws SemanticException {
      choice.accept(v);
   }
   public <R,A> R accept(ansem.visitor.GJVisitor<R,A> v, A argu) {
      return choice.accept(v,argu);
   }
   public <R> R accept(ansem.visitor.GJNoArguVisitor<R> v)  throws SemanticException {
      return choice.accept(v);
   }
   public <A> void accept(ansem.visitor.GJVoidVisitor<A> v, A argu) {
      choice.accept(v,argu);
   }

   public Node choice;
   public int which;   //numero da regra utilizada
}

