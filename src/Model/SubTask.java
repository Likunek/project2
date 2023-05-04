package Model;

public class SubTask extends Task{
    Epic epic;

   public SubTask(String name , String description, Epic epic ) {
        super(name, description);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }
}
