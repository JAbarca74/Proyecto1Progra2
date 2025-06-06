package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.util.JPAUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

import javax.persistence.EntityManager;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ReportsController extends Controller implements Initializable {

    @FXML
    private PieChart chartTiposEspacios;

    @FXML
    private PieChart chartReservasCoworking;

    // Este método lo llama JavaFX cuando carga el FXML:
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarGraficoTiposEspacios();
        cargarGraficoReservasPorCoworking();
    }

    // Si tu clase base Controller obliga a implementar initialize() sin parámetros, déjalo vacío:
    @Override
    public void initialize() {
        // No precisamos lógica adicional aquí; solo cumplimos el contrato abstracto.
    }

    private void cargarGraficoTiposEspacios() {
        Map<String, Long> datos = obtenerReservasPorTipoEspacio();
        ObservableList<PieChart.Data> lista = FXCollections.observableArrayList();
        datos.forEach((tipoName, cantidad) ->
            lista.add(new PieChart.Data(tipoName, cantidad))
        );
        chartTiposEspacios.setData(lista);
    }

    private void cargarGraficoReservasPorCoworking() {
        Map<String, Long> datos = obtenerReservasPorCoworking();
        ObservableList<PieChart.Data> lista = FXCollections.observableArrayList();
        datos.forEach((coworkingName, cantidad) ->
            lista.add(new PieChart.Data(coworkingName, cantidad))
        );
        chartReservasCoworking.setData(lista);
    }

    /**
     * Cuenta cuántas reservas hay por cada tipo de espacio.
     * Según tu entidad CoworkingSpaces.java, el atributo que apunta a SpaceTypes se llama "typeId",
     * y dentro de ese SpaceTypes está la propiedad "typeName".
     */
    private Map<String, Long> obtenerReservasPorTipoEspacio() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            String jpql =
                "SELECT cs.typeId.typeName, COUNT(r) " +
                "FROM Reservations r " +
                "JOIN r.coworkingSpaceId cs " +
                "GROUP BY cs.typeId.typeName";

            // Especificamos Object[].class porque cada fila es un Object[]{ String typeName, Long count }
            @SuppressWarnings("unchecked")
            List<Object[]> resultados = em.createQuery(jpql, Object[].class).getResultList();

            return resultados.stream()
                             .collect(Collectors.toMap(
                                 row -> (String) row[0],    // row[0] = cs.typeId.typeName
                                 row -> (Long)   row[1]     // row[1] = COUNT(r)
                             ));
        } finally {
            em.close();
        }
    }

    /**
     * Cuenta cuántas reservas hay por cada coworking (nombre de CoworkingSpaces).
     * En la entidad CoworkingSpaces.java, el atributo que guarda el nombre se llama "name".
     */
    private Map<String, Long> obtenerReservasPorCoworking() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            String jpql =
                "SELECT cs.name, COUNT(r) " +
                "FROM Reservations r " +
                "JOIN r.coworkingSpaceId cs " +
                "GROUP BY cs.name";

            @SuppressWarnings("unchecked")
            List<Object[]> resultados = em.createQuery(jpql, Object[].class).getResultList();

            return resultados.stream()
                             .collect(Collectors.toMap(
                                 row -> (String) row[0],    // row[0] = cs.name
                                 row -> (Long)   row[1]     // row[1] = COUNT(r)
                             ));
        } finally {
            em.close();
        }
    }
}
