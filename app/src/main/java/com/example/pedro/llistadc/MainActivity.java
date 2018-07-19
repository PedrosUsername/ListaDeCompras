package com.example.pedro.llistadc;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class MainActivity extends AppCompatActivity {

    String RELACAO = "LListaDC";
    DatabaseHelper databaseHelper;
    ArrayList<Produto> lista_de_produtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        lista_de_produtos = databaseHelper.getAllProdutosFromDBRelatedTo( RELACAO );

        final FloatingActionButton fab = findViewById(R.id.fab);
        ListView products = findViewById(R.id.products);

        ArrayAdapter<Produto> adapter = new ArrayAdapter<Produto>(this, R.layout.list_view_white_text, R.id.list_content, lista_de_produtos);
        products.setAdapter(adapter);

        products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(lista_de_produtos.get(i).type == 1) {
                    Intent intent = new Intent(MainActivity.this, ShowPastaContents.class);
                    intent.putExtra("serial_obj", lista_de_produtos.get(i));
                    startActivity(intent);
                }

            }
        });

        products.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ActionMode mActionMode;
                MyActionModeCallback callback = new MyActionModeCallback(MainActivity.this, lista_de_produtos, i, RELACAO);
                mActionMode = startActionMode(callback);
                mActionMode.setTitle(R.string.menu_context_title);

                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
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
                            databaseHelper.addDataToDB(mProduto.getText().toString(), 0, "LListaDC");

                            dialog.dismiss();
                        }
                    });


                    mNovaPasta.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            Pasta novaPasta = new Pasta(mProduto.getText().toString());
                            lista_de_produtos.add(novaPasta);
                            databaseHelper.addDataToDB(mProduto.getText().toString(), 1, "LListaDC");

                            dialog.dismiss();
                        }
                    });




                    dialog.show();
            }
        });
    }


}
