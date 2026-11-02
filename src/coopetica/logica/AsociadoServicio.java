package coopetica.logica;

import coopetica.datos.AsociadoArchivoDAO;
import coopetica.modelo.Asociado;
import java.io.IOException;
import java.util.ArrayList;

public class AsociadoServicio {

    private AsociadoArchivoDAO dao;

    public AsociadoServicio() throws IOException {
        this.dao = new AsociadoArchivoDAO();
    }

    public ArrayList<Asociado> listar() throws IOException {
        return dao.listar();
    }

    public String registrar(Asociado a)
            throws IOException {

        if (a.getCedula().isEmpty() || a.getNombre().isEmpty()) {
            return "La cedula y el nombre son obligatorios.";
        }

        
        if (a.getSaldoAportes() < 0) {
            return "El saldo no puede ser negativo.";
        }

        for (Asociado asociado : dao.listar()) {
            if (asociado.getCedula().equals(a.getCedula())) {
                return "Ya existe un asociado con esa cedula.";
            }
        }

        dao.agregar(a);
        return "OK";
    }
    //Esta es la cantidad de asociados
    public int obtenerCantidadAsociados() throws IOException {
        ArrayList<Asociado> lista = dao.listar();
        return lista.size();
    }
    //Esta es la suam de todos los aportes
    public double obtenerSumaAportes() throws IOException {
        ArrayList<Asociado> lista = dao.listar();
        double total = 0;
        for (Asociado a : lista) {
            total += a.getSaldoAportes();
        }
        return total;
    }
    //Aqui se busca a una persona por cedula y devuelve null si no existe
    public Asociado buscarPorCedula(String cedulaBuscada) throws IOException {
        ArrayList<Asociado> lista = dao.listar();
        for (Asociado a : lista) {
            if (a.getCedula().trim().equalsIgnoreCase(cedulaBuscada.trim())) {
                return a;
            }
        }
        return null;
    }
}