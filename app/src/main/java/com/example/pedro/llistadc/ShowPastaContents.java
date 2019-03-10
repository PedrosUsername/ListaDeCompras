package com.example.pedro.llistadc;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowPastaContents extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    ArrayList<Produto> lista_de_produtos;
    int contadorID;
    String path;
    Produto p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pasta_contents);


        databaseHelper = new DatabaseHelper(getApplicationContext());
        Intent i = getIntent();
        Bundle b = i.getExtras();
        p = (Produto) b.getSerializable("serial_obj");
        path = p.getPath();
        lista_de_produtos = databaseHelper.getAllProdutosFromDBRelatedTo( path );

        if(lista_de_produtos.isEmpty())
            contadorID = 0;
        else
            contadorID = lista_de_produtos.get( lista_de_produtos.size()-1 ).getId() + 1;


        TextView pastaAtual = findViewById(R.id.textView);
        pastaAtual.setText( p.getTitle() );

        FloatingActionButton fab = findViewById(R.id.fab_pasta);
        final ListView products = findViewById(R.id.products_pasta);

        final ArrayAdapter<Produto> adapter = new ArrayAdapter<Produto>(this, R.layout.list_view_white_text, R.id.list_content, lista_de_produtos){
            public View getView ( int position, View convertView, ViewGroup parent){

                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(R.id.list_content);

                if ((lista_de_produtos.get(position).type == 1) && (!products.isItemChecked(position))) {
                    tv.setTextColor(getResources().getColor(R.color.piss));
                    view.setBackgroundColor(getResources().getColor(R.color.customDarkerGreen));
                }
                else if ((lista_de_produtos.get(position).type == 0) && (!products.isItemChecked(position))) {
                    tv.setTextColor(getResources().getColor(R.color.customWhite));
                    view.setBackgroundColor(getResources().getColor(R.color.customDarkGreen));
                }
                else if ((lista_de_produtos.get(position).type == 1) && (products.isItemChecked(position))) {
                    tv.setTextColor(getResources().getColor(R.color.piss));
                    view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
                else if ((lista_de_produtos.get(position).type == 0) && (products.isItemChecked(position))) {
                    tv.setTextColor(getResources().getColor(R.color.customWhite));
                    view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }

                return view;
            }
        };
        products.setAdapter(adapter);

        //suporte a multipla escolha de produtos
        products.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        products.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            ArrayList<Produto> lista_de_produtos_CAB;
            //o valor do i muda a cada onItemCheckedStateChanged, então aqui guardamos o valor do primeiro i

            MenuItem menuItem, menuItem2;
            int first_i;


            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

                //incrementa a lista do CAB
                if(b){
                    lista_de_produtos_CAB.add(lista_de_produtos.get(i));
                }else{
                    lista_de_produtos_CAB.remove(lista_de_produtos.get(i));
                }
                // -------------------------

                if (lista_de_produtos_CAB.size() == 1){
                    first_i = lista_de_produtos.indexOf(lista_de_produtos_CAB.get(0));

                    this.menuItem.setEnabled(true);
                    this.menuItem.setVisible(true);

                    this.menuItem2.setEnabled(false);
                    this.menuItem2.setVisible(false);
                }

                if(lista_de_produtos_CAB.size() > 1){
                    this.menuItem.setEnabled(false);
                    this.menuItem.setVisible(false);

                    this.menuItem2.setEnabled(true);
                    this.menuItem2.setVisible(true);
                }

                adapter.notifyDataSetChanged();
            }



            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                // Inflate the menu for the CAB
                lista_de_produtos_CAB = new ArrayList<>();

                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                this.menuItem = menu.findItem(R.id.item_edit);
                this.menuItem2 = menu.findItem(R.id.item_compac);

                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.item_delete:

                        while(lista_de_produtos_CAB.size() > 0) {
                            Log.d("deletando produto: ", lista_de_produtos_CAB.get(0).getTitle());
                            databaseHelper.deleteDataFromDBWithPath(lista_de_produtos_CAB.get(0).getPath());
                            lista_de_produtos.remove(lista_de_produtos_CAB.get(0));
                            lista_de_produtos_CAB.remove(0);
                        }

                        adapter.notifyDataSetChanged();
                        actionMode.finish();
                        break;


                    case R.id.item_edit:

                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ShowPastaContents.this);
                        View mView = inflater.inflate(R.layout.dialog_input_edit, null);
                        final EditText mProduto = mView.findViewById(R.id.produto_input);
                        final String oldValue = lista_de_produtos_CAB.get(0).toString();
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
                                String novoTitulo = mProduto.getText().toString().replace("'", "´");

                                if( !mProduto.getText().toString().isEmpty() && ( !lista_de_produtos_tem(novoTitulo) )) {
                                    lista_de_produtos.get(first_i).setTitle(novoTitulo);
                                    databaseHelper.editDataFromDB(novoTitulo, lista_de_produtos_CAB.get(0).getPath());
                                }else{
                                    int duration = Toast.LENGTH_SHORT;

                                    if ( !lista_de_produtos_tem(novoTitulo) ) {
                                        Toast toast = Toast.makeText(getApplicationContext(), "O produto deve ter identificação", duration);
                                        toast.show();
                                    }else {
                                        Toast toast = Toast.makeText(getApplicationContext(), "O produto "+novoTitulo+" já foi adicionado", duration);
                                        toast.show();
                                    }
                                }

                                adapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });




                        dialog.show();


                        actionMode.finish();
                    break;

                    case R.id.item_compac:

                        Pasta novaPasta = new Pasta("- Nova Pasta -");

                        novaPasta.setId( contadorID );
                        contadorID++;
                        novaPasta.setPath(path+","+novaPasta.getId());
                        lista_de_produtos.add(novaPasta);
                        novaPasta.setTitle( novaPasta.getTitle().replace("'", "´") );
                        databaseHelper.addDataToDB(novaPasta.getTitle(), 1, novaPasta.getId(), novaPasta.getPath());

                        while(lista_de_produtos_CAB.size() > 0) {
                            Log.d("????????????????", "antes -> "+lista_de_produtos_CAB.get(0).getPath());
                            databaseHelper.editPathFromDB(novaPasta.getPath() + ","+ lista_de_produtos_CAB.get(0).getId() , lista_de_produtos_CAB.get(0).getPath());
                            Log.d("????????????????", "depoix -> "+lista_de_produtos_CAB.get(0).getPath());
                            lista_de_produtos.remove(lista_de_produtos_CAB.get(0));
                            lista_de_produtos_CAB.remove(0);

                        }

                        adapter.notifyDataSetChanged();
                        actionMode.finish();
                    break;


                    default:
                        Toast toast = Toast.makeText(getApplicationContext(), "Erro estranho...", Toast.LENGTH_SHORT);
                        toast.show();

                        finish();
                }

                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });
        // **********************************************************************

        products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(lista_de_produtos.get(i).type == 1) {
                    Intent intent = new Intent(ShowPastaContents.this, ShowPastaContents.class);
                    Log.d("path atual", path);
                    Log.d("entrando path", lista_de_produtos.get(i).getPath());
                    intent.putExtra("serial_obj", lista_de_produtos.get(i));
                    startActivity(intent);
                }

            }
        });



