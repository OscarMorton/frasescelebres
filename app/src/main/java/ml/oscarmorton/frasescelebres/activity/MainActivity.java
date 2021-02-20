package ml.oscarmorton.frasescelebres.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ml.oscarmorton.frasescelebres.DB.DBHelper;
import ml.oscarmorton.frasescelebres.R;
import ml.oscarmorton.frasescelebres.UserSession;
import ml.oscarmorton.frasescelebres.fragments.basic.AutoresEdidFragment;
import ml.oscarmorton.frasescelebres.fragments.basic.AutoresFragment;
import ml.oscarmorton.frasescelebres.fragments.basic.CategoriaEdidFragment;
import ml.oscarmorton.frasescelebres.fragments.basic.CategoriaFragment;
import ml.oscarmorton.frasescelebres.fragments.basic.FrasesEdidFragment;
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
       show list of particular frases/author/category
       Exception control cant connect
       Delete frase/autor/categoria
       modify frase/autor/categoria
       Frase del dia
       Make read me!
       Maybes:

     */
    private IAPIService apiService;
    public DBHelper dbHelper;
    private SharedPreferences preferences;

    private String currentUserPermissions;


    public ArrayList<Frase> frases;
    public StringBuilder sb;

    private UserSession userSession;

    private DrawerLayout drawer;


    private MenuItem navFrases, navFrasesEdid, navAuthor, navAuthorEdid, navCategory, navCategoryEdid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Toolbar
        Toolbar toolbar = findViewById(R.id.tooldbar);
        setSupportActionBar(toolbar);

        // Getting the ip and the port from preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);


        //Getting the apiRest Client

        apiService = RestClient.getInstance(this);
        // Getting the user permissions
        currentUserPermissions = "";
        currentUserPermissions = getIntent().getStringExtra("usuarioTipo");


        frases = new ArrayList<>();
        sb = new StringBuilder();

        //  tvHello = findViewById(R.id.tvHello);
        //bLoadFrases = findViewById(R.id.bLoadFrases);
        //vHello.setText(currentUserPermissions);


        // Creating the drawer
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        // getting the IDs of the nav items
        Menu menuNav = navigationView.getMenu();
        navFrases = menuNav.findItem(R.id.nav_frases);
        navFrasesEdid = menuNav.findItem(R.id.nav_edid_frase);
        navAuthor = menuNav.findItem(R.id.nav_author);
        navAuthorEdid = menuNav.findItem(R.id.nav_edid_author);
        navCategory = menuNav.findItem(R.id.nav_category);
        navCategoryEdid = menuNav.findItem(R.id.nav_edid_category);

        // Deactivating the nav items by default
        navFrases.setVisible(false);
        navFrasesEdid.setVisible(false);
        navAuthor.setVisible(false);
        navAuthorEdid.setVisible(false);
        navCategory.setVisible(false);
        navCategoryEdid.setVisible(false);

        // Depending on the user, we activate navigation items
        if (currentUserPermissions.equalsIgnoreCase("admin")) {
            navFrases.setVisible(true);
            navFrasesEdid.setVisible(true);
            navAuthor.setVisible(true);
            navAuthorEdid.setVisible(true);
            navCategory.setVisible(true);
            navCategoryEdid.setVisible(true);
        } else if (currentUserPermissions.equalsIgnoreCase("user")) {
            navFrases.setVisible(true);
            navAuthor.setVisible(true);
            navCategory.setVisible(true);
        }


        // Generating the users in the Database
        dbHelper = DBHelper.getInstance(this);


        getFrases();
        //Getting all the frases and converting it to 1 string using string builder


        //addFrase();
        //addFraseValues("Frase de prueba","2020-02-25",1,1);

    }

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

    public void getFrases() {
        String content = "";

        apiService.getFrases().enqueue(new Callback<List<Frase>>() {
            @Override
            public void onResponse(Call<List<Frase>> call, Response<List<Frase>> response) {
                if (response.isSuccessful()) {
                    frases = new ArrayList<>(response.body());
                    // Once fouud, I add the frases to the users session
                    userSession = new UserSession(frases);


                    for (int i = 0; i < frases.size(); i++) {
                        Log.d(MainActivity.class.getSimpleName(), "Frase " + i + ":" + frases.get(i).toString());
                    }
                    Log.i(MainActivity.class.getSimpleName(), "GotFrases");

                } else {
                    Log.i(MainActivity.class.getSimpleName(), "Get frases not succesful");

                }
            }

            @Override
            public void onFailure(Call<List<Frase>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {
            case R.id.nav_frases:
                loadFragmentFrases(FrasesFragment.SeachType.NONE, 0);

                break;
            case R.id.nav_author:
                loadFragmentAutores();
                break;
            case R.id.nav_category:
                loadFragmentCategoria();
                break;
            case R.id.nav_edid_frase:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FrasesEdidFragment()).commit();
                break;

            case R.id.nav_edid_author:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AutoresEdidFragment()).commit();
                break;

            case R.id.nav_edid_category:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoriaEdidFragment()).commit();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

    public void loadFragmentFrases( FrasesFragment.SeachType type, int id) {
        FrasesFragment frasesFragment = new FrasesFragment();
        Bundle bundle;
        frasesFragment.setFrasesListener(this);
        bundle = new Bundle();
        bundle.putSerializable(FrasesFragment.KEY_FRASES, userSession);

        switch (type){
            case NONE:
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

        frasesFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frasesFragment).commit();

    }

    public void loadFragmentAutores() {
        AutoresFragment autoresFragment = new AutoresFragment();
        Bundle bundle;
        autoresFragment.setAutoresListener(this);
        bundle = new Bundle();

        bundle.putSerializable(AutoresFragment.KEY_AUTORES, userSession); // CONTINUE HERE

        autoresFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, autoresFragment).commit();

    }

    public void loadFragmentCategoria() {
        CategoriaFragment categoriaFragment = new CategoriaFragment();
        Bundle bundle;
        categoriaFragment.setCategoriaListener(this);
        bundle = new Bundle();
        bundle.putSerializable(CategoriaFragment.KEY_CATEGORIA, userSession); // CONTINUE HERE
        categoriaFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, categoriaFragment).commit();

    }
    @Override
    public void onFraseSelected(int position) {
        Log.d(MainActivity.class.getSimpleName(), "Frase selected: ");
        Toast.makeText(this, "Frase seleccionado", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onAutorSelected(int position) {

        loadFragmentFrases(FrasesFragment.SeachType.AUTOR, position);
    }

    @Override
    public void onCategoriaSelected(int position) {
        Toast.makeText(this,"Categoria Selected", Toast.LENGTH_SHORT).show();
        loadFragmentFrases(FrasesFragment.SeachType.CATEGORIA, position);



    }
}
