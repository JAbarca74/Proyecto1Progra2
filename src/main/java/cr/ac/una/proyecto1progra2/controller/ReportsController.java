package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.dto.SpaceReservationDTO;
import cr.ac.una.proyecto1progra2.util.JPAUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Pair;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.persistence.EntityManager;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ReportsController extends Controller implements Initializable {

    @FXML private TableView<SpaceReservationDTO> tablaEspacios;
    @FXML private TableColumn<SpaceReservationDTO, String> colEspacio;
    @FXML private TableColumn<SpaceReservationDTO, Long> colTotalEspacios;

    @FXML private TableView<Pair<String,Long>> tablaUsuarios;
    @FXML private TableColumn<Pair<String,Long>, String> colUsuario;
    @FXML private TableColumn<Pair<String,Long>, Long> colTotalUsuarios;

    @FXML private PieChart chartReservasCoworking;
    @FXML private BarChart<String,Number> graficoHoras;
    @FXML private CategoryAxis ejeX;
    @FXML private NumberAxis ejeY;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarGraficoReservasPorMes();
        cargarTablaEspacios();
        cargarTablaUsuarios();
        cargarHorasPico();
    }

    @Override
    public void initialize() { /* no usado */ }

    private void cargarTablaEspacios() {
        Map<String, Long> datos = obtenerReservasPorEspacio();
        ObservableList<SpaceReservationDTO> lista = FXCollections.observableArrayList();
        datos.forEach((n, t) -> lista.add(new SpaceReservationDTO(n, t)));
        colEspacio.setCellValueFactory(cd -> cd.getValue().spaceNameProperty());
        colTotalEspacios.setCellValueFactory(cd -> cd.getValue().totalProperty().asObject());
        tablaEspacios.setItems(lista);
    }

    private Map<String, Long> obtenerReservasPorEspacio() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            List<Object[]> results = em.createQuery(
                "SELECT sp.name, COUNT(r) "
              + "FROM Reservations r "
              + "JOIN r.coworkingSpaceId cs "
              + "JOIN cs.spaceId sp "
              + "GROUP BY sp.name", Object[].class
            ).getResultList();

            return results.stream().collect(Collectors.toMap(
                row -> (String) row[0],
                row -> (Long)   row[1]
            ));
        } finally {
            em.close();
        }
    }

    private void cargarTablaUsuarios() {
        Map<String, Long> datos = obtenerReservasPorUsuario();
        ObservableList<Pair<String,Long>> lista = FXCollections.observableArrayList();
        datos.forEach((u, t) -> lista.add(new Pair<>(u, t)));
        colUsuario.setCellValueFactory(cd ->
            new SimpleStringProperty(cd.getValue().getKey()));
        colTotalUsuarios.setCellValueFactory(cd ->
            new SimpleLongProperty(cd.getValue().getValue()).asObject());
        tablaUsuarios.setItems(lista);
    }

    private Map<String, Long> obtenerReservasPorUsuario() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            List<Object[]> results = em.createQuery(
                "SELECT u.username, COUNT(r) "
              + "FROM Reservations r "
              + "JOIN r.userId u "
              + "GROUP BY u.username", Object[].class
            ).getResultList();

            return results.stream().collect(Collectors.toMap(
                row -> (String) row[0],
                row -> (Long)   row[1]
            ));
        } finally {
            em.close();
        }
    }

    private void cargarGraficoReservasPorMes() {
        Map<String, Long> datos = obtenerReservasPorMes();
        ObservableList<PieChart.Data> lista = FXCollections.observableArrayList();
        datos.forEach((mes, cnt) ->
            lista.add(new PieChart.Data(mes, cnt)));
        chartReservasCoworking.setTitle("Reservas por Mes");
        chartReservasCoworking.setData(lista);
    }

    private Map<String, Long> obtenerReservasPorMes() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            @SuppressWarnings("unchecked")
            List<Object[]> resultados = em.createNativeQuery(
                "SELECT TO_CHAR(RESERVATION_DATE,'YYYY-MM'), COUNT(ID) "
              + "FROM TB_RESERVATIONS "
              + "GROUP BY TO_CHAR(RESERVATION_DATE,'YYYY-MM') "
              + "ORDER BY 1"
            ).getResultList();

            return resultados.stream().collect(Collectors.toMap(
                row -> (String)   row[0],
                row -> ((Number)  row[1]).longValue(),
                (a,b)->a,
                LinkedHashMap::new
            ));
        } finally {
            em.close();
        }
    }

    private void cargarHorasPico() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            @SuppressWarnings("unchecked")
            List<Object[]> resultados = em.createNativeQuery(
                "SELECT EXTRACT(HOUR FROM START_TIME), COUNT(ID) "
              + "FROM TB_RESERVATIONS "
              + "GROUP BY EXTRACT(HOUR FROM START_TIME) "
              + "ORDER BY 1"
            ).getResultList();

            XYChart.Series<String,Number> serie = new XYChart.Series<>();
            serie.setName("Reservas por hora");
            for (Object[] f : resultados) {
                serie.getData().add(
                  new XYChart.Data<>(f[0].toString(), ((Number)f[1]).longValue())
                );
            }
            graficoHoras.getData().setAll(serie);

        } finally {
            em.close();
        }
    }
}
