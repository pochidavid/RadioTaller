package lourdes8122.radiotaller.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import lourdes8122.radiotaller.model.Programa;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class AppRepository {
    private final ProgramacionService programacionService;
    private final ProgramacionDao programacionDao;
    private final SubscripcionesDao subscripcionesDao;
    private final Executor executor;

    @Inject
    public AppRepository(
            ProgramacionService programacionService,
            ProgramacionDao programacionDao,
            SubscripcionesDao subscripcionesDao,
            Executor executor){

        this.programacionService = programacionService;
        this.programacionDao = programacionDao;
        this.subscripcionesDao = subscripcionesDao;
        this.executor = executor;

    }

    public LiveData<List<Programa>> getProgramas() {
        final MutableLiveData<List<Programa>> data = new MutableLiveData<>();

        programacionService.getProgramas().enqueue(new Callback<List<Programa>>() {
            @Override
            public void onResponse(Call<List<Programa>> call, Response<List<Programa>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Programa>> call, Throwable t) {

            }

        });

        return data;
    }
}
