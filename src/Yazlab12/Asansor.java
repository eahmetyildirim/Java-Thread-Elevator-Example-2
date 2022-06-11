
package Yazlab12;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Asansor extends Thread{
   int Maxkapasite=10,yolcuSayisi=0;
      public String name;
   public static ArrayList<Asansor> asansorler=new ArrayList<Asansor>();
   boolean yon=true; // true yukarı false aşağı
   int[] yolcular={0,0,0,0,0};
   int bulunulanKat=0;

   public boolean aktif;

    public Asansor(String name) {
        this.name = name;
        this.aktif=false;
    }
   
   
   
    @Override
    public void run() {
        while(!interrupted()){
          while(this.aktif==false && yolcuSayisi==0){
              System.out.println("Asansor:"+name+ "beklemede.");
          }
           
            YolcuIndir();
            YolcuAl();
         
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Asansor.class.getName()).log(Level.SEVERE, null, ex);
            }
            yonBelirle();
            
         
        }
    }
    public  void YolcuAl(){

    synchronized(kontrol.lock2){
       if(bulunulanKat==0 && kontrol.giris_kuyruk.size()>0){
        for(int i=0;i<kontrol.giris_kuyruk.size();i++){
            if(yolcuSayisi>=Maxkapasite)
                break;
            
            yolcular[kontrol.giris_kuyruk.get(i)]++;
           
          
            kontrol.giris_kuyruk.remove(i);
            yolcuSayisi++;
            
          }
        //   System.out.println("Zemin kattan yolcu alındı");
           
       }else if(kontrol.cikis_kuyruk[bulunulanKat]!=0 && yolcuSayisi!=Maxkapasite){
             
            if(yolcuSayisi+kontrol.cikis_kuyruk[bulunulanKat]<=Maxkapasite){
                 yolcular[0]+=kontrol.cikis_kuyruk[bulunulanKat];
                 yolcuSayisi+=kontrol.cikis_kuyruk[bulunulanKat];
                 kontrol.katlarKisiSayisi[bulunulanKat]-=kontrol.cikis_kuyruk[bulunulanKat];
                 kontrol.cikis_kuyruk[bulunulanKat]=0;
            }else if(yolcuSayisi+kontrol.cikis_kuyruk[bulunulanKat]>Maxkapasite){
                int alinacak_yolcu_S=Maxkapasite-yolcuSayisi;
                 yolcular[0]+=alinacak_yolcu_S;
                 yolcuSayisi+=alinacak_yolcu_S;
                 kontrol.katlarKisiSayisi[bulunulanKat]-=alinacak_yolcu_S;
                 kontrol.cikis_kuyruk[bulunulanKat]-=alinacak_yolcu_S;
            
            }
               
                
             
    //  System.out.println("Ara kattan yolcu alındı"); 
       }
   //     System.out.println(yolcular[0]+" "+yolcular[1]+" "+yolcular[2]+" "+yolcular[3]+" "+yolcular[4]+"  Bulunulan kat:"+bulunulanKat);
       kontrol.girisKuyruk_kontrol=false; 
       }
    }
    public synchronized void YolcuIndir(){
         synchronized(kontrol.lock1){
            if(bulunulanKat==0 && yolcular[0]!=0){
                yolcuSayisi-=yolcular[0];
                yolcular[0]=0;
           //     System.out.println("Zemin katta yolcu indi");
            }else if(yolcular[bulunulanKat]!=0){
                yolcuSayisi-=yolcular[bulunulanKat];
                kontrol.katlarKisiSayisi[bulunulanKat]+=yolcular[bulunulanKat];
                yolcular[bulunulanKat]=0;
         //       System.out.println("Ara katta yolcu indi");
            }
        }
    }       
    public synchronized void yonBelirle(){
        if(bulunulanKat==0){
        int yolcu_say=yolcular[1]+yolcular[2]+yolcular[3]+yolcular[4];
            if(yolcu_say!=0){
            yon=true;
            bulunulanKat++;
            }
        }else if(bulunulanKat==4){
        yon=false;
        bulunulanKat--;
        }else{
             int yolcu_say=0;
             for(int i=bulunulanKat+1;i<5;i++){
             yolcu_say+=yolcular[i];
             }
             if(yon==true && yolcu_say>0){
             bulunulanKat++;
             }else if(yon==true && yolcu_say==0){
             yon=false;
             bulunulanKat--;
             }else if(yon==false){
             bulunulanKat--;
                     
             }
             
        }
        
    }
    public int getYolcuSayisi() {
        return yolcuSayisi;
    }

    public void setYolcuSayisi(int yolcuSayisi) {
        this.yolcuSayisi = yolcuSayisi;
    }

    public boolean isYon() {
        return yon;
    }

    public void setYon(boolean yon) {
        this.yon = yon;
    }

    public int[] getYolcular() {
        return yolcular;
    }

    public void setYolcular(int[] yolcular) {
        this.yolcular = yolcular;
    }

    public int getBulunulanKat() {
        return bulunulanKat;
    }

    public void setBulunulanKat(int bulunulanKat) {
        this.bulunulanKat = bulunulanKat;
    }
}
