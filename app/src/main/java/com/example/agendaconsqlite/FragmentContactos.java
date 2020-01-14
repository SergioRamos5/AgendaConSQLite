package com.example.agendaconsqlite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
            Cursor c = sqLiteDatabase.rawQuery("select * from Contactos", null);
            mAdapter = new MiRecyclerAdapter(R.layout.holder, c, from, to);

            //region  LONG CLICK
            mAdapter.setLongListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    pos = recyclerView.getChildAdapterPosition(v);
                    datos = mAdapter.getItem(pos);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("¿Eliminar contacto"+ datos.getNombre() +"?")
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

                    }
                }
            });
            //endregion

            recyclerView.setAdapter(mAdapter);
            if (Utilidades.visualizacion == Utilidades.LISTA)
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            else
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

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

}
