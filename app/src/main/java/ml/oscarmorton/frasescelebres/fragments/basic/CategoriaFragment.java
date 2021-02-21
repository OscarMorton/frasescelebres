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
import ml.oscarmorton.frasescelebres.activity.AddCategoria;
import ml.oscarmorton.frasescelebres.activity.AddFrase;
import ml.oscarmorton.frasescelebres.adaptors.AdaptorCategoria;
import ml.oscarmorton.frasescelebres.adaptors.AdaptorFrases;
import ml.oscarmorton.frasescelebres.interfacess.listeners.ICategoriaListener;
import ml.oscarmorton.frasescelebres.interfacess.listeners.IFrasesListener;
import ml.oscarmorton.frasescelebres.model.Categoria;
import ml.oscarmorton.frasescelebres.model.Frase;

public class CategoriaFragment extends Fragment implements View.OnClickListener {
    private static final String KEY = "KEY_BUNDLE";
    public static final String KEY_CATEGORIA = "KEY_CATEGORIA";
    public static final String KEY_USER_PERMISION_CATEGORIA = "KEY_USER_PERMISION_CATEGORIA";
    public static final String KEY_ADD_CATEGORY = "KEY_ADD_CATEGORY";


    private FloatingActionButton fabCategoria;
    private ArrayList<Categoria> categorias;
    private RecyclerView rvCategoriaList;
    private ICategoriaListener listener;
    private UserSession userSession;
    private View view;
    private int userPermision;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        userPermision = 0;
        userSession = (UserSession) getArguments().getSerializable(KEY_CATEGORIA);
        categorias = userSession.getCategorias();

        view = inflater.inflate(R.layout.fragment_categorias, container, false);
        fabCategoria = view.findViewById(R.id.fabCategoria);
        fabCategoria.setOnClickListener(this);

        userPermision = getArguments().getInt(KEY_USER_PERMISION_CATEGORIA);

        // If the user is not an admin, we hide the add button
        if(userPermision == 0){
            fabCategoria.setVisibility(View.INVISIBLE);
        }


        return view;
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

    @Override
    public void onClick(View v) {
        Intent aAddCategory = new Intent(getContext(), AddCategoria.class);
        aAddCategory.putExtra(CategoriaFragment.KEY_ADD_CATEGORY, userSession);
        startActivity(aAddCategory);

    }
}