package eventos.modelo;

import java.util.Objects;

/**
 * Abstracción para cualquier persona dentro del sistema.
 * Centraliza las validaciones y garantiza encapsulamiento de los datos personales.
 */
public abstract class Persona implements DescripcionDetallable {

    private final String nombre;
    private final String correoElectronico;

    protected Persona(String nombre, String correoElectronico) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
        this.correoElectronico = Objects.requireNonNull(correoElectronico, "El correo no puede ser nulo");
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    /**
     * Devuelve una descripción básica reutilizable por las subclases.
     */
    protected String descripcionBasica() {
        return nombre + " (" + correoElectronico + ")";
    }
}
