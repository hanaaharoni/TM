package com.hanaah.iptiq.core;

import com.hanaah.iptiq.exception.MaximumCapacityReachedException;
import com.hanaah.iptiq.exception.ProcessNotFoundException;
import com.hanaah.iptiq.model.Priority;
import com.hanaah.iptiq.model.Process;
import com.hanaah.iptiq.model.SortBy;
import lombok.Synchronized;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultTaskManager implements TaskManager {

	private final int capacity;
	private final Map<UUID, Process> processes;

	public DefaultTaskManager(int capacity) {
		this.capacity = capacity;
		this.processes = new HashMap<>(capacity);
	}

	@Override
	@Synchronized
	public void addProcess(Process process) throws MaximumCapacityReachedException {
		if (this.processes.size() >= this.capacity) {
			throw new MaximumCapacityReachedException(String.format("Maximum capacity of [%d] was reached.", capacity));
		}
		this.processes.put(process.getPid(), process);
	}

	@Override
	public List<Process> listRunningProcess(SortBy sortBy) {
		return new ArrayList<>(this.processes.values()).stream().sorted((o1, o2) -> {
			if (sortBy.equals(SortBy.PID))
				return o1.getPid().compareTo(o2.getPid());
			if (sortBy.equals(SortBy.CREATION_DATE))
				return o1.getCreationTimestamp().compareTo(o2.getCreationTimestamp());
			else return o1.getPriority().compareTo(o2.getPriority());
		}).collect(Collectors.toList());
	}

	@Override
	@Synchronized
	public void killProcess(String pid) throws ProcessNotFoundException {
		UUID id = UUID.fromString(pid);
		if (!this.processes.containsKey(id)) {
			throw new ProcessNotFoundException(String.format("Process with id [%s] was not found", pid));
		}
		this.processes.get(id).kill();
		this.processes.remove(id);
	}

	@Override
	@Synchronized
	public void killGroup(Priority priority) {
		this.processes.values().stream()
				.filter(process -> process.getPriority().equals(priority))
				.forEach(Process::kill);
	}

	@Override
	@Synchronized
	public void killAll() {
		this.processes.values().forEach(Process::kill);
	}
}
