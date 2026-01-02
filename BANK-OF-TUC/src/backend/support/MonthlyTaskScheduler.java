package backend.support;

import java.util.ArrayList;
import java.util.List;

public class MonthlyTaskScheduler {

    private final List<Runnable> monthlyTasks = new ArrayList<>();

    public void addTask(Runnable task) {
        monthlyTasks.add(task);
    }

    public void executeTasks() {
        for (Runnable task : monthlyTasks) {
            try {
                task.run();
            } catch (Exception e) {
                System.err.println("[MonthlyTaskScheduler] Task failed: " + e.getMessage());
            }
        }
    }
}
