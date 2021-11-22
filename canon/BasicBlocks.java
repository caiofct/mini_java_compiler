package Canon;

public class BasicBlocks {
  public StmListList blocks;
  public gci.temp.Label done;

  private StmListList lastBlock;
  private gci.tree.StmList lastStm;

  private void addStm(gci.tree.Stm s) {
	lastStm = lastStm.tail = new gci.tree.StmList(s,null);
  }

  private void doStms(gci.tree.StmList l) {
      if (l==null) 
	doStms(new gci.tree.StmList(new gci.tree.JUMP(done), null));
      else if (l.head instanceof gci.tree.JUMP
	      || l.head instanceof gci.tree.CJUMP) {
	addStm(l.head);
	mkBlocks(l.tail);
      } 
      else if (l.head instanceof gci.tree.LABEL)
           doStms(new gci.tree.StmList(new gci.tree.JUMP(((gci.tree.LABEL)l.head).label),
	  			   l));
      else {
	addStm(l.head);
	doStms(l.tail);
      }
  }

  void mkBlocks(gci.tree.StmList l) {
     if (l==null) return;
     else if (l.head instanceof gci.tree.LABEL) {
	lastStm = new gci.tree.StmList(l.head,null);
        if (lastBlock==null)
  	   lastBlock= blocks= new StmListList(lastStm,null);
        else
  	   lastBlock = lastBlock.tail = new StmListList(lastStm,null);
	doStms(l.tail);
     }
     else mkBlocks(new gci.tree.StmList(new gci.tree.LABEL(new gci.temp.Label()), l));
  }
   

  public BasicBlocks(gci.tree.StmList stms) {
    done = new gci.temp.Label();
    mkBlocks(stms);
  }
}
