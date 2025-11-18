# Planificador de eventos (TP POO)

Permite planificar eventos de distinto tamaño, gestionando asistentes, recursos y un calendario visual.

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
 
## Funcionalidades y detalles:
- Registro y gestión de eventos: la vista Swing permite crear/editar eventos con nombre, descripción, fecha/hora y ubicación, validando entradas y persistiendo los cambios a través del controlador MVC.
- Visualización y UI: existe lista ordenada de eventos, formulario central y detalle, más un calendario mensual navegable que muestra eventos por día.
- Registro de asistentes y vista detallada: se agregan asistentes con confirmación y correo, visibles en el detalle del evento; se manejan también recursos por evento.
- Gestión de recursos y calendario (funcionalidades adicionales): Recurso usa un Set para evitar duplicados y se persiste junto con cada evento, mientras que el calendario agrupa eventos por día mediante Map<LocalDate, List<Evento>>.
- Persistencia en archivos de texto: el controlador carga/guarda en data/eventos.txt, serializando eventos, asistentes y recursos.
- Clases y alcance: se utilizan cinco clases (Evento, Asistente, Recurso, ControladorEventos, VentanaEventos), cumpliendo el rango de entre 4 y 6 clases y usando acoplamiento/cohesión adecuada.
