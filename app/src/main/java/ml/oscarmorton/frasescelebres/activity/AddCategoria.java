package ml.oscarmorton.frasescelebres.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ml.oscarmorton.frasescelebres.R;
import ml.oscarmorton.frasescelebres.UserSession;
import ml.oscarmorton.frasescelebres.fragments.basic.AutoresFragment;
import ml.oscarmorton.frasescelebres.fragments.basic.CategoriaFragment;
import ml.oscarmorton.frasescelebres.interfacess.IAPIService;
import ml.oscarmorton.frasescelebres.model.Autor;
import ml.oscarmorton.frasescelebres.model.Categoria;
import ml.oscarmorton.frasescelebres.rest.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCategoria extends AppCompatActivity {

    private UserSession userSession;
    private IAPIService apiService;
    private EditText etNombre;
    private Button bSaveCategory;
    private String nombreCategoria;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcategoria);

        etNombre = findViewById(R.id.etNombreCategoriaEdid);
        apiService = RestClient.getInstance(this);

        Intent i = getIntent();
        userSession = (UserSession) i.getSerializableExtra(CategoriaFragment.KEY_ADD_CATEGORY);

        bSaveCategory = (Button)findViewById(R.id.bSaveCategoria);

        Toast.makeText(this, String.valueOf(etNombre.getText()), Toast.LENGTH_SHORT).show();

        bSaveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nombreCategoria = String.valueOf(etNombre.getText());
                addCategory(nombreCategoria);

            }
        });

    }
    public void addCategory(String nombre) {
        int id = userSession.getLastIdUserAutores();


        Categoria categoriaAdd = new Categoria(id, nombre);
        apiService.addCategoria(categoriaAdd).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        Log.i(MainActivity.class.getSimpleName(), "Categoria added correctly");
                    } else {
                        Toast.makeText(AddCategoria.this, "Categoria not added, ERROR SQL ", Toast.LENGTH_SHORT).show();
                        Log.i(MainActivity.class.getSimpleName(), "Error al añadir la frase");

                        Log.i(MainActivity.class.getSimpleName(), response.raw().toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(AddCategoria.this, t.getMessage() + "FAILED BUT STILL CREATED", Toast.LENGTH_LONG).show();
            }
        });
    }
}
