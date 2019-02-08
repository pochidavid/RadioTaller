package lourdes8122.radiotaller.model;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "programas")
public class Programa {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;
    private Boolean activo;

    @Ignore
    transient private List<Horario> horarios = new ArrayList<>();

    @Ignore
    public Programa(String nombre){
        this.nombre = nombre;
        this.activo = true;
    }

    @Ignore
    public Programa(String nombre, Boolean activo){
        this.nombre = nombre;
        this.activo = activo;
    }

    public Programa(int id, String nombre, Boolean activo){
        this.id = id;
        this.nombre = nombre;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Ignore
    public List<Horario> getHorarios() {
        return horarios == null? new ArrayList<>() : horarios;
    }

    public void setHorarios(@Nullable List<Horario> horarios) {
        this.horarios = horarios == null? new ArrayList<>() : horarios;
    }

    public void addHorario(@Nullable Horario h){
        if(h != null) horarios.add(h);
    }

    public void addHorarios(@Nullable List<Horario> horarios){
        if(horarios != null) horarios.addAll(horarios);
    }

    @NonNull
    @Override
    public String toString() {
        return "("+id+") Programa: "+nombre+", Activo: "+activo;
    }
}
