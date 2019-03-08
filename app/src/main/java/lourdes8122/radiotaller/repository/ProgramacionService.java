package lourdes8122.radiotaller.repository;

import java.util.List;

import lourdes8122.radiotaller.model.Horario;
import lourdes8122.radiotaller.model.Programa;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProgramacionService {
    @GET("programas.php")
    Call<List<Programa>> getProgramas();

    @GET("horarios.php")
    Call<List<Horario>> getHorarios();

    @GET("horario.php")
    Call<List<Horario>> getHorarios(@Query("programaid") int programaId);

    @POST("programas.php")
    Call<Programa> savePrograma(@Body Programa programa);

    @POST("horarios.php")
    Call<Horario> saveHorario(@Body Horario horario);
}
