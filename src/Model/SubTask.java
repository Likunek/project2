package Model;

public class SubTask extends Task{
    int epicId;

   public SubTask(String name , String description, int epic) {
        super(name, description);
        this.epicId = epic;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
