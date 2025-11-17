# Planificador de eventos (TP POO)

Permite planificar eventos de distinto tamaño, gestionando asistentes, recursos y un calendario visual, además de emitir notificaciones e informes de participación.

## Estructura
src/
 └─ eventos/
     ├─ modelo/
     │   ├─ Evento.java        // entidad principal, sobrecarga de constructores
     │   ├─ Asistente.java     // registro de asistentes y feedback
     │   └─ Recurso.java       // recursos con Set para evitar duplicados
     ├─ controlador/
     │   ├─ ControladorEventos.java // lógica + persistencia en data/eventos.txt (List/Map/ordenamiento)
     │   └─ NotificadorEventos.java // hilo que genera recordatorios en data/notificaciones.txt
     └─ vista/
         └─ VentanaEventos.java // interfaz Swing, calendario y formularios
data/
 ├─ eventos.txt         // base en texto
 └─ notificaciones.txt  // se genera al iniciar
```

## Funcionalidad destacada

- Registro, edición y visualización de eventos (lista y detalle).
- Formularios para asistentes y recursos asociados a cada evento.
- Calendario mensual gráfico basado en `Map<LocalDate, List<Evento>>`.
- Informe de participación con feedback recopilado
- Persistencia total en archivos de texto

