package gci.frame;

/**
 *
 * @author Caio Fellipe
 */
public abstract class AccessList {
    public Access head;
    public AccessList tail;

    public AccessList(Access head, AccessList tail){
           this.head=head;
           this.tail=tail;
    }
}
