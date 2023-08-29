package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{

    private final List<Integer> subTasksId;
    private LocalDateTime endTime;

    public Epic(String name , String description) {
        super(name, description);
        subTasksId = new ArrayList<>();
    }

    @Override
    public String getEndTime() {
       try {
           return endTime.format(formatter);
       } catch (NullPointerException exception){
           throw new NullPointerException("У epic отсутсвуют подзадачи, вычислить endTime невозможно");
       }

    }

    public List<Integer> getSubTasksId() {
        return subTasksId;
    }


    public void addSubtaskId(int id){
        subTasksId.add(id);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }


}
