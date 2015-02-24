package com.aktivainfo.prodajakarata;

public class Uplata {


	Integer UplataId = null;
    String NacinPlacanja = null;
    String Uplata = null;
    String Isplata = null;
    String Iznos = null;
    
    
    public Uplata(){
    }

    public Uplata(Integer uid, String np, String up, String is, String i){
        this.UplataId = uid;
        this.NacinPlacanja = np;
        this.Uplata = up;
        this.Isplata = is;
        this.Iznos = i;
    }

    public Integer getUplataId() {
        return UplataId;
    }
    public void setUplataId(Integer id) {
        this.UplataId = id;
    }


    public String getNacinPlacanja() {
        return NacinPlacanja;
    }
    public void setNacinPlacanja(String ip) {
        this.NacinPlacanja = ip;
    }
    
    public String getIznos() {
        return Iznos;
    }
    public void setIznos(String i) {
        this.Iznos = i;
    }
    
    public String getUplata() {
        return Uplata;
    }
    public void setUplata(String i) {
        this.Uplata = i;
    }
    
    public String getIsplata() {
        return Isplata;
    }
    public void setIsplata(String i) {
        this.Isplata = i;
    }
    
}