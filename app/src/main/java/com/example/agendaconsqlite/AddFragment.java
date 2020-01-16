package com.example.agendaconsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agendaconfragments.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddFragment extends Fragment {

    private Datos datos;
    private FloatingActionButton fab;
    EditText nombre, apellido, correo, telefono;
    onSelectedItemAdd listenerAdd;
    RadioButton amigos, trabajo, familia;
    SQLiteDatabase sqLiteDatabase;
    OHCategoria ohCategoria;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add, container, false);

        ohCategoria = new OHCategoria(getActivity(), "BBDContactos",null,1);

        nombre = v.findViewById(R.id.eT_Nombre);
        apellido = v.findViewById(R.id.eT_Apellido);
        correo = v.findViewById(R.id.eT_email);
        telefono = v.findViewById(R.id.eT_Telefono);
        familia = v.findViewById(R.id.rB_Familia);
        amigos = v.findViewById(R.id.rB_Amigos);
        trabajo = v.findViewById(R.id.rB_Trabajo);

        fab = v.findViewById(R.id.fab1);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteDatabase = ohCategoria.getWritableDatabase();

                ContentValues cv = new ContentValues();
                cv.put("nombre", nombre.getText().toString());
                cv.put("apellidos",apellido.getText().toString());
                cv.put("telefono",telefono.getText().toString());
                cv.put("email",correo.getText().toString());
                String img = MainActivity.ConvertirImagenString(BitmapFactory.decodeResource(getResources(), R.drawable.cabeza));
                cv.put("imagen", img);
                cv.put("amigos",amigos.isChecked());
                cv.put("trabajo",trabajo.isChecked());
                cv.put("familia",familia.isChecked());
                sqLiteDatabase.insert("Contactos",null,cv);
                listenerAdd.onItemAddSelected(datos);

            }
        });

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listenerAdd = (onSelectedItemAdd) context;
    }
}
