package com.example.pedro.llistadc;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ShowPastaContents extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    ArrayList<Produto> lista_de_produtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pasta_contents);


        databaseHelper = new DatabaseHelper(getApplicationContext());
        Intent i = getIntent();
        Bundle b = i.getExtras();
        Produto p = (Produto) b.getSerializable("serial_obj");
        lista_de_produtos = databaseHelper.getAllProdutosFromDBRelatedTo( p.getTitle() );
        final String RELACAO = p.getTitle();


        FloatingActionButton fab = findViewById(R.id.fab_pasta);
        ListView products = findViewById(R.id.products_pasta);

        ArrayAdapter<Produto> adapter = new ArrayAdapter<Produto>(this, R.layout.list_view_white_text, R.id.list_content, lista_de_produtos);
        products.setAdapter(adapter);


        products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(lista_de_produtos.get(i).type == 1) {
                    Intent intent = new Intent(ShowPastaContents.this, ShowPastaContents.class);
                    intent.putExtra("serial_obj", lista_de_produtos.get(i));
                    startActivity(intent);
                }

            }
        });



        products.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ActionMode mActionMode;
                MyActionModeCallback callback = new MyActionModeCallback(ShowPastaContents.this, lista_de_produtos, i, RELACAO);
                mActionMode = startActionMode(callback);
                mActionMode.setTitle(R.string.menu_context_title);

                return true;
            }
        });



        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ShowPastaContents.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_input, null);
                final EditText mProduto = mView.findViewById(R.id.produto_input);

                final Button mNovoItem = mView.findViewById(R.id.novo_item);
                final Button mNovaPasta = mView.findViewById(R.id.nova_pasta);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                mNovoItem.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Item novoItem = new Item(mProduto.getText().toString());
                        lista_de_produtos.add(novoItem);
                        databaseHelper.addDataToDB(mProduto.getText().toString(), 0, RELACAO);

                        dialog.dismiss();
                    }
                });


                mNovaPasta.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Pasta novaPasta = new Pasta(mProduto.getText().toString());
                        lista_de_produtos.add(novaPasta);
                        databaseHelper.addDataToDB(mProduto.getText().toString(), 1, RELACAO);
                        dialog.dismiss();
                    }
                });




                dialog.show();
            }
        });
    }

}
