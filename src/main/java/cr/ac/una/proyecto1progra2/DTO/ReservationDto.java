package cr.ac.una.proyecto1progra2.DTO;



import java.time.LocalDateTime;

public class ReservationDto {
    private Long id;
    private Long userId;
    private Long coworkingSpaceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean cancelled;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getCoworkingSpaceId() { return coworkingSpaceId; }
    public void setCoworkingSpaceId(Long coworkingSpaceId) { this.coworkingSpaceId = coworkingSpaceId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
}
