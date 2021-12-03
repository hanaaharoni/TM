package com.hanaah.taskmanager;

import com.hanaah.taskmanager.exceptions.ProcessAlreadyExistsException;
import com.hanaah.taskmanager.models.Process;
import java.util.LinkedHashMap;
import java.util.Optional;

import lombok.Synchronized;

public class FifoTaskManager extends AbstractTaskManager {

    public FifoTaskManager(int capacity) {
        super(capacity, new LinkedHashMap<>());
    }

    @Override
    @Synchronized
    public void addProcess(Process newProcess) throws ProcessAlreadyExistsException {
        if (this.getProcessMap().containsKey(newProcess.getProcessId())) {
            throw new ProcessAlreadyExistsException(newProcess.getProcessId());
        }
        if (this.getProcessMap().size() >= this.getCapacity()) {
            Optional<Process> processToKill = this.getProcessMap().values().stream().findFirst();
            processToKill.ifPresent(process -> {
                process.kill();
                this.getProcessMap().remove(process.getProcessId());
            });
        }
        this.getProcessMap().put(newProcess.getProcessId(), newProcess);
    }
}

