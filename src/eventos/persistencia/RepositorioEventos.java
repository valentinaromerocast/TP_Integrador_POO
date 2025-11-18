package eventos.persistencia;

import eventos.modelo.Evento;

import java.util.List;

/**
 * Abstracción de la fuente de datos de eventos para favorecer la modularidad.
 */
public interface RepositorioEventos {

    /**
     * Carga todos los eventos persistidos.
     *
     * @return lista de eventos
     */
    List<Evento> cargar();

    /**
     * Persiste los eventos provistos.
     *
     * @param eventos eventos a almacenar
     */
    void guardar(List<Evento> eventos);
}
