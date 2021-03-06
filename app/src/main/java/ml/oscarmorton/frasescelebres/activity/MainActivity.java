package ml.oscarmorton.frasescelebres.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ml.oscarmorton.frasescelebres.DB.DBHelper;
import ml.oscarmorton.frasescelebres.R;
import ml.oscarmorton.frasescelebres.UserSession;
import ml.oscarmorton.frasescelebres.fragments.basic.AutoresFragment;
import ml.oscarmorton.frasescelebres.fragments.basic.CategoriaFragment;
import ml.oscarmorton.frasescelebres.fragments.basic.FrasesFragment;
import ml.oscarmorton.frasescelebres.interfacess.IAPIService;
import ml.oscarmorton.frasescelebres.interfacess.listeners.IAutorListener;
import ml.oscarmorton.frasescelebres.interfacess.listeners.ICategoriaListener;
import ml.oscarmorton.frasescelebres.interfacess.listeners.IFrasesListener;
import ml.oscarmorton.frasescelebres.model.Autor;
import ml.oscarmorton.frasescelebres.model.Categoria;
import ml.oscarmorton.frasescelebres.model.Frase;
import ml.oscarmorton.frasescelebres.rest.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IFrasesListener, IAutorListener, ICategoriaListener {

    /*TODO
       Delete frase/autor/categoria GET BACK TO THIS
       modify frase/autor/categoria
       Add frase/autor/categoria
       Frase del dia
       Make read me!
       Maybes:

     */
    private IAPIService apiService;
    public DBHelper dbHelper;
    private String currentUserPermissions;

    public UserSession userSession;

    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.tooldbar);
        setSupportActionBar(toolbar);

        // Initializing userSession
        userSession = new UserSession(this);

        // Getting the ip and the port from preferences


        //Getting the apiRest Client
        apiService = RestClient.getInstance(this);

        // Getting the user permissions
        currentUserPermissions = "";
        currentUserPermissions = getIntent().getStringExtra("usuarioTipo");


        // Creating the drawer
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        // Generating the users in the Database
        dbHelper = DBHelper.getInstance(this);


        //addFrase();
        //addFraseValues("Frase de prueba","2020-02-25",1,1);

    }
