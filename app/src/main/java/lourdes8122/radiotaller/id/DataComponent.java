package lourdes8122.radiotaller.id;

import javax.inject.Singleton;

import dagger.Component;
import lourdes8122.radiotaller.ConfiguracionFragment;
import lourdes8122.radiotaller.ProgramacionFragment;
import lourdes8122.radiotaller.ProgramacionViewModel;
import lourdes8122.radiotaller.RadioTallerApplication;
import lourdes8122.radiotaller.repository.AppRepository;

@Singleton
@Component(modules = {DataModule.class, ViewModelModule.class})
public interface DataComponent {

    void inject(RadioTallerApplication radioTallerApplication);

    void inject(AppRepository appRepository);

    void inject(ProgramacionViewModel programacionViewModel);

    void inject(ProgramacionFragment programacionFragment);

    void inject(ConfiguracionFragment configuracionFragment);
}
