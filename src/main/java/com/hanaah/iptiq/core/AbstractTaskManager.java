package com.hanaah.iptiq.core;

import com.hanaah.iptiq.exception.ProcessNotFoundException;
import com.hanaah.iptiq.model.Priority;
import com.hanaah.iptiq.model.Process;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Synchronized;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter(AccessLevel.PROTECTED)
public abstract class AbstractTaskManager implements TaskManager {

	protected int capacity;
	protected Map<UUID, Process> processMap;

	public AbstractTaskManager(int capacity, Map<UUID, Process> processMap) {
		this.capacity = capacity;
		this.processMap = processMap;
	}

	@Override
	public List<Process> listRunningProcess() {
		return this.getProcessMap().values().stream().sorted().collect(Collectors.toList());
	}

	@Override
	@Synchronized
	public void killProcess(UUID processId) throws ProcessNotFoundException {
		if (!this.getProcessMap().containsKey(processId)) {
			throw new ProcessNotFoundException(processId);
		}
		this.getProcessMap().get(processId).kill();
		this.getProcessMap().remove(processId);
	}

	@Override
	@Synchronized
	public void killGroup(Priority priority) {
		this.getProcessMap().values().stream()
				.filter(process -> process.getPriority().equals(priority))
				.forEach(process -> {
					try {
						this.killProcess(process.getProcessId());
					} catch (ProcessNotFoundException ignored) {
						// The exception is ignored because if the
						// process doesn't exist it means it was already killed.
					}
				});
	}

	@Override
	@Synchronized
	public void killAll() {
		this.getProcessMap().values().forEach(Process::kill);
		this.getProcessMap().clear();
	}
}
