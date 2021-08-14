package com.hanaah.iptiq.core;

import com.hanaah.iptiq.exception.MaximumCapacityReachedException;
import com.hanaah.iptiq.exception.ProcessNotFoundException;
import com.hanaah.iptiq.model.Priority;
import com.hanaah.iptiq.model.Process;
import com.hanaah.iptiq.model.SortBy;
import lombok.Synchronized;

import java.util.*;
import java.util.stream.Collectors;

public class FifoTaskManager extends LinkedList<Process> implements TaskManager {

	private final int capacity;

	public FifoTaskManager(int capacity) {
		this.capacity = capacity;
	}

	@Override
	@Synchronized
	public void addProcess(Process process) throws MaximumCapacityReachedException {
		if (super.size() >= this.capacity) {
			throw new MaximumCapacityReachedException(String.format("Maximum capacity of [%d] was reached.", capacity));
		}
		super.removeFirst();
		super.add(process);
	}

	@Override
	public List<Process> listRunningProcess(SortBy sortBy) {
		return new ArrayList<>(this);
	}

	@Override
	@Synchronized
	public void killProcess(String pid) throws ProcessNotFoundException {
		UUID id = UUID.fromString(pid);
		Optional<Process> process = this.stream().filter(p -> p.getPid().equals(id)).findFirst();
		if (process.isPresent()) {
			process.get().kill();
		} else {
			throw new ProcessNotFoundException(String.format("Process with id [%s] was not found", pid));
		}
	}

	@Override
	@Synchronized
	public void killGroup(Priority priority) {
		List<Process> groupToKill = this.stream()
				.filter(process -> process.getPriority().equals(priority))
				.peek(this::remove)
				.collect(Collectors.toList());
		groupToKill.forEach(Process::kill);
	}

	@Override
	@Synchronized
	public void killAll() {
		this.forEach(Process::kill);
		this.clear();
	}
}
