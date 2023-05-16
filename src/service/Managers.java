package service;

public class Managers {

   public static TaskManager getDefaultTaskManager(){
       return new InMemoryTaskManager();
   }

   public static HistoryManager getDefaultHistoryManager(){
       return new InMemoryHistoryManager();
   }
}
