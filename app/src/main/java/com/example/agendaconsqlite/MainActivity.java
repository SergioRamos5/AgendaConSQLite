package com.example.agendaconsqlite;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.agendaconfragments.R;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements onSelectedItemListener, onSelectedItemEditar {

    ArrayList<Datos> datos;
    DrawerLayout drawerLayout;

    FragmentContactos fragmentContactos;
    SQLiteDatabase sqLiteDatabase;
    OHCategoria ohCategoria;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawerlayout);
        crearToolbar();
        ohCategoria = new OHCategoria(this, "BBDContactos",null,1);

        //insertarDatos();

        crearFragmentPrincipal();

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                sqLiteDatabase = ohCategoria.getReadableDatabase();

                switch (menuItem.getItemId()) {
                    case R.id.Familia:
                        cursor = sqLiteDatabase.rawQuery("SELECT * FROM contactos WHERE familia = ?", new String[]{"1"});
                        break;
                    case R.id.Amigos:
                        cursor = sqLiteDatabase.rawQuery("SELECT * FROM contactos WHERE amigos = ?", new String[]{"1"});
                        break;
                    case R.id.Trabajo:
                        cursor = sqLiteDatabase.rawQuery("SELECT * FROM contactos WHERE trabajo = ?", new String[]{"1"});
                        break;
                    default:
                        cursor = sqLiteDatabase.rawQuery("SELECT * FROM contactos", null);
                        break;
                }

                crearFragmentPrincipal();
                return true;
            }
        });
    }

    public void crearToolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void crearFragmentPrincipal()
    {
        FragmentManager FM = getSupportFragmentManager();
        FragmentTransaction FT  = FM.beginTransaction();
        Fragment fragment = new FragmentContactos(cursor);
        FT.replace(R.id.fragment_container, fragment);
        FT.commit();
    }


    static public Bitmap convertirStringBitmap(String imagen){
        byte[] decodedString = Base64.decode(imagen, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
    }

    static public String ConvertirImagenString(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] byte_arr = stream.toByteArray();
        String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
        return image_str;
    }

    public void insertarDatos()
    {
        sqLiteDatabase = ohCategoria.getWritableDatabase();
        String img;
        if (sqLiteDatabase != null);
        {
            ContentValues valores = new ContentValues();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            valores.put("nombre", "Sergio");
            valores.put("apellidos", "Ramos Santonja");
            valores.put("telefono", "676813768");
            valores.put("email", "sergio@gmail.com");
            img = ConvertirImagenString(BitmapFactory.decodeResource(getResources(), R.drawable.cabeza));
            valores.put("imagen", img);
            sqLiteDatabase.insert("Contactos", null, valores);

            valores.put("nombre", "Paula");
            valores.put("apellidos", "Valero Ferrandez");
            valores.put("telefono", "678485124");
            valores.put("email", "paula@gmail.com");
            img = ConvertirImagenString(BitmapFactory.decodeResource(getResources(), R.drawable.cabeza));
            valores.put("imagen", img);
            sqLiteDatabase.insert("Contactos", null, valores);
            sqLiteDatabase.close();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
            drawerLayout.openDrawer(GravityCompat.START);
            break;
            case R.id.vista_lista:
                Utilidades.visualizacion = Utilidades.LISTA;
                break;
            case R.id.vista_parrilla:
                Utilidades.visualizacion = Utilidades.PARRILLA;
                break;
        }
        crearFragmentPrincipal();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(Datos datos) {
        Fragment fragmentEdit = new EditFragment();

        Bundle args = new Bundle();

        fragmentEdit.setArguments(args);

        args.putParcelable("Datos", datos);
        FragmentManager FM = getSupportFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        FT.replace(R.id.fragment_container, fragmentEdit);
        FT.addToBackStack(null);

        FT.commit();
    }

    @Override
    public void onItemEdSelected(Datos datos) {

        FragmentManager FM = getSupportFragmentManager();
        FragmentTransaction FT  = FM.beginTransaction();
        Fragment fragment = new FragmentContactos(cursor);
        FT.replace(R.id.fragment_container, fragment);
        FT.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vista, menu);
        return true;
    }

}
