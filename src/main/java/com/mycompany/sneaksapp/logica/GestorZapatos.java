package com.mycompany.sneaksapp.logica;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;


public class GestorZapatos {
    private List<Zapato> zapatos;
    private final File archivoDatos;
    

    public GestorZapatos() {
        zapatos = new ArrayList<>();
        archivoDatos = new File("src/main/java/com/mycompany/sneaksapp/persistencia/datosTabla.csv");
        cargarDatosDesdeArchivo();
    }
    
    
    public List<Zapato> getZapatos() {
        return zapatos;
    }

    public void addZapato(Zapato zapato) {
        zapatos.add(zapato);
        guardarDatosEnArchivo();
    }

    public void removeZapato(Zapato zapato) {
        zapatos.remove(zapato);
        guardarDatosEnArchivo();
    }
    
    public void eliminarZapatos(List<Integer> zapatoIds) {
        zapatos.removeIf(zapato -> zapatoIds.contains(zapato.getShoeid()));
        guardarDatosEnArchivo();
    }

    public void guardarDatosEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivoDatos))) {
        for (Zapato zapato : zapatos) {
            pw.println(zapato.getShoeid() + "," + zapato.getBrand() + "," + zapato.getModel() + "," + 
                       zapato.getSize() + "," + zapato.getColor() + "," + zapato.getUbication() + "," + 
                       zapato.getPrice() + "," + zapato.getImage());
        }
    } catch (IOException e) {
        e.printStackTrace();
        
    }
    }

    private void cargarDatosDesdeArchivo() {
    try (BufferedReader br = new BufferedReader(new FileReader(archivoDatos))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            // Asegúrate de que haya suficientes datos en la línea para crear un Zapato
            if(datos.length >= 8) {
                int shoeid = Integer.parseInt(datos[0]); // Lee el ID desde el archivo
                Zapato zapato = new Zapato(
                        datos[1], // Marca
                        datos[2], // Modelo
                        Byte.parseByte(datos[3]), // Talla
                        datos[4], // Color
                        datos[5], // Ubicación
                        Integer.parseInt(datos[6]), // Precio
                        datos[7]  // Imagen
                );
                zapato.setShoeid(shoeid); // Establece el ID del zapato con el valor leído
                zapatos.add(zapato);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}