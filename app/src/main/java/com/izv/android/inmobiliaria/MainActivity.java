package com.izv.android.inmobiliaria;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;


public class MainActivity extends Activity {

    private ArrayList<Casa> inmuebles;
    private ArrayList<Casa> datos = new ArrayList<Casa>();
    private Adaptador ad;
    private final int ANADIR = 0;
    private String tipoNuevo;
    private final int ACTIVIDAD_FOTOS = 2;
    private int HACER_FOTO=1;
    private ArrayList<Bitmap> arrayFotos;
    private int posicion=0;
    private int idFoto;
    private Button btAnterior,btSiguiente,btBorrar,btAnadir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);
        initComponents();

        final ListView ls = (ListView) findViewById(R.id.lv_lista);
        ad = new Adaptador(this, R.layout.detalle, datos);
        ls.setAdapter(ad);
        registerForContextMenu(ls);
        btSiguiente = (Button)findViewById(R.id.btSiguiente);
        btAnterior = (Button)findViewById(R.id.btAnterior);
        btBorrar = (Button)findViewById(R.id.btBorrar);
        btAnadir = (Button)findViewById(R.id.btAnadir);

        final Fragmento2 f2 = (Fragmento2)getFragmentManager().findFragmentById(R.id.fragment4);
        //para saber si estoy en portrait o landscape
        final boolean horizontal = f2!=null && f2.isInLayout();

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Casa c = (Casa)ls.getItemAtPosition(position);
                view.setSelected(true);
                if(horizontal){
                    btSiguiente.setEnabled(true);
                    btAnterior.setEnabled(true);
                    btBorrar.setEnabled(true);
                    btAnadir.setEnabled(true);
                    idFoto=c.getId();
                    File carpetaFotos  = new File(String.valueOf(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
                    arrayFotos=f2.insertarFotos(arrayFotos,position,datos,carpetaFotos);
                    f2.primeraFoto(arrayFotos,0);
                }else{
                    Intent i = new Intent(MainActivity.this,Fragmento2.class);
                    i.putExtra("id",c.getId());
                    startActivityForResult(i, ACTIVIDAD_FOTOS);
                }
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_anadir) {
            anadir();
        }else if (id == R.id.action_ordenarLocalidad) {
            ordenarLocalidad();
        }else if (id == R.id.action_ordenarDireccion) {
            ordenarDireccion();
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        if (id == R.id.action_borrar) {
            borrar(index);
        } else if (id == R.id.action_editar) {
            editar(index);
        }
        return super.onContextItemSelected(item);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextual, menu);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ANADIR) {
            int id;
            Double precio;
            String localidad, direccion, tipo;
            Bundle dsc = data.getExtras();

            id = dsc.getInt("id");
            precio = dsc.getDouble("precio");
            localidad = dsc.getString("localidad");
            direccion = dsc.getString("direccion");
            tipo = dsc.getString("tipo");

            datos.add(new Casa(id, precio,localidad,direccion,tipo));

            crearXML();
            visualizarInmuebles();
            ad.notifyDataSetChanged();
            tostada("Casa añadida correctamente");
        }else if (resultCode == RESULT_OK && requestCode == HACER_FOTO) {

            Bitmap foto = (Bitmap)data.getExtras().get("data");
            FileOutputStream salida;
            String nombrefoto;
            try {
                String[] fecha=getFecha().split("-");
                nombrefoto="inmueble_"+idFoto+"_"+fecha[0]+"_"+fecha[1]+"_"+fecha[2]+"_"+fecha[3]+"_"+fecha[4]+"_"+fecha[5];
                salida = new FileOutputStream(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/"+nombrefoto+".jpg");
                Log.v("salida", salida + "");
                foto.compress(Bitmap.CompressFormat.JPEG, 90, salida);
            } catch (FileNotFoundException e) {
            }



        }
    }


    /****************************************************/
    /*                                                  */
    /*               auxiliares                         */
    /*                                                  */
    /****************************************************/

    private void initComponents() {
        datos = new ArrayList<Casa>();
        leerXML();
    }

    private void tostada(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    public void visualizarInmuebles() {
        ad = new Adaptador(this, R.layout.detalle, datos);
        final ListView ls = (ListView) findViewById(R.id.lv_lista);
        ls.setAdapter(ad);
        registerForContextMenu(ls);
    }


    /****************************************************/
    /*                                                  */
    /*               metodos click                      */
    /*                                                  */
    /****************************************************/

    private void anadir() {
        Intent i = new Intent(this, Anadir.class);
        Bundle b = new Bundle();
        b.putParcelableArrayList("arraylist", datos);
        i.putExtras(b);
        startActivityForResult(i, ANADIR);
    }

    private boolean borrar(final int pos) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Eliminar");
        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                borrarXML(pos);
                tostada("¿Quieres borrarlo?");
                visualizarInmuebles();
            }
        });
        alert.setNegativeButton(android.R.string.no, null);
        alert.show();
        return true;
    }

    private boolean editar(final int index) {
        final int id;
        final Double precio;
        final String localidad, direccion, tipo;

        id = datos.get(index).getId();
        precio = datos.get(index).getPrecio();
        localidad = datos.get(index).getLocalidad();
        direccion = datos.get(index).getDireccion();
        tipo = datos.get(index).getTipo();

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Editar");

        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.editar, null);
        alert.setView(vista);

        final EditText etEditarPrecio, etEditarLocalidad, etEditarDireccion;
        final Spinner etEditarTipo;
        etEditarPrecio = (EditText) vista.findViewById(R.id.etEditarPrecio);
        etEditarLocalidad = (EditText) vista.findViewById(R.id.etEditarLocalidad);
        etEditarDireccion = (EditText) vista.findViewById(R.id.etEditarDireccion);

        etEditarTipo = (Spinner)vista.findViewById(R.id.spinnerEditarTipo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.tipoInmueble, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etEditarTipo.setAdapter(adapter);


        etEditarPrecio.setText(precio+"");
        etEditarLocalidad.setText(localidad);
        etEditarDireccion.setText(direccion);
        if (tipo.equals("Piso")) {
            etEditarTipo.setSelection(0);
        } else if (tipo.equals("Casa")) {
            etEditarTipo.setSelection(1);
        }
        etEditarTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                tipoNuevo= parent.getItemAtPosition(pos).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (etEditarPrecio.getText().toString().equals("") == true || etEditarLocalidad.getText().toString().equals("") == true || etEditarDireccion.getText().toString().equals("") == true) {
                    tostada("Campos sin rellenar");
                }else {
                    Casa casaAntigua = new Casa(id,precio,localidad,direccion,tipo);
                    Casa casaNueva = new Casa(id,Double.parseDouble(etEditarPrecio.getText().toString()), etEditarLocalidad.getText().toString(), etEditarDireccion.getText().toString(),tipoNuevo);
                    editarXML(casaNueva,casaAntigua);
                    ad.notifyDataSetChanged();
                    tostada("Se han editado correctamente");
                    visualizarInmuebles();
                }

            }
        });
        alert.setNegativeButton(android.R.string.no, null);
        alert.show();
        return true;
    }
    private boolean settings() {
        return true;
    }



    public void leerXML(){
        Opxml xml = new Opxml();
        datos = xml.leer(getApplicationContext());
        visualizarInmuebles();
    }
    public void crearXML(){
        Opxml xml = new Opxml();
        xml.crear(getApplicationContext(), datos);
        datos = xml.leer(getApplicationContext());
    }
    public void borrarXML(int pos){

        int id;
        Double precio;
        String localidad, direccion, tipo;

        id = datos.get(pos).getId();
        precio = datos.get(pos).getPrecio();
        localidad = datos.get(pos).getLocalidad();
        direccion = datos.get(pos).getDireccion();
        tipo = datos.get(pos).getTipo();

        eliminarFotoPorID(id);



        Casa c = new Casa(id, precio, localidad,direccion,tipo);

        Opxml xml = new Opxml();
        xml.eliminar(getApplicationContext(), datos, c);

    }
    public void editarXML(Casa casaNueva, Casa casaVieja){
        Opxml cxml = new Opxml();
        cxml.modificar(getApplicationContext(), datos, casaNueva, casaVieja);
    }
    public void siguiente(View v){
        final Fragmento2 f2 = (Fragmento2)getFragmentManager().findFragmentById(R.id.fragment4);
        posicion++;
        if(arrayFotos.size()==0){

        }else {
            if (posicion>arrayFotos.size()-1){
                posicion=arrayFotos.size()-1;
                f2.avanzarFoto(arrayFotos,posicion);
            }else{
                f2.avanzarFoto(arrayFotos,posicion);
            }}
    }
    public void anterior(View v){
        final Fragmento2 f2 = (Fragmento2)getFragmentManager().findFragmentById(R.id.fragment4);
        posicion--;
        if(arrayFotos.size()==0){

        }else {
            if (posicion<0){
                posicion=0;
                f2.avanzarFoto(arrayFotos,posicion);
            }else{
                f2.avanzarFoto(arrayFotos,posicion);
            }}
    }
    public void hacerFoto(View v){

        Intent i = new Intent ("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(i, HACER_FOTO);
        File carpetaFotos  = new File(String.valueOf(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
    }

    private String getFecha(){
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String formatteDate = df.format(date);
        return formatteDate;
    }

    public boolean eliminarFoto(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Borrar foto");
        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int cont=0;
                File carpetaFotos  = new File(String.valueOf(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
                String[] archivosCarpetaFotos = carpetaFotos.list();
                for (int i=0;i<archivosCarpetaFotos.length;i++){
                    if (archivosCarpetaFotos[i].indexOf("inmueble_"+idFoto) != -1){
                        if (cont==posicion) {
                            File archivoaBorrar = new File(String.valueOf(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)), archivosCarpetaFotos[i]);
                            archivoaBorrar.delete();
                        }
                        cont++;
                    }
                }
            }
        });
        alert.setNegativeButton(android.R.string.no, null);
        alert.show();
        return true;
    }

    public void eliminarFotoPorID(final int id){
        int cont=0;
        File carpetaFotos  = new File(String.valueOf(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
        String[] archivosCarpetaFotos = carpetaFotos.list();
        for (int i=0;i<archivosCarpetaFotos.length;i++){
            if (archivosCarpetaFotos[i].indexOf("inmueble_"+id) != -1){
                File archivoaBorrar = new File(String.valueOf(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)), archivosCarpetaFotos[i]);
                archivoaBorrar.delete();

            }
        }

    }

    /****************************************************/
    /*                                                  */
    /*               metodos ordenar                    */
    /*                                                  */
    /****************************************************/

    public void ordenarDireccion() {
        Collections.sort(datos, new Comparator<Casa>() {
            @Override
            public int compare(Casa c1, Casa c2) {
                return c1.getDireccion().compareToIgnoreCase(c2.getDireccion());
            }
        });
        ad.notifyDataSetChanged();
    }
    public void ordenarLocalidad() {
        Collections.sort(datos, new Comparator<Casa>() {
            @Override
            public int compare(Casa c1, Casa c2) {
                return c1.getLocalidad().compareToIgnoreCase(c2.getLocalidad());
            }
        });
        ad.notifyDataSetChanged();
    }

}
