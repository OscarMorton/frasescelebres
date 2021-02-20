package ml.oscarmorton.frasescelebres.fragments.basic;


import android.os.Bundle;
import android.util.Log;
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
import ml.oscarmorton.frasescelebres.interfacess.listeners.IFrasesListener;
import ml.oscarmorton.frasescelebres.model.Frase;

public class FrasesFragment extends Fragment {
    private static final String KEY = "KEY_BUNDLE";
    public static final String KEY_FRASES = "KEY_FRASES";
    public static final String KEY_TYPE = "KEY_TYPE";
    public static final String KEY_ID = "KEY_ID";

    public enum SeachType{
        AUTOR,CATEGORIA, NONE
    }


    private ArrayList<Frase> frases;
    private RecyclerView rvListFrases;
    private IFrasesListener listener;
    private UserSession userSession;
    private int type;
    private int id;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        id = 0;
        userSession = (UserSession) getArguments().getSerializable(KEY_FRASES);
        type = -1;
        type = getArguments().getInt(KEY_TYPE);
        Log.d("TESTFRAGMENT", "TYPE :  " + type);
        //TODO TEST THIS
        if(type == 1){
            id = getArguments().getInt(FrasesFragment.KEY_ID);

            frases = userSession.getFrasesFromAutorId(id);
        }else if(type == 2){
            id = getArguments().getInt(FrasesFragment.KEY_ID);
            frases = userSession.getFrasesFromCategorisId(id);
        }else{
            frases = userSession.getFrases();

        }





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