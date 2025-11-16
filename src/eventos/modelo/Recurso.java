package eventos.modelo;

import java.util.Objects;

/**
 * Representa un recurso necesario para un evento (salón, equipo, catering, etc.).
 */
public class Recurso {

    private final String tipo;
    private final String descripcion;
    private final int cantidad;

    public Recurso(String tipo, String descripcion, int cantidad) {
        this.tipo = Objects.requireNonNull(tipo, "El tipo es obligatorio");
        this.descripcion = descripcion == null ? "" : descripcion;
        this.cantidad = Math.max(1, cantidad);
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String aToken() {
        return String.join("^", tipo, descripcion.replace("^", " "), Integer.toString(cantidad));
    }

    public static Recurso desdeToken(String token) {
        String[] partes = token.split("\\^", -1);
        if (partes.length < 3) {
            return new Recurso("Recurso", "Sin descripción", 1);
        }
        return new Recurso(partes[0], partes[1], Integer.parseInt(partes[2]));
    }

    @Override
    public String toString() {
        return tipo + " - " + descripcion + " (x" + cantidad + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipo.toLowerCase(), descripcion.toLowerCase());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Recurso otro)) return false;
        return tipo.equalsIgnoreCase(otro.tipo) && descripcion.equalsIgnoreCase(otro.descripcion);
    }
}
