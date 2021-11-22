package gci.temp;

import java.util.Hashtable;
import java.util.Vector;

public class Temp  {
   private static int count;
   private int num;

   /**
    * Mapeia os temporarios a suas respectivas variaveis no programa
    */
   public static Hashtable<String, Vector<Temp>> temps = new Hashtable<String, Vector<Temp>>();

   /**
    * Adiciona um temporário para uma determinada variável
    * @param var Nome da variável
    * @param t Temporário
    * @return void
    */
   public static void addTemp(String var, Temp t)
   {
       Vector<Temp> ts = temps.get(var);
       if(ts == null) //variável ainda não existe
       {
           Vector<Temp> vt = new Vector<Temp>();
           vt.add(t);
           temps.put(var, vt);
       }
       else //variável já existe
       {
           ts.add(t);
           temps.put(var, ts);
       }
   }

   /**
    * Deletar o último temporário para uma determinada variável
    * @param var Nome da variável
    * @return void
    */
   public static void deleteTemp(String var)
   {
        Vector<Temp> ts = temps.get(var);
        if(ts != null)
        {
            ts.removeElementAt(ts.size() -1 );
        }
   }

   /**
    * Verifica se uma variável existe
    * @param var Nome da variável
    * @return true variável existe
    * @return false variável não existe
    */
   public static boolean varExist(String var)
   {
       Vector<Temp> vt = temps.get(var);
       if(vt == null) return false;

       if(vt.size() > 0) return true;

       return false;
   }

   /**
    * Retorna o último temporário de uma variável declarada de um determinado nome
    * @param var Nome da Variável
    * @return o temporário correspondente a variável
    */
   public static Temp getTemp(String var)
   {
       if(temps.get(var) != null) return temps.get(var).lastElement();

       return null;
   }
   @Override
   public String toString() {return "t" + num;}
   public Temp() { 
     num=count++;
   }
}

