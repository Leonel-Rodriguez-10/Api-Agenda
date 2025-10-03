package com.example.agenda.api;

import com.example.agenda.models.Contacto;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ContactoService {
    @GET("contactos")
    Call<List<Contacto>> listar();

    @POST("contactos")
    Call<Contacto> crear(@Body Contacto c);

    @PUT("contactos/{id}")
    Call<Contacto> actualizar(@Path("id") Long id, @Body Contacto c);

    @DELETE("contactos/{id}")
    Call<Void> eliminar(@Path("id") Long id);
}
