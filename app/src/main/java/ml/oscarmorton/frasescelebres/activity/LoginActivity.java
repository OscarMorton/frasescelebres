package ml.oscarmorton.frasescelebres.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ml.oscarmorton.frasescelebres.DB.DBHelper;
import ml.oscarmorton.frasescelebres.R;
import ml.oscarmorton.frasescelebres.TipoUsuario;


public class LoginActivity extends AppCompatActivity {

    private Button bLogin;

    private DBHelper dbHelper;
    private EditText username, password;
    private Button bLoadFrases;
    private TipoUsuario tipoUsuario;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bLogin = findViewById(R.id.bLogin);
        username = findViewById(R.id.username1);
        password = findViewById(R.id.password1);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(LoginActivity.this, MainActivity.class);
                dbHelper = DBHelper.getInstance(LoginActivity.this);
                tipoUsuario = dbHelper.checkusernamepassword(username.getText().toString(), password.getText().toString());

                switch (tipoUsuario){
                    case ADMIN:
                        intent.putExtra("usuarioTipo", "admin");
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "Admin activado", Toast.LENGTH_SHORT).show();

                        break;
                    case NOADMIN:
                        intent.putExtra("usuarioTipo", "user");
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "Usuario activado", Toast.LENGTH_SHORT).show();

                        break;

                    case INCORRECTO:
                        Toast.makeText(LoginActivity.this, "Usuario Incorrecto", Toast.LENGTH_SHORT).show();

                        break;
                }



            }


        });



    }
}
