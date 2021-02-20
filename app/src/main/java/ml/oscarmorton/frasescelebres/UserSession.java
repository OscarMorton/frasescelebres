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

    public UserSession() {

    }


    public ArrayList<Frase> getFrases() {
        return frases;
    }

    public void setFrases(ArrayList<Frase> frases) {
        this.frases = frases;
    }


    public ArrayList<Autor> getAutores() {
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




    public void setAutores(ArrayList<Autor> autores) {
        this.autores = autores;
    }

    public ArrayList<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(ArrayList<Categoria> categorias) {
        this.categorias = categorias;
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

    public ArrayList<String> getAllAutorIds(){
      ArrayList<String> ids = new ArrayList<>();
        Log.d(UserSession.class.getSimpleName(), "got ids ");

        for(int i = 0; i < autores.size(); i++){
           ids.add(String.valueOf(autores.get(i).getId()));
            Log.d(UserSession.class.getSimpleName(), "got id: " + String.valueOf(autores.get(i).getId()));
        }
        return  ids;
    }

    public ArrayList<String> getAllCategoriasIds(){
        ArrayList<String> ids = new ArrayList<>();
        Log.d(UserSession.class.getSimpleName(), "got ids ");

        for(int i = 0; i < autores.size(); i++){
            ids.add(String.valueOf(categorias.get(i).getId()));
            Log.d(UserSession.class.getSimpleName(), "got id: " + String.valueOf(categorias.get(i).getId()));
        }
        return  ids;
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
