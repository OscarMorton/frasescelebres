package ml.oscarmorton.frasescelebres.interfacess;

import java.util.List;

import ml.oscarmorton.frasescelebres.model.Autor;
import ml.oscarmorton.frasescelebres.model.Categoria;
import ml.oscarmorton.frasescelebres.model.Frase;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IAPIService {
    @GET("frase/all")
    Call<List<Frase>> getFrases();

    @GET("autor/all")
    Call<List<Autor>> getAutores();

    @GET("categoria/all")
    Call<List<Categoria>> getCategoria();

    @DELETE("autor/delete//{id}")
    Call<Void> deleteAutor(@Path("id") int id);

    @DELETE("categoria/delete//{id}")
    Call<Void> deleteCategoria(@Path("id") int id);

    @DELETE("frase/delete//{id}")
    Call<Void> deleteFrase(@Path("id") int id);



    @POST("frase/add")
    Call<Boolean> addFrase(@Body Frase frase);

    @POST("autor/add")
    Call<Boolean> addAutor(@Body Autor autor);

    @POST("categoria/add")
    Call<Boolean> addCategoria(@Body Categoria categoria);

    @POST("frase/addValues")
    @FormUrlEncoded
    Call<Boolean> addFraseValues(@Field("id") int id,
                                @Field("texto") String texto,
                                 @Field("fechaProgramada") String fechaProgramada,
                                 @Field("idAutor") int idAutor,
                                 @Field("idCategoria")int idCategoria);

}
