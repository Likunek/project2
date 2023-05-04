package Model;

import java.util.List;

public class Epic extends Task{
     List<SubTask> subTasks;

    public Epic(String name , String description ) {
        super(name, description);
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<SubTask> subTasks) {
        this.subTasks = subTasks;
    }
}
