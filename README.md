# Planificador de eventos (TP POO)

Aplicación de escritorio desarrollada únicamente con los contenidos vistos en la materia de Programación Orientada a Objetos (Java, Swing, colecciones, archivos de texto, hilos, excepciones, patrones GRASP/SOLID). Permite planificar eventos de distinto tamaño, gestionando asistentes, recursos y un calendario visual, además de emitir notificaciones e informes de participación.

## Estructura (4‑6 clases)

```
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
 ├─ eventos.txt         // base en texto (puede quedar vacía)
 └─ notificaciones.txt  // se genera al iniciar
```

## Funcionalidad destacada

- Registro, edición y visualización de eventos (lista y detalle).
- Formularios para asistentes y recursos asociados a cada evento.
- Calendario mensual gráfico basado en `Map<LocalDate, List<Evento>>`.
- Persistencia total en archivos de texto, sin bases de datos ni librerías externas.

## Requisitos previos

- Java 17+ (se usan API estándar: `java.time`, `java.nio.file`, Swing).

## Cómo compilar y ejecutar

```bash
cd /workspace
javac -d out $(find src -name '*.java')
java -cp out eventos.vista.VentanaEventos
```

Al primer inicio se crearán automáticamente `data/eventos.txt` y `data/notificaciones.txt` si no existen.

## Archivo de datos

El formato es plano y auto-explicativo (campos separados por `|`, listas por `;` y atributos por `^`). Se puede editar manualmente si se respetan los separadores, o simplemente trabajar desde la interfaz gráfica.