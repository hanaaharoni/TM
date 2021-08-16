package com.hanaah.iptiq.core;

import com.hanaah.iptiq.exception.MaximumCapacityReachedException;
import com.hanaah.iptiq.exception.ProcessAlreadyExistsException;
import com.hanaah.iptiq.model.Process;
import java.util.HashMap;
import lombok.Synchronized;

public class PrefixCapacityTaskManager extends AbstractTaskManager {

    public PrefixCapacityTaskManager(int capacity) {
        super(capacity, new HashMap<>());
    }

    @Override
    @Synchronized
    public void addProcess(Process newProcess)
        throws MaximumCapacityReachedException, ProcessAlreadyExistsException {
        if (this.getProcessMap().containsKey(newProcess.getProcessId())) {
            throw new ProcessAlreadyExistsException(newProcess.getProcessId());
        }
        if (this.getProcessMap().size() >= this.getCapacity()) {
            throw new MaximumCapacityReachedException(capacity);
        }
        this.getProcessMap().put(newProcess.getProcessId(), newProcess);
    }
}
