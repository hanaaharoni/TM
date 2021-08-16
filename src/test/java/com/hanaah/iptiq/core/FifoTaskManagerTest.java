package com.hanaah.iptiq.core;

import com.hanaah.iptiq.exception.MaximumCapacityReachedException;
import com.hanaah.iptiq.exception.ProcessAlreadyExistsException;
import com.hanaah.iptiq.model.Priority;
import com.hanaah.iptiq.model.Process;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FifoTaskManagerTest extends TaskManagerBasicTest {


    private static final int CAPACITY = 3;
    @Getter
    private TaskManager taskManager;

    @BeforeEach
    public void reset() {
        this.taskManager = new FifoTaskManager(CAPACITY);
    }

    @Test
    public void When_capacityIsFull_Then_firstInIsEvicted()
        throws MaximumCapacityReachedException, ProcessAlreadyExistsException {
        TaskManager taskManager = this.getTaskManager();

        Process firstInProcess = mockProcess(Priority.HIGH);
        taskManager.addProcess(firstInProcess);

        for (int i = 0; i < CAPACITY - 1; i++) {
            Process otherProcess = mockProcess(Priority.HIGH);
            taskManager.addProcess(otherProcess);
        }
        Process excessProcess = mockProcess(Priority.HIGH);
        taskManager.addProcess(excessProcess);

        Assertions.assertFalse(taskManager.listRunningProcess().contains(firstInProcess));
        Assertions.assertTrue(taskManager.listRunningProcess().contains(excessProcess));
    }
}