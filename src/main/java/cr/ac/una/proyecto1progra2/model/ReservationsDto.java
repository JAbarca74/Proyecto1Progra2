package cr.ac.una.proyecto1progra2.model;

import java.time.LocalDate;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class ReservationsDto {

    public SimpleStringProperty id;
    public SimpleObjectProperty<LocalDate> startTime;
    public SimpleObjectProperty<LocalDate> endTime;
    public SimpleObjectProperty<Boolean> cancelled;
    public SimpleStringProperty coworkingSpaceId;
    public SimpleStringProperty userId;
    public Long version;
    private Boolean modificado;

    public ReservationsDto() {
        this.modificado = false;
        this.id = new SimpleStringProperty();
        this.startTime = new SimpleObjectProperty<>();
        this.endTime = new SimpleObjectProperty<>();
        this.cancelled = new SimpleObjectProperty<>(false);
        this.coworkingSpaceId = new SimpleStringProperty();
        this.userId = new SimpleStringProperty();
    }

    public ReservationsDto(Reservations reservations) {
    }

    public Long getId() {
        if (id.get() != null && !id.get().isEmpty())
            return Long.valueOf(id.get());
        else
            return null;
    }

    public void setId(Long id) {
        this.id.set(id != null ? id.toString() : null);
    }

    public LocalDate getStartTime() {
        return startTime.get();
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime.set(startTime);
    }

    public LocalDate getEndTime() {
        return endTime.get();
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime.set(endTime);
    }

    public String getIsCancelled() {
        return cancelled.get() ? "S" : "N";
    }

    public void setIsCancelled(String cancelled) {
        this.cancelled.set(cancelled != null && cancelled.equalsIgnoreCase("S"));
    }

    public Long getCoworkingSpaceId() {
        if (coworkingSpaceId.get() != null && !coworkingSpaceId.get().isEmpty())
            return Long.valueOf(coworkingSpaceId.get());
        else
            return null;
    }

    public void setCoworkingSpaceId(Long id) {
        this.coworkingSpaceId.set(id != null ? id.toString() : null);
    }

    public Long getUserId() {
        if (userId.get() != null && !userId.get().isEmpty())
            return Long.valueOf(userId.get());
        else
            return null;
    }

    public void setUserId(Long id) {
        this.userId.set(id != null ? id.toString() : null);
    }

    public Boolean getModificado() {
        return modificado;
    }

    public void setModificado(Boolean modificado) {
        this.modificado = modificado;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ReservationsDto{" + "id=" + id.get() + ", startTime=" + startTime.get() + ", endTime=" + endTime.get() + ", cancelled=" + cancelled.get() + '}';
    }
}
