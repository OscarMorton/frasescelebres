package ml.oscarmorton.frasescelebres.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ml.oscarmorton.frasescelebres.R;
import ml.oscarmorton.frasescelebres.UserSession;
import ml.oscarmorton.frasescelebres.fragments.basic.AutoresFragment;
import ml.oscarmorton.frasescelebres.fragments.basic.FrasesFragment;
import ml.oscarmorton.frasescelebres.interfacess.IAPIService;
import ml.oscarmorton.frasescelebres.model.Autor;
import ml.oscarmorton.frasescelebres.model.Categoria;
import ml.oscarmorton.frasescelebres.model.Frase;
import ml.oscarmorton.frasescelebres.rest.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAutor extends AppCompatActivity {
    private UserSession userSession;
    private IAPIService apiService;
    private EditText etMuerte, etNacimiento, etNombre, etProfesion;
    private Button bSaveAutor;
    private String muerte, nombre, profesion;
    private int nacimiento;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addautor);

        apiService = RestClient.getInstance(this);

        // Gettins ids
        etMuerte = findViewById(R.id.etMuerte);
        etNacimiento = findViewById(R.id.etNacimiento);
        etNombre = findViewById(R.id.etNameAuthor);
        etProfesion = findViewById(R.id.etProfesion);
        bSaveAutor = findViewById(R.id.bSaveAuthor);

        Toast.makeText(this, String.valueOf(etNombre.getText()), Toast.LENGTH_SHORT).show();

        Intent i = getIntent();
        userSession = (UserSession) i.getSerializableExtra(AutoresFragment.KEY_ADD_AUTOR);

        bSaveAutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                muerte = String.valueOf(etMuerte.getText());
                nacimiento = Integer.parseInt(String.valueOf(etNacimiento.getText()));
                nombre = String.valueOf(etNombre.getText());
                profesion = String.valueOf(etProfesion.getText());
                addAutor(nombre,nacimiento,muerte,profesion);


            }
        });

    }

    public void addAutor(String nombre, int nacimiento, String muerte, String profesion) {

        int id = userSession.getLastIdUserAutores();
        Autor autoradd = new Autor( id, nombre, nacimiento, muerte, profesion);
        apiService.addAutor(autoradd).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        Log.i(MainActivity.class.getSimpleName(), "Autor added correctly");
                    } else {
                        Toast.makeText(AddAutor.this, "Autor not added, ERROR SQL ", Toast.LENGTH_SHORT).show();
                        Log.i(MainActivity.class.getSimpleName(), "Error al añadir la frase");

                        Log.i(MainActivity.class.getSimpleName(), response.raw().toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(AddAutor.this, t.getMessage() + "FAILED BUT STILL CREATED", Toast.LENGTH_LONG).show();
            }
        });
    }

}