/*
    public void addFraseValues(String frase, String fecha, int idAutor, int idCategoria) {
        Log.i(MainActivity.class.getSimpleName(), "Añadiendo frase ...");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        apiService.addFraseValues(frase, fecha, idAutor, idCategoria).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        Log.i(MainActivity.class.getSimpleName(), "Frase añadida correctamente");
                    } else {
                        Log.i(MainActivity.class.getSimpleName(), "Error al añadir la frase");

                        Log.i(MainActivity.class.getSimpleName(), response.raw().toString());
                    }
                } else {
                    Log.i(MainActivity.class.getSimpleName(), "Frase añadido sin respuesta");

                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

 */

    /*
    public void addFrase() {
        Log.i(MainActivity.class.getSimpleName(), "Añadiendo frase ...");
        Autor autor = new Autor(1, "Autor 1", 10, null, "Fontanero");
        Categoria categoria = new Categoria(1, "Categoria 1");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Frase frase = new Frase("Otra frase", sdf.format(new Date()), autor, categoria);
        apiService.addFrase(frase).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        Log.i(MainActivity.class.getSimpleName(), "Frase añadida correctamente");
                    } else {
                        Log.i(MainActivity.class.getSimpleName(), "Error al añadir la frase");

                        Log.i(MainActivity.class.getSimpleName(), response.raw().toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

     */




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case R.id.nav_frases:
                loadFragmentFrases(FrasesFragment.SeachType.ALL, 0);

                break;
            case R.id.nav_author:
                loadFragmentAutores();
                break;
            case R.id.nav_category:
                loadFragmentCategoria();
                break;
            case R.id.nav_login:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            case R.id.nav_action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;

        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    // On back pressed we close the drawer
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }

    // Menu stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        } else if (id == R.id.loginItem) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     *
     * @param type The type of list we want
     * @param id The id of the autho or category
     */
    public void loadFragmentFrases(FrasesFragment.SeachType type, int id) {
        FrasesFragment frasesFragment = new FrasesFragment();
        Bundle bundle;
        frasesFragment.setFrasesListener(this);
        bundle = new Bundle();
        bundle.putSerializable(FrasesFragment.KEY_FRASES, userSession);

        // We sent a in a bundle of the type of list we want, authors, categories or all of them
        switch (type) {
            case ALL:
                // do nothing
                break;
            case AUTOR:
                bundle.putInt(FrasesFragment.KEY_TYPE, 1);
                break;
            case CATEGORIA:
                bundle.putInt(FrasesFragment.KEY_TYPE, 2);
                break;
        }

        bundle.putInt(FrasesFragment.KEY_ID, id);

        if(currentUserPermissions.equalsIgnoreCase("admin")){
            bundle.putInt(FrasesFragment.KEY_USER_PERMISION_FRASES,1);
        }else{
            bundle.putInt(FrasesFragment.KEY_USER_PERMISION_FRASES,0);

        }


        frasesFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frasesFragment).commit();
        setTitle("Frases");

    }

    public void loadFragmentAutores() {
        AutoresFragment autoresFragment = new AutoresFragment();
        Bundle bundle;
        autoresFragment.setAutoresListener(this);
        bundle = new Bundle();

        bundle.putSerializable(AutoresFragment.KEY_AUTORES, userSession);

        if(currentUserPermissions.equalsIgnoreCase("admin")){
            bundle.putInt(AutoresFragment.KEY_USER_PERMISION_AUTOR,1);
        }else{
            bundle.putInt(AutoresFragment.KEY_USER_PERMISION_AUTOR,0);

        }

        autoresFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, autoresFragment).commit();
        setTitle("Autores");

    }

    public void loadFragmentCategoria() {
        CategoriaFragment categoriaFragment = new CategoriaFragment();
        Bundle bundle;
        categoriaFragment.setCategoriaListener(this);
        bundle = new Bundle();
        bundle.putSerializable(CategoriaFragment.KEY_CATEGORIA, userSession);

        if(currentUserPermissions.equalsIgnoreCase("admin")){
            bundle.putInt(CategoriaFragment.KEY_USER_PERMISION_CATEGORIA,1);
        }else{
            bundle.putInt(CategoriaFragment.KEY_USER_PERMISION_CATEGORIA,0);

        }


        categoriaFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, categoriaFragment).commit();
        setTitle("Categorias");
    }


    // Deletes
    public void deleteFrase(int position) {
        Frase frase = userSession.getFrases().get(position);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Borrar Frase " + frase.getId());
        alert.setMessage("¿Seguro que desea borrar la frase " + frase.getId() + "?");

        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                Call<Void> deleteRequest = apiService.deleteFrase(frase.getId());
                deleteRequest.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d(MainActivity.class.getSimpleName(), "DELETED ");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d(MainActivity.class.getSimpleName(), "NOT DELETED " + t.getMessage());

                    }
                });

            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                return;
            }
        });
        alert.show();

    }

    public void deleteAutor(int position){
        Autor autor = userSession.getAutores().get(position);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Borrar Frase " + autor.getId());
        alert.setMessage("¿Seguro que desea borrar la frase " + autor.getId() + "?");

        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                Call<Void> deleteRequest = apiService.deleteAutor(autor.getId());
                deleteRequest.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d(MainActivity.class.getSimpleName(), "DELETED ");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d(MainActivity.class.getSimpleName(), "NOT DELETED " + t.getMessage());

                    }
                });

            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                return;
            }
        });
        alert.show();
    }

    public void deleteCategoria(int position){
        Categoria categoria = userSession.getCategorias().get(position);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Borrar Frase " + categoria.getId());
        alert.setMessage("¿Seguro que desea borrar la frase " + categoria.getId() + "?");

        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                Call<Void> deleteRequest = apiService.deleteCategoria(categoria.getId());
                deleteRequest.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d(MainActivity.class.getSimpleName(), "DELETED ");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d(MainActivity.class.getSimpleName(), "NOT DELETED " + t.getMessage());

                    }
                });

            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                return;
            }
        });
        alert.show();
    }


    // Listeners
    @Override
    public void onFraseSelected(int position) {
        Log.d(MainActivity.class.getSimpleName(), "Frase selected: ");
        Toast.makeText(this, "Frase seleccionado", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onLongClickFrase(int position) {
        Log.d(MainActivity.class.getSimpleName(), "Long click frases activated ");
        Toast.makeText(this, userSession.getFrases().get(position).getAutor().getNombre(), Toast.LENGTH_SHORT).show();
        if (currentUserPermissions.equalsIgnoreCase("admin")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Admin options");
            builder.setItems(new CharSequence[]
                            {"Cancel", "Edit", "Delete"},
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            switch (which) {
                                case 0:

                                    break;
                                case 1:
                                    Toast.makeText(MainActivity.this, "Edid frase", Toast.LENGTH_SHORT).show();
                                    break;
                                case 2:
                                    deleteFrase(position);
                                    break;

                            }
                        }
                    });
            builder.create().show();
        }

    }

    @Override
    public void onAutorSelected(int position) {

        loadFragmentFrases(FrasesFragment.SeachType.AUTOR, position);
    }

    @Override
    public void onLongClickAutor(int position) {
        Log.d(MainActivity.class.getSimpleName(), "Long click Autor activated ");
        Toast.makeText(this, userSession.getAutores().get(position).getNombre(), Toast.LENGTH_SHORT).show();
        if (currentUserPermissions.equalsIgnoreCase("admin")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Admin options");
            builder.setItems(new CharSequence[]
                            {"Cancel", "Edit", "Delete"},
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            switch (which) {
                                case 0:

                                    break;
                                case 1:
                                    Toast.makeText(MainActivity.this, "Edid frase", Toast.LENGTH_SHORT).show();
                                    break;
                                case 2:
                                    deleteAutor(position);
                                    break;

                            }
                        }
                    });
            builder.create().show();
        }

    }

    @Override
    public void onCategoriaSelected(int position) {
        Toast.makeText(this, "Categoria Selected", Toast.LENGTH_SHORT).show();
        loadFragmentFrases(FrasesFragment.SeachType.CATEGORIA, position);


    }

    @Override
    public void onLongClickCategoria(int position) {
        Log.d(MainActivity.class.getSimpleName(), "Long click frases activated ");
        Toast.makeText(this, userSession.getCategorias().get(position).getNombre(), Toast.LENGTH_SHORT).show();
        if (currentUserPermissions.equalsIgnoreCase("admin")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Admin options");
            builder.setItems(new CharSequence[]
                            {"Cancel", "Edit", "Delete"},
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            switch (which) {
                                case 0:

                                    break;
                                case 1:
                                    Toast.makeText(MainActivity.this, "Edid frase", Toast.LENGTH_SHORT).show();
                                    break;
                                case 2:
                                    deleteCategoria(position);
                                    break;

                            }
                        }
                    });
            builder.create().show();
        }
    }
}
