package com.example.pedro.llistadc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedro on 20/04/18.
 */

public class Produto implements Serializable{
        int type = 0;
        String title = "null";
        List<Produto> another_list;

        @Override
        public String toString(){
                return this.title;
        }

        public void setTitle(String s){
                this.title = s;
        }

        public String getTitle(){ return this.title; }

        public ArrayList<Produto> getLista(){ return (ArrayList<Produto>) another_list; }
}
