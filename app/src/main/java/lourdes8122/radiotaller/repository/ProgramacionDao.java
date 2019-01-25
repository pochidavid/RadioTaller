package lourdes8122.radiotaller.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import lourdes8122.radiotaller.model.Horario;
import lourdes8122.radiotaller.model.Programa;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ProgramacionDao {

    @Insert(onConflict = REPLACE)
    void save(Programa programa);

    @Insert(onConflict = REPLACE)
    void save(Horario horario);

    @Query("SELECT * FROM programas WHERE id = :programaId")
    LiveData<Programa> loadPrograma(int programaId);

    @Query("SELECT * FROM programas")
    LiveData<List<Programa>> getProgramas();

    @Query("SELECT * FROM horarios")
    LiveData<List<Horario>> getHorarios();

    @Query("SELECT * FROM horarios WHERE programa_id = :programaId")
    LiveData<List<Horario>> getHorariosByPrograma(int programaId);

    @Insert(onConflict = REPLACE)
    void insertAll(Programa ... programas);

    @Insert(onConflict = REPLACE)
    void insertAll(Horario ... horarios);

    @Delete
    void delete(Programa programa);

    @Delete
    void delete(Horario horario);

    @Insert(onConflict = REPLACE)
    void insertAll(List<Programa> body);
}
