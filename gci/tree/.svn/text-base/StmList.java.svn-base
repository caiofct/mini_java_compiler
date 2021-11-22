package gci.tree;
public class StmList {
  public Stm head;
  public StmList tail;
  public boolean mark=false;
  public StmList(Stm h, StmList t) {head=h; tail=t;}

    public String print() {
        StmList varrer;
        String result = "";
		for (varrer=this;(varrer!=null)&&(varrer.head!=null);varrer=varrer.tail)
			result = result + "\n" + (varrer.head.print());

        return result;
    }

    @SuppressWarnings("empty-statement")
  public void remove(int i) {
      StmList varrer;
      int j=0;
      if (i==0) {
          this.head = this.tail==null ? null : this.tail.head;
          this.tail = this.tail==null ? null : this.tail.tail;
      }
      for (varrer=this; (j<i-1)&&(varrer!=null);j++,varrer=varrer.tail);
      varrer.tail = varrer.tail.tail;
  }

  public boolean isMarked() {
      return mark;
  }

  public void mark() {
      mark=true;
  }

  public boolean isEmpty() {
      return (head==null);
  }

    @SuppressWarnings("empty-statement")
  public void add(Stm stm) {
      StmList varrer;
      if (stm == null) {
          return;
      }
      for(varrer=this;(varrer.tail!=null)&&(varrer.head!=null);varrer=varrer.tail);
      if (varrer.head == null)
          varrer.head = stm;
      else
          varrer.tail = new StmList(stm,null);
  }

    @SuppressWarnings("empty-statement")
    public void add(Stm stm, int i) {
        StmList varrer;
        int j=0;
        for (varrer=this;(j<i)&&(varrer!=null);j++,varrer=varrer.tail);
        if (varrer!=null) {
            varrer.tail = new StmList(varrer.head,varrer.tail);
            varrer.head = stm;
        }
    }

    @SuppressWarnings("empty-statement")
    public void add(StmList sl) {
        StmList varrer;
        if (this.head == null) {
            this.head = sl.head;
            this.tail = sl.tail;
            return;
        }
        if (this.tail == null) {
            this.tail=sl;
            return;
        }
        for (varrer=this;(varrer.tail.tail!=null)&&(varrer.tail.head!=null);varrer=varrer.tail);
        if (varrer.tail.head==null) {
            varrer.tail=sl;
        }
        else {
            varrer.tail.tail=sl;
        }
    }

    @SuppressWarnings("empty-statement")
    public Stm get(int i) {
        StmList varrer;
        int j=0;
        for (varrer=this;(j<i)&&(varrer!=null);j++,varrer=varrer.tail);
        if (varrer==null)
            return null;
        return varrer.head;
    }

    @SuppressWarnings("empty-statement")
    public void set(Stm stm, int i) {
        StmList varrer;
        int j=0;
        for (varrer=this;(j<i)&&(varrer!=null);j++,varrer=varrer.tail);
        if (varrer!=null)
            varrer.head = stm;
    }

    @SuppressWarnings("empty-statement")
    public int size() {
        StmList varrer;
        int j=0;
        for (varrer=this;(varrer!=null)&&(varrer.head!=null);j++,varrer=varrer.tail);
        return j;
    }
    
}



