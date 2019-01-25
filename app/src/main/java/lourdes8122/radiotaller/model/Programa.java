package lourdes8122.radiotaller.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Relation;
import lourdes8122.radiotaller.model.Horario;

@Entity(tableName = "programas")
public class Programa {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;
    private Boolean activo;

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
}
