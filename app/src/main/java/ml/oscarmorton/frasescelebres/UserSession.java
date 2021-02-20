package ml.oscarmorton.frasescelebres;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

import ml.oscarmorton.frasescelebres.model.Autor;
import ml.oscarmorton.frasescelebres.model.Categoria;
import ml.oscarmorton.frasescelebres.model.Frase;

public class UserSession implements Serializable {

    private ArrayList<Frase> frases;
    private ArrayList<Autor> autores;
    private ArrayList<Categoria> categorias;

    public UserSession(ArrayList<Frase> frases) {
        this.frases = frases;
    }


    public ArrayList<Frase> getFrases() {
        return frases;
    }

    public void setFrases(ArrayList<Frase> frases) {
        this.frases = frases;
    }


    // Getters for the authors and categories
    public ArrayList<Autor> getAutores() {
        int currentID = -1;
        ArrayList<Integer> idsAutores = new ArrayList<Integer>();

        ArrayList<Autor> autores = new ArrayList<>();
        for (int i = 0; i < frases.size(); i++) {
            currentID = frases.get(i).getAutor().getId();
            Log.d("TESTAUTHORS", frases.get(i).getAutor().toString());

           if(!checkIdInArray(idsAutores, currentID)){
               idsAutores.add(currentID);
                autores.add(frases.get(i).getAutor());
               Log.d("TESTAUTHORS", "TEST IF: " + frases.get(i).getAutor().toString());

           }


        }
        return autores;
    }

    private boolean checkIdInArray(ArrayList<Integer> ids, int idToFind){
        boolean inArray = false;
        for(int i = 0; i < ids.size(); i++){
            if(ids.get(i) == idToFind){
                inArray = true;
            }
        }
        return  inArray;
    }



    public ArrayList<Categoria> getCategoria() {
        ArrayList<Categoria> categorias = new ArrayList<>();
        for (int i = 0; i < frases.size(); i++) {
            categorias.add(frases.get(i).getCategoria());

        }
        return categorias;
    }


    public ArrayList<Frase> getFrasesFromAutorId(int id) {

        ArrayList<Frase> frasesReturn = new ArrayList<>();
        for (int i = 0; i < frases.size(); i++) {
            if (frases.get(i).getAutor().getId() == id) {
                frasesReturn.add(frases.get(i));
            }

        }
        return frasesReturn;
    }

    public ArrayList<Frase> getFrasesFromCategorisId(int id) {

        ArrayList<Frase> frasesReturn = new ArrayList<>();
        for (int i = 0; i < frases.size(); i++) {
            if (frases.get(i).getCategoria().getId() == id) {
                frasesReturn.add(frases.get(i));
            }

        }
        return frasesReturn;
    }


}
