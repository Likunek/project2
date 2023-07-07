package Model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{
    private final List<Integer> subTasksId;

    public Epic(String name , String description ) {
        super(name, description);
        subTasksId = new ArrayList<>();
    }

    public List<Integer> getSubTasksId() {
        return subTasksId;
    }


    public void addSubtaskId(int id){
        subTasksId.add(id);
    }
}
