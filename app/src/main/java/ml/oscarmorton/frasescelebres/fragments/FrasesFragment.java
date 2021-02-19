package ml.oscarmorton.frasescelebres.fragments;


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
import ml.oscarmorton.frasescelebres.adaptors.AdaptorFrases;
import ml.oscarmorton.frasescelebres.listeners.IFrasesListener;
import ml.oscarmorton.frasescelebres.model.Frase;

public class FrasesFragment extends Fragment {
    private static final String KEY = "KEY_BUNDLE";
    public static final String KEY_FRASES = "KEY_FRASES";

    private ArrayList<Frase> frases;
    private RecyclerView rvListFrases;
    private IFrasesListener listener;
    private UserSession userSession;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        userSession = (UserSession) getArguments().getSerializable(KEY_FRASES);
        frases = userSession.getFrases();
        return inflater.inflate(R.layout.fragment_frases,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rvListFrases = getView().findViewById(R.id.rvListFrases);
        rvListFrases.setAdapter(new AdaptorFrases(frases,listener));
        rvListFrases.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));



    }

    public void setFrasesListener(IFrasesListener listener) {
        this.listener = listener;
    }

    public ArrayList<Frase> getDatos() {
        return frases;
    }
}
