package com.example.pedro.llistadc;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedro on 20/04/18.
 */

public class Produto implements Serializable{
        private int id = 0;
        int type = 0;
        private int nivel = 0;
        private String path = "0";
        String title = "null";
        List<Produto> another_list;

        @Override
        public String toString(){
                return this.title;
        }

        public int getId() { return this.id; }

        public void setId(int id) { this.id = id; }

        public int getNivel(){ return this.nivel; }

        public void setNivel(int nivel) { this.nivel = nivel; }

        public String getPath(){ return this.path; }

        public void setPath(String path) { this.path = path; }

        public void addToPath(String s) { this.path = this.path.concat(s); }

        public void setTitle(String s){
                this.title = s;
        }

        public String getTitle(){ return this.title; }

        public ArrayList<Produto> getLista(){ return (ArrayList<Produto>) another_list; }

        public void setLista(List<Produto> l) { this.another_list = l; }
}
