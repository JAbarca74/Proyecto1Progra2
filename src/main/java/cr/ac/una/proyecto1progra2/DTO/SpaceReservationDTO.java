package cr.ac.una.proyecto1progra2.dto;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SpaceReservationDTO {
    private final StringProperty spaceName;
    private final LongProperty   total;

    public SpaceReservationDTO(String spaceName, Long total) {
        this.spaceName = new SimpleStringProperty(spaceName);
        this.total     = new SimpleLongProperty(total);
    }

    public String getSpaceName() {
        return spaceName.get();
    }

    public Long getTotal() {
        return total.get();
    }

    public StringProperty spaceNameProperty() {
        return spaceName;
    }

    public LongProperty totalProperty() {
        return total;
    }
}
