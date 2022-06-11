package Yazlab12;


import static java.lang.Thread.interrupted;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class kontrol extends Thread{
    Random random=new Random();
    public static boolean girisKuyruk_kontrol=false;
    static Object lock1=new Object(),lock2=new Object();
    static ArrayList<Integer> giris_kuyruk=new ArrayList<>();
    static int[] cikis_kuyruk={0,0,0,0,0},katlarKisiSayisi={0,0,0,0,0};

    @Override
    public void run() {
        
        avm_giris.start();
        avm_cikis.start();
        asansorKontrolTh.start();
        
    
    }
    
    
    Thread avm_giris=new Thread(new Runnable() {
        @Override
        public void run() {
              while(!interrupted()){
                girisYapapanlar();
                try {
                Thread.sleep(500);  
                } catch (InterruptedException ex) {
                Logger.getLogger(kontrol.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    });
    Thread avm_cikis=new Thread(new Runnable() {
        @Override
        public void run() {
              while(!interrupted()){
            cikisYapanlar();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(kontrol.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }
    });
    Thread asansorKontrolTh=new Thread(new Runnable() {
        @Override
        public void run() {
              while(!interrupted()){
                asansor_kontrol();
               }
        }
    });
    public void asansor_kontrol(){
      
       int aktif_asansor=0;
       for(int i=0;i<4;i++){   
            if(Asansor.asansorler.get(i).aktif==true){
            aktif_asansor++;
            }
       }
       int kuyruk_sayisi=giris_kuyruk.size()+cikis_kuyruk[1]+cikis_kuyruk[2]+cikis_kuyruk[3]+cikis_kuyruk[4];
    
  
      
        if((kuyruk_sayisi>=(aktif_asansor+1)*20 ) && aktif_asansor<4){
         Asansor.asansorler.get(aktif_asansor).aktif=true;
        // Asansor.asansorler.get(aktif_asansor).start();
         
       
        Asansor.asansorler.get(aktif_asansor).name=aktif_asansor+"";
        }
        if((kuyruk_sayisi<(aktif_asansor+1)*20) && aktif_asansor!=0){
         Asansor.asansorler.get(aktif_asansor-1).aktif=false;
        }
        
    }
    public synchronized void girisYapapanlar(){
     
        synchronized(lock1){
     
            int gelenSayisi=random.nextInt(10)+1;
         
            for(int i=0;i<gelenSayisi;i++){
            giris_kuyruk.add(random.nextInt(4)+1);
            }
          
       //   System.out.println("Giris:"+giris_kuyruk);
           girisKuyruk_kontrol=true; 
           }
    }
    public synchronized void cikisYapanlar(){
        synchronized(lock2){
            
            int cikacakSayisi=random.nextInt(4)+1;
            for(int i=0;i<4;i++){
                int kat=random.nextInt(4)+1;
                if(katlarKisiSayisi[kat]==0)
                    continue;
                
                int cikacak=random.nextInt(cikacakSayisi)+1;
                if((katlarKisiSayisi[kat]-cikis_kuyruk[kat])<cikacak)
                    continue;
                    
                cikis_kuyruk[kat]+=cikacak;
             
                cikacakSayisi-=cikacak;
                
                if(cikacakSayisi==0)
                break;
                }
        }
    }
}
