package ml.oscarmorton.frasescelebres.fragments.basic;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ml.oscarmorton.frasescelebres.R;
import ml.oscarmorton.frasescelebres.UserSession;
import ml.oscarmorton.frasescelebres.activity.AddAutor;
import ml.oscarmorton.frasescelebres.activity.AddFrase;
import ml.oscarmorton.frasescelebres.adaptors.AdaptorAutor;
import ml.oscarmorton.frasescelebres.adaptors.AdaptorFrases;
import ml.oscarmorton.frasescelebres.interfacess.listeners.IAutorListener;
import ml.oscarmorton.frasescelebres.interfacess.listeners.IFrasesListener;
import ml.oscarmorton.frasescelebres.model.Autor;
import ml.oscarmorton.frasescelebres.model.Frase;

public class AutoresFragment extends Fragment implements  View.OnClickListener {
    private static final String KEY = "KEY_BUNDLE";
    public static final String KEY_AUTORES = "KEY_AUTORES";
    public static final String KEY_ADD_AUTOR = "KEY_ADD_AUTOR";
    public static final String KEY_USER_PERMISION_AUTOR = "KEY_USER_PERMISION_AUTOR";



    private ArrayList<Autor> autores;
    private RecyclerView rvListAutores;
    private IAutorListener listener;
    private UserSession userSession;
    private FloatingActionButton fabAutor;
    private View view;
    private int userPermision;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        userPermision = 0;
        userSession = (UserSession) getArguments().getSerializable(KEY_AUTORES);
        autores = userSession.getAutores();

        view = inflater.inflate(R.layout.fragment_autores, container, false);
        fabAutor = view.findViewById(R.id.fabAutor);
        fabAutor.setOnClickListener(this);


        userPermision = getArguments().getInt(KEY_USER_PERMISION_AUTOR);

        // If the user is not an admin, we hide the add button
        if(userPermision == 0){
            fabAutor.setVisibility(View.INVISIBLE);
        }


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rvListAutores = getView().findViewById(R.id.rvListAutores);
        rvListAutores.setAdapter(new AdaptorAutor(autores,listener));
        rvListAutores.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));



    }

    public void setAutoresListener(IAutorListener listener) {
        this.listener = listener;
    }

    public ArrayList<Autor> getDatos() {
        return autores;
    }

    @Override
    public void onClick(View v) {
        Intent addAutor = new Intent(getContext(), AddAutor.class);
        addAutor.putExtra(AutoresFragment.KEY_ADD_AUTOR, userSession);
        startActivity(addAutor);
    }
}