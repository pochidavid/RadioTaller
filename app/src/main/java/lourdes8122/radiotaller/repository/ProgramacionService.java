package lourdes8122.radiotaller.repository;

import java.util.List;

import lourdes8122.radiotaller.model.Programa;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ProgramacionService {
    @GET("programas")
    Call<List<Programa>> getProgramas();

    @GET("horarios")
    Call<List<Programa>> getHorarios();

}
