package roomescape;

public class Reservation {

    private Long id;
    private String name;
    private String date;
    private String time;

    // 기본 생성자 (JSON 역직렬화를 위해 필요)
    public Reservation() {}

    public Reservation(Long id, String name, String date, String time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDate() { return date; }
    public String getTime() { return time; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
}
