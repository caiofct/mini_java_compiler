package gci.frame;
import gci.Fragmentos;
import gci.ProcFrag;
import gci.temp.*;
import gci.tree.Expression;
import gci.tree.ExpressionList;
import java.util.Hashtable;
/**
 * Classe que representa um frame em minijava
 * Ele é usado em toda chamada de funcao
 * os formals guarda os parametros de entrada de uma funcao quando uma funcao e' chamada
 * @author Fabio, Caio Fellipe
 */
public class Frame {
    public Label name;
    public ExpressionList formals; // todos os argumentos que sao passados para o proximo frame (ex. argumentos de chamada de funcao)
    public Hashtable<String,Expression> mapFormals; // mapeia os formais declarados com os formais passados
    /**
     * Registrador de retorno;
     */
    private static Temp rv = new Temp(); // existe somente um registrador de retorno para todo o programa
    /**
     * Guarda o frame corrente
     */
    public static Frame level; // frame corrente da pilha
    public Frame last; // frame anterior
    static
    {
        level = null;
    }
    public static Frame newFrame (Label name)
    {
        Frame lastFrame = level;
        
        level = new Frame();
        level.name = name;
        level.formals = new ExpressionList(null, null);
        level.mapFormals = new Hashtable<String,Expression>();
        //level.currentFormals = new ExpressionList(null, null);
        if(lastFrame != null)
        {
            level.last = lastFrame;

        }
        else
        {
            level.last = null;
        }
        return level;
    }

    public static int wordSize() { return 4; };

    public static Temp RV()
    {
        return rv;
    }

    /**
     * cria um novo fragmento
     * @param body
     */
    public void procEntryExit(gci.tree.Stm body)
    {
        ProcFrag frag = new ProcFrag(body, this);
        Fragmentos.addFrag(frag);
    }

    public static gci.tree.Expression externalCall(String func, gci.tree.ExpressionList args) {
       return new gci.tree.CALL(new gci.tree.NAME(new gci.temp.Label(func)), args);
    }
}
