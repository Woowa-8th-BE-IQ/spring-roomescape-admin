package roomescape;

public class ReservationTime {

    private Long id;
    private String startAt;

    public ReservationTime() {}

    public ReservationTime(Long id, String startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public Long getId() { return id; }
    public String getStartAt() { return startAt; }

    public void setId(Long id) { this.id = id; }
    public void setStartAt(String startAt) { this.startAt = startAt; }
}
