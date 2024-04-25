package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task  {
    protected String name;
    protected String description;
    protected int id;
    protected Status status;
    protected Duration duration;
    protected LocalDateTime startTime;

    public final static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");


    protected Task() {
    }

    public Task(String name, String description, Status status, int DurationInMinutes) {
        this.name = name;
        this.description = description;
        this.status = status;
        duration = Duration.ofMinutes(DurationInMinutes);
        startTime = LocalDateTime.now();
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getDurationMinutes() {
        return (int)duration.toMinutes();
    }

    public LocalDateTime getEndTime()
    {
        return  startTime.plus(duration);
    }



    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDuration(int durationOfMinutes) { //в минутах
        this.duration = Duration.ofMinutes(durationOfMinutes);
    }

    public void setStartTime(String startTime) {
        this.startTime =LocalDateTime.parse(startTime, Task.timeFormat);
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s,%d", id, this.getClass().getSimpleName(),  name, status, description, startTime.format(timeFormat), getDurationMinutes()); //id,type,name,status,description, start time, duration  -пример ожидаемого заполнения

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }
}
