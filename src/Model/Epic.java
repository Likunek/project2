package Model;

import java.util.List;

public class Epic extends Task{
     List<Integer> subTasksId;

    public Epic(String name , String description ) {
        super(name, description);
    }

    public List<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void setSubTasksId(List<Integer> subTasksId) {
        this.subTasksId = subTasksId;
    }
}
