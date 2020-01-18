package com.example.agendaconsqlite;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendaconfragments.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.app.Activity.RESULT_OK;

public class FragmentContactos extends Fragment {

    SQLiteDatabase sqLiteDatabase;
    OHCategoria ohCategoria;
    int pos;
    RecyclerView recyclerView;
    SwipeDetector swipeDetector;
    FloatingActionButton fab;
    MiRecyclerAdapter mAdapter;
    String[] from;
    int [] to;
    Datos datos;
    private onSelectedItemListener listener;
    private Cursor c;

    public FragmentContactos(Cursor c) {
        this.c = c;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_recycler, container, false);
        from = new String[]{"nombre", "apellidos", "telefono", "email", "imagen"};
        to = new int[] {R.id.Nombre, R.id.Apellido, R.id.Telefono, R.id.Correo, R.id.Imagen};
        ohCategoria = new OHCategoria(getActivity(), "BBDContactos",null,1);

        sqLiteDatabase = ohCategoria.getReadableDatabase();
        if (sqLiteDatabase != null)
        {
            recyclerView = v.findViewById(R.id.recycler);
            if (c == null)
                c = sqLiteDatabase.rawQuery("select * from Contactos", null);

            if (Utilidades.visualizacion == Utilidades.LISTA)
                mAdapter = new MiRecyclerAdapter(R.layout.holder, c, from, to);
            else
                mAdapter = new MiRecyclerAdapter(R.layout.holder_parrilla, c, from, to);

            //region  LONG CLICK
            mAdapter.setLongListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    pos = recyclerView.getChildAdapterPosition(v);
                    datos = mAdapter.getItem(pos);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("¿Eliminar contacto "+ datos.getNombre() +"?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String[] args = {String.valueOf(datos.getId())};

                                    sqLiteDatabase.delete("Contactos", "id=?", args );
                                    actualizar();
                                    recyclerView.setAdapter(mAdapter);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    return true;
                }
            });
            //endregion

            //region CLICK & TOUCH
            swipeDetector = new SwipeDetector();
            mAdapter.setTouchListener(swipeDetector);
            mAdapter.setClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (swipeDetector.swipeDetected())
                    {
                        if (swipeDetector.getAction() == SwipeDetector.Action.LR){
                            pos = recyclerView.getChildAdapterPosition(view);
                            datos = mAdapter.getItem(pos);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("¿Llamar a"+datos.getNombre()+"?")
                                    .setPositiveButton("Llamar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intento = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + datos.getTelefono()));
                                            startActivity(intento);

                                        }
                                    })
                                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else if (swipeDetector.getAction() == SwipeDetector.Action.RL)
                        {
                            pos = recyclerView.getChildAdapterPosition(view);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("¿Enviar mensaje a"+datos.getNombre()+"?")
                                    .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intento = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",datos.getCorreo(), null));
                                            startActivity(intento);
                                        }
                                    })
                                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }else
                    {
                        pos = recyclerView.getChildAdapterPosition(view);
                        datos = mAdapter.getItem(pos);
                        listener.onItemSelected(datos);
                    }
                }
            });
            //endregion

            //region IMAGEN
            mAdapter.ClickImagen(new OnClickImagen() {
                @Override
                public void onClickImagen(View v) {

                    pos = recyclerView.getChildAdapterPosition(v);
                    datos = mAdapter.getItem(pos);
                    PopupMenu popupMenu = new PopupMenu(getContext(), v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_contextual, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

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
                                    datos.setImagen(null);
                                    actualizar();
                                    break;
                                case R.id.cancel:
                                    break;
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                }
            });

            //endregion

            recyclerView.setAdapter(mAdapter);
            if (Utilidades.visualizacion == Utilidades.LISTA)
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            else
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

            //region FAB
            fab = v.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager FM = getActivity().getSupportFragmentManager();
                    FragmentTransaction FT  = FM.beginTransaction();
                    Fragment addfragment = new AddFragment();
                    FT.replace(R.id.fragment_container, addfragment);
                    FT.commit();
                }
            });
            //endregion
        }

        return v;
    }

    private void actualizar()
    {
        Cursor c = sqLiteDatabase.rawQuery("select * from Contactos", null);
        mAdapter = new MiRecyclerAdapter(R.layout.holder, c, from, to);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK && requestCode == 2){
            Uri ruta = data.getData();
            Bitmap b = bitmapFromUri(ruta);
            datos.setImagen(Bitmap.createScaledBitmap(b, 100,100, true));

            actualizar();
        }
        else if (resultCode == RESULT_OK && requestCode == 1)
        {
            datos.setImagen((Bitmap) data.getExtras().get("data"));
            actualizar();
        }

    }

    private Bitmap bitmapFromUri(Uri uri) {
        ImageView imageViewTemp = new ImageView(getContext());
        imageViewTemp.setImageURI(uri);
        BitmapDrawable d = (BitmapDrawable) imageViewTemp.getDrawable();
        return d.getBitmap();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (onSelectedItemListener) context;
    }
}
