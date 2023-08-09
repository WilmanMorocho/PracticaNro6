/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import controlador.DAO.ejgrafo.CiudadDao;
import controlador.DAO.ejgrafo.grafos.CiudadGrafo;
import controlador.ed.grafo.busqueda.Floyd;
import controlador.ed.lista.ListaEnlazada;
import controlador.ed.lista.exception.PosicionException;
import controlador.ed.lista.exception.VacioException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.System.Logger.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import modelo.Ciudad;
import modelo.Ruta;
import org.jboss.logging.Logger;
import vista.modelo.ModeloTablaCiudad;
import vista.utilidades.UtilidadesVistaGrafo;

/**
 *
 * @author wilman
 */
public class FrmRuta extends javax.swing.JDialog {

    private CiudadGrafo ciuG = new CiudadGrafo();
    private ModeloTablaCiudad modelo = new ModeloTablaCiudad();
    private CiudadDao ciuD = new CiudadDao();
    private UtilidadesVistaGrafo utilidades = new UtilidadesVistaGrafo();

    /**
     * Creates new form FrmRuta
     */
    public FrmRuta(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        cargarCombo();
        cargarTabla();
        inicializarRutasLista();

    }

    public void cargarTabla() {
        modelo.setLista(ciuD.listar());
        tblTabla.setModel(modelo);
        tblTabla.updateUI();
    }

    public void cargarCombo() {
        try {
            utilidades.cargarCombo(ciuD.listar(), cbxOrigen);
            utilidades.cargarCombo(ciuD.listar(), cbxDestino);
        } catch (Exception e) {
        }
    }

    private ListaEnlazada<Ruta> rutasLista; // Agrega esta variable

