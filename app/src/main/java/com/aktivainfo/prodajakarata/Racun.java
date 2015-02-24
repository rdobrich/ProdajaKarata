package com.aktivainfo.prodajakarata;

public class Racun {


	Integer RacunId = null;
	String RacunGuid = null;
    String VrijemeRacuna = null;
    String BrojRacuna = null;
    String Izlet = null;
    String Karte = null;
    String Meniji = null;
    
    
    public Racun(){
    }

    public Racun(Integer rId, String rg, String vr, String br, String i, String k, String m){
        this.RacunId = rId;
        this.RacunGuid = rg;
        this.VrijemeRacuna = vr;
        this.BrojRacuna = br;
        this.Izlet = i;
        this.Karte = k;
        this.Meniji = m;
    }

    public Integer getRacunId() {
        return RacunId;
    }
    public void setRacunId(Integer id) {
        this.RacunId = id;
    }

    public String getRacunGuid() {
        return RacunGuid;
    }
    public void setRacunGuid(String rg) {
        this.RacunGuid = rg;
    }

    public String getVrijemeRacuna() {
        return VrijemeRacuna;
    }
    public void setVrijemeRacuna(String vr) {
        this.VrijemeRacuna = vr;
    }
    
    public String getBrojRacuna() {
        return BrojRacuna;
    }
    public void setBrojRacuna(String br) {
        this.BrojRacuna = br;
    }
    
    public String getIzlet() {
        return Izlet;
    }
    public void setIzlet(String i) {
        this.Izlet = i;
    }
    
    public String getKarte() {
        return Karte;
    }
    public void setKarte(String k) {
        this.Karte = k;
    }
    
    public String getMeniji() {
        return Meniji;
    }
    public void setMeniji(String m) {
        this.Meniji = m;
    }
    
}