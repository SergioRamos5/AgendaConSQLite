package com.example.agendaconsqlite;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
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


import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class FragmentPrincipal extends Fragment {

    public RecyclerView recyclerView;
    public final ArrayList<Datos> datos;
    private onSelectedItemListener listener;

    int pos;
    private SwipeDetector swipeDetector;
    private FloatingActionButton fab;


    public FragmentPrincipal(ArrayList<Datos> datos) {
        this.datos = datos;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler, container, false);


        /*adaptador = new Adaptador(datos);
        adaptador.setLongListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                pos = recyclerView.getChildAdapterPosition(v);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("¿Eliminar contacto " + datos.get(pos).getNombre() + " ?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                datos.remove(pos);
                                recyclerView.setAdapter(adaptador);
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

        swipeDetector = new SwipeDetector();
        adaptador.setTouchListener(swipeDetector);
        adaptador.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (swipeDetector.swipeDetected())
                {
                    if (swipeDetector.getAction() == SwipeDetector.Action.LR){
                        pos = recyclerView.getChildAdapterPosition(view);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("¿Llamar a " + datos.get(pos).getNombre() + " ?")
                                .setPositiveButton("Llamar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intento = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+datos.get(pos).getTelefono()));
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
                        builder.setMessage("¿Enviar mensaje a " + datos.get(pos).getNombre() + " ?")
                                .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intento = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",datos.get(pos).getCorreo(), null));
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
                    listener.onItemSelected(datos.get(pos));
                }
            }
        });

        adaptador.ClickImagen(new OnClickImagen() {
            @Override
            public void onClickImagen(View v) {
                pos = recyclerView.getChildAdapterPosition(v);

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
                                datos.get(pos).setImagen(null);
                                recyclerView.setAdapter(adaptador);
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
        recyclerView = v.findViewById(R.id.recycler);
        recyclerView.setAdapter(adaptador);
        recyclerView.setHasFixedSize(true);/*

        /* Para poder mostrar el Recycler en forma de Lista o en forma de Parrilla.*/

        if (Utilidades.visualizacion == Utilidades.LISTA)
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager FM = getActivity().getSupportFragmentManager();
                FragmentTransaction FT  = FM.beginTransaction();
                Fragment addfragment = new AddFragment();
                FT.replace(R.id.fragment_container, addfragment);
                FT.commit();
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       /* if(resultCode == RESULT_OK && requestCode == 2){
            Uri ruta = data.getData();
            Bitmap b = bitmapFromUri(ruta);
            datos.get(pos).setImagen(Bitmap.createScaledBitmap(b, 100,100, true));

            recyclerView.setAdapter(adaptador);
        }
        else if (resultCode == RESULT_OK && requestCode == 1)
        {
            datos.get(pos).setImagen((Bitmap) data.getExtras().get("data"));
            recyclerView.setAdapter(adaptador);
        }*/

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
