package coopetica.presentacion;

import coopetica.logica.AsociadoServicio;
import coopetica.modelo.Asociado;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

public class VentanaPrincipal extends JFrame {

    private AsociadoServicio servicio;

    private JTextField campoCedula;
    private JTextField campoNombre;
    private JTextField campoSaldo;
    private JTextField campoBuscar;

    private JButton botonRegistrar;
    private JButton botonListar;
    private JButton botonResumen;
    private JButton botonBuscar;

    private JLabel etiquetaNombreAutor;
    private JLabel etiquetaEstado;
    private JTextArea areaResultados;

    public VentanaPrincipal(AsociadoServicio servicio) {
        this.servicio = servicio;

        setTitle("CoopeTica - Jeremy Angulo Ovares");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        campoCedula = new JTextField(10);
        campoNombre = new JTextField(15);
        campoSaldo = new JTextField(8);
        campoBuscar = new JTextField(10);

        botonRegistrar = new JButton("Registrar");
        botonListar = new JButton("Listar");
        botonResumen = new JButton("Resumen");
        botonBuscar = new JButton("Buscar");

        etiquetaNombreAutor = new JLabel("Estudiante: Jeremy Angulo Ovares");
        etiquetaEstado = new JLabel(" ");

        areaResultados = new JTextArea(12, 50);
        areaResultados.setEditable(false);

        JPanel panelFormulario = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFormulario.add(new JLabel("Cedula:"));
        panelFormulario.add(campoCedula);
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(campoNombre);
        panelFormulario.add(new JLabel("Saldo:"));
        panelFormulario.add(campoSaldo);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.add(botonRegistrar);
        panelBotones.add(botonListar);
        panelBotones.add(botonResumen);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.add(new JLabel("Buscar por cedula:"));
        panelBusqueda.add(campoBuscar);
        panelBusqueda.add(botonBuscar);

        JPanel panelNorte = new JPanel(new GridLayout(4, 1));
        panelNorte.add(etiquetaNombreAutor);
        panelNorte.add(panelFormulario);
        panelNorte.add(panelBotones);
        panelNorte.add(panelBusqueda);

        add(panelNorte, BorderLayout.NORTH);
        add(new JScrollPane(areaResultados), BorderLayout.CENTER);
        add(etiquetaEstado, BorderLayout.SOUTH);

        botonRegistrar.addActionListener(e -> registrar());
        botonListar.addActionListener(e -> listar());
        botonResumen.addActionListener(e -> mostrarResumen());
        botonBuscar.addActionListener(e -> buscar());

        pack();
        setLocationRelativeTo(null);
    }

    private void registrar() {
        try {
            String cedula = campoCedula.getText().trim();
            String nombre = campoNombre.getText().trim();
            double saldo = Double.parseDouble(campoSaldo.getText().trim());

            Asociado nuevo = new Asociado(cedula, nombre, saldo);
            String mensaje = servicio.registrar(nuevo);

            if (mensaje.equals("OK")) {
                campoCedula.setText("");
                campoNombre.setText("");
                campoSaldo.setText("");
                campoCedula.requestFocus();
                mostrarEstado("Asociado registrado correctamente", Color.BLUE);
                JOptionPane.showMessageDialog(this,
                        "Asociado registrado correctamente",
                        "Registro", JOptionPane.INFORMATION_MESSAGE);
                listar();
            } else {
                mostrarEstado(mensaje, Color.RED);
                JOptionPane.showMessageDialog(this, mensaje,
                        "No se pudo registrar", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            mostrarEstado("El saldo debe ser un numero.", Color.RED);
            JOptionPane.showMessageDialog(this,
                    "El saldo debe ser un numero.",
                    "No se pudo registrar", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo acceder al archivo de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listar() {
        try {
            ArrayList<Asociado> lista = servicio.listar();
            String texto = "";
            for (int i = 0; i < lista.size(); i++) {
                texto = texto + formatear(lista.get(i)) + "\n";
            }
            areaResultados.setText(texto);
            mostrarEstado("Asociados listados: " + lista.size(), Color.BLUE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo leer el archivo de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarResumen() {
        try {
            int cantidad = servicio.obtenerCantidadAsociados();
            double total = servicio.obtenerSumaAportes();
            JOptionPane.showMessageDialog(this,
                    "Asociados registrados: " + cantidad
                    + "\nTotal de aportes: " + total,
                    "Resumen", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo leer el archivo de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscar() {
        try {
            Asociado encontrado = servicio.buscarPorCedula(campoBuscar.getText().trim());
            if (encontrado == null) {
                areaResultados.setText("");
                mostrarEstado("No existe un asociado con esa cedula", Color.RED);
            } else {
                areaResultados.setText(formatear(encontrado));
                mostrarEstado("Asociado encontrado", Color.BLUE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo leer el archivo de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatear(Asociado a) {
        return "Cedula: " + a.getCedula()
                + " | Nombre: " + a.getNombre()
                + " | Aportes: " + a.getSaldoAportes();
    }

    private void mostrarEstado(String texto, Color color) {
        etiquetaEstado.setText(texto);
        etiquetaEstado.setForeground(color);
    }
}
