# Planificador de eventos (TP POO)

Aplicación de escritorio desarrollada únicamente con los contenidos vistos en la materia de Programación Orientada a Objetos (Java, Swing, colecciones, archivos de texto, hilos, excepciones, patrones GRASP/SOLID). Permite planificar eventos de distinto tamaño, gestionando asistentes, recursos y un calendario visual.

## Estructura
src/
 └─ eventos/
     ├─ modelo/
     │   ├─ Evento.java        // entidad principal, sobrecarga de constructores
     │   ├─ Asistente.java     // registro de asistentes y feedback
     │   └─ Recurso.java       // recursos con Set para evitar duplicados
     ├─ controlador/
     │   ├─ ControladorEventos.java // lógica + persistencia en data/eventos.txt (List/Map/ordenamiento)
     └─ vista/
         └─ VentanaEventos.java // interfaz Swing, calendario y formularios
data/
 └─ eventos.txt         // base en texto (puede quedar vacía)
Funcionalidad destacada
Registro, edición y visualización de eventos (lista y detalle).
Formularios para asistentes y recursos asociados a cada evento.
Calendario mensual gráfico basado en Map<LocalDate, List<Evento>>.
Persistencia total en archivos de texto, sin bases de datos ni librerías externas.
