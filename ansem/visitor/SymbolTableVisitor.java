//
// Generated by JTB 1.3.2
//

package ansem.visitor;
import ansem.syntaxtree.*;
import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class SymbolTableVisitor implements Visitor {
   //
   // Auto class visitors--probably don't need to be overridden.
   //
   public void visit(NodeList n) throws SemanticException {
      n.accept(this);
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
         e.nextElement().accept(this);
   }

   public void visit(NodeListOptional n) throws SemanticException{
      if ( n.present() )
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
            e.nextElement().accept(this);
   }

   public void visit(NodeOptional n) throws SemanticException{
      if ( n.present() )
         n.node.accept(this);
   }

   public void visit(NodeSequence n) throws SemanticException{
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
         e.nextElement().accept(this);
   }

   public void visit(NodeToken n) { }

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
   public void visit(FormalRest n) throws SemanticException {
      // TODO: falta inserir a lista de parametros do metodo, isso deve ser feito usando
      // o currentMethod no visitor de formalList e formalRest
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
    * <PRE>
    * f0 -> ( Type() &lt;ID&gt; ( FormalRest() )* )?
    * </PRE>
    */
   public void visit(FormalList n) throws SemanticException {
       // TODO: falta inserir a lista de parametros do metodo, isso deve ser feito usando
      // o currentMethod no visitor de formalList e formalRest
      n.f0.accept(this);
   }

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
   public void visit(MethodDecl n) throws SemanticException {
      SemanticInfo.currentMethod = n;

      String type = n.f1.f0.choice.toString();
      String id = n.f2.toString();
      n.name = id;
      n.returnType = type;
      
      if(n.f4.f0.node != null) //se existe algum parâmetro no método
      {
          Node node;
          NodeSequence ns = (NodeSequence)n.f4.f0.node;
          node = ((Type)ns.elementAt(0)).f0.choice;

          String typeparam = ((Type)ns.elementAt(0)).f0.choice.toString();
          if(node instanceof NodeSequence) typeparam = "int array";

          String idparam = ns.elementAt(1).toString();

          NodeListOptional nlo = (NodeListOptional)ns.elementAt(2);

          FormalRest fr;

          /*repetição de parâmetros do método, para o primeiro parâmetro
           * nunca irá acontecer pois o primeiro parâmetro de um método nunca será repetido, mas é melhor prevenir :P
           */
          if(!n.addParam(idparam, typeparam))
          {
              NodeToken nt = (NodeToken) ns.elementAt(1);

              throw new SemanticException("repetition of param '"+idparam+"' on method '"+ n.name +"' in class '"+ SemanticInfo.currentClass.name+"'",nt.beginLine, nt.beginColumn);
          }

          for(int i=0; i < nlo.size(); i++)
          {
            fr = (FormalRest)nlo.nodes.elementAt(i);
            typeparam = fr.f1.f0.choice.toString();
            node = fr.f1.f0.choice;
            if(node instanceof NodeSequence) typeparam = "int array";
            idparam = fr.f2.toString();

            /*repetição de parâmetros do método
             * agora sim é possível de acontecer, pois será testado do segundo parâmetro em diante
             */
            if(!n.addParam(idparam, typeparam))
            {
                NodeToken nt = fr.f2;

                throw new SemanticException("repetition of param '"+idparam+"' on method '"+ n.name +"' in class '"+ SemanticInfo.currentClass.name+"'",nt.beginLine, nt.beginColumn);
            }
          }
      }

      /*Adicionando método na classe*/
      SemanticInfo.currentClass.addMethod(n.name, n);
            
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      n.f10.accept(this);
      n.f11.accept(this);
      n.f12.accept(this);
      
   }

   /**
    * <PRE>
    * f0 -> &lt;INT&gt; &lt;LCOL&gt; &lt;RCOL&gt;
    *       | &lt;INT&gt;
    *       | &lt;BOOLEAN&gt;
    *       | &lt;ID&gt;
    * </PRE>
    */
   public void visit(Type n) throws SemanticException {
      n.f0.accept(this);
   }

   /**
    * <PRE>
    * f0 -> Type()
    * f1 -> &lt;ID&gt;
    * f2 -> &lt;PVIRGULA&gt;
    * </PRE>
    */


   public void visit(VarDecl n) throws SemanticException {
      String type = n.f0.f0.choice.toString();
      String id = n.f1.toString();


      
      //Tentando adicionar a variável na lista de variáveis locais da classe
      if(SemanticInfo.currentMethod == null)
      {
          //variável já existe na classe
          if(!SemanticInfo.currentClass.addVar(id, type))
          {
              //rebolando exceção de variável repetida na classe
              throw new SemanticException("repetition of variable '"+ id +"' in class '"+ SemanticInfo.currentClass.name+"'",n.f1.beginLine, n.f1.beginColumn);
          }
          
      }
      else if(!SemanticInfo.currentMethod.addVar(id, type))
      {
          /*variável já existe no método*/
          throw new SemanticException("repetition of variable '"+ id +"' in method '"+SemanticInfo.currentMethod.name+"' of class '"+ SemanticInfo.currentClass.name+"'",n.f1.beginLine, n.f1.beginColumn);
          
      }

      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
    }

   /**
    * <PRE>
    * f0 -> &lt;CLASS&gt; &lt;ID&gt; &lt;LCHAVE&gt; ( VarDecl() )* ( MethodDecl() )* &lt;RCHAVE&gt;
    *       | &lt;CLASS&gt; &lt;ID&gt; &lt;EXTENDS&gt; &lt;ID&gt; &lt;LCHAVE&gt; ( VarDecl() )* ( MethodDecl() )* &lt;RCHAVE&gt;
    * </PRE>
    */
   public void visit(ClassDecl n) throws SemanticException {
      SemanticInfo.currentClass = n;
      SemanticInfo.currentMethod = null;
      
      NodeSequence s = (NodeSequence)n.f0.choice;

      n.name = s.elementAt(1).toString(); //nome da classe
      if(n.f0.which == 1) //usando a produção de herança
      {
         n.parent = s.elementAt(3).toString(); //armazenando o pai da classe
         if(!SemanticInfo.hashClasses.containsKey(n.parent))
         {
             NodeToken nt = (NodeToken)s.elementAt(3);
             //rebolando a exceção para uma herança em que a classe pai não existe
             throw new SemanticException("attempt to use parent class '"+n.parent+"' that doesn't exist",nt.beginLine, nt.beginColumn);
         }
      }
      else //usando a produção sem herança
      {
        n.parent = null; //pai da classe não existe
      }

      /**
       * Verificando o nome da classe já existe
       */
      if(SemanticInfo.hashClasses.containsKey(n.name))
      {

          if(s.elementAt(1) instanceof NodeToken)
          {
              NodeToken nt = (NodeToken)s.elementAt(1);
              //rebolando a exceção para classe repetida com o nº da linha e coluna
              throw new SemanticException("repetition of class '"+n.name+"'",nt.beginLine, nt.beginColumn);
          }

          //rebolando a exceção para classe repetida
          throw new SemanticException("repetition of class '"+n.name+"'");
      }
      
      //tudo ok, então coloca o nome da classe na tabela de símbolos e continua
      SemanticInfo.hashClasses.put(n.name, n);

      n.f0.accept(this);
   }

   /**
    * <PRE>
    * f0 -> &lt;VIRGULA&gt;
    * f1 -> Exp()
    * </PRE>
    */
   public void visit(ExpRest n) throws SemanticException {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
    * <PRE>
    * f0 -> ( Exp() ( ExpRest() )* )?
    * </PRE>
    */
   public void visit(ExpList n) throws SemanticException {
      n.f0.accept(this);
   }

   /**
    * <PRE>
    * f0 -> ExpSemLR()
    * f1 -> ExpComRR()
    * </PRE>
    */
   public void visit(Exp n) throws SemanticException {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
    * <PRE>
    * f0 -> ( &lt;BINOP&gt; Exp() ExpComRR() | &lt;LCOL&gt; Exp() &lt;RCOL&gt; ExpComRR() | &lt;DOT&gt; &lt;LENGTH&gt; ExpComRR() | &lt;DOT&gt; &lt;ID&gt; &lt;LPAR&gt; ExpList() &lt;RPAR&gt; ExpComRR() )?
    * </PRE>
    */
   public void visit(ExpComRR n) throws SemanticException {
      /*if(n.f0.node != null)
      {
        NodeChoice nc = (NodeChoice)n.f0.node;
        if(nc.which == 3)
        {
            NodeSequence ns = (NodeSequence)nc.choice;
            

        }

      }*/

      n.f0.accept(this);
   }

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
   public void visit(ExpSemLR n) throws SemanticException {
      n.f0.accept(this);
   }

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
   public void visit(Statement n) throws SemanticException {
      n.f0.accept(this);
   }

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
   public void visit(MainClass n) throws SemanticException {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      n.f10.accept(this);
      n.f11.accept(this);
      n.f12.accept(this);
      n.f13.accept(this);
      n.f14.accept(this);
      n.f15.accept(this);
      n.f16.accept(this);
   }

   /**
    * <PRE>
    * f0 -> MainClass()
    * f1 -> ( ClassDecl() )*
    * f2 -> &lt;EOF&gt;
    * </PRE>
    */
   public void visit(Start n) throws SemanticException {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

}