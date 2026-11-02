import coopetica.logica.AsociadoServicio;
import coopetica.presentacion.VentanaPrincipal;
import java.io.IOException;
import javax.swing.JOptionPane;

public class main {
    public static void main(String[] args) {
        try {
            AsociadoServicio servicio = new AsociadoServicio();
            VentanaPrincipal ventana = new VentanaPrincipal(servicio);
            ventana.setVisible(true);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo abrir el archivo de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
