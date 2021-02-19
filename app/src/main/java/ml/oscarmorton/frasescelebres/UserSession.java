package ml.oscarmorton.frasescelebres;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

import ml.oscarmorton.frasescelebres.model.Frase;

public class UserSession implements Serializable {

    private ArrayList<Frase> frases;

    public UserSession(ArrayList<Frase> frases) {
        this.frases = frases;
    }


    public ArrayList<Frase> getFrases() {
        return frases;
    }

    public void setFrases(ArrayList<Frase> frases) {
        this.frases = frases;
    }
}
