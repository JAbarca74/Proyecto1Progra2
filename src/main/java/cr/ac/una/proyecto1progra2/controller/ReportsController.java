package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.dto.SpaceReservationDTO;
import cr.ac.una.proyecto1progra2.util.JPAUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.util.Pair;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;


import javax.persistence.EntityManager;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ReportsController extends Controller implements Initializable {

    @FXML
    private PieChart chartTiposEspacios;
    @FXML
    private TableView<SpaceReservationDTO> tablaEspacios;
    @FXML
    private TableColumn<SpaceReservationDTO, String> colEspacio;
    @FXML
    private TableColumn<SpaceReservationDTO, Long> colTotalEspacios;
    @FXML
    private TableView<Pair<String,Long>> tablaUsuarios;
    @FXML
    private TableColumn<Pair<String,Long>, String> colUsuario;
    @FXML
    private TableColumn<Pair<String,Long>, Long>   colTotalUsuarios;
    @FXML
    private BarChart<String,Number> graficoHoras;
    @FXML
    private CategoryAxis ejeX;
    @FXML
    private NumberAxis   ejeY;

    @FXML
    private PieChart chartReservasCoworking;

    // Este método lo llama JavaFX cuando carga el FXML:
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarGraficoTiposEspacios();
        cargarGraficoReservasPorCoworking();
        cargarTablaEspacios();
        cargarTablaUsuarios(); 
        cargarGraficoTiposEspacios();
        cargarHorasPico();
        
    }

    // Si tu clase base Controller obliga a implementar initialize() sin parámetros, déjalo vacío:
    @Override
    public void initialize() {
        // No precisamos lógica adicional aquí; solo cumplimos el contrato abstracto.
    }
    
    private void cargarTablaEspacios() {
    Map<String, Long> resultados = obtenerReservasPorEspacio();
    ObservableList<SpaceReservationDTO> lista = FXCollections.observableArrayList();

    resultados.forEach((nombre, total) -> 
        lista.add(new SpaceReservationDTO(nombre, total))
    );

    colEspacio.setCellValueFactory(cd -> cd.getValue().spaceNameProperty());
    colTotalEspacios.setCellValueFactory(cd -> cd.getValue().totalProperty().asObject());
    tablaEspacios.setItems(lista);
}
    
    
   private Map<String, Long> obtenerReservasPorEspacio() {
    EntityManager em = JPAUtil.getEMF().createEntityManager();
    try {
        // Cadena JPQL limpia, sin comentarios
        String jpql = """
            SELECT sp.name, COUNT(r)
            FROM Reservations r
            JOIN r.coworkingSpaceId cs
            JOIN cs.spaceId sp
            GROUP BY sp.name
        """;
        List<Object[]> resultados = em.createQuery(jpql, Object[].class)
                                     .getResultList();
        return resultados.stream()
                         .collect(Collectors.toMap(
                             row -> (String) row[0],
                             row -> (Long)   row[1]
                         ));
    } finally {
        em.close();
    }
}


   private void cargarTablaUsuarios() {
    // 1) Obtenemos el Map<String,Long> desde JPA (igual que antes)
    Map<String, Long> resultados = obtenerReservasPorUsuario();

    // 2) Convertimos a lista de Pair
    ObservableList<Pair<String,Long>> lista = FXCollections.observableArrayList();
    resultados.forEach((usuario, total) ->
        lista.add(new Pair<>(usuario, total))
    );

    // 3) Asignamos CellValueFactory leyendo de Pair.getKey() y Pair.getValue()
    colUsuario.setCellValueFactory(cd ->
        new SimpleStringProperty(cd.getValue().getKey())
    );
    colTotalUsuarios.setCellValueFactory(cd ->
        new SimpleLongProperty(cd.getValue().getValue()).asObject()
    );

    // 4) Seteamos los items en la tabla
    tablaUsuarios.setItems(lista);
}

   private Map<String, Long> obtenerReservasPorUsuario() {
    EntityManager em = JPAUtil.getEMF().createEntityManager();
    try {
        String jpql = """
            SELECT u.username, COUNT(r)
            FROM Reservations r
            JOIN r.userId u
            GROUP BY u.username
        """;
        List<Object[]> resultados = em
            .createQuery(jpql, Object[].class)
            .getResultList();

        return resultados.stream()
            .collect(Collectors.toMap(
                row -> (String) row[0],
                row -> (Long)   row[1]
            ));
    } finally {
        em.close();
    }
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
    
    private void cargarHorasPico() {
    EntityManager em = JPAUtil.getEMF().createEntityManager();
    try {
        // Consulta nativa usando EXTRACT para Oracle:
        String sql =
            "SELECT EXTRACT(HOUR FROM START_TIME) AS hora, COUNT(ID) AS total " +
            "FROM TB_RESERVATIONS " +
            "GROUP BY EXTRACT(HOUR FROM START_TIME) " +
            "ORDER BY hora";

        @SuppressWarnings("unchecked")
        List<Object[]> resultados = em
            .createNativeQuery(sql)
            .getResultList();

       
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Reservas por hora");

        for (Object[] fila : resultados) {
            
            String hora = fila[0].toString();
            Number total = ((Number) fila[1]).longValue();
            serie.getData().add(new XYChart.Data<>(hora, total));
        }

    
        graficoHoras.getData().clear();
        graficoHoras.getData().add(serie);

    } finally {
        em.close();
    }
}

 
    private Map<String, Long> obtenerReservasPorTipoEspacio() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            String jpql =
                "SELECT cs.typeId.typeName, COUNT(r) " +
                "FROM Reservations r " +
                "JOIN r.coworkingSpaceId cs " +
                "GROUP BY cs.typeId.typeName";

            
            @SuppressWarnings("unchecked")
            List<Object[]> resultados = em.createQuery(jpql, Object[].class).getResultList();

            return resultados.stream()
                             .collect(Collectors.toMap(
                                 row -> (String) row[0],    
                                 row -> (Long)   row[1]    
                             ));
        } finally {
            em.close();
        }
    }

    
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
                                 row -> (String) row[0],    
                                 row -> (Long)   row[1]     
                             ));
        } finally {
            em.close();
        }
    }
}
