package lourdes8122.radiotaller.model;

public class Usuario {

    private int Id;
    private String Nombre;
    private String Apellido;
    private String FechaNacimiento;
    private String Pais;
    private String Ciudad;
    private String Provincia;
    private String Correo;

    public Usuario(int id, String nombre, String apellido, String fechaNacimiento, String pais, String ciudad, String provincia, String correo) {
        Id = id;
        Nombre = nombre;
        Apellido = apellido;
        FechaNacimiento = fechaNacimiento;
        Pais = pais;
        Ciudad = ciudad;
        Provincia = provincia;
        Correo = correo;
    }

    public Usuario() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        FechaNacimiento = fechaNacimiento;
    }

    public String getPais() {
        return Pais;
    }

    public void setPais(String pais) {
        Pais = pais;
    }

    public String getCiudad() {
        return Ciudad;
    }

    public void setCiudad(String ciudad) {
        Ciudad = ciudad;
    }

    public String getProvincia() {
        return Provincia;
    }

    public void setProvincia(String provincia) {
        Provincia = provincia;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }
}
