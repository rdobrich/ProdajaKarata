package com.aktivainfo.prodajakarata;

public class Karta {

	Integer linijaId = null;
	Integer tipKarteId = null;
    String tipKarte = null;
    String cijenaKarte = null;
    String brojKarata = null;
    String akontacija = null;

    public Karta(){
    }

    public Karta(Integer id, Integer tkid, String tk, String ck, String ak, String bk){
        this.linijaId = id;
        this.tipKarteId = tkid;
        this.tipKarte = tk;
        this.cijenaKarte = ck;
        this.brojKarata = bk;
        this.akontacija = ak;
    }

    public Integer getLinijaId() {
        return linijaId;
    }
    public void setLinijaId(Integer id) {
        this.linijaId = id;
    }
    
    public Integer getTipKarteId() {
        return tipKarteId;
    }
    public void setTipKarteId(Integer tkid) {
        this.tipKarteId = tkid;
    }

    public String getTipKarte() {
        return tipKarte;
    }
    public void setTipKarte(String tipKarte) {
        this.tipKarte = tipKarte;
    }
    
    public String getCijenaKarte() {
        return cijenaKarte;
    }
    public void setCijenaKarte(String cijenaKarte) {
        this.cijenaKarte = cijenaKarte;
    }

    public String getBrojKarata() {
        return brojKarata;
    }
    public void setBrojKarata(String brojKarata) {
        this.brojKarata = brojKarata;
    }

    public String getAkontacija() {
        return akontacija;
    }
    public void setAkontacija(String akontacija) {
        this.akontacija = akontacija;
    }

}

