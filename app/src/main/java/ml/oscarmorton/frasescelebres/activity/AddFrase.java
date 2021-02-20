package ml.oscarmorton.frasescelebres.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ml.oscarmorton.frasescelebres.R;
import ml.oscarmorton.frasescelebres.UserSession;
import ml.oscarmorton.frasescelebres.fragments.basic.FrasesFragment;

public class AddFrase  extends AppCompatActivity {
    private Spinner sIdAutor, sCategoria;
    private UserSession userSession;
    private EditText etFechaProgramada, etTexto;
    private Button bSaveFrase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfrase);

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
                Toast.makeText(AddFrase.this,"Saved!", Toast.LENGTH_SHORT).show();
            }
        });




    }
}
