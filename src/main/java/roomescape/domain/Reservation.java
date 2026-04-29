package roomescape.domain;

public class Reservation {

    private Long id;
    private String name;
    private String date;
    private ReservationTime time;

    public Reservation() {}

    public Reservation(Long id, String name, String date, ReservationTime time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDate() { return date; }
    public ReservationTime getTime() { return time; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDate(String date) { this.date = date; }
    public void setTime(ReservationTime time) { this.time = time; }
}
