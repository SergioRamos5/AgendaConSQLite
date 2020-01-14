package com.example.agendaconsqlite;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.agendaconfragments.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.app.Activity.RESULT_OK;

public class EditFragment extends Fragment {

    private FloatingActionButton fab;
    private Datos datos;
    private EditText nombre, apellido, telefono, correo;
    private onSelectedItemEditar listenerEdit;
    private ImageView imagen;
    private RadioButton amigos, trabajo, familia;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.editar_contacto, container, false);


        fab = v.findViewById(R.id.fab2);
        nombre = v.findViewById(R.id.nombre_edit);
        apellido = v.findViewById(R.id.apellido_edit);
        telefono = v.findViewById(R.id.telefono_edit);
        correo = v.findViewById(R.id.email_edit);
        imagen = v.findViewById(R.id.contacto_edit);
        familia = v.findViewById(R.id.rB_Familia_edit);
        amigos = v.findViewById(R.id.rB_Amigos_edit);
        trabajo = v.findViewById(R.id.rB_Trabajo_edit);


        imagen.setOnCreateContextMenuListener(this);

        datos = getArguments().getParcelable("Datos");

        nombre.setText(datos.getNombre());
        apellido.setText(datos.getApellidos());
        correo.setText(datos.getCorreo());
        telefono.setText(datos.getTelefono());
        familia.setChecked(datos.isFamilia());
        amigos.setChecked(datos.isAmigos());
        trabajo.setChecked(datos.isTrabajo());
        if (datos.getImagen() != null)
            imagen.setImageBitmap(Bitmap.createScaledBitmap(datos.getImagen(), 400,400, true));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datos.setNombre(nombre.getText().toString());
                datos.setApellidos(apellido.getText().toString());
                datos.setCorreo(correo.getText().toString());
                datos.setTelefono(telefono.getText().toString());
                datos.setAmigos(amigos.isChecked());
                datos.setFamilia(familia.isChecked());
                datos.setTrabajo(trabajo.isChecked());

                listenerEdit.onItemEditSelected(datos);
            }
        });

       return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listenerEdit = (onSelectedItemEditar) context;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater menuInflater = new MenuInflater(v.getContext());
        menuInflater.inflate(R.menu.menu_contextual,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.takePhoto:
                Intent intento = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intento,1);
                break;
            case R.id.openGalery:
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 2);
                break;
            case R.id.deletePhoto:
                imagen.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.cabeza));
                break;
            case R.id.cancel:
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == 2){
            Uri ruta = data.getData();
            Bitmap b = bitmapFromUri(ruta);
            imagen.setImageBitmap(b);
            datos.setImagen(Bitmap.createScaledBitmap(b, 100,100, true));

        }
        else if (resultCode == RESULT_OK && requestCode == 1)
        {
            datos.setImagen((Bitmap) data.getExtras().get("data"));
        }

    }

    private Bitmap bitmapFromUri(Uri uri) {
        ImageView imageViewTemp = new ImageView(getContext());
        imageViewTemp.setImageURI(uri);
        BitmapDrawable d = (BitmapDrawable) imageViewTemp.getDrawable();
        return d.getBitmap();
    }
}
