package ansem.syntaxtree;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class SemanticInfo {

    public static String mainClassName; //nome da classe main
    public static ClassDecl currentClass = null; //retorna a referência para a classe atual
    public static MethodDecl currentMethod = null; //retorna a referência para o método atual
    
    public static Hashtable<String,ClassDecl> hashClasses = new Hashtable<String, ClassDecl>(); //classes e seus nomes - tabela de simbolos
    /**
     * Retorna o numero de variaveis da classe
     * @param nome_classe
     * @return
     */
    public static int getTotalVariaveisClasse(String nome_classe)
    {
        ClassDecl classe = SemanticInfo.hashClasses.get(nome_classe);
        return classe.getTotalVariaveis();
    }

    /**
     * Retorna o nó MethodDecl para um método de uma determinada classe
     * @param nome_classe Nome da Classe
     * @param nome_metodo Nome do Método
     * @return nó MethodDecl
     */
    public static MethodDecl getMethodDecl(String nome_classe, String nome_metodo)
    {
        ClassDecl cd = hashClasses.get(nome_classe);
        Hashtable<String, Vector<MethodDecl>> methods = cd.getMethods();

        return methods.get(nome_metodo).elementAt(0);
    }

    /**
     * Recupera o tipo do objeto
     * @param objeto_nome nome da variavel que representa o objeto
     * @param escopo_classe classe para busca
     * @param escopo_metodo metodo para busca
     * @return
     */
    public static String getObjectType(String objeto_nome,String escopo_classe,String escopo_metodo)
    {
        String result = null;
        ClassDecl classe = hashClasses.get(escopo_classe);
        Hashtable<String, Vector<MethodDecl>> metodos = classe.getMethods();
        MethodDecl metodo = metodos.get(escopo_metodo).elementAt(0);
        result = metodo.getVariableType(objeto_nome);
        if(result != null)
            return result;
        result = classe.getVariableType(objeto_nome);
        return result;
    }

    /**
     * Retorna o índice da posição de uma variável dentro de uma classe
     * @param nome_classe Nome da classe
     * @param nome_var Nome da variável
     * @return int Posição da variável
     */
    public static int getClassVarIndex(String nome_classe, String nome_var)
    {
        ClassDecl cd = hashClasses.get(nome_classe);

        //classe não existe
        if(cd == null) return -1;

        Enumeration<String> vars = cd.hashVariables.keys();

        int index = 0;
        while(vars.hasMoreElements())
        {
            if(nome_var.equals(vars.nextElement())) return index;
            index++;
        }

        return -1;
    }
    
}
