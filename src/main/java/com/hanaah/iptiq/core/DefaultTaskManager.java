package com.hanaah.iptiq.core;

import com.hanaah.iptiq.exception.MaximumCapacityReachedException;
import com.hanaah.iptiq.exception.ProcessNotFoundException;
import com.hanaah.iptiq.model.Priority;
import com.hanaah.iptiq.model.Process;
import lombok.Synchronized;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultTaskManager implements TaskManager {

	protected final int capacity;
	private final Map<UUID, Process> processMap;

	public DefaultTaskManager(int capacity) {
		this.capacity = capacity;
		this.processMap = new HashMap<>();
	}

	@Override
	@Synchronized
	public void addProcess(Process process) throws MaximumCapacityReachedException {
		if (this.processMap.size() >= this.capacity) {
			throw new MaximumCapacityReachedException(String.format("Maximum capacity of [%d] was reached.", capacity));
		}
		this.processMap.put(process.getPid(), process);
	}

	@Override
	public List<Process> listRunningProcess(Comparator<Process> comparator) {
		return this.processMap.values().stream().sorted(comparator).collect(Collectors.toList());
	}

	@Override
	@Synchronized
	public void killProcess(String pid) throws ProcessNotFoundException {
		UUID id = UUID.fromString(pid);
		if (!this.processMap.containsKey(id)) {
			throw new ProcessNotFoundException(String.format("Process with id [%s] was not found", pid));
		}
		this.processMap.get(id).kill();
		this.processMap.remove(id);
	}

	@Override
	@Synchronized
	public void killGroup(Priority priority) {
		this.processMap.values().stream()
				.filter(process -> process.getPriority().equals(priority))
				.forEach(Process::kill);
	}

	@Override
	@Synchronized
	public void killAll() {
		this.processMap.values().forEach(Process::kill);
	}
}
