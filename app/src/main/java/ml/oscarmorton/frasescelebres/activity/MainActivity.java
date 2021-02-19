package ml.oscarmorton.frasescelebres.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
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
import ml.oscarmorton.frasescelebres.fragments.AutoresEdidFragment;
import ml.oscarmorton.frasescelebres.fragments.AutoresFragment;
import ml.oscarmorton.frasescelebres.fragments.CategoriaEdidFragment;
import ml.oscarmorton.frasescelebres.fragments.CategoriaFragment;
import ml.oscarmorton.frasescelebres.fragments.FrasesEdidFragment;
import ml.oscarmorton.frasescelebres.fragments.FrasesFragment;
import ml.oscarmorton.frasescelebres.interfacess.IAPIService;
import ml.oscarmorton.frasescelebres.listeners.IFrasesListener;
import ml.oscarmorton.frasescelebres.model.Autor;
import ml.oscarmorton.frasescelebres.model.Categoria;
import ml.oscarmorton.frasescelebres.model.Frase;
import ml.oscarmorton.frasescelebres.rest.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IFrasesListener {

    /*TODO
       list all frases
       list all author
       list all categories
       show list of particular frases/author/category
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
        navFrases.setEnabled(false);
        navFrasesEdid.setEnabled(false);
        navAuthor.setEnabled(false);
        navAuthorEdid.setEnabled(false);
        navCategory.setEnabled(false);
        navCategoryEdid.setEnabled(false);

        // Depending on the user, we activate navigation items
        if (currentUserPermissions.equalsIgnoreCase("admin")) {
            navFrases.setEnabled(true);
            navFrasesEdid.setEnabled(true);
            navAuthor.setEnabled(true);
            navAuthorEdid.setEnabled(true);
            navCategory.setEnabled(true);
            navCategoryEdid.setEnabled(true);
        } else if (currentUserPermissions.equalsIgnoreCase("user")) {
            navFrases.setEnabled(true);
            navAuthor.setEnabled(true);
            navCategory.setEnabled(true);
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
                loadFragmentTest();

                break;
            case R.id.nav_author:
                break;
            case R.id.nav_category:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoriaFragment()).commit();
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

    public void loadFragmentTest() {
        FrasesFragment frasesFragment = new FrasesFragment();
        Bundle bundle;
        frasesFragment.setFrasesListener(this);
        bundle = new Bundle();
        bundle.putSerializable(FrasesFragment.KEY_FRASES, userSession); // CONTINUE HERE
        frasesFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frasesFragment).commit();

    }

    @Override
    public void onFraseSelected(int position) {
        Log.d(MainActivity.class.getSimpleName(), "Frase selected: ");
        Toast.makeText(this, "Frase seleccionado", Toast.LENGTH_SHORT).show();
    }
}