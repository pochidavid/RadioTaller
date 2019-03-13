package lourdes8122.radiotaller.model;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;


@Entity(primaryKeys = {"dayOfWeek","time"}, tableName = "horarios",
        foreignKeys = @ForeignKey(entity = Programa.class, parentColumns = "id", childColumns = "programa_id"),
        indices = {@Index(value = {"programa_id"})}
)
public class Horario {
    @NonNull
    private int dayOfWeek;
    @NonNull
    private Date time;

    @ColumnInfo(name = "programa_id")
    private int programaId;

    public Horario(int dayOfWeek, Date time, int programaId){
        if(dayOfWeek > 8 || dayOfWeek < 1) throw new DayOfWeekError();
        this.dayOfWeek = dayOfWeek;
        this.time = time;
        this.programaId = programaId;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        if(dayOfWeek > 8 || dayOfWeek < 1) throw new DayOfWeekError();

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

    public String getDayOfWeekString(){
        switch (dayOfWeek){
            case 0: return "error";
            case 1: return "Domingo";
            case 2: return "Lunes";
            case 3: return "Martes";
            case 4: return "Miércoles";
            case 5: return "Jueves";
            case 6: return "Viernes";
            case 7: return "Sábado";
            case 8: return "Lunes a Sábado";
            default: return null;
        }
    }

    public String getTimeString(){

        if(time.getMinutes()==0) return time.getHours()+" hs";
        else return time.getHours()+":"+time.getMinutes()+" hs";
    }

    @NonNull
    @Override
    public String toString() {
        return "Programa: "+programaId+ " " + dayOfWeek +" | "+getDayOfWeekString()+" "+getTimeString();
    }
}


