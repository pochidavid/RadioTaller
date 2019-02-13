package lourdes8122.radiotaller.id;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import lourdes8122.radiotaller.ProgramacionViewModel;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ProgramacionViewModel.class)
    abstract ViewModel bindProgramacionViewModel(ProgramacionViewModel programacionViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