/*        products.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String itemPath = path + "," + lista_de_produtos.get(i).getId();

                ActionMode mActionMode;
                MyActionModeCallback callback = new MyActionModeCallback(ShowPastaContents.this, lista_de_produtos, i, itemPath, adapter);
                mActionMode = startActionMode(callback);
                mActionMode.setTitle(R.string.menu_context_title);

                return true;
            }
        });
*/


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

                        if(( !mProduto.getText().toString().isEmpty() ) && ( !lista_de_produtos_tem(novoItem) )) {
                        novoItem.setId( contadorID );
                        contadorID++;
                        novoItem.setPath( path+","+novoItem.getId() );
                        lista_de_produtos.add(novoItem);
                        novoItem.setTitle( novoItem.getTitle().replace("'", "´") );
                        databaseHelper.addDataToDB(novoItem.getTitle(), 0, novoItem.getId(), novoItem.getPath());
                        }else {
                            int duration = Toast.LENGTH_SHORT;

                            if ( !lista_de_produtos_tem(novoItem) ) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Produto sem nome", duration);
                                toast.show();
                            }else {
                                Toast toast = Toast.makeText(getApplicationContext(), "O produto "+novoItem.getTitle()+" já foi adicionado", duration);
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
                                Toast toast = Toast.makeText(getApplicationContext(), "O produto "+novaPasta.getTitle()+" já foi adicionado", duration);
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
    public boolean lista_de_produtos_tem( String s ){
        int i;

        for(i=0; i<lista_de_produtos.size(); i++){
            if(lista_de_produtos.get(i).getTitle().equals( s ) ) {
                return true;
            }
        }

        return false;
    }
}
