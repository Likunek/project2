package Model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static java.time.Duration.ofMinutes;

public class Task {
    private int id;
    private String name;
    private Status status;
    private String description;
    protected Duration duration;
    protected LocalDateTime startTime;
    public DateTimeFormatter formatter;


    public Task(String name, String description){
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm");
    }

    public void data(int duration, String startTime1){
        this.duration = ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime1, formatter);
    }

    public String getEndTime() {
        try {
            return startTime.plus(duration).format(formatter);
        } catch (NullPointerException exception){
            throw new NullPointerException("У task отсутсвует заданное время, вычислить endTime невозможно");
        }
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = LocalDateTime.parse(startTime, formatter);
    }
    @Override
    public String toString() {
        return "Model.Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", duration='" + duration + '\'' +
                ", startTime='" + startTime + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && name.equals(task.name) && status.equals(task.status) && description.equals(task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, description);
    }



}
