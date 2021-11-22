package gci.translate;
import gci.tree.*;

/**
 *
 * @author Caio Fellipe
 */
public class Nx {
    private Stm stm;
    
    public Nx(Stm stm)
    {
        this.stm = stm;
    }

    public Stm unNx()
    {
        return stm;
    }

    public Expression unEx()
    {
        return null;
    }
}
