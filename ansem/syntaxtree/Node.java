//
// Generated by JTB 1.3.2
//

package ansem.syntaxtree;

import gci.tree.IRType;

/**
 * The interface which all syntax tree classes must implement.
 */
public interface Node extends java.io.Serializable {
   public IRType accept(ansem.visitor.IRVisitor v) throws SemanticException;
   public void accept(ansem.visitor.Visitor v) throws SemanticException;
   public <R,A> R accept(ansem.visitor.GJVisitor<R,A> v, A argu);
   public <R> R accept(ansem.visitor.GJNoArguVisitor<R> v) throws SemanticException;
   public <A> void accept(ansem.visitor.GJVoidVisitor<A> v, A argu);
}
