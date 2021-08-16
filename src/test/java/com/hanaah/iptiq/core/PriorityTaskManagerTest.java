package com.hanaah.iptiq.core;

import com.hanaah.iptiq.exception.MaximumCapacityReachedException;
import com.hanaah.iptiq.exception.ProcessAlreadyExistsException;
import com.hanaah.iptiq.model.Priority;
import com.hanaah.iptiq.model.Process;
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

        Assertions.assertFalse(taskManager.listRunningProcess().contains(leastPriorProcess));
        Assertions.assertTrue(taskManager.listRunningProcess().contains(excessProcess));
        Assertions.assertTrue(taskManager.listRunningProcess().contains(secondLeastPrior));

        Process excessProcess2 = mockProcess(Priority.HIGH);
        taskManager.addProcess(excessProcess2);

        Assertions.assertFalse(taskManager.listRunningProcess().contains(secondLeastPrior));
        Assertions.assertTrue(taskManager.listRunningProcess().contains(excessProcess2));
    }
}