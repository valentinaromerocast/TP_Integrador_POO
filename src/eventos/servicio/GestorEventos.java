package eventos.servicio;

import eventos.modelo.Asistente;
import eventos.modelo.Evento;
import eventos.modelo.Recurso;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Aplica los principios GRASP (experto de la información) y SOLID (Single Responsibility)
 * centralizando la lógica de negocio y la persistencia básica basada en archivos de texto.
 */
public class GestorEventos {

    private final Path archivoDatos;
    private final List<Evento> eventos;

    public GestorEventos(Path archivoDatos) {
        this.archivoDatos = archivoDatos;
        this.eventos = new ArrayList<>();
        cargar();
    }

    public List<Evento> obtenerEventosOrdenados() {
        return eventos.stream()
                .sorted(Comparator.comparing(Evento::getFechaHora))
                .collect(Collectors.toList());
    }

    public List<Evento> eventosProximos() {
        LocalDateTime ahora = LocalDateTime.now();
        return obtenerEventosOrdenados().stream()
                .filter(e -> !e.getFechaHora().isBefore(ahora))
                .collect(Collectors.toList());
    }

    public List<Evento> eventosPasados() {
        LocalDateTime ahora = LocalDateTime.now();
        return obtenerEventosOrdenados().stream()
                .filter(e -> e.getFechaHora().isBefore(ahora))
                .collect(Collectors.toList());
    }

    public Map<LocalDate, List<Evento>> calendarioPorDia() {
        Map<LocalDate, List<Evento>> calendario = new HashMap<>();
        for (Evento evento : obtenerEventosOrdenados()) {
            calendario.computeIfAbsent(evento.obtenerDia(), clave -> new ArrayList<>()).add(evento);
        }
        return calendario;
    }

    public void registrarEvento(Evento evento) {
        boolean existe = eventos.stream().anyMatch(e -> e.getId().equals(evento.getId()));
        if (existe) {
            throw new IllegalArgumentException("Ya existe un evento con ese identificador");
        }
        eventos.add(evento);
        guardar();
    }

    public void actualizarEvento(Evento eventoModificado) {
        Evento actual = buscarPorId(eventoModificado.getId())
                .orElseThrow(() -> new IllegalArgumentException("Evento inexistente"));
        actual.setNombre(eventoModificado.getNombre());
        actual.setDescripcion(eventoModificado.getDescripcion());
        actual.setFechaHora(eventoModificado.getFechaHora());
        actual.setUbicacion(eventoModificado.getUbicacion());
        guardar();
    }

    public void registrarAsistente(String idEvento, Asistente asistente) {
        Evento evento = buscarPorId(idEvento)
                .orElseThrow(() -> new IllegalArgumentException("Evento inexistente"));
        evento.agregarAsistente(asistente);
        guardar();
    }

    public void asignarRecurso(String idEvento, Recurso recurso) {
        Evento evento = buscarPorId(idEvento)
                .orElseThrow(() -> new IllegalArgumentException("Evento inexistente"));
        evento.agregarRecurso(recurso);
        guardar();
    }

    public String generarInformeParticipacion() {
        StringBuilder builder = new StringBuilder("INFORME DE PARTICIPACIÓN\n");
        for (Evento evento : obtenerEventosOrdenados()) {
            builder.append("- ").append(evento.resumenParticipacion()).append("\n");
            evento.getAsistentes().stream()
                    .filter(a -> !a.getRetroalimentacion().isBlank())
                    .forEach(a -> builder.append("    * ")
                            .append(a.getNombre())
                            .append(": ")
                            .append(a.getRetroalimentacion())
                            .append("\n"));
        }
        return builder.toString();
    }

    public Optional<Evento> buscarPorId(String id) {
        return eventos.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }

    private void cargar() {
        try {
            if (!Files.exists(archivoDatos)) {
                Files.createDirectories(archivoDatos.getParent());
                Files.createFile(archivoDatos);
                return;
            }
            Files.lines(archivoDatos)
                    .filter(linea -> !linea.isBlank())
                    .map(Evento::desdeLinea)
                    .forEach(eventos::add);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo cargar el archivo de eventos", e);
        }
    }

    private void guardar() {
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
