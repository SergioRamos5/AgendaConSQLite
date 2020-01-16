package com.example.agendaconsqlite;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView[] view = new TextView[4];
    ImageView imagen;
    OnClickImagen listenerImagen;
    public SimpleViewHolder(@NonNull View itemView, int[] to) {
        super(itemView);
        view[0] = itemView.findViewById(to[0]);
        view[1] = itemView.findViewById(to[1]);
        view[2] = itemView.findViewById(to[2]);
        view[3] = itemView.findViewById(to[3]);
        imagen =  itemView.findViewById(to[4]);
        imagen.setOnClickListener(this);

    }

    public void bind(int pos, String dato)
    {
        view[pos].setText(dato);
    }

    public void bind(Bitmap dato)
    {
        imagen.setImageBitmap(dato);
    }

    public void ClickImagen(OnClickImagen listenerImgane)
    {
        if(listenerImgane != null) this.listenerImagen = listenerImgane;
    }

    @Override
    public void onClick(View v) {
        if (listenerImagen != null)
            listenerImagen.onClickImagen(v);
    }
}
