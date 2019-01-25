package lourdes8122.radiotaller.model;

import java.sql.Date;
import java.time.LocalTime;
import java.time.temporal.ChronoField;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Entity(primaryKeys = {"dayOfWeek","time"}, tableName = "horarios",
        foreignKeys = @ForeignKey(entity = Programa.class, parentColumns = "id", childColumns = "programa_id"),
        indices = {@Index(value = {"programa_id"})}
)
@TypeConverters(value = TimeConverter.class)
public class Horario {
    @NonNull
    private int dayOfWeek;
    @NonNull
    private Date time;

    @ColumnInfo(name = "programa_id")
    private int programaId;

    public Horario(int dayOfWeek, Date time, int programaId){
        if(dayOfWeek > 7 || dayOfWeek < 1) throw new DayOfWeekError();
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this.programaId = programaId;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        if(dayOfWeek > 7 || dayOfWeek < 1) throw new DayOfWeekError();

        this.dayOfWeek = dayOfWeek;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getProgramaId() {
        return programaId;
    }

    public void setProgramaId(int programaId) {
        this.programaId = programaId;
    }
}


