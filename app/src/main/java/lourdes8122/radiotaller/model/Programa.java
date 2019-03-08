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
    private int activo;

    @Ignore
    transient private List<Horario> horarios = new ArrayList<>();

    @Ignore
    public Programa(String nombre){
        this.nombre = nombre;
        this.activo = 1;
    }

    @Ignore
    public Programa(String nombre, int activo){
        this.nombre = nombre;
        this.activo = activo;
    }

    public Programa(int id, String nombre, int activo){
        this.id = id;
        this.nombre = nombre;
        this.activo = activo;
    }

    public int getId() {
        return this.id;
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

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    @Ignore
    public List<Horario> getHorarios() {
        if(this.horarios==null) {
            horarios = new ArrayList<>();
        }
        return this.horarios;
    }

    public void setHorarios(@Nullable List<Horario> horarios) {
        if(this.horarios==null) {
            this.horarios = new ArrayList<>();
        }
        this.horarios = horarios;
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
        return "("+this.id+") Programa: "+this.nombre+", Activo: "+this.activo;
    }
}
