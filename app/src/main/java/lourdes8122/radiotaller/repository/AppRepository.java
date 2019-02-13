package lourdes8122.radiotaller.repository;

import android.util.Log;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;

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
            Executor executor) {

        this.programacionService = programacionService;
        this.programacionDao = programacionDao;
        this.subscripcionesDao = subscripcionesDao;
        this.executor = executor;

    }

    /*
    public LiveData<List<Programa>> getProgramas() {
        refreshProgramacion();

        final MutableLiveData<List<Programa>> data = new MutableLiveData<>();



        //Busca los horarios y espera a que se complete la tarea
        CompletionService<String> service
                = new ExecutorCompletionService<>(executor);

        List<Programa> programas = new ArrayList<>();

        service.submit( ()->{
            programas.addAll(programacionDao.getProgramas().getValue());
        },"Programas Cargados");

        try {
            if (service.take().isDone())
                Log.v("Radio Repository", "Programas Cargados \n" + programas);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Programa p : programas) {

            service.submit(
                    () -> {
                        List<Horario> horarios = new ArrayList<>();
                        horarios.addAll(programacionDao.getHorariosByPrograma(p.getId()).getValue());
                        p.setHorarios(horarios);


                    }, "Horarios Cargados");
            try {
                if (service.take().isDone())
                    Log.v("Radio Repository", "Horarios Cargados \n" + p.getHorarios());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        data.setValue(programas);

        return data;
    }

    */

    public LiveData<List<Programa>> getProgramas() {
        final MutableLiveData<List<Programa>> data = new MutableLiveData<>();

        programacionService.getProgramas().enqueue(new Callback<List<Programa>>() {
            @Override
            public void onResponse(Call<List<Programa>> call, Response<List<Programa>> response) {
                System.out.println(response.body());
                List<Programa> programas = response.body();

                //Busca los horarios y espera a que se complete la tarea
                CompletionService<String> service
                        = new ExecutorCompletionService<>(executor);
                for(Programa p: programas){

                    service.submit(
                            new Runnable() {
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
                            },"Horarios Cargados");
                    try {
                        if(service.take().isDone()) Log.v("Radio Repository","Horarios Cargados \n"+p.getHorarios());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                data.setValue(programas);
            }

            @Override
            public void onFailure(Call<List<Programa>> call, Throwable t) {

            }

        });

        return data;
    }

    public LiveData<List<Horario>> getHorarios() {
        refreshProgramacion();

        return programacionDao.getHorarios();
    }

    public LiveData<List<Horario>> getHorarios(int idPrograma) {
        refreshProgramacion();

        return programacionDao.getHorariosByPrograma(idPrograma);
    }

    public void refreshProgramacion() {
        executor.execute(() -> {
            try {
                Response<List<Programa>> programasResponse = programacionService.getProgramas().execute();
                programacionDao.insertAllProgramas(programasResponse.body());

                Response<List<Horario>> horariosResponse = programacionService.getHorarios().execute();
                programacionDao.insertAllHorarios(horariosResponse.body());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
