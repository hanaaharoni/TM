package com.hanaah.iptiq.core;

import com.hanaah.iptiq.exception.MaximumCapacityReachedException;
import com.hanaah.iptiq.model.Priority;
import com.hanaah.iptiq.model.Process;
import lombok.Synchronized;

import java.util.HashMap;

public class PrefixCapacityTaskManager extends AbstractTaskManager {

	public PrefixCapacityTaskManager(int capacity) {
		super(capacity, new HashMap<>());
	}

	@Override
	@Synchronized
	public void addProcess(Priority priority) throws MaximumCapacityReachedException {
		if (this.getProcessMap().size() >= this.capacity) {
			throw new MaximumCapacityReachedException(capacity);
		}
		Process process = new Process(priority);
		this.getProcessMap().put(process.getProcessId(), process);
	}
}
