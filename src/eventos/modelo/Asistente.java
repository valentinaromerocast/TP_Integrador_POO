package eventos.modelo;

/**
 * Representa a una persona registrada para asistir a un evento.
 */
public class Asistente extends Persona {

    private boolean confirmoAsistencia;
    private String retroalimentacion;

    public Asistente(String nombre, String correoElectronico) {
        this(nombre, correoElectronico, false, "");
    }

    public Asistente(String nombre, String correoElectronico, boolean confirmoAsistencia, String retroalimentacion) {
        super(nombre, correoElectronico);
        this.confirmoAsistencia = confirmoAsistencia;
        this.retroalimentacion = retroalimentacion == null ? "" : retroalimentacion;
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
                escapar(getNombre()),
                escapar(getCorreoElectronico()),
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
        return descripcionDetallada();
    }

    @Override
    public String descripcionDetallada() {
        String estado = confirmoAsistencia ? "Confirmado" : "Pendiente";
        if (retroalimentacion.isBlank()) {
            return descripcionBasica() + " - " + estado;
        }
        return descripcionBasica() + " - " + estado + " - Feedback: " + retroalimentacion;
    }
}
