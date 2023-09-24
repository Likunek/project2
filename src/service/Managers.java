package service;

import java.net.URI;

public class Managers {

   public static TaskManager getDefaultHttpTaskManager(URI url) {
       return new HttpTaskManager(url);
   }

   public static HistoryManager getDefaultHistoryManager() {
       return new InMemoryHistoryManager();
   }
}
