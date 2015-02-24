package com.aktivainfo.prodajakarata;

public class Mjesto {

	Integer MjestoId = null;
    String nazivMjesta = null;
    Integer vremenskiPomak = null;
    String vrijemePolaska = null;
    
    
    public Mjesto(){
    }

    public Mjesto(Integer lid, String nl, Integer vp, String pol){
        this.MjestoId = lid;
        this.nazivMjesta = nl;
        this.vremenskiPomak = vp;
        this.vrijemePolaska = pol;
    }

    public Integer getMjestoId() {
        return MjestoId;
    }
    public void setMjestoId(Integer id) {
        this.MjestoId = id;
    }

    public String getNazivMjesta() {
        return nazivMjesta;
    }
    public void setNazivMjesta(String nazivMjesta) {
        this.nazivMjesta = nazivMjesta;
    }
    public Integer getvremnskiPomak() {
        return vremenskiPomak;
    }
    public void setvremnskiPomak(Integer vremenskiPomak) {
        this.vremenskiPomak = vremenskiPomak;
    }
    
    public String getVrijemePolaska() {
        return vrijemePolaska;
    }
    public void setVrijemePolaska(String vrijemePolaska) {
        this.vrijemePolaska = vrijemePolaska;
    }
    
}
