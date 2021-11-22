package Canon;

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

public class TraceSchedule {

  public gci.tree.StmList stms;
  BasicBlocks theBlocks;
  java.util.Dictionary table = new java.util.Hashtable();

   private Print print;
   private FileOutputStream fos;

  gci.tree.StmList getLast(gci.tree.StmList block) {
     gci.tree.StmList l=block;
     while (l.tail.tail!=null)  l=l.tail;
     return l;
  }

  void trace(gci.tree.StmList l) {
   for(;;) {
     gci.tree.LABEL lab = (gci.tree.LABEL)l.head;
     table.remove(lab.label);
     gci.tree.StmList last = getLast(l);
     gci.tree.Stm s = last.tail.head;
     if (s instanceof gci.tree.JUMP) {
	gci.tree.JUMP j = (gci.tree.JUMP)s;
        gci.tree.StmList target = (gci.tree.StmList)table.get(j.targets.head);
	if (j.targets.tail==null && target!=null) {
               last.tail=target;
	       l=target;
        }
	else {
	  last.tail.tail=getNext();
	  return;
        }
     }
     else if (s instanceof gci.tree.CJUMP) {
	gci.tree.CJUMP j = (gci.tree.CJUMP)s;
        gci.tree.StmList t = (gci.tree.StmList)table.get(j.iftrue);
        gci.tree.StmList f = (gci.tree.StmList)table.get(j.iffalse);
        if (f!=null) {
	  last.tail.tail=f; 
	  l=f;
	}
        else if (t!=null) {
	  last.tail.head=new gci.tree.CJUMP(gci.tree.CJUMP.notRel(j.relop),
					j.left,j.right,
					j.iffalse,j.iftrue);
	  last.tail.tail=t;
	  l=t;
        }
        else {
	  gci.temp.Label ff = new gci.temp.Label();
	  last.tail.head=new gci.tree.CJUMP(j.relop,j.left,j.right,
					j.iftrue,ff);
	  last.tail.tail=new gci.tree.StmList(new gci.tree.LABEL(ff),
		           new gci.tree.StmList(new gci.tree.JUMP(j.iffalse),
					    getNext()));
	  return;
        }
     }
     else throw new Error("Bad basic block in TraceSchedule");
    }
  }

  gci.tree.StmList getNext() {
      if (theBlocks.blocks==null) 
	return new gci.tree.StmList(new gci.tree.LABEL(theBlocks.done), null);
      else {
	 gci.tree.StmList s = theBlocks.blocks.head;
	 gci.tree.LABEL lab = (gci.tree.LABEL)s.head;
	 if (table.get(lab.label) != null) {
          trace(s);
	  return s;
         }
         else {
	   theBlocks.blocks = theBlocks.blocks.tail;
           return getNext();
         }
      }
  }

  public TraceSchedule(BasicBlocks b) {
        try {
           fos = new FileOutputStream("saidaBlocos.txt");
        } catch (FileNotFoundException ex) {
            System.err.println(ex.toString());
        }

       PrintStream p = new PrintStream(fos);

    theBlocks=b;
    for(StmListList l = b.blocks; l!=null; l=l.tail)
       table.put(((gci.tree.LABEL)l.head.head).label, l.head);
    stms=getNext();
    p.print(stms.print());
    table=null;
  }        
}


