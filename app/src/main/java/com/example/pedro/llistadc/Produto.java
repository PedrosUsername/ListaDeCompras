package com.example.pedro.llistadc;

import java.util.List;

/**
 * Created by pedro on 20/04/18.
 */

public class Produto{
        String title = "null";
        List<Produto> another_list;

        @Override
        public String toString(){
                return this.title;
        }
}
