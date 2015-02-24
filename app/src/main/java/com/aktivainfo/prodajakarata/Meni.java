package com.aktivainfo.prodajakarata;

public class Meni {

	Integer menuId = null;
    String nazivMenija = null;
    String kolicina = null;
    String cijena = null;
    String akontacija = null;
    Integer linijaId = null;
    Integer redoslijed = null;
    Integer vrstaKarteId = null;
    
    
    public Meni(){
    }

    public Meni(Integer mid, String nm, String cijena, String akontacija, Integer linijaId, Integer redoslijed, Integer vkId, String kol){
        this.menuId = mid;
        this.nazivMenija = nm;
        this.cijena = cijena;
        this.akontacija = akontacija;
        this.linijaId = linijaId;
        this.redoslijed = redoslijed;
        this.vrstaKarteId = vkId; 
        this.kolicina = kol;
    }

    public Integer getId() {
        return menuId;
    }
    public void setId(Integer id) {
        this.menuId = id;
    }

    public String getNazivMenija() {
        return nazivMenija;
    }
    public void setNazivMenija(String nazivMenija) {
        this.nazivMenija = nazivMenija;
    }
    
    public String getKolicina() {
        return kolicina;
    }
    public void setKolicina(String kolicina) {
        this.kolicina = kolicina;
    }
    
    public String getAkontacija() {
        return akontacija;
    }
    public void setAkontacija(String akontacija) {
        this.akontacija = akontacija;
    }
    
    public String getCijena() {
        return cijena;
    }
    public void setCijena(String cijena) {
        this.cijena = cijena;
    }
    
    public Integer getLinijaId() {
        return linijaId;
    }
    public void setLinijaId(Integer id) {
        this.linijaId = id;
    }
    
    public Integer getRedoslijed() {
        return redoslijed;
    }
    public void setRedoslijed(Integer redoslijed) {
        this.redoslijed = redoslijed;
    }

    public Integer getVrstaKarteId() {
        return vrstaKarteId;
    }
    public void setVrstaKarteId(Integer vrstaKarteId) {
        this.vrstaKarteId = vrstaKarteId;
    }
    
}

