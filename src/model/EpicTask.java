package model;


import service.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

public class EpicTask extends Task {


    private HashMap<Integer, SubTask> childSubTasks = new HashMap<>();

    LocalDateTime endTime;
    public EpicTask(String name, String description, Status status){
        this.name = name;
        this.description = description;
        this.status = status;
        startTime = LocalDateTime.now();
        endTime = LocalDateTime.now();
    }

    public void updateStartEndMomentsOfSubList()
    {
        if (!childSubTasks.isEmpty()) {
            boolean i = true;
            for (SubTask sub : childSubTasks.values()) {
                if (i) {
                    startTime = sub.getStartTime();
                    endTime = sub.getEndTime();
                    i = false;
                } else {
                    if (sub.getEndTime().isAfter(endTime))
                        endTime = sub.getEndTime();
                    if (sub.getStartTime().isBefore(startTime))
                        startTime = sub.getStartTime();
                }
            }
        }
    }



    @Override
    public int getDurationMinutes() {
      updateStartEndMomentsOfSubList();
      return (int) Duration.between(startTime, endTime).toMinutes();
    }

    @Override
    public LocalDateTime getStartTime() {
        updateStartEndMomentsOfSubList();
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        updateStartEndMomentsOfSubList();
        return endTime;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s,%d", id, this.getClass().getSimpleName(),  name, status, description, getStartTime().format(timeFormat), getDurationMinutes());
        //id,type,name,status,description, start time, duration
    }

    @Override
    public Status getStatus() {
                    return status;
    }


    public void setStartEndDurationTime(String startTime,int duration) {
        this.startTime = LocalDateTime.parse(startTime, timeFormat);
        this.duration = Duration.ofMinutes(duration);
        endTime = this.startTime.plus(this.duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EpicTask epicTask = (EpicTask) o;
        return Objects.equals(childSubTasks, epicTask.childSubTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), childSubTasks);
    }

    public void clearSubList()
    {
        childSubTasks.clear();
    }

    public HashMap<Integer, SubTask> getSubtasksMap () {
        return childSubTasks;
    }
}
