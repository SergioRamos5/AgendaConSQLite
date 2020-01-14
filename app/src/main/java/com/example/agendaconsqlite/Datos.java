package com.example.agendaconsqlite;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Datos implements Parcelable {
    private int id;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String correo;
    private Bitmap imagen;
    private boolean amigos;
    private boolean familia;
    private boolean trabajo;

    public Datos() {
    }


    public Datos(int id, String nombre, String apellidos, String telefono, String correo, Bitmap imagen, boolean amigos, boolean familia, boolean trabajo) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.correo = correo;
        this.imagen = imagen;
        this.amigos = amigos;
        this.familia = familia;
        this.trabajo = trabajo;
    }


    protected Datos(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        apellidos = in.readString();
        telefono = in.readString();
        correo = in.readString();
        imagen = in.readParcelable(Bitmap.class.getClassLoader());
        amigos = in.readByte() != 0;
        familia = in.readByte() != 0;
        trabajo = in.readByte() != 0;
    }

    public static final Creator<Datos> CREATOR = new Creator<Datos>() {
        @Override
        public Datos createFromParcel(Parcel in) {
            return new Datos(in);
        }

        @Override
        public Datos[] newArray(int size) {
            return new Datos[size];
        }
    };

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public boolean isAmigos() {
        return amigos;
    }

    public void setAmigos(boolean amigos) {
        this.amigos = amigos;
    }

    public boolean isFamilia() {
        return familia;
    }

    public void setFamilia(boolean familia) {
        this.familia = familia;
    }

    public boolean isTrabajo() {
        return trabajo;
    }

    public void setTrabajo(boolean trabajo) {
        this.trabajo = trabajo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(apellidos);
        dest.writeString(telefono);
        dest.writeString(correo);
        dest.writeParcelable(imagen, flags);
        dest.writeByte((byte) (amigos ? 1 : 0));
        dest.writeByte((byte) (familia ? 1 : 0));
        dest.writeByte((byte) (trabajo ? 1 : 0));
    }
}

