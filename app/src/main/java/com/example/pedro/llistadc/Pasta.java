package com.example.pedro.llistadc;

/**
 * Created by pedro on 20/04/18.
 */

class Pasta extends Produto{

    public Pasta(String s){
        this.title = s;
    }

    public void setTitle(String s){
        this.title = s;
    }

    public String getTitle(){ return this.title; }

    public void addItem(Item i){
        this.another_list.add(i);
    }

}
