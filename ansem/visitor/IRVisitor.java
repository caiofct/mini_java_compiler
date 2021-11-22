/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ansem.visitor;
import gci.tree.*;
import ansem.syntaxtree.*;

/**
 *
 * @author Caio Fellipe
 */
public interface IRVisitor {
   //
   // VOID Auto class visitors
   //

   public IRType visit(NodeList n) throws SemanticException;
   public IRType visit(NodeListOptional n) throws SemanticException;
   public IRType visit(NodeOptional n) throws SemanticException;
   public IRType visit(NodeSequence n) throws SemanticException;
   public IRType visit(NodeToken n) throws SemanticException;

   //
   // User-generated visitor methods below
   //

   /**
    * <PRE>
    * f0 -> &lt;VIRGULA&gt;
    * f1 -> Type()
    * f2 -> &lt;ID&gt;
    * </PRE>
    */
   public IRType visit(FormalRest n) throws SemanticException;

   /**
    * <PRE>
    * f0 -> ( Type() &lt;ID&gt; ( FormalRest() )* )?
    * </PRE>
    */
   public IRType visit(FormalList n) throws SemanticException;

   /**
    * <PRE>
    * f0 -> &lt;PUBLIC&gt;
    * f1 -> Type()
    * f2 -> &lt;ID&gt;
    * f3 -> &lt;LPAR&gt;
    * f4 -> FormalList()
    * f5 -> &lt;RPAR&gt;
    * f6 -> &lt;LCHAVE&gt;
    * f7 -> ( VarDecl() )*
    * f8 -> ( Statement() )*
    * f9 -> &lt;RETURN&gt;
    * f10 -> Exp()
    * f11 -> &lt;PVIRGULA&gt;
    * f12 -> &lt;RCHAVE&gt;
    * </PRE>
    */
   public IRType visit(MethodDecl n) throws SemanticException;

   /**
    * <PRE>
    * f0 -> &lt;INT&gt; &lt;LCOL&gt; &lt;RCOL&gt;
    *       | &lt;INT&gt;
    *       | &lt;BOOLEAN&gt;
    *       | &lt;ID&gt;
    * </PRE>
    */
   public IRType visit(Type n) throws SemanticException;

   /**
    * <PRE>
    * f0 -> Type()
    * f1 -> &lt;ID&gt;
    * f2 -> &lt;PVIRGULA&gt;
    * </PRE>
    */
   public IRType visit(VarDecl n) throws SemanticException;

   /**
    * <PRE>
    * f0 -> &lt;CLASS&gt; &lt;ID&gt; &lt;LCHAVE&gt; ( VarDecl() )* ( MethodDecl() )* &lt;RCHAVE&gt;
    *       | &lt;CLASS&gt; &lt;ID&gt; &lt;EXTENDS&gt; &lt;ID&gt; &lt;LCHAVE&gt; ( VarDecl() )* ( MethodDecl() )* &lt;RCHAVE&gt;
    * </PRE>
    */
   public IRType visit(ClassDecl n) throws SemanticException;

   /**
    * <PRE>
    * f0 -> &lt;VIRGULA&gt;
    * f1 -> Exp()
    * </PRE>
    */
   public IRType visit(ExpRest n) throws SemanticException;

   /**
    * <PRE>
    * f0 -> ( Exp() ( ExpRest() )* )?
    * </PRE>
    */
   public IRType visit(ExpList n) throws SemanticException;

   /**
    * <PRE>
    * f0 -> ExpSemLR()
    * f1 -> ExpComRR()
    * </PRE>
    */
   public IRType visit(Exp n) throws SemanticException;

   /**
    * <PRE>
    * f0 -> ( &lt;BINOP&gt; Exp() ExpComRR() | &lt;LCOL&gt; Exp() &lt;RCOL&gt; ExpComRR() | &lt;DOT&gt; &lt;LENGTH&gt; ExpComRR() | &lt;DOT&gt; &lt;ID&gt; &lt;LPAR&gt; ExpList() &lt;RPAR&gt; ExpComRR() )?
    * </PRE>
    */
   public IRType visit(ExpComRR n) throws SemanticException;

   /**
    * <PRE>
    * f0 -> &lt;INTEGER_LITERAL&gt;
    *       | &lt;TRUE&gt;
    *       | &lt;FALSE&gt;
    *       | &lt;ID&gt;
    *       | &lt;THIS&gt;
    *       | &lt;NEW&gt; &lt;INT&gt; &lt;LCOL&gt; Exp() &lt;RCOL&gt;
    *       | &lt;NEW&gt; &lt;ID&gt; &lt;LPAR&gt; &lt;RPAR&gt;
    *       | &lt;UNOP&gt; Exp()
    *       | &lt;LPAR&gt; Exp() &lt;RPAR&gt;
    * </PRE>
    */
   public IRType visit(ExpSemLR n) throws SemanticException;

   /**
    * <PRE>
    * f0 -> &lt;LCHAVE&gt; ( Statement() )* &lt;RCHAVE&gt;
    *       | &lt;IF&gt; &lt;LPAR&gt; Exp() &lt;RPAR&gt; Statement() &lt;ELSE&gt; Statement()
    *       | &lt;WHILE&gt; &lt;LPAR&gt; Exp() &lt;RPAR&gt; Statement()
    *       | &lt;SYSTEM&gt; &lt;DOT&gt; &lt;OUT&gt; &lt;DOT&gt; &lt;PRINTLN&gt; &lt;LPAR&gt; Exp() &lt;RPAR&gt; &lt;PVIRGULA&gt;
    *       | &lt;ID&gt; &lt;ASSIGN&gt; Exp() &lt;PVIRGULA&gt;
    *       | &lt;ID&gt; &lt;LCOL&gt; Exp() &lt;RCOL&gt; &lt;ASSIGN&gt; Exp() &lt;PVIRGULA&gt;
    * </PRE>
    */
   public IRType visit(Statement n) throws SemanticException;

   /**
    * <PRE>
    * f0 -> &lt;CLASS&gt;
    * f1 -> &lt;ID&gt;
    * f2 -> &lt;LCHAVE&gt;
    * f3 -> &lt;PUBLIC&gt;
    * f4 -> &lt;STATIC&gt;
    * f5 -> &lt;VOID&gt;
    * f6 -> &lt;MAIN&gt;
    * f7 -> &lt;LPAR&gt;
    * f8 -> &lt;STRING&gt;
    * f9 -> &lt;LCOL&gt;
    * f10 -> &lt;RCOL&gt;
    * f11 -> &lt;ID&gt;
    * f12 -> &lt;RPAR&gt;
    * f13 -> &lt;LCHAVE&gt;
    * f14 -> Statement()
    * f15 -> &lt;RCHAVE&gt;
    * f16 -> &lt;RCHAVE&gt;
    * </PRE>
    */
   public IRType visit(MainClass n) throws SemanticException;

   /**
    * <PRE>
    * f0 -> MainClass()
    * f1 -> ( ClassDecl() )*
    * f2 -> &lt;EOF&gt;
    * </PRE>
    */
   public IRType visit(Start n) throws SemanticException;

}
