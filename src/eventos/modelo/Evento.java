package eventos.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Entidad principal que concentra la información de cada evento.
 */
public class Evento {

    public static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final String id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaHora;
    private String ubicacion;
    private final List<Asistente> asistentes;
    private final Set<Recurso> recursos;

    public Evento(String nombre, String descripcion, LocalDateTime fechaHora, String ubicacion) {
        this(UUID.randomUUID().toString(), nombre, descripcion, fechaHora, ubicacion,
                new ArrayList<>(), new LinkedHashSet<>());
    }

    public Evento(String id,
                  String nombre,
                  String descripcion,
                  LocalDateTime fechaHora,
                  String ubicacion,
                  List<Asistente> asistentes,
                  Set<Recurso> recursos) {
        this.id = Objects.requireNonNull(id, "El identificador es obligatorio");
        this.nombre = Objects.requireNonNull(nombre, "El nombre es obligatorio");
        this.descripcion = descripcion == null ? "" : descripcion;
        this.fechaHora = Objects.requireNonNull(fechaHora, "La fecha y hora son obligatorias");
        this.ubicacion = Objects.requireNonNull(ubicacion, "La ubicación es obligatoria");
        this.asistentes = new ArrayList<>(Objects.requireNonNullElseGet(asistentes, ArrayList::new));
        this.recursos = new LinkedHashSet<>(Objects.requireNonNullElseGet(recursos, LinkedHashSet::new));
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public List<Asistente> getAsistentes() {
        return Collections.unmodifiableList(asistentes);
    }

    public Set<Recurso> getRecursos() {
        return Collections.unmodifiableSet(recursos);
    }

    public void agregarAsistente(Asistente asistente) {
        asistentes.add(Objects.requireNonNull(asistente, "El asistente es obligatorio"));
    }

    public void agregarRecurso(Recurso recurso) {
        recursos.add(Objects.requireNonNull(recurso, "El recurso es obligatorio"));
    }

    public LocalDate obtenerDia() {
        return fechaHora.toLocalDate();
    }

    public String resumenParticipacion() {
        long confirmados = asistentes.stream().filter(Asistente::confirmoAsistencia).count();
        return nombre + ": " + confirmados + "/" + asistentes.size() + " asistentes confirmados";
    }

    public String aLinea() {
        String asistentesCodificados = asistentes.stream()
                .map(Asistente::aToken)
                .collect(Collectors.joining(";"));
        String recursosCodificados = recursos.stream()
                .map(Recurso::aToken)
                .collect(Collectors.joining(";"));
        return String.join("|",
                id,
                limpiar(nombre),
                limpiar(descripcion),
                fechaHora.format(FORMATO),
                limpiar(ubicacion),
                asistentesCodificados,
                recursosCodificados);
    }

    public static Evento desdeLinea(String linea) {
        String[] partes = linea.split("\\|", -1);
        if (partes.length < 7) {
            throw new IllegalArgumentException("Línea inválida: " + linea);
        }
        List<Asistente> asistentes = new ArrayList<>();
        if (!partes[5].isEmpty()) {
            for (String token : partes[5].split(";")) {
                if (!token.isBlank()) {
                    asistentes.add(Asistente.desdeToken(token));
                }
            }
        }
        Set<Recurso> recursos = new LinkedHashSet<>();
        if (!partes[6].isEmpty()) {
            for (String token : partes[6].split(";")) {
                if (!token.isBlank()) {
                    recursos.add(Recurso.desdeToken(token));
                }
            }
        }

        return new Evento(
                partes[0],
                partes[1],
                partes[2],
                LocalDateTime.parse(partes[3], FORMATO),
                partes[4],
                asistentes,
                recursos);
    }

    private static String limpiar(String valor) {
        return valor.replace("|", "/");
    }

    @Override
    public String toString() {
        return nombre + " - " + fechaHora.format(FORMATO) + " - " + ubicacion;
    }
}
