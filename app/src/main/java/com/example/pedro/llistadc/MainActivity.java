package com.example.pedro.llistadc;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class MainActivity extends AppCompatActivity {

    String RELACAO = "LListaDC";
    int contadorID;
    int nivel = 0;
    String path = "0";
    DatabaseHelper databaseHelper;
    ArrayList<Produto> lista_de_produtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        lista_de_produtos = databaseHelper.getAllProdutosFromDBRelatedTo(path);

        if (lista_de_produtos.isEmpty())
            contadorID = 0;
        else
            contadorID = lista_de_produtos.get(lista_de_produtos.size() - 1).getId() + 1;

        final FloatingActionButton fab = findViewById(R.id.fab);
        ListView products = findViewById(R.id.products);

        final ArrayAdapter<Produto> adapter = new ArrayAdapter<Produto>(this, R.layout.list_view_white_text, R.id.list_content, lista_de_produtos){
            public View getView ( int position, View convertView, ViewGroup parent){

                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(R.id.list_content);

                if (lista_de_produtos.get(position).type == 1) {
                    tv.setTextColor(getResources().getColor(R.color.piss));
                    view.setBackgroundColor(getResources().getColor(R.color.customDarkerGreen));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.customWhite));
                    view.setBackgroundColor(getResources().getColor(R.color.customDarkGreen));
                }

                return view;
            }
        };
        products.setAdapter(adapter);

        products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(lista_de_produtos.get(i).type == 1) {
                    Intent intent = new Intent(MainActivity.this, ShowPastaContents.class);
                    Log.d("path atual", path);
                    Log.d("entrando path", lista_de_produtos.get(i).getPath());
                    intent.putExtra("serial_obj", lista_de_produtos.get(i));
                    startActivity(intent);
                }

            }
        });

        products.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String itemPath = path + "," + lista_de_produtos.get(i).getId();

                ActionMode mActionMode;
                MyActionModeCallback callback = new MyActionModeCallback(MainActivity.this, lista_de_produtos, i, itemPath, adapter);
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
                            novoItem.setNivel(nivel);

                            if(( !mProduto.getText().toString().isEmpty() ) && ( !lista_de_produtos_tem(novoItem) )) {
                                novoItem.setId( contadorID );
                                contadorID++;
                                novoItem.setPath(path+","+novoItem.getId());
                                lista_de_produtos.add(novoItem);
                                novoItem.setTitle( novoItem.getTitle().replace("'", "´") );
                                databaseHelper.addDataToDB(novoItem.getTitle(), 0, novoItem.getId(), novoItem.getPath());
                            }else {
                                int duration = Toast.LENGTH_SHORT;

                                if ( !lista_de_produtos_tem(novoItem) ) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Produto sem nome", duration);
                                    toast.show();
                                }else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Esse produto ja foi adicionado", duration);
                                    toast.show();
                                }
                            }

                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });


                    mNovaPasta.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            Pasta novaPasta = new Pasta(mProduto.getText().toString());
                            novaPasta.setNivel(nivel);

                            if( !mProduto.getText().toString().isEmpty() && ( !lista_de_produtos_tem(novaPasta) )) {
                                novaPasta.setId( contadorID );
                                contadorID++;
                                novaPasta.setPath(path+","+novaPasta.getId());
                                lista_de_produtos.add(novaPasta);
                                novaPasta.setTitle( novaPasta.getTitle().replace("'", "´") );
                                databaseHelper.addDataToDB(novaPasta.getTitle(), 1, novaPasta.getId(), novaPasta.getPath());
                            }else{
                                int duration = Toast.LENGTH_SHORT;

                                if ( !lista_de_produtos_tem(novaPasta) ) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Produto sem nome", duration);
                                    toast.show();
                                }else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Esse produto ja foi adicionado", duration);
                                    toast.show();
                                }
                            }

                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });




                    dialog.show();
            }
        });
    }

    public boolean lista_de_produtos_tem( Produto p ){
        int i;

        for(i=0; i<lista_de_produtos.size(); i++){
            if(lista_de_produtos.get(i).getTitle().equals( p.getTitle() ) ) {
                return true;
            }
        }

        return false;
    }
}
