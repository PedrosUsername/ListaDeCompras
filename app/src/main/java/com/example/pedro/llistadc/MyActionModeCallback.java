package com.example.pedro.llistadc;

import android.app.Activity;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;


/**
 * Created by pedro on 21/04/18.
 */

public class MyActionModeCallback implements ActionMode.Callback {
    private Context context;
    private DatabaseHelper databaseHelper;
    private List<Produto> lista_de_produtos;
    private int i;

    public MyActionModeCallback(Context context, List lista_de_produtos, int i) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
        this.lista_de_produtos = lista_de_produtos;
        this.i = i;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        LayoutInflater inflater = LayoutInflater.from(context);

        switch (item.getItemId()){

            case R.id.item_delete:
                databaseHelper.deleteDataFromDB(lista_de_produtos.get(i).getTitle());
                lista_de_produtos.remove(i);

                mode.finish();
            break;


            case R.id.item_edit:

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                View mView = inflater.inflate(R.layout.dialog_input_edit, null);
                final EditText mProduto = mView.findViewById(R.id.produto_input);
                final String oldValue = lista_de_produtos.get(i).toString();
                mProduto.setText(oldValue);

                final Button mCancelar = mView.findViewById(R.id.cancelar);
                final Button mConcluir = mView.findViewById(R.id.concluir);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                mCancelar.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });


                mConcluir.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        lista_de_produtos.get(i).setTitle(mProduto.getText().toString());
                        databaseHelper.editDataFromDB(oldValue, mProduto.getText().toString());

                        dialog.dismiss();
                    }
                });




                dialog.show();


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
