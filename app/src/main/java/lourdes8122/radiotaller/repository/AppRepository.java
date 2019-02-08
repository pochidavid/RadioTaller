package lourdes8122.radiotaller.repository;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import lourdes8122.radiotaller.model.Horario;
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
                System.out.println(response.body());
                List<Programa> programas = response.body();

                for(Programa p: programas){
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                List<Horario> horarios = new ArrayList<>();
                                horarios.addAll(programacionService.getHorarios(p.getId()).execute().body());
                                p.setHorarios(horarios);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                data.setValue(programas);
            }

            @Override
            public void onFailure(Call<List<Programa>> call, Throwable t) {

            }

        });

        return data;
    }

    public LiveData<List<Horario>> getHorarios(){
        final MutableLiveData<List<Horario>> data = new MutableLiveData<>();

        programacionService.getHorarios().enqueue(new Callback<List<Horario>>() {
            @Override
            public void onResponse(Call<List<Horario>> call, Response<List<Horario>> response) {
                System.out.println(response.body());
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Horario>> call, Throwable t) {

            }

        });

        return data;
    }

    public LiveData<List<Horario>> getHorarios(int idPrograma){
        final MutableLiveData<List<Horario>> data = new MutableLiveData<>();

        programacionService.getHorarios().enqueue(new Callback<List<Horario>>() {
            @Override
            public void onResponse(Call<List<Horario>> call, Response<List<Horario>> response) {
                System.out.println(response.body());
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Horario>> call, Throwable t) {

            }

        });

        return data;
    }

    //Todo: borrar metodos de creacion
    public void savePrograma(Programa programa){
            programacionService.savePrograma(programa).enqueue(new Callback<Programa>() {
                @Override
                public void onResponse(Call<Programa> call, Response<Programa> response) {

                }

                @Override
                public void onFailure(Call<Programa> call, Throwable t) {

                }
            });

    }

    public void saveHorario(Horario horario){
        programacionService.saveHorario(horario).enqueue(new Callback<Horario>() {
            @Override
            public void onResponse(Call<Horario> call, Response<Horario> response) {

            }

            @Override
            public void onFailure(Call<Horario> call, Throwable t) {

            }
        });

    }
}
