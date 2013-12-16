package sata.metastock.indices;

import java.math.BigDecimal;

public class MediaMovel {
 
 private double[] close = null;
 private int mm = 9;
 
 public MediaMovel(double[] close,int mm){
  
  this.close = close;
  this.mm = mm; 
  
 }
 
 public double[] getMediaMovel(){
  
  double[] mediaMovel = new double[close.length];
  
  for(int k=0;k<mm && k < mediaMovel.length;k++){    
   mediaMovel[k]=-1;
  }
  
  for(int i=mm-1;i<close.length ;i++){
      
   double soma = 0.0;
   for(int j=0;j<mm;j++){     
    soma += close[i-j];
   }
   mediaMovel[i] = (new BigDecimal(soma).divide(new BigDecimal(mm),4,BigDecimal.ROUND_HALF_UP)).doubleValue();   
   
  }
  return mediaMovel;
 }
 
 /**
  * 
  * @param mm1 é o mm menor
  * @param mm2 é o mm maior
  * @return
  */
 public boolean[] getPtosCompra(double[] mm1,double[] mm2){
  
  boolean[] ptosCompra = new boolean[mm1.length];
  
  for(int i=0;i<mm1.length-1;i++){
   
   if(!(mm1[i]==-1 || mm2[i]==-1)){
    
    if(mm1[i]<mm2[i] && mm1[i+1]>mm2[i+1]){
     ptosCompra[i+1]=true; 
    }
    
   }
  }
  
  return ptosCompra;
 }
 
 /**
  * @param mm1 é o mm menor
  * @param mm2 é o mm maior
  * @return
  */
 public boolean[] getPtosVenda(double[] mm1,double[] mm2){ 
  
  boolean[] ptosVendas = new boolean[mm1.length];
  
  for(int i=0;i<mm1.length-1;i++){
   
   if(!(mm1[i]==-1 || mm2[i]==-1)){
    
    if(mm1[i]>mm2[i] && mm1[i+1]<mm2[i+1]){ 
     ptosVendas[i+1]=true;
    }
    
   }
  }
  
  return ptosVendas;
 }
 
 
 public boolean verificaConsistencia(boolean[] ptoCompras,boolean[] ptoVendas){
  
  boolean comprado = false; 
  boolean vendido = false;
  
  for(int i=0;i<ptoCompras.length;i++){
   
   if(vendido){
    
    if(ptoVendas[i]){
     return false;   // 2 ptos de venda seguidos
    }else if(ptoCompras[i]){ 
     vendido = false;
     comprado = true;
    }
    
   }
   
   if(comprado){
    if(ptoVendas[i]){
     vendido = true;
     comprado = false;   
    }else if(ptoCompras[i]){
     return false; // 2 ptos de compra seguidos 
    }
   }
   
   if(!vendido && !comprado){
    if(ptoVendas[i]){
     vendido = true;
     comprado = false; 
    }else if(ptoCompras[i]){
     vendido = false;
     comprado = true; 
    }
   }
   
  }
  
  return true;
 }

}


