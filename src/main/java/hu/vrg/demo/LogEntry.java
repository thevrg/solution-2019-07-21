package hu.vrg.demo;

class LogEntry {
    private int id;
    private String message;

    public LogEntry(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}
