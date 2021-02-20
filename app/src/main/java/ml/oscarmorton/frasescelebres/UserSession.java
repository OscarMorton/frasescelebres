package ml.oscarmorton.frasescelebres;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import ml.oscarmorton.frasescelebres.activity.MainActivity;
import ml.oscarmorton.frasescelebres.interfacess.IAPIService;
import ml.oscarmorton.frasescelebres.model.Autor;
import ml.oscarmorton.frasescelebres.model.Categoria;
import ml.oscarmorton.frasescelebres.model.Frase;
import ml.oscarmorton.frasescelebres.rest.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSession implements Serializable {

    private ArrayList<Frase> frases;
    private ArrayList<Autor> autores;
    private ArrayList<Categoria> categorias;
    transient IAPIService apiService;
    private static int lastIDFrases;

    public UserSession(Context context) {
        this.frases = new ArrayList<>();
        this.autores = new ArrayList<>();
        this.categorias = new ArrayList<>();
        this.apiService = RestClient.getInstance(context);
        getFrasesStart();
        getAutoresStart();
        getCategoriasStart();

        //TODO GET BACK TO THIS

        Log.d("TESTINGSIZE", String.valueOf(frases.size()));


    }



    public void getAutoresStart() {
        apiService.getAutores().enqueue(new Callback<List<Autor>>() {
            @Override
            public void onResponse(Call<List<Autor>> call, Response<List<Autor>> response) {
                if (response.isSuccessful()) {
                    autores = new ArrayList<>(response.body());
                    // Once fouud, I add the frases to the users session
                    setAutores(autores);


                    for (int i = 0; i < autores.size(); i++) {
                        Log.d(MainActivity.class.getSimpleName(), "Autores " + i + ":" + autores.get(i).toString());
                    }
                    Log.i(MainActivity.class.getSimpleName(), "gotAutores");

                } else {
                    Log.i(MainActivity.class.getSimpleName(), "Get frases not succesful");

                }
            }

            @Override
            public void onFailure(Call<List<Autor>> call, Throwable t) {
                // Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getCategoriasStart() {
        apiService.getCategoria().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful()) {
                    categorias = new ArrayList<>(response.body());
                    // Once fouud, I add the frases to the users session
                    setCategorias(categorias);


                    for (int i = 0; i < categorias.size(); i++) {
                        Log.d(MainActivity.class.getSimpleName(), "Categorias " + i + ":" + categorias.get(i).toString());
                    }
                    Log.i(MainActivity.class.getSimpleName(), "GotCategorias");

                } else {
                    Log.i(MainActivity.class.getSimpleName(), "Get frases not succesful");

                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                //  Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getFrasesStart() {
        apiService.getFrases().enqueue(new Callback<List<Frase>>() {
            @Override
            public void onResponse(Call<List<Frase>> call, Response<List<Frase>> response) {
                if (response.isSuccessful()) {
                    frases = new ArrayList<>(response.body());
                    lastIDFrases = frases.get(frases.size() -1).getId();
                    // Once fouud, I add the frases to the users session
                    setFrases(frases);


                    for (int i = 0; i < frases.size(); i++) {
                        Log.d(MainActivity.class.getSimpleName(), "Frase " + i + ":" + frases.get(i).toString());
                    }
                    Log.i(MainActivity.class.getSimpleName(), "GotFrases");

                } else {
                    Log.i(MainActivity.class.getSimpleName(), "Get frases not succesful");

                }
            }

            @Override
            public void onFailure(Call<List<Frase>> call, Throwable t) {
                // Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


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

    private boolean checkIdInArray(ArrayList<Integer> ids, int idToFind) {
        boolean inArray = false;
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i) == idToFind) {
                inArray = true;
            }
        }
        return inArray;
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

    public Autor getAutorById(int id) {

        Autor autor = null;

        for (int i = 0; i < autores.size(); i++) {
            if (autores.get(i).getId() == id) {
                autor = autores.get(i);

            }
        }
        return autor;
    }

    public Categoria getCategoriaById(int id) {

        Categoria categoria = null;

        for (int i = 0; i < categorias.size(); i++) {
            if (categorias.get(i).getId() == id) {
                categoria = categorias.get(i);
            }
        }
        return categoria;
    }

    public int getLastIdUserFrases() {

        int aux = lastIDFrases;

        lastIDFrases++;

        return aux;
    }


    public ArrayList<String> getAllAutorIds() {
        ArrayList<String> ids = new ArrayList<>();
        Log.d(UserSession.class.getSimpleName(), "got ids ");

        for (int i = 0; i < autores.size(); i++) {
            ids.add(String.valueOf(autores.get(i).getId()));
            Log.d(UserSession.class.getSimpleName(), "got id: " + String.valueOf(autores.get(i).getId()));
        }
        return ids;
    }

    public ArrayList<String> getAllCategoriasIds() {
        ArrayList<String> ids = new ArrayList<>();
        Log.d(UserSession.class.getSimpleName(), "got ids ");

        for (int i = 0; i < autores.size(); i++) {
            ids.add(String.valueOf(categorias.get(i).getId()));
            Log.d(UserSession.class.getSimpleName(), "got id: " + String.valueOf(categorias.get(i).getId()));
        }
        return ids;
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
