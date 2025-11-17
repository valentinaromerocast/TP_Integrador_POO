package eventos.vista;

import eventos.modelo.Asistente;
import eventos.modelo.Evento;
import eventos.modelo.Recurso;
import eventos.controlador.ControladorEventos;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Vista Swing que materializa la capa de presentación del MVC.
 */
public class VentanaEventos extends JFrame {

    private final ControladorEventos controladorEventos;
    private final DefaultListModel<Evento> modeloEventos;
    private final JList<Evento> listaEventos;
    private final JTextArea areaDetalle;
    private final JTextField campoNombre;
    private final JTextArea campoDescripcion;
    private final JTextField campoFecha;
    private final JTextField campoHora;
    private final JTextField campoUbicacion;
    private final JTextField campoNombreAsistente;
    private final JTextField campoCorreoAsistente;
    private final JCheckBox checkConfirmado;
    private final JTextField campoTipoRecurso;
    private final JTextField campoDescripcionRecurso;
    private final JSpinner spinnerCantidadRecurso;
    private final JTable tablaCalendario;
    private final JLabel etiquetaMesCalendario;
    private LocalDate mesVisible;

    public VentanaEventos() {
        super("Planificador de eventos");
        this.controladorEventos = new ControladorEventos(Path.of("data", "eventos.txt"));
        this.modeloEventos = new DefaultListModel<>();
        this.listaEventos = new JList<>(modeloEventos);
        this.areaDetalle = new JTextArea(12, 30);
        this.campoNombre = new JTextField(20);
        this.campoDescripcion = new JTextArea(3, 20);
        this.campoFecha = new JTextField("2025-01-01", 10);
        this.campoHora = new JTextField("10:00", 5);
        this.campoUbicacion = new JTextField(15);
        this.campoNombreAsistente = new JTextField(10);
        this.campoCorreoAsistente = new JTextField(10);
        this.checkConfirmado = new JCheckBox("Confirmado");
        this.campoTipoRecurso = new JTextField(10);
        this.campoDescripcionRecurso = new JTextField(10);
        this.spinnerCantidadRecurso = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        this.tablaCalendario = new JTable(new DefaultTableModel(6, 7));
        this.etiquetaMesCalendario = new JLabel();
        this.mesVisible = LocalDate.now().withDayOfMonth(1);

        construirVentana();
        cargarEventos();
    }

    private void construirVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(panelListaEventos(), BorderLayout.WEST);
        add(panelFormularioEvento(), BorderLayout.CENTER);
        add(panelLateralDerecho(), BorderLayout.EAST);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel panelListaEventos() {
        JPanel panel = new JPanel(new BorderLayout());
        listaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEventos.addListSelectionListener(this::mostrarDetalleEvento);
        panel.add(new JLabel("Eventos (ordenados por fecha)"), BorderLayout.NORTH);
        panel.add(new JScrollPane(listaEventos), BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(300, 500));
        return panel;
    }

    private JPanel panelFormularioEvento() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel formulario = new JPanel(new GridLayout(0, 2, 5, 5));
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del evento"));
        formulario.add(new JLabel("Nombre:"));
        formulario.add(campoNombre);
        formulario.add(new JLabel("Descripción:"));
        formulario.add(new JScrollPane(campoDescripcion));
        formulario.add(new JLabel("Fecha (yyyy-MM-dd):"));
        formulario.add(campoFecha);
        formulario.add(new JLabel("Hora (HH:mm):"));
        formulario.add(campoHora);
        formulario.add(new JLabel("Ubicación:"));
        formulario.add(campoUbicacion);

        JButton btnGuardar = new JButton("Guardar/Actualizar");
        btnGuardar.addActionListener(e -> guardarEvento());
        formulario.add(btnGuardar);

        JButton btnLimpiar = new JButton("Limpiar formulario");
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        formulario.add(btnLimpiar);

        panel.add(formulario, BorderLayout.NORTH);

