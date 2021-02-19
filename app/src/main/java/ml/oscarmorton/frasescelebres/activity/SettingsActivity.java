package ml.oscarmorton.frasescelebres.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ml.oscarmorton.frasescelebres.fragments.OptionsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new OptionsFragment()).commit();
    }
}