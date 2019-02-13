package lourdes8122.radiotaller.repository;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import lourdes8122.radiotaller.model.Horario;
import lourdes8122.radiotaller.model.Programa;

@Database(entities = {Programa.class,Horario.class},version = 1)
@TypeConverters(value = {Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProgramacionDao programacionDao();
    public abstract SubscripcionesDao subscripcionesDao();
}
