package gci;
import ansem.visitor.*;
import ansem.syntaxtree.*;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
import gci.tree.*;
import gci.translate.*;
import gci.frame.*;
import gci.temp.Label;
import gci.temp.Temp;
import java.io.FileOutputStream;

/**
 *
 * @author Caio Fellipe
 */
public class IRTreeVisitor implements IRVisitor {

   private Print print;
   private static String currentClass = null;
   private static String currentMethod = null;
   private FileOutputStream fos;
   public IRTreeVisitor()
   {
        try {
           fos = new FileOutputStream("saidaIRTree.txt");
        } catch (FileNotFoundException ex) {
            System.err.println(ex.toString());
        }

       PrintStream p = new PrintStream(fos);
       print = new Print(p);
   }
   
   public IRType visit(NodeList n) throws SemanticException {
      n.accept(this);
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
         e.nextElement().accept(this);

      return null;
   }

   public IRType visit(NodeListOptional n) throws SemanticException{
      if ( n.present() )
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
            e.nextElement().accept(this);

      return null;
   }

   public IRType visit(NodeOptional n) throws SemanticException{
      if ( n.present() )
         n.node.accept(this);

      return null;
   }

   public IRType visit(NodeSequence n) throws SemanticException{
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
         e.nextElement().accept(this);

      return null;
   }

   public IRType visit(NodeToken n) { return null; }

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
   public IRType visit(FormalRest n) throws SemanticException {
      /*Temp tmp = new Temp();
      Temp.temps.put(n.f2.tokenImage, tmp); //adicionando variável no hash de temporários
      Expression exp = new Ex(new TEMP(tmp)).unEx(); //criando temporário, pq nao temos um frame real para usar  :'(

      print.prExp(exp); //imprimindo este nó

      return exp;*/
       return null;
   }