    // ... otros atributos y métodos ...
    public void inicializarRutasLista() {
        rutasLista = new ListaEnlazada<>();

        // Ruta del archivo JSON que contiene las rutas
        String rutaArchivoRutas = "data/ruta.json"; // Ajusta la ruta del archivo JSON
        System.out.println(rutaArchivoRutas);

        try (FileReader reader = new FileReader(rutaArchivoRutas)) {
            Gson gson = new Gson();
            JsonObject jsonRoot = gson.fromJson(reader, JsonObject.class);

            JsonObject listaEnlazada = jsonRoot.getAsJsonObject("controlador.ed.lista.ListaEnlazada");
            JsonObject cabecera = listaEnlazada.getAsJsonObject("cabecera");

            // Recorrer la estructura y agregar las rutas a rutasLista
            agregarRutasDesdeJson(cabecera);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void agregarRutasDesdeJson(JsonObject nodo) {
        if (nodo.has("info")) {
            JsonObject info = nodo.getAsJsonObject("info");
            int idCiudadOri = info.get("idCiudadOri").getAsInt();
            String nomCiudadDes = info.get("nomCiudadDes").getAsString();
            double distancia = info.get("distancia").getAsDouble();
            Ruta ruta = new Ruta(idCiudadOri, nomCiudadDes, distancia);
            rutasLista.insertar(ruta);
        }
        if (nodo.has("sig")) {
            JsonObject siguiente = nodo.getAsJsonObject("sig");
            agregarRutasDesdeJson(siguiente);
        }
    }

    private ListaEnlazada<Ciudad> ciudadesLista;
    //private ListaEnlazada<Ruta> rutasLista;

    public void inicializarCiudadesLista() {
        ciudadesLista = new ListaEnlazada<>();

        // Ruta del archivo JSON que contiene las ciudades
        String rutaArchivoCiudades = "data/ciudad.json";

        try (FileReader reader = new FileReader(rutaArchivoCiudades)) {
            Gson gson = new Gson();
            JsonObject jsonRoot = gson.fromJson(reader, JsonObject.class);

            JsonObject listaEnlazada = jsonRoot.getAsJsonObject("controlador.ed.lista.ListaEnlazada");
            JsonObject cabecera = listaEnlazada.getAsJsonObject("cabecera");

            // Recorrer la estructura y agregar las ciudades a ciudadesLista
            agregarCiudadesDesdeJson(cabecera);

            // Configurar los ComboBoxes en tu interfaz
            cbxOrigen.removeAllItems();
            cbxDestino.removeAllItems();
            for (Ciudad ciudad : ciudadesLista.toArray()) {
                cbxOrigen.addItem(ciudad.getNombre()); // Agregar el nombre de la ciudad
                cbxDestino.addItem(ciudad.getNombre()); // Agregar el nombre de la ciudad
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void agregarCiudadesDesdeJson(JsonObject nodo) {
        if (nodo.has("info")) {
            JsonObject info = nodo.getAsJsonObject("info");
            int id = info.get("id").getAsInt();
            String nombre = info.get("nombre").getAsString();
            Ciudad ciudad = new Ciudad(id, nombre);
            ciudadesLista.insertar(ciudad);
        }
        if (nodo.has("sig")) {
            JsonObject siguiente = nodo.getAsJsonObject("sig");
            agregarCiudadesDesdeJson(siguiente);
        }
    }

    private Ciudad encontrarCiudadPorNombre(String nombre) throws VacioException, PosicionException {
        for (int i = 0; i < ciudadesLista.size(); i++) {
            if (ciudadesLista.get(i).getNombre().equals(nombre)) {
                return ciudadesLista.get(i); // Devuelve la ciudad encontrada
            }
        }
        return null; // Si no se encuentra la ciudad
    }

    private void buscarFloyd() throws VacioException, PosicionException {
        String nombreCiudadOrigen = (String) cbxOrigen.getSelectedItem();
        String nombreCiudadDestino = (String) cbxDestino.getSelectedItem();

        Ciudad ciudadOrigen = encontrarCiudadPorNombre(nombreCiudadOrigen);
        Ciudad ciudadDestino = encontrarCiudadPorNombre(nombreCiudadDestino);

        try {
            ListaEnlazada<String> rutaMasCorta = Floyd.encontrarRutaMasCorta(ciudadesLista, rutasLista, ciudadOrigen, ciudadDestino);
            if (!rutaMasCorta.isEmpty()) {
                StringBuilder rutaStr = new StringBuilder();
                for (String ciudad : rutaMasCorta.toArray()) {
                    rutaStr.append(ciudad).append(" -> ");
                }
                rutaStr.delete(rutaStr.length() - 4, rutaStr.length()); // Eliminar el último " -> "
                JOptionPane.showMessageDialog(this, "Ruta más corta:\n" + rutaStr.toString(), "Ruta más corta", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró una ruta válida.", "Ruta no encontrada", JOptionPane.WARNING_MESSAGE);
            }
        } catch (VacioException | PosicionException ex) {
            JOptionPane.showMessageDialog(this, "Error al calcular la ruta más corta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buscarBellman(){
    
        // Obtener las ciudades de origen y destino seleccionadas en los JComboBox
        Ciudad origen = (Ciudad) cbxOrigen.getSelectedItem();
        Ciudad destino = (Ciudad) cbxDestino.getSelectedItem();

        try {
            // Ejecutar el algoritmo de Bellman-Ford desde la ciudad de origen
            ciuG.bellmanFord(origen);
        } catch (VacioException ex) {
            Logger.getLogger(FrmRuta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PosicionException ex) {
            Logger.getLogger(FrmRuta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbxOrigen = new javax.swing.JComboBox<>();
        cbxDestino = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        btnFloyd = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTabla = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Buscar Ruta"));

        jLabel3.setText("Origen:");

        jLabel4.setText("Destino:");

        cbxOrigen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbxDestino.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setText("Bellman");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnFloyd.setText("Floyd");
        btnFloyd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFloydActionPerformed(evt);
            }
        });

        jButton3.setText("Registrar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbxDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnFloyd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(16, 16, 16))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbxDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(cbxOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(btnFloyd)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Ciudades"));

        jButton4.setText("Grafo");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        tblTabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblTabla);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        new FrmRegistrar(new javax.swing.JFrame(), true).setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        new FrmGrafo(null, true, ciuG.getGrafo()).setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnFloydActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFloydActionPerformed
        inicializarCiudadesLista();
        try {
            long startTime = System.currentTimeMillis(); // Tiempo inicial
            buscarFloyd();
            long endTime = System.currentTimeMillis(); // Tiempo final
            long executionTime = endTime - startTime; // Tiempo de ejecución en milisegundos
            System.out.println("Tiempo de ejecución: " + executionTime + " ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnFloydActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jButton1ActionPerformed

/**
 * @param args the command line arguments
 */
public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

}
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmRuta.class  

.getName()).log(java.util.logging.Level.SEVERE, null, ex);

} catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmRuta.class  

.getName()).log(java.util.logging.Level.SEVERE, null, ex);

} catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmRuta.class  

.getName()).log(java.util.logging.Level.SEVERE, null, ex);

} catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmRuta.class  

.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrmRuta dialog = new FrmRuta(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFloyd;
    private javax.swing.JComboBox<String> cbxDestino;
    private javax.swing.JComboBox<String> cbxOrigen;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblTabla;
    // End of variables declaration//GEN-END:variables
}
