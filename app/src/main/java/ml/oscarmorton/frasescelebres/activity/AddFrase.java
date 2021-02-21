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
import ml.oscarmorton.frasescelebres.fragments.basic.FrasesFragment;
import ml.oscarmorton.frasescelebres.interfacess.IAPIService;
import ml.oscarmorton.frasescelebres.model.Autor;
import ml.oscarmorton.frasescelebres.model.Categoria;
import ml.oscarmorton.frasescelebres.model.Frase;
import ml.oscarmorton.frasescelebres.rest.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFrase  extends AppCompatActivity {
    private Spinner sIdAutor, sCategoria;
    private UserSession userSession;
    private EditText etFechaProgramada, etTexto;
    private Button bSaveFrase;
    private IAPIService apiService;
    private String frase;
    private String fecha;
    private int autor;
    private int categoria ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfrase);


        apiService = RestClient.getInstance(this);

        // Getting ids
        etFechaProgramada = findViewById(R.id.etFechaProgramadaFrase);
        etTexto = findViewById(R.id.edTextFrase);
        sIdAutor = findViewById(R.id.sAuthorIdFrase);
        sCategoria = findViewById(R.id.sCategoriaIds);
        bSaveFrase = findViewById(R.id.bSaveFrase);

        Intent i = getIntent();
        userSession = (UserSession) i.getSerializableExtra(FrasesFragment.KEY_ADD_FRASE);

        sIdAutor.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, userSession.getAllAutorIds()));
        sCategoria.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, userSession.getAllCategoriasIds()));

        bSaveFrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frase = String.valueOf(etTexto.getText());
                fecha = String.valueOf(etFechaProgramada.getText());
                autor = Integer.parseInt(String.valueOf(sIdAutor.getSelectedItemId()));
                categoria = Integer.parseInt(String.valueOf(sCategoria.getSelectedItemId()));
                autor += 1;
                categoria += 1;
                Log.i(MainActivity.class.getSimpleName(), "ID autor instruducido : " +autor);
                Log.i(MainActivity.class.getSimpleName(), "ID categoria instruducido : " +categoria);


                if(isValid(String.valueOf(fecha))){
                    addFrase(frase,fecha,autor,categoria);
                }else{
                    Toast.makeText(AddFrase.this,"Date not valid (yyyy-MM-dd)", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }
    public void addFrase(String frase, String fecha, int idAutor, int idCategoria) {

        Log.i(MainActivity.class.getSimpleName(), "Añadiendo frase ...");
        Autor autor = userSession.getAutorById(idAutor);
        Categoria categoria = userSession.getCategoriaById(idCategoria);


        Frase fraseadd = new Frase(userSession.getLastIdUserFrases(),frase, fecha, autor, categoria);
        apiService.addFrase(fraseadd).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        Log.i(MainActivity.class.getSimpleName(), "Frase añadida correctamente");
                        Toast.makeText(AddFrase.this, "Frase Saved!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(AddFrase.this, "Frase not added, make sure the date is not repeating", Toast.LENGTH_SHORT).show();
                        Log.i(MainActivity.class.getSimpleName(), "Error al añadir la frase");

                        Log.i(MainActivity.class.getSimpleName(), response.raw().toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(AddFrase.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }




    /**
     * VChecks if a string date is valid with the format yyyy-MM-dd
     * @param date
     * @return
     */
    private boolean isValid(String date){
        DateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

}
