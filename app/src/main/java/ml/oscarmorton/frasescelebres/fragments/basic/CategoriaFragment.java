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
import ml.oscarmorton.frasescelebres.adaptors.AdaptorCategoria;
import ml.oscarmorton.frasescelebres.adaptors.AdaptorFrases;
import ml.oscarmorton.frasescelebres.interfacess.listeners.ICategoriaListener;
import ml.oscarmorton.frasescelebres.interfacess.listeners.IFrasesListener;
import ml.oscarmorton.frasescelebres.model.Categoria;
import ml.oscarmorton.frasescelebres.model.Frase;

public class CategoriaFragment extends Fragment {
    private static final String KEY = "KEY_BUNDLE";
    public static final String KEY_CATEGORIA = "KEY_CATEGORIA";

    private ArrayList<Categoria> categorias;
    private RecyclerView rvCategoriaList;
    private ICategoriaListener listener;
    private UserSession userSession;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        userSession = (UserSession) getArguments().getSerializable(KEY_CATEGORIA);
        categorias = userSession.getCategoria();
        return inflater.inflate(R.layout.fragment_categorias,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rvCategoriaList = getView().findViewById(R.id.rvListCategoria);
        rvCategoriaList.setAdapter(new AdaptorCategoria(categorias,listener));
        rvCategoriaList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));



    }

    public void setCategoriaListener(ICategoriaListener listener) {
        this.listener = listener;
    }

    public ArrayList<Categoria> getDatos() {
        return categorias;
    }
}