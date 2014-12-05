package com.izv.android.inmobiliaria;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by abraham on 3/12/14.
 */
public class Opxml {
    public static void crear(Context contexto, ArrayList<Casa> lista){
        try{
            FileOutputStream fosxml = new FileOutputStream(new File(contexto.getExternalFilesDir(null), "casas.xml"));

            XmlSerializer docxml= Xml.newSerializer();
            docxml.setOutput(fosxml, "UTF-8");
            docxml.startDocument(null, Boolean.valueOf(true));
            docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

            docxml.startTag(null, "inmobiliaria");
            for(int i = 0; i<lista.size();i++){
                docxml.startTag(null, "casa");
                docxml.attribute(null, "id", String.valueOf(lista.get(i).getId()));
                docxml.attribute(null, "precio", String.valueOf(lista.get(i).getPrecio()));
                docxml.attribute(null, "localidad", lista.get(i).getLocalidad());
                docxml.attribute(null, "direccion", lista.get(i).getDireccion());
                docxml.attribute(null, "tipo", lista.get(i).getTipo());
                docxml.endTag(null, "casa");
            }
            docxml.endTag(null, "casa");
            docxml.endDocument();
            docxml.flush();
            fosxml.close();
        }catch(Exception e){
            System.out.println("ERROR AL GUARDAR LOS DATOS");
        }
    }

    public static void eliminar(Context con, ArrayList<Casa> lista , Casa c){
        for(int i=0;i<lista.size();i++){
            if(lista.get(i).equals(c)){
                lista.remove(i);
                crear(con, lista);
                break;
            }
        }

    }

    public static void modificar(Context con, ArrayList<Casa> lista, Casa casaNueva, Casa casaVieja){
        for(int i=0;i<lista.size();i++){
            if(lista.get(i).equals(casaVieja)){
                lista.remove(i);
                lista.add(casaNueva);
                crear(con, lista);
                break;
            }
        }
    }

    public static ArrayList<Casa> leer(Context contexto){
        ArrayList<Casa> datos = new ArrayList<Casa>();
        try{
            XmlPullParser lectorxml= Xml.newPullParser();
            lectorxml.setInput(new FileInputStream(new File(contexto.getExternalFilesDir(null), "casas.xml")), "utf-8");
            int evento = lectorxml.getEventType();
            while(evento!=XmlPullParser.END_DOCUMENT){
                if(evento==XmlPullParser.START_TAG){
                    String etiqueta = lectorxml.getName();
                    if(etiqueta.compareTo("casa")==0){
                        datos.add(new Casa(Integer.parseInt(lectorxml.getAttributeValue(null, "id")),
                                Double.valueOf(lectorxml.getAttributeValue(null, "precio")),
                                lectorxml.getAttributeValue(null, "localidad"),
                                lectorxml.getAttributeValue(null, "direccion"),
                                lectorxml.getAttributeValue(null, "tipo")
                        ));
                    }
                }
                evento = lectorxml.next();
            }

        }catch (Exception e) {
            System.out.println("Error al leer archivo");
        }
        if(datos.size()!=0){
            return datos;
        }else{
            return new ArrayList<Casa>();
        }
    }

}
