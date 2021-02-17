package ml.oscarmorton.frasescelebres.rest;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import ml.oscarmorton.frasescelebres.interfacess.IAPIService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static IAPIService instance;
    private static  String baseURL = "http://192.168.1.38:8090";
    private static SharedPreferences preferences;
    private static String ip;
    private static String port;



    /** Lo hacemos privado para evitar que se puedan crear instancias de esta forma */
    private RestClient() {

    }

    public static synchronized IAPIService getInstance(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);


        StringBuilder sb  = new StringBuilder();
        ip = "";
        port = "";
        ip = preferences.getString("ipPath", "192.168.1.1");
        port = preferences.getString("portPath", "8090");

        sb.append("http://");
        sb.append(ip);
        sb.append(":");
        sb.append(port);


        if(instance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(sb.toString())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            instance = retrofit.create(IAPIService.class);
        }
        return instance;
    }
}
