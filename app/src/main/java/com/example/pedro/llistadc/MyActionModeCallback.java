package com.example.pedro.llistadc;

import android.app.AlertDialog;
import android.content.Context;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;


/**
 * Created by pedro on 21/04/18.
 */

public class MyActionModeCallback implements ActionMode.Callback {
    private Context context;
    private List lista_de_produtos;
    private int i;

    public MyActionModeCallback(Context context, List lista_de_produtos, int i) {
        this.context = context;
        this.lista_de_produtos = lista_de_produtos;
        this.i = i;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        switch (item.getItemId()){

            case R.id.item_delete:
                lista_de_produtos.remove(i);
                mode.finish();
            break;


            case R.id.item_edit:
                
                mode.finish();
            break;

            default:
                Toast toast = Toast.makeText(context, "um erro estranho aconteceu", Toast.LENGTH_SHORT);
                toast.show();

                mode.finish();
        }
        return false;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // TODO Auto-generated method stub
        mode.getMenuInflater().inflate(R.menu.context_menu, menu);
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        // TODO Auto-generated method stub

        mode.setTitle("CheckBox is Checked");
        return false;
    }
}
