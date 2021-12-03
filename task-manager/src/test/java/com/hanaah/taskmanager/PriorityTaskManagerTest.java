package com.hanaah.taskmanager;

import com.hanaah.taskmanager.exceptions.MaximumCapacityReachedException;
import com.hanaah.taskmanager.exceptions.ProcessAlreadyExistsException;
import com.hanaah.taskmanager.models.Priority;
import com.hanaah.taskmanager.models.Process;
import com.hanaah.taskmanager.models.SortBy;
import java.util.List;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PriorityTaskManagerTest extends TaskManagerBasicTest {


    private static final int CAPACITY = 3;
    @Getter
    private TaskManager taskManager;

    @BeforeEach
    public void reset() {
        this.taskManager = new PriorityTaskManager(CAPACITY);
    }

    @Test
    public void When_capacityIsFull_Then_leastPriorIsEvicted()
        throws MaximumCapacityReachedException, ProcessAlreadyExistsException {
        TaskManager taskManager = this.getTaskManager();

        Process mostPriorProcess = mockProcess(Priority.HIGH);
        taskManager.addProcess(mostPriorProcess);

        Process secondLeastPrior = mockProcess(Priority.MEDIUM);
        taskManager.addProcess(secondLeastPrior);

        Process leastPriorProcess = mockProcess(Priority.LOW);
        taskManager.addProcess(leastPriorProcess);

        Process excessProcess = mockProcess(Priority.HIGH);
        taskManager.addProcess(excessProcess);

        List<Process> processes = taskManager.listRunningProcess(SortBy.PROCESS_ID);
        Assertions.assertFalse(processes.contains(leastPriorProcess));
        Assertions.assertTrue(processes.contains(excessProcess));
        Assertions.assertTrue(processes.contains(secondLeastPrior));

        Process excessProcess2 = mockProcess(Priority.HIGH);
        taskManager.addProcess(excessProcess2);

        processes = taskManager.listRunningProcess(SortBy.PROCESS_ID);
        Assertions.assertFalse(processes.contains(secondLeastPrior));
        Assertions.assertTrue(processes.contains(excessProcess2));
    }
}