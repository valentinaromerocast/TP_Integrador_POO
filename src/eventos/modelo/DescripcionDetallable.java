package eventos.modelo;

/**
 * Define el contrato para obtener una representación textual detallada de un objeto del dominio.
 * Permite consumir diferentes tipos de entidades de manera polimórfica en las capas superiores.
 */
public interface DescripcionDetallable {

    /**
     * Devuelve una descripción lista para mostrarse en la capa de presentación.
     *
     * @return representación textual detallada
     */
    String descripcionDetallada();
}
