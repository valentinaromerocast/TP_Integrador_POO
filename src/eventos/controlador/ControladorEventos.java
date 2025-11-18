package eventos.controlador;

import eventos.modelo.Asistente;
import eventos.modelo.Evento;
import eventos.modelo.Recurso;
import eventos.persistencia.RepositorioEventos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Actúa como Controlador en la arquitectura MVC aplicando los principios GRASP/SOLID.
 * Contiene la lógica de negocio y delega la persistencia a un repositorio.
 */
public class ControladorEventos {

    private final RepositorioEventos repositorioEventos;
    private final List<Evento> eventos;

    public ControladorEventos(RepositorioEventos repositorioEventos) {
        this.repositorioEventos = repositorioEventos;
        this.eventos = new ArrayList<>();
        cargar();
    }

    public List<Evento> obtenerEventosOrdenados() {
        return eventos.stream()
                .sorted(Comparator.comparing(Evento::getFechaHora))
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

    public void eliminarEvento(String idEvento) {
        boolean eliminado = eventos.removeIf(evento -> evento.getId().equals(idEvento));
        if (!eliminado) {
            throw new IllegalArgumentException("Evento inexistente");
        }
        guardar();
    }

    public Optional<Evento> buscarPorId(String id) {
        return eventos.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }

    private void cargar() {
        eventos.clear();
        eventos.addAll(repositorioEventos.cargar());
    }

    private void guardar() {
        repositorioEventos.guardar(eventos);
    }
}
