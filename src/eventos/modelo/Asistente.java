package eventos.modelo;

import java.util.Objects;

/**
 * Representa a una persona registrada para asistir a un evento.
 */
public class Asistente {

    private final String nombre;
    private final String correoElectronico;
    private boolean confirmoAsistencia;
    private String retroalimentacion;

    public Asistente(String nombre, String correoElectronico) {
        this(nombre, correoElectronico, false, "");
    }

    public Asistente(String nombre, String correoElectronico, boolean confirmoAsistencia, String retroalimentacion) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
        this.correoElectronico = Objects.requireNonNull(correoElectronico, "El correo no puede ser nulo");
        this.confirmoAsistencia = confirmoAsistencia;
        this.retroalimentacion = retroalimentacion == null ? "" : retroalimentacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public boolean confirmoAsistencia() {
        return confirmoAsistencia;
    }

    public void confirmarAsistencia(boolean confirmoAsistencia) {
        this.confirmoAsistencia = confirmoAsistencia;
    }

    public String getRetroalimentacion() {
        return retroalimentacion;
    }

    public void actualizarRetroalimentacion(String retroalimentacion) {
        this.retroalimentacion = retroalimentacion == null ? "" : retroalimentacion;
    }

    public String aToken() {
        return String.join("^",
                escapar(nombre),
                escapar(correoElectronico),
                Boolean.toString(confirmoAsistencia),
                escapar(retroalimentacion));
    }

    public static Asistente desdeToken(String token) {
        String[] partes = token.split("\\^", -1);
        if (partes.length < 4) {
            return new Asistente("Desconocido", "sin-correo", false, "");
        }
        return new Asistente(
                desescapar(partes[0]),
                desescapar(partes[1]),
                Boolean.parseBoolean(partes[2]),
                desescapar(partes[3]));
    }

    private static String escapar(String valor) {
        return valor.replace("^", " ");
    }

    private static String desescapar(String valor) {
        return valor;
    }

    @Override
    public String toString() {
        return nombre + " (" + correoElectronico + ")" + (confirmoAsistencia ? " - Confirmado" : "");
    }
}