   /**
    * <PRE>
    * f0 -> ( Type() &lt;ID&gt; ( FormalRest() )* )?
    * </PRE>
    */
   public IRType visit(FormalList n) throws SemanticException {
       // nao sei se isso e' necessario depois que eu li a parte "CLASSES AND OBJECTS" na pagina 158
      if(n.f0.present())
      {
          NodeSequence ns = (NodeSequence) n.f0.node;
          String param1 = ((NodeToken)ns.elementAt(1)).tokenImage;
          Expression exp1 = Frame.level.last.formals.tail.head;
          Frame.level.mapFormals.put(param1, exp1);

          NodeListOptional nlo = (NodeListOptional)ns.elementAt(2);
          if(nlo.present())
          {
              ExpressionList current = Frame.level.last.formals.tail.tail;
              for(int i = 0; i < nlo.size(); i++)
              {
                  FormalRest fr = (FormalRest)nlo.elementAt(i);
                  Frame.level.mapFormals.put(fr.f2.tokenImage, current.head);
                  current = current.tail;

              }
          }
      }
      return null;
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
   public IRType visit(MethodDecl n) throws SemanticException {
      currentMethod = n.name;
      Frame.newFrame(new Label(currentClass + "$" + currentMethod));
      Frame.level.mapFormals = new Hashtable<String,Expression>();
      n.f4.accept(this);
      Stm varDecl = null;
      Stm statements = null;
      Stm _return = null;
      if(n.f7.present())
      {
          for(int i = n.f7.size() - 1; i >= 0; i--)
          {
              Stm vd = new Ex((Expression)((VarDecl)n.f7.elementAt(i)).accept(this)).unNx();
              if(varDecl == null)
              {
                   varDecl = new SEQ(vd,null);
              }
              else
              {
                  varDecl = new SEQ(vd,varDecl);
              }
          }
      }

      if(n.f8.present())
      {
          for(int i = n.f8.size() - 1; i >= 0; i--)
          {
              Stm stm = (Stm)((Statement)n.f8.elementAt(i)).accept(this);
              if(varDecl == null)
              {
                   statements = new SEQ(stm,null);
              }
              else
              {
                  statements = new SEQ(stm,statements);
              }
          }
      }
      _return = new Nx(new MOVE(new TEMP(Frame.RV()),(Expression)n.f10.accept(this))).unNx();

      // como é que junta vardecl, statements,
      // e _return pra colocar um comando do tipo Stm
      //e passar para Frame.level.procEntryExit(resultadoDaJuncao)?
      // agora e' necessario remover as variaveis do contexto Temp.deleteTemp
      Stm result = null;
      if(varDecl != null)
      {
          result = new SEQ(varDecl,null);
      }
      if(statements != null)
      {
          if(result != null)
          {
              ((SEQ)result).right = result;
          }
          else
          {
              result = new SEQ(statements,null);
          }
      }
      if(result != null)
      {
          if(((SEQ)result).right == null)
          {
              ((SEQ)result).right = _return;
          }
          else
          {
              ((SEQ)result).right = new SEQ(statements,_return);
          }
      }
      else
      {
          result = _return;
      }

      Frame.level.procEntryExit(result);
      print.prStm(result);
      Frame.level = Frame.level.last; // saindo do contexto atual e voltando ao anterior

      MethodDecl mdd = SemanticInfo.getMethodDecl(currentClass, currentMethod);
      Hashtable<String, String> hvs = mdd.hashVariables;

      Enumeration<String> variaveis = hvs.keys();
      while(variaveis.hasMoreElements())
      {
          Temp.deleteTemp(variaveis.nextElement());
      }

      return new TEMP(Frame.RV()); // o resultado da chamada de um metodo e' o valor armazenado no reg de retorno, eu acho
   }

   /**
    * <PRE>
    * f0 -> &lt;INT&gt; &lt;LCOL&gt; &lt;RCOL&gt;
    *       | &lt;INT&gt;
    *       | &lt;BOOLEAN&gt;
    *       | &lt;ID&gt;
    * </PRE>
    */
   public IRType visit(Type n) throws SemanticException {
      n.f0.accept(this);

      return null;
   }

   /**
    * <PRE>
    * f0 -> Type()
    * f1 -> &lt;ID&gt;
    * f2 -> &lt;PVIRGULA&gt;
    * </PRE>
    */
   public IRType visit(VarDecl n) throws SemanticException {

      Temp tmp = new Temp();
      Temp.addTemp(n.f1.tokenImage, tmp);//adicionando variável no hash de temporários

      Expression exp = new Ex(new TEMP(tmp)).unEx(); //criando temporário, pq nao temos um frame real para usar  :'(

//      print.prExp(exp);

      return exp; 
    }

   /**
    * <PRE>
    * f0 -> &lt;CLASS&gt; &lt;ID&gt; &lt;LCHAVE&gt; ( VarDecl() )* ( MethodDecl() )* &lt;RCHAVE&gt;
    *       | &lt;CLASS&gt; &lt;ID&gt; &lt;EXTENDS&gt; &lt;ID&gt; &lt;LCHAVE&gt; ( VarDecl() )* ( MethodDecl() )* &lt;RCHAVE&gt;
    * </PRE>
    */
   public IRType visit(ClassDecl n) throws SemanticException {
      currentClass = n.name;
      currentMethod = null;
      //n.f0.accept(this);

      return null;
   }

      /**
    * <PRE>
    * f0 -> &lt;VIRGULA&gt;
    * f1 -> Exp()
    * </PRE>
    */
   public IRType visit(ExpRest n) throws SemanticException {

     return n.f1.accept(this);
   }

   /**
    * <PRE>
    * f0 -> ( Exp() ( ExpRest() )* )?
    * </PRE>
    */
   public IRType visit(ExpList n) throws SemanticException {
      if(!n.f0.present()) //nenhum parâmetro
      {
          return null;
      }


      NodeSequence ns = (NodeSequence)n.f0.node;
      Exp exp = (Exp)ns.elementAt(0);

      NodeListOptional nlo = (NodeListOptional)ns.elementAt(1);
      if(!nlo.present()) //apenas um parâmetro
          return (IRType) new ExpressionList((Expression)exp.accept(this), null);

      ExpressionList expl = null;
      for(int i = nlo.nodes.size() - 1; i >= 0; i--)
      {
           if(expl == null)
           {
               expl = new ExpressionList((Expression)nlo.nodes.get(i).accept(this),null);
           }
           else
           {

               expl = new ExpressionList((Expression)nlo.nodes.get(i).accept(this),expl );
           }
       }

       return (IRType) new ExpressionList((Expression)exp.accept(this),expl);
    }

   /**
    * <PRE>
    * f0 -> ExpSemLR()
    * f1 -> ExpComRR()
    * </PRE>
    */
   public IRType visit(Exp n) throws SemanticException {
      n.f1.expslr = (Expression) n.f0.accept(this);
      n.f1.expslr2 = n.f0;
      Expression exp3 = (Expression)n.f1.accept(this);
      
      return exp3;

   }

   /**
    * <PRE>
    * f0 -> ( &lt;BINOP&gt; Exp() ExpComRR()
    *       | &lt;LCOL&gt; Exp() &lt;RCOL&gt; ExpComRR()
    *       | &lt;DOT&gt; &lt;LENGTH&gt; ExpComRR()
    *       | &lt;DOT&gt; &lt;ID&gt; &lt;LPAR&gt; ExpList() &lt;RPAR&gt; ExpComRR() )?
    * </PRE>
    */
   public IRType visit(ExpComRR n) throws SemanticException {
      if(n.f0.present())
      {
          NodeChoice nc = (NodeChoice)n.f0.node;
          if(nc.which == 0) // binop
          {
              Expression expleft = n.expslr;
              NodeSequence ns = (NodeSequence)nc.choice;
              String op = ns.elementAt(0).toString();
              Exp expr = (Exp)ns.elementAt(1);
              Expression expright = (Expression)expr.accept(this);
              Expression result = null;
              ExpComRR exprr = (ExpComRR)ns.elementAt(2);
              if(op.equals("+")) //soma
              {
                 result = new Ex(new BINOP(BINOP.PLUS, expleft, expright)).unEx();
              }
              else if(op.equals("-")) //subtração
              {
                 result = new Ex(new BINOP(BINOP.MINUS, expleft, expright)).unEx();
              }
              else if(op.equals("*")) //multiplicação
              {
                 result = new Ex(new BINOP(BINOP.MUL, expleft, expright)).unEx();
              }
              else if(op.equals("<")) //menor
              {
                 result = new Ex(new BINOP(CJUMP.LT, expleft, expright)).unEx();
              }
              else if(op.equals("&&")) //and
              {
                  result = new Ex(new BINOP(BINOP.AND, expleft, expright)).unEx();
              }
              exprr.expslr = result;
              return exprr.accept(this);

          }
          else if(nc.which == 1) // (Exp[Exp])
          {
              Expression exp_id = n.expslr;
              NodeSequence ns = (NodeSequence)nc.choice;
              Exp exp_size = (Exp) ns.elementAt(1);
              Expression result = (Expression)new Ex(new MEM(new BINOP(BINOP.PLUS,new MEM(exp_id), new BINOP(BINOP.MUL,new BINOP(BINOP.PLUS,(Expression)exp_size.accept(this),new CONST(1)),new CONST(Frame.wordSize()))))).unEx();
              ExpComRR exprr = (ExpComRR)ns.elementAt(3);
              exprr.expslr = result;
              return exprr.accept(this);
          }
          else if(nc.which == 2) // (Exp).length
          {
              NodeSequence ns = (NodeSequence)nc.choice;
              Expression exp_id = n.expslr;
              Expression result = (Expression)new Ex(new MEM(new BINOP(BINOP.PLUS,new MEM(exp_id), new CONST(0)))).unEx();
              ExpComRR exprr = (ExpComRR)ns.elementAt(2);
              exprr.expslr = result;
              return exprr.accept(this);
          }
          else if(nc.which == 3) // obj.metodo
          {
              String tipo_variavel = null;
              if(n.expslr2.f0.which == 6) // o metodo e' chamada depois de um new A().Teste()
              {
                  NodeSequence ns = (NodeSequence)n.expslr2.f0.choice;
                  NodeToken nt = (NodeToken)ns.elementAt(1);
                  tipo_variavel = nt.tokenImage;
              }
              else
              {
                String variavel_nome = ((NodeToken)n.expslr2.f0.choice).tokenImage;
                tipo_variavel = SemanticInfo.getObjectType(variavel_nome, currentClass, currentMethod);
              }
              String nome_metodo = ((NodeToken)((NodeSequence)((NodeChoice)n.f0.node).choice).elementAt(1)).tokenImage;

              Frame.level.formals.head = n.expslr;
              NodeSequence ns = (NodeSequence)nc.choice;
              ExpressionList parametros = (ExpressionList)ns.elementAt(3).accept(this);
              Frame.level.formals.tail = parametros;
              MethodDecl md = SemanticInfo.getMethodDecl(tipo_variavel, nome_metodo);
              Label label = new Label();
              Expression call = new Ex(new CALL(new NAME(label),Frame.level.formals)).unEx();

              String oldClass = currentClass;
              String oldMethod = currentMethod;
              currentClass = tipo_variavel;
              Expression exp_metodo = (Expression)md.accept(this);
              currentClass = oldClass;
              currentMethod = oldMethod;

              Expression result = new ESEQ(new Ex(call).unNx(),new ESEQ(new LABEL(label),exp_metodo));
              return result;
 
          }
      }
      else {
          return n.expslr;
      }
      
      //System.out.println("Regra nao implementada em visit(ExpComRR n)");
      return null;
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
   public IRType visit(ExpSemLR n) throws SemanticException {
      Expression exp;

      if(n.f0.which == 0) //inteiro literal
      {
          exp = new Ex(new CONST(Integer.parseInt(n.f0.choice.toString()))).unEx();
          return exp;
      }
      else if(n.f0.which == 1) //constante TRUE
      {
          exp = new Ex(new CONST(1)).unEx();
          return exp;
      }
      else if(n.f0.which == 2) //constante FALSE
      {
          exp = new Ex(new CONST(0)).unEx();
          return exp;
      }
      else if(n.f0.which == 3) //variável, por ser um temp, um parametro de metodo ou uma variavel declarada na classe
      {
          String var = ((NodeToken)n.f0.choice).tokenImage;
          if(Temp.varExist(var)) //variável
          {
              exp = new Ex(new TEMP(Temp.getTemp(var))).unEx();
              return exp;
          }else if(Frame.level.mapFormals.containsKey(var))
          {
              return Frame.level.mapFormals.get(var);
          }
          else
          {
              int posicao_da_variavel_na_classe = SemanticInfo.getClassVarIndex(currentClass,var);
              if(posicao_da_variavel_na_classe >= 0)
              {
                  Expression exp_this = Frame.level.last.formals.head;
                  return (Expression)new Ex(new MEM(new BINOP(BINOP.PLUS,new MEM(exp_this), new BINOP(BINOP.MUL,new CONST(posicao_da_variavel_na_classe),new CONST(Frame.wordSize()))))).unEx();
              }

          }
      }
      else if(n.f0.which == 4) // this
      {
          exp = Frame.level.last.formals.head;
          return exp;
      }
      else if(n.f0.which == 5) // Criar array
      {
          NodeSequence ns = (NodeSequence) n.f0.choice;
          Exp e = (Exp) ns.elementAt(3);
          Expression arrayLength = new BINOP(BINOP.MUL,new CONST(Frame.wordSize()),new BINOP(BINOP.PLUS, (Expression) e.accept(this), new CONST(1))) ;
          exp = new Ex(gci.frame.Frame.externalCall("initArray", new ExpressionList(arrayLength, null))).unEx();

          return exp;
      }
      else if(n.f0.which == 6) // Criar objeto
      {
          NodeSequence ns = (NodeSequence) n.f0.choice;
          String classe_nome = ((NodeToken)ns.elementAt(1)).tokenImage;
          int num_variaveis = SemanticInfo.getTotalVariaveisClasse(classe_nome);
          int objectLength = Frame.wordSize()*num_variaveis;
          exp = new Ex(gci.frame.Frame.externalCall("initObject", new ExpressionList(new CONST(objectLength), null))).unEx();
          return exp;
      }
      else if(n.f0.which == 7) //(!Exp)
      {
          NodeSequence ns = (NodeSequence) n.f0.choice;
          Exp e = (Exp) ns.elementAt(1);
          Expression eval = (Expression)e.accept(this);
          Expression result = null;
          result = new Ex(new BINOP(BINOP.MINUS,new CONST(1),eval)).unEx();
          return result;
      }
      else if(n.f0.which == 8) // (Exp)
      {
          NodeSequence ns = (NodeSequence) n.f0.choice;
          Exp e = (Exp) ns.elementAt(1);
          return e.accept(this);
      }

      //System.out.println("Regra nao implementada em visit(ExpSemLR n)");
      return null;
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
   public IRType visit(Statement n) throws SemanticException {
       Stm stm = null;
       if(n.f0.which == 0)
       {
           stm = null;
           NodeSequence ns = (NodeSequence)n.f0.choice;
           NodeListOptional nlo = (NodeListOptional)ns.nodes.elementAt(1);
           for(int i = nlo.nodes.size() - 1; i >= 0; i--)
           {
               if(stm == null)
               {
                    stm = new SEQ((Stm)nlo.nodes.get(i).accept(this),null);
               }
               else
               {
                   stm = new SEQ((Stm)nlo.nodes.get(i).accept(this),stm);
               }
           }
//           if(stm != null)
//            print.prStm(stm);
           return stm;
       }
       else if(n.f0.which == 1) //comando if
       {
            NodeSequence ns = (NodeSequence)n.f0.choice;
            Expression eval = (Expression)ns.elementAt(2).accept(this);

            Label ltrue = new Label();
            Label lfalse = new Label();
            stm = new SEQ(new Nx(new CJUMP(CJUMP.EQ, eval, new CONST(1), ltrue, lfalse)).unNx(),
                        new SEQ(new SEQ(new LABEL(ltrue), (Stm)ns.elementAt(4).accept(this)),
                        new SEQ(new LABEL(lfalse), (Stm)ns.elementAt(6).accept(this))));
//            print.prStm(stm);
            return stm;
            
       }
       else if(n.f0.which == 2) //comando while
       {
           Label ltest = new Label();
           Label lT = new Label();
           Label lF = new Label();

           NodeSequence ns = (NodeSequence)n.f0.choice;
           Expression eval = (Expression)ns.elementAt(2).accept(this);
           stm = new SEQ(
                new SEQ(
                        new SEQ(new LABEL(ltest),new CJUMP(CJUMP.EQ,eval,new CONST(1),lT,lF)),
                        new SEQ(new SEQ(new LABEL(lT),(Stm)ns.elementAt(4).accept(this)), new JUMP(ltest))
                ),
                new LABEL(lF)
            );
//           print.prStm(stm);
           return stm;

       }
       else if(n.f0.which == 3) // print de int
       {
           NodeSequence ns = (NodeSequence)n.f0.choice;
           Expression eval = (Expression)ns.elementAt(6).accept(this);
           stm = new Ex(gci.frame.Frame.externalCall("print", new ExpressionList(eval, null))).unNx();
//           print.prExp(result);
           
           return stm;
       }
       else if(n.f0.which == 4) //atribuição
       {
           NodeSequence ns = (NodeSequence)n.f0.choice;
           String var = ((NodeToken)ns.elementAt(0)).tokenImage;
           
            Expression exp =(Expression) ns.elementAt(2).accept(this);

            Temp t = Temp.getTemp(var);
            if(t != null)
            {
                stm = new Nx(new MOVE(new TEMP(t), exp)).unNx();
            }
            int posicao_da_variavel_na_classe = SemanticInfo.getClassVarIndex(currentClass,var);
            if(posicao_da_variavel_na_classe >= 0)
            {
              Expression exp_this = Frame.level.last.formals.head;
              Expression exp_result = (Expression)new Ex(new MEM(new BINOP(BINOP.PLUS,new MEM(exp_this), new BINOP(BINOP.MUL,new CONST(posicao_da_variavel_na_classe),new CONST(Frame.wordSize()))))).unEx();
              stm = new Nx(new MOVE(exp_result, exp)).unNx();
            
            }
//            print.prStm(stm);
            return stm;
       }
       else if(n.f0.which == 5) //atribuição de vetor
       {
           NodeSequence ns = (NodeSequence)n.f0.choice;
           Expression exp_direito =(Expression) ns.elementAt(5).accept(this);
           Expression exp_indice =(Expression) ns.elementAt(2).accept(this);
           String var = ((NodeToken)ns.elementAt(0)).tokenImage;

           stm = new Nx(new MOVE(new MEM(new BINOP(BINOP.PLUS,new TEMP(Temp.getTemp(var)), new BINOP(BINOP.PLUS,exp_indice,new CONST(1)))),exp_direito)).unNx();
//           print.prStm(stm);
           return stm;
       }
      //System.out.println("REGRA NAO IMPLEMENTADA EM visit(Statement n)");
      return n.f0.accept(this);
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
   public IRType visit(MainClass n) throws SemanticException {
      String nome_classe = n.f1.tokenImage;
      Frame f = Frame.newFrame(new Label(nome_classe + "$" + "main"));
      Stm stm = (Stm)n.f14.accept(this);
      print.prStm(stm);
      f.procEntryExit(stm);
      return null;
   }

   /**
    * <PRE>
    * f0 -> MainClass()
    * f1 -> ( ClassDecl() )*
    * f2 -> &lt;EOF&gt;
    * </PRE>
    */
   public IRType visit(Start n) throws SemanticException {
      n.f0.accept(this);
      n.f1.accept(this);
      
      return null;
   }

}
