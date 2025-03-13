package com.example.spring_monitoring_communication;

public class DateAndStringRequest {
    private String date;   // Data trimisă de frontend
    private String string; // Stringul trimis de frontend

    // Getter și Setter pentru date
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Getter și Setter pentru string
    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
