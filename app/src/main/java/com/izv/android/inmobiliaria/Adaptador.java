package com.izv.android.inmobiliaria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Adaptador extends ArrayAdapter<Casa>{

    private Context contexto;
    private ArrayList<Casa> lista;
    private int recurso;
    private static LayoutInflater i;


    public static class ViewHolder{
        public TextView tvCalle,tvPrecio,tvTipo,tvLocalidad;
        public ImageView iv;
        public int posicion;
    }

    public Adaptador(Context context, int resource, ArrayList<Casa> objects) {
        super(context , resource , objects);
        recurso= resource;
        lista=objects;
        this.i= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList getLista() {
        return lista;
    }

    public void setLista(ArrayList lista) {
        this.lista = lista;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh=null;
        if(convertView==null){
            convertView=i.inflate(recurso,null);
            vh=new ViewHolder();
            vh.tvCalle=(TextView)convertView.findViewById(R.id.tvDireccion);
            vh.tvPrecio=(TextView)convertView.findViewById(R.id.tvPrecio);
            vh.tvTipo=(TextView)convertView.findViewById(R.id.tvTipo);
            vh.tvLocalidad=(TextView)convertView.findViewById(R.id.tvLocalidad);
            vh.iv = (ImageView)convertView.findViewById(R.id.ivFoto);
            convertView.setTag(vh);

        }else{
            vh=(ViewHolder)convertView.getTag();

        }
        vh.posicion=position;
        vh.tvCalle.setText(lista.get(position).getDireccion());
        vh.tvPrecio.setText(lista.get(position).getPrecio()+"");
        vh.tvTipo.setText(lista.get(position).getTipo()+"");
        vh.tvLocalidad.setText(lista.get(position).getLocalidad() + "");


        Casa a = lista.get(position);

        if(a.getTipo().toString().equals("Casa")){
            vh.iv.setImageResource(R.drawable.casa);
        } else if(a.getTipo().toString().equals("Piso")){
            vh.iv.setImageResource(R.drawable.piso);
        }

        return convertView;
    }
}