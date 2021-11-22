/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gci;

import gci.tree.StmList;
import java.util.Vector;

/**
 * Guarda o conjunto de fragmentos do programa
 * @author Fabio
 */
public class Fragmentos {
    public static Vector<ProcFrag> frags = new Vector<ProcFrag>();
    public static StmList stmList = null;
    private static StmList lastStmList = null;
    public static void addFrag(ProcFrag pf)
    {
        frags.add(pf);
        if(stmList == null)
        {
            stmList = new StmList(pf.stm, null);
            lastStmList = stmList;
        }
        else
        {
            StmList newStmList = new StmList(pf.stm,null);
            lastStmList.tail = newStmList;
            lastStmList = newStmList;
        }
    }
}
