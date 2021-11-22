/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ansem.syntaxtree;

/**
 * Exceção da Análise semântica
 *
 * @author caio
 */
public class SemanticException extends Exception
{
    private int line, column;

    public SemanticException(String msg)
    {
        super(msg);
        this.line = -1;
        this.column = -1;
    }

    public SemanticException(String msg, int line, int column)
    {
        super(msg);
        this.line = line;
        this.column = column;
    }

    public int getLine()
    {
        return line;
    }

    public int getColumn()
    {
        return column;
    }

    @Override
    public String toString()
    {
        if(line == -1)
            return getMessage();
        
        return getMessage() + " on line: " + line + " column: " + column;
    }


}
