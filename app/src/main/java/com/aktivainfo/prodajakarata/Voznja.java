package com.aktivainfo.prodajakarata;

/**
 * Created by vjekoslav.mezdic on 20.09.13..
 */
public class Voznja {
    Integer izletId = null;
    Integer voznjaId = null;
    String imeVoznje = null;
    String datumVoznje = null;
    String vrijemeVoznje = null;
    String slobodnaMjesta = null;
    Integer prodajaDozvoljena = null;
    String prikazTeksta = null;
    Integer tipObracunaCijene = null;
    Integer maximumKarata = null;
    String iznosAkontacije = null;

    public Voznja(){
    }

    public Voznja(Integer id, Integer vd, String iv, String dv, String vv, String sm, Integer pd, String pt, Integer toc, Integer mk, String ia){
        this.izletId = id;
        this.voznjaId = vd;
        this.imeVoznje = iv;
        this.datumVoznje = dv;
        this.vrijemeVoznje = vv;
        this.slobodnaMjesta = sm;
        this.prodajaDozvoljena = pd;
        this.prikazTeksta = pt;
        this.tipObracunaCijene = toc;
        this.maximumKarata = mk;
        this.iznosAkontacije = ia;
    }

    public Integer getIzletId() {
        return izletId;
    }
    public void setIzletId(Integer id) {
        this.izletId = id;
    }
    
    public Integer getVoznjaId() {
        return voznjaId;
    }
    public void setVoznjaId(Integer id) {
        this.voznjaId = id;
    }

    public String getImeVoznje() {
        return imeVoznje;
    }
    public void setImeVoznje(String imeVoznje) {
        this.imeVoznje = imeVoznje;
    }
    
    public String getDatumVoznje() {
        return datumVoznje;
    }
    public void setDatumVoznje(String datumVoznje) {
        this.datumVoznje = datumVoznje;
    }

    public String getVrijemeVoznje() {
        return vrijemeVoznje;
    }
    public void setVrijemeVoznje(String imeVoznje) {
        this.imeVoznje = imeVoznje;
    }

    public String getSlobodnaMjesta() {
        return slobodnaMjesta;
    }
    public void setSlobodnaMjesta(String slobodnaMjesta) {
        this.slobodnaMjesta = slobodnaMjesta;
    }
    
    public Integer getProdajaDozvoljena() {
        return prodajaDozvoljena;
    }
    public void setProdajaDozvoljena(Integer prodajaDozvoljena) {
        this.prodajaDozvoljena = prodajaDozvoljena;
    }

    public String getPrikazTeksta() {
        return prikazTeksta;
    }
    public void setPrikazTeksta(String prikazTeksta) {
        this.prikazTeksta = prikazTeksta;
    }
    
    public Integer getTipObracunaCijene() {
        return tipObracunaCijene;
    }
    public void setTipObracunaCijene(Integer tipObracunaCijene) {
        this.tipObracunaCijene = tipObracunaCijene;
    }
    
    public Integer getMaximumKarata() {
        return maximumKarata;
    }
    public void setMaximumKarata(Integer maximumKarata) {
        this.maximumKarata = maximumKarata;
    }
    
    public String getIznosAkontacije() {
        return iznosAkontacije;
    }
    public void setIznosAkontacije(String iznosAkontacije) {
        this.iznosAkontacije = iznosAkontacije;
    }
    
    
    
}
