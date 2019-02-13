package lourdes8122.radiotaller;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import lourdes8122.radiotaller.model.Programa;
import lourdes8122.radiotaller.repository.AppRepository;

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

        loadProgramasFromRepo();
    }

    public LiveData<List<Programa>> getProgramas() {
        return programas;
    }

    private void loadProgramasFromRepo(){
        programas = appRepo.getProgramas();
    }
}
