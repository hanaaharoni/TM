package com.hanaah.taskmanager;

import com.hanaah.taskmanager.exceptions.MaximumCapacityReachedException;
import com.hanaah.taskmanager.exceptions.ProcessAlreadyExistsException;
import com.hanaah.taskmanager.models.Priority;
import com.hanaah.taskmanager.models.Process;
import com.hanaah.taskmanager.models.SortBy;
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

        Assertions.assertFalse(taskManager.listRunningProcess(SortBy.PROCESS_ID).contains(firstInProcess));
        Assertions.assertTrue(taskManager.listRunningProcess(SortBy.PROCESS_ID).contains(excessProcess));
    }
}