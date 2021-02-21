package ml.oscarmorton.frasescelebres.fragments.basic;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ml.oscarmorton.frasescelebres.R;
import ml.oscarmorton.frasescelebres.UserSession;
import ml.oscarmorton.frasescelebres.activity.AddFrase;
import ml.oscarmorton.frasescelebres.activity.MainActivity;
import ml.oscarmorton.frasescelebres.activity.SettingsActivity;
import ml.oscarmorton.frasescelebres.adaptors.AdaptorFrases;
import ml.oscarmorton.frasescelebres.interfacess.listeners.IFrasesListener;
import ml.oscarmorton.frasescelebres.model.Frase;

public class FrasesFragment extends Fragment implements  View.OnClickListener {
    private static final String KEY = "KEY_BUNDLE";
    public static final String KEY_FRASES = "KEY_FRASES";
    public static final String KEY_TYPE = "KEY_TYPE";
    public static final String KEY_ID = "KEY_ID";
    public static final String KEY_ADD_FRASE = "KEY_ADD_FRASE";
    public static final String KEY_USER_PERMISION_FRASES = "KEY_USER_PERMISION_FRASES";



    public enum SeachType{
        AUTOR,CATEGORIA, ALL
    }


    private ArrayList<Frase> frases;
    private RecyclerView rvListFrases;
    private IFrasesListener listener;
    private UserSession userSession;
    private int type;
    private int id;
    private int userPermision;
    private FloatingActionButton fabFrases;
    private View view;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        id = 0;
        userPermision = 0;
        userSession = (UserSession) getArguments().getSerializable(KEY_FRASES);
        type = -1;
        type = getArguments().getInt(KEY_TYPE);
        userPermision = getArguments().getInt(KEY_USER_PERMISION_FRASES);


        //If we are searching or a specific author/category, we get the correct list
        if(type == 1){
            id = getArguments().getInt(FrasesFragment.KEY_ID);
            frases = userSession.getFrasesFromAutorId(id + 1 );
        }else if(type == 2){
            id = getArguments().getInt(FrasesFragment.KEY_ID);
            frases = userSession.getFrasesFromCategorisId(id + 1);
        }else{
            frases = userSession.getFrases();

        }


        view = inflater.inflate(R.layout.fragment_frases, container, false);
        fabFrases = view.findViewById(R.id.fabFrases);
        fabFrases.setOnClickListener(this);

        // If the user is not an admin, we hide the add button
        if(userPermision == 0){
            fabFrases.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rvListFrases = getView().findViewById(R.id.rvListFrases);
        rvListFrases.setAdapter(new AdaptorFrases(frases,listener));
        rvListFrases.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));



    }
    @Override
    public void onClick(View v) {

        Intent  addFraseIntent = new Intent(getContext(), AddFrase.class);
        addFraseIntent.putExtra(FrasesFragment.KEY_ADD_FRASE, userSession);
        startActivity(addFraseIntent);


    }


    public void setFrasesListener(IFrasesListener listener) {
        this.listener = listener;
    }

    public ArrayList<Frase> getDatos() {
        return frases;
    }
}