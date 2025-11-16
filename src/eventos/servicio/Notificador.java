package eventos.servicio;

import eventos.modelo.Evento;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Tarea en segundo plano que revisa cada cierto tiempo los eventos próximos y
 * registra recordatorios simples en un archivo de texto.
 */
public class Notificador implements Runnable {

    private final GestorEventos gestorEventos;
    private final Path bitacoraNotificaciones;
    private final long intervaloMilisegundos;
    private final AtomicBoolean activo = new AtomicBoolean(true);

    public Notificador(GestorEventos gestorEventos, Path bitacoraNotificaciones, long intervaloMilisegundos) {
        this.gestorEventos = gestorEventos;
        this.bitacoraNotificaciones = bitacoraNotificaciones;
        this.intervaloMilisegundos = intervaloMilisegundos;
    }

    public void detener() {
        activo.set(false);
    }

    @Override
    public void run() {
        try {
            if (!Files.exists(bitacoraNotificaciones)) {
                Files.createDirectories(bitacoraNotificaciones.getParent());
                Files.createFile(bitacoraNotificaciones);
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo preparar la bitácora de notificaciones", e);
        }

        while (activo.get()) {
            revisarEventos();
            try {
                Thread.sleep(intervaloMilisegundos);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void revisarEventos() {
        List<Evento> proximos = gestorEventos.eventosProximos();
        LocalDateTime ahora = LocalDateTime.now();
        for (Evento evento : proximos) {
            long horasFaltantes = Duration.between(ahora, evento.getFechaHora()).toHours();
            if (horasFaltantes >= 0 && horasFaltantes <= 24) {
                registrarRecordatorio(evento, horasFaltantes);
            }
        }
    }

    private void registrarRecordatorio(Evento evento, long horasFaltantes) {
        String mensaje = LocalDateTime.now() + " - Recordatorio: " + evento.getNombre() +
                " inicia en " + horasFaltantes + "h";
        try {
            Files.writeString(bitacoraNotificaciones, mensaje + System.lineSeparator(),
                    java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("No se pudo escribir recordatorio: " + e.getMessage());
        }
    }
}
