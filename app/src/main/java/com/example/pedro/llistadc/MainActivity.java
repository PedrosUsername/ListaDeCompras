package com.example.pedro.llistadc;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_input, null);
                    final EditText mProduto = (EditText) mView.findViewById(R.id.produto_input);

                    final Button mNovoItem = (Button) mView.findViewById(R.id.novo_item);
                    final Button mNovaPasta = (Button) mView.findViewById(R.id.nova_pasta);



                    mNovoItem.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            if( !mProduto.getText().toString().isEmpty() ){
                                Toast.makeText(MainActivity.this,
                                        R.string.success,
                                        Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Toast.makeText(MainActivity.this,
                                        R.string.fuck,
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });


                    mNovaPasta.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            if( !mProduto.getText().toString().isEmpty() ){
                                Toast.makeText(MainActivity.this,
                                        R.string.success,
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(MainActivity.this,
                                        R.string.fuck,
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });



                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();
            }
        });
    }

}
