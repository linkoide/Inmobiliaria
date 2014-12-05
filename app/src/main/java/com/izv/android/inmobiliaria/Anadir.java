package com.izv.android.inmobiliaria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class Anadir extends Activity {

    private int id;
    private Double precio;
    private String localidad, direccion, tipo;
    EditText etAnadirPrecio, etAnadirLocalidad, etAnadirDireccion;
    Spinner etAnadirTipo;
    private ArrayList<Casa> datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir);
        Bundle b = getIntent().getExtras();
        datos = b.getParcelableArrayList("arraylist");

        etAnadirPrecio = (EditText)findViewById(R.id.etAnadirPrecio);
        etAnadirLocalidad = (EditText)findViewById(R.id.etAnadirLocalidad);
        etAnadirDireccion = (EditText)findViewById(R.id.etAnadirDireccion);
        etAnadirTipo = (Spinner)findViewById(R.id.spinnerAnadirTipo);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.tipoInmueble, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etAnadirTipo.setAdapter(adapter);



        etAnadirTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                tipo= parent.getItemAtPosition(pos).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }



    public void AnadirBoton(View v) {
        localidad = etAnadirLocalidad.getText().toString();
        direccion = etAnadirDireccion.getText().toString();
        precio = Double.valueOf(etAnadirPrecio.getText().toString()).doubleValue();
        id = datos.size()+1;



        if (etAnadirPrecio.getText().toString().equals("") == true || etAnadirLocalidad.getText().toString().equals("") == true || etAnadirDireccion.getText().toString().equals("") == true) {
            tostada("Campos sin rellenar");
        } else {
            Intent result = new Intent();
            result.putExtra("localidad", localidad);
            result.putExtra("direccion", direccion);
            result.putExtra("precio", precio);
            result.putExtra("id", id);
            result.putExtra("tipo", tipo);
            setResult(Activity.RESULT_OK, result);
            finish();
            this.finish();
        }


        Log.v("tipo",""+tipo);

    }



    private void tostada(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
