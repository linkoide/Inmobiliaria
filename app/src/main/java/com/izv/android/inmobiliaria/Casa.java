package com.izv.android.inmobiliaria;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abraham on 3/12/14.
 *
 * CLASE POHO.
 */
public class Casa implements Comparable<Casa>, Parcelable{
    private int id;
    private double precio;
    private String localidad, direccion, tipo;


    public Casa(int id, double precio, String localidad, String direccion, String tipo) {
        this.id = id;
        this.precio = precio;
        this.localidad = localidad;
        this.direccion = direccion;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Casa.class != o.getClass()) return false;

        Casa casa = (Casa) o;

        if (id != casa.id) return false;
        if (Double.compare(casa.precio, precio) != 0) return false;
        if (direccion != null ? !direccion.equals(casa.direccion) : casa.direccion != null)
            return false;
        if (localidad != null ? !localidad.equals(casa.localidad) : casa.localidad != null)
            return false;
        if (tipo != null ? !tipo.equals(casa.tipo) : casa.tipo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        temp = Double.doubleToLongBits(precio);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (localidad != null ? localidad.hashCode() : 0);
        result = 31 * result + (direccion != null ? direccion.hashCode() : 0);
        result = 31 * result + (tipo != null ? tipo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Casa{" +
                "id=" + id +
                ", precio=" + precio +
                ", localidad='" + localidad + '\'' +
                ", direccion='" + direccion + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }



    // ==================== Parcelable ====================
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.id);
        parcel.writeDouble(this.precio);
        parcel.writeString(this.localidad);
        parcel.writeString(this.direccion);
        parcel.writeString(this.tipo);
    }

    private Casa(Parcel c) {
        this.id = c.readInt();
        this.precio = c.readDouble();
        this.localidad = c.readString();
        this.direccion = c.readString();
        this.tipo = c.readString();
    }

    public static final Parcelable.Creator<Casa> CREATOR = new Parcelable.Creator<Casa>() {
        public Casa createFromParcel(Parcel in) {
            return new Casa(in);
        }

        public Casa[] newArray(int size) {
            return new Casa[size];
        }
    };


    @Override
    public int compareTo(Casa another) {
        return 0;
    }
}
