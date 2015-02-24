package com.aktivainfo.prodajakarata;

public class Popust {


	Integer PopustId = null;
    Integer iznosPopusta = null;
    
    
    public Popust(){
    }

    public Popust(Integer lid, Integer ip){
        this.PopustId = lid;
        this.iznosPopusta = ip;
    }

    public Integer getPopustId() {
        return PopustId;
    }
    public void setPopustId(Integer id) {
        this.PopustId = id;
    }


    public Integer getIznosPopusta() {
        return iznosPopusta;
    }
    public void setIznosPopusta(Integer iznosPopusta) {
        this.iznosPopusta = iznosPopusta;
    }
    
}