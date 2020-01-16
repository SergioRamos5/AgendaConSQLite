package com.example.agendaconsqlite;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MiRecyclerAdapter extends CursorRecyclerAdapterAbs implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {

    private int mLayout;
    private int[] mFrom;
    private int[] mTo;
    private View.OnClickListener listener;
    private View.OnLongClickListener longListener;
    private View.OnTouchListener listenerTouch;
    private View view;
    private OnClickImagen listenerImagen;

    public MiRecyclerAdapter(int layout, Cursor cursor, String[] from, int[] to) {
        super(cursor);
        mLayout = layout;
        mTo = to;
        findColumns(cursor, from);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false);

        SimpleViewHolder holder = new SimpleViewHolder(view, mTo);

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        view.setOnTouchListener(this);
        holder.ClickImagen(new OnClickImagen() {
            @Override
            public void onClickImagen(View v) {
                listenerImagen.onClickImagen(view);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {
        if (Utilidades.visualizacion == Utilidades.LISTA)
        {
            ((SimpleViewHolder) holder).bind(0, cursor.getString(mFrom[0]));
            ((SimpleViewHolder) holder).bind(1, cursor.getString(mFrom[1]));
            ((SimpleViewHolder) holder).bind(2, cursor.getString(mFrom[2]));
            ((SimpleViewHolder) holder).bind(3, cursor.getString(mFrom[3]));
            Bitmap theImage = MainActivity.convertirStringBitmap(cursor.getString(mFrom[4]));
            ((SimpleViewHolder) holder).bind(theImage);
        }
        else
        {
            ((SimpleViewHolder) holder).bind(0, cursor.getString(mFrom[0]));
            ((SimpleViewHolder) holder).bind(1, cursor.getString(mFrom[1]));
            Bitmap theImage = MainActivity.convertirStringBitmap(cursor.getString(mFrom[4]));
            ((SimpleViewHolder) holder).bind(theImage);
        }

    }


    private void findColumns(Cursor c, String[] from)
    {
        if (c != null) {
            int i;
            int count = from.length;
            if (mFrom == null || mFrom.length != count)
                mFrom = new int[count];
            for (i = 0; i < count; i++)
                mFrom[i] = c.getColumnIndexOrThrow(from[i]);
        }
        else
            mFrom = null;
    }

    public void setClickListener(View.OnClickListener listener)
    {
        if(listener != null)
            this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) listener.onClick(v);
    }

    public void setLongListener(View.OnLongClickListener longListener)
    {
        if (longListener != null)
            this.longListener = longListener;
    }

    @Override
    public boolean onLongClick(View v) {
        if (longListener != null)
            longListener.onLongClick(v);
        return true;
    }

    public void setTouchListener(View.OnTouchListener listenerTouch)
    {
        if (listenerTouch != null)
            this.listenerTouch = listenerTouch;
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        if (listenerTouch != null)
            listenerTouch.onTouch(view, motionEvent);
        return false;
    }

    public Datos getItem(int position)
    {
        getCursor().moveToPosition(position);
        return getContactoFromCursor(getCursor());
    }

    private Datos getContactoFromCursor(Cursor cursor){
        Datos c = new Datos();
        c.setId(cursor.getInt(0));
        c.setNombre(cursor.getString(1));
        c.setApellidos(cursor.getString(2));
        c.setTelefono(cursor.getString(3));
        c.setCorreo(cursor.getString(4));
        c.setImagen(MainActivity.convertirStringBitmap(cursor.getString(5)));
        c.setAmigos(cursor.getInt(6) == 1);
        c.setFamilia(cursor.getInt(7) == 1);
        c.setTrabajo(cursor.getInt(8) == 1);
        return c;
    }

    public void ClickImagen(OnClickImagen listenerImagen)
    {
        if (listenerImagen != null)
            this.listenerImagen = listenerImagen;
    }
}
