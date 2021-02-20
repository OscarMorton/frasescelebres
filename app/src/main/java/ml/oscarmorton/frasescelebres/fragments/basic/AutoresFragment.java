package ml.oscarmorton.frasescelebres.fragments.basic;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ml.oscarmorton.frasescelebres.R;
import ml.oscarmorton.frasescelebres.UserSession;
import ml.oscarmorton.frasescelebres.adaptors.AdaptorAutor;
import ml.oscarmorton.frasescelebres.adaptors.AdaptorFrases;
import ml.oscarmorton.frasescelebres.interfacess.listeners.IAutorListener;
import ml.oscarmorton.frasescelebres.interfacess.listeners.IFrasesListener;
import ml.oscarmorton.frasescelebres.model.Autor;
import ml.oscarmorton.frasescelebres.model.Frase;

public class AutoresFragment extends Fragment {
    private static final String KEY = "KEY_BUNDLE";
    public static final String KEY_AUTORES = "KEY_AUTORES";

    private ArrayList<Autor> autores;
    private RecyclerView rvListAutores;
    private IAutorListener listener;
    private UserSession userSession;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        userSession = (UserSession) getArguments().getSerializable(KEY_AUTORES);
        autores = userSession.getAutores();
        return inflater.inflate(R.layout.fragment_autores,container,false);
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
}