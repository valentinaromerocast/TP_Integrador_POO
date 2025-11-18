package eventos.persistencia;

import eventos.modelo.Evento;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Implementación basada en archivos de texto plano.
 */
public class RepositorioEventosArchivo implements RepositorioEventos {

    private final Path archivoDatos;

    public RepositorioEventosArchivo(Path archivoDatos) {
        this.archivoDatos = archivoDatos;
    }

    @Override
    public List<Evento> cargar() {
        try {
            if (!Files.exists(archivoDatos)) {
                Files.createDirectories(archivoDatos.getParent());
                Files.createFile(archivoDatos);
                return new ArrayList<>();
            }
            List<Evento> eventos = new ArrayList<>();
            try (Stream<String> lineas = Files.lines(archivoDatos)) {
                lineas.filter(linea -> !linea.isBlank())
                        .map(Evento::desdeLinea)
                        .forEach(eventos::add);
            }
            return eventos;
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo cargar el archivo de eventos", e);
        }
    }

    @Override
    public void guardar(List<Evento> eventos) {
        try {
            List<String> lineas = eventos.stream()
                    .map(Evento::aLinea)
                    .toList();
            Files.write(archivoDatos, lineas);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar el archivo de eventos", e);
        }
    }
}
