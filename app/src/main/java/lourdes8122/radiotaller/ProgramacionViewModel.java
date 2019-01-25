package lourdes8122.radiotaller;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import lourdes8122.radiotaller.repository.AppRepository;
import lourdes8122.radiotaller.model.Programa;

public class ProgramacionViewModel extends ViewModel {
    private LiveData<List<Programa>> programas;
    private AppRepository appRepo;

    // Instructs Dagger 2 to provide the AppRepository parameter.
    @Inject
    public ProgramacionViewModel(AppRepository appRepo) {
        this.appRepo = appRepo;
    }

    public void init() {
        if(this.programas != null){

            return;
        }
        programas = appRepo.getProgramas();
    }

    public LiveData<List<Programa>> getProgramas() {
        return programas;
    }
}