        areaDetalle.setEditable(false);
        areaDetalle.setBorder(BorderFactory.createTitledBorder("Detalle del evento seleccionado"));
        panel.add(new JScrollPane(areaDetalle), BorderLayout.CENTER);
        return panel;
    }

    private JPanel panelLateralDerecho() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(panelAsistentes());
        panel.add(panelRecursos());
        panel.add(panelCalendario());
        return panel;
    }

    private JPanel panelAsistentes() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Registrar asistente"));
        panel.add(new JLabel("Nombre:"));
        panel.add(campoNombreAsistente);
        panel.add(new JLabel("Correo:"));
        panel.add(campoCorreoAsistente);
        panel.add(checkConfirmado);
        JButton btnAgregar = new JButton("Agregar asistente");
        btnAgregar.addActionListener(e -> agregarAsistente());
        panel.add(btnAgregar);
        return panel;
    }

    private JPanel panelRecursos() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Gestionar recursos"));
        panel.add(new JLabel("Tipo:"));
        panel.add(campoTipoRecurso);
        panel.add(new JLabel("Descripción:"));
        panel.add(campoDescripcionRecurso);
        panel.add(new JLabel("Cantidad:"));
        panel.add(spinnerCantidadRecurso);
        JButton btnAgregar = new JButton("Asignar recurso");
        btnAgregar.addActionListener(e -> agregarRecurso());
        panel.add(btnAgregar);
        return panel;
    }

    private JPanel panelCalendario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Calendario mensual"));
        JPanel encabezado = new JPanel();
        JButton btnAnterior = new JButton("<");
        JButton btnSiguiente = new JButton(">");
        btnAnterior.addActionListener(e -> {
            mesVisible = mesVisible.minusMonths(1);
            actualizarCalendario();
        });
        btnSiguiente.addActionListener(e -> {
            mesVisible = mesVisible.plusMonths(1);
            actualizarCalendario();
        });
        encabezado.add(btnAnterior);
        encabezado.add(etiquetaMesCalendario);
        encabezado.add(btnSiguiente);
        panel.add(encabezado, BorderLayout.NORTH);

        tablaCalendario.setEnabled(false);
        String[] dias = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        DefaultTableModel modeloTabla = (DefaultTableModel) tablaCalendario.getModel();
        modeloTabla.setColumnIdentifiers(dias);
        panel.add(new JScrollPane(tablaCalendario), BorderLayout.CENTER);
        actualizarCalendario();
        return panel;
    }

    private void cargarEventos() {
        modeloEventos.clear();
        for (Evento evento : controladorEventos.obtenerEventosOrdenados()) {
            modeloEventos.addElement(evento);
        }
        if (!modeloEventos.isEmpty()) {
            listaEventos.setSelectedIndex(0);
        }
        actualizarCalendario();
    }

    private void mostrarDetalleEvento(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) {
            return;
        }
        Evento seleccionado = listaEventos.getSelectedValue();
        if (seleccionado == null) {
            areaDetalle.setText("");
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("Nombre: ").append(seleccionado.getNombre()).append("\n")
                .append("Descripción: ").append(seleccionado.getDescripcion()).append("\n")
                .append("Fecha y hora: ").append(seleccionado.getFechaHora().format(Evento.FORMATO)).append("\n")
                .append("Ubicación: ").append(seleccionado.getUbicacion()).append("\n\n")
                .append("Asistentes:\n");
        for (Asistente asistente : seleccionado.getAsistentes()) {
            builder.append(" - ").append(asistente.toString()).append("\n");
        }
        builder.append("\nRecursos:\n");
        for (Recurso recurso : seleccionado.getRecursos()) {
            builder.append(" - ").append(recurso.toString()).append("\n");
        }
        areaDetalle.setText(builder.toString());

        campoNombre.setText(seleccionado.getNombre());
        campoDescripcion.setText(seleccionado.getDescripcion());
        campoFecha.setText(seleccionado.getFechaHora().toLocalDate().toString());
        campoHora.setText(seleccionado.getFechaHora().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        campoUbicacion.setText(seleccionado.getUbicacion());
    }

    private void guardarEvento() {
        try {
            if (campoNombre.getText().isBlank()) {
                throw new IllegalArgumentException("El nombre es obligatorio");
            }
            if (campoUbicacion.getText().isBlank()) {
                throw new IllegalArgumentException("La ubicación es obligatoria");
            }
            LocalDate fecha = LocalDate.parse(campoFecha.getText().trim());
            LocalTime hora = LocalTime.parse(campoHora.getText().trim(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);

            Evento seleccionado = listaEventos.getSelectedValue();
            if (seleccionado == null) {
                Evento nuevo = new Evento(
                        campoNombre.getText().trim(),
                        campoDescripcion.getText().trim(),
                        fechaHora,
                        campoUbicacion.getText().trim());
                controladorEventos.registrarEvento(nuevo);
            } else {
                seleccionado.setNombre(campoNombre.getText().trim());
                seleccionado.setDescripcion(campoDescripcion.getText().trim());
                seleccionado.setFechaHora(fechaHora);
                seleccionado.setUbicacion(campoUbicacion.getText().trim());
                controladorEventos.actualizarEvento(seleccionado);
            }
            cargarEventos();
            limpiarFormulario();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Datos inválidos: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        campoNombre.setText("");
        campoDescripcion.setText("");
        campoFecha.setText("2025-01-01");
        campoHora.setText("10:00");
        campoUbicacion.setText("");
        listaEventos.clearSelection();
    }

    private void agregarAsistente() {
        Evento seleccionado = listaEventos.getSelectedValue();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un evento primero");
            return;
        }
        if (campoNombreAsistente.getText().isBlank() || campoCorreoAsistente.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Complete nombre y correo del asistente");
            return;
        }
        Asistente asistente = new Asistente(
                campoNombreAsistente.getText().trim(),
                campoCorreoAsistente.getText().trim(),
                checkConfirmado.isSelected(),
                "");
        controladorEventos.registrarAsistente(seleccionado.getId(), asistente);
        cargarEventos();
        campoNombreAsistente.setText("");
        campoCorreoAsistente.setText("");
        checkConfirmado.setSelected(false);
    }

    private void agregarRecurso() {
        Evento seleccionado = listaEventos.getSelectedValue();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un evento primero");
            return;
        }
        if (campoTipoRecurso.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "El tipo de recurso es obligatorio");
            return;
        }
        int cantidad = (int) spinnerCantidadRecurso.getValue();
        Recurso recurso = new Recurso(
                campoTipoRecurso.getText().trim(),
                campoDescripcionRecurso.getText().trim(),
                cantidad);
        controladorEventos.asignarRecurso(seleccionado.getId(), recurso);
        cargarEventos();
        campoTipoRecurso.setText("");
        campoDescripcionRecurso.setText("");
        spinnerCantidadRecurso.setValue(1);
    }

    private void actualizarCalendario() {
        etiquetaMesCalendario.setText(mesVisible.getMonth().toString() + " " + mesVisible.getYear());
        DefaultTableModel modeloTabla = (DefaultTableModel) tablaCalendario.getModel();
        for (int fila = 0; fila < modeloTabla.getRowCount(); fila++) {
            for (int col = 0; col < modeloTabla.getColumnCount(); col++) {
                modeloTabla.setValueAt("", fila, col);
            }
        }

        Map<LocalDate, List<Evento>> calendario = controladorEventos.calendarioPorDia();
        LocalDate fechaCursor = mesVisible;
        int diasMes = mesVisible.lengthOfMonth();
        DayOfWeek primerDiaSemana = mesVisible.getDayOfWeek();
        int columna = (primerDiaSemana.getValue() + 6) % 7; // lunes=1 => 0
        int fila = 0;

        for (int dia = 1; dia <= diasMes; dia++) {
            LocalDate fecha = fechaCursor.withDayOfMonth(dia);
            StringBuilder contenido = new StringBuilder(Integer.toString(dia));
            List<Evento> eventosDia = calendario.getOrDefault(fecha, List.of());
            if (!eventosDia.isEmpty()) {
                contenido.append("\n").append(eventosDia.get(0).getNombre());
                if (eventosDia.size() > 1) {
                    contenido.append(" (+").append(eventosDia.size() - 1).append(")");
                }
            }
            modeloTabla.setValueAt(contenido.toString(), fila, columna);
            columna++;
            if (columna == 7) {
                columna = 0;
                fila++;
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaEventos().setVisible(true));
    }
}
