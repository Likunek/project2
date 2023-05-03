package Model;

public class SubTask extends Task{
    Epic epic;

    public SubTask(String name) {
        super(name);
    }

    public Epic getEpic() {
        return epic;
    }
}
