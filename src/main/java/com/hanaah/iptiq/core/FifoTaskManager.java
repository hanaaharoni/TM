package com.hanaah.iptiq.core;

import com.hanaah.iptiq.model.Priority;
import com.hanaah.iptiq.model.Process;
import lombok.Synchronized;

import java.util.LinkedHashMap;
import java.util.Optional;

public class FifoTaskManager extends AbstractTaskManager {

	public FifoTaskManager(int capacity) {
		super(capacity, new LinkedHashMap<>());
	}

	@Override
	@Synchronized
	public void addProcess(Priority priority) {
		if (this.getProcessMap().size() >= this.capacity) {
			Optional<Process> processToKill = this.getProcessMap().values().stream().findFirst();
			processToKill.ifPresent(process -> {
				process.kill();
				this.getProcessMap().remove(process.getProcessId());
			});
		}
		Process newProcess = new Process(priority);
		this.getProcessMap().put(newProcess.getProcessId(), newProcess);
	}
}

