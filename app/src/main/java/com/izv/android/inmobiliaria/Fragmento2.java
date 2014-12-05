package com.izv.android.inmobiliaria;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.io.File;
import java.util.ArrayList;

public class Fragmento2 extends Fragment {

    private View v;

    private ArrayList<Bitmap> arrayFotos;

    public Fragmento2(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.layout_segundo_fragmento, container, false);
        return v;
    }

    public void primeraFoto(ArrayList<Bitmap> arrayFotos,int pos){

        ImageView iv = (ImageView)v.findViewById(R.id.ivFoto);
        if(arrayFotos.size()==0){
            Drawable myDrawable = getResources().getDrawable(R.drawable.foto);
            iv.setImageDrawable(myDrawable);
        }else{
            iv.setImageBitmap(arrayFotos.get(pos));
        }
    }
    public void avanzarFoto(ArrayList<Bitmap> arrayFotos,int pos){
        ImageView iv = (ImageView)v.findViewById(R.id.ivFoto);
        iv.setImageBitmap(arrayFotos.get(pos));
    }


    public ArrayList<Bitmap> insertarFotos(ArrayList<Bitmap> arrayFotos,int posicion,ArrayList<Casa> datos,File carpetaFotos){

        String[] archivosCarpetaFotos = carpetaFotos.list();
        arrayFotos = new ArrayList<Bitmap>();
        Casa c=datos.get(posicion);
        String id=""+c.getId();
        Bitmap bm;
        for (int i=0;i<archivosCarpetaFotos.length;i++){
            if (archivosCarpetaFotos[i].indexOf("casa_"+id) != -1){
                bm = BitmapFactory.decodeFile(carpetaFotos.getAbsolutePath() + "/" + archivosCarpetaFotos[i]);
                arrayFotos.add(bm);
            }
        }
        return arrayFotos;
    }

}
