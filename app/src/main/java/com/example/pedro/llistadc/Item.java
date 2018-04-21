package com.example.pedro.llistadc;

/**
 * Created by pedro on 20/04/18.
 */

public class Item extends Produto{

    public Item(String s){
        this.title = s;
    }

    public void setTitle(String s){
        this.title = s;
    }

    public void addItem(Item i){
        this.another_list.add(i);
    }

}
