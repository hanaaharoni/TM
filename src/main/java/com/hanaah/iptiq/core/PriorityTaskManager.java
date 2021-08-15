package com.hanaah.iptiq.core;

import com.hanaah.iptiq.exception.ProcessNotFoundException;
import com.hanaah.iptiq.model.Priority;
import com.hanaah.iptiq.model.Process;
import com.hanaah.iptiq.model.SortBy;
import lombok.Synchronized;

import java.util.*;
import java.util.stream.Collectors;

public class PriorityTaskManager extends DefaultTaskManager {

	private final PriorityQueue<Process> processQueue;

	public PriorityTaskManager(int capacity) {
		super(capacity);
		this.processQueue = new PriorityQueue<>((a, b) -> {
			if (a.getPriority() == b.getPriority()) {
				return a.getCreationTimestamp().compareTo(b.getCreationTimestamp());
			} else {
				return a.getPriority().compareTo(b.getPriority());
			}
		});
	}

	@Override
	@Synchronized
	public void addProcess(Process process) {
		if (!this.processQueue.isEmpty() && this.processQueue.size() >= this.capacity) {
			Process processToBeKilled = this.processQueue.poll();
			processToBeKilled.kill();
		}
		this.processQueue.add(process);
	}

	@Override
	public List<Process> listRunningProcess(Comparator<Process> c) {
		return this.processQueue.stream().sorted(c).collect(Collectors.toList());
	}

	@Override
	@Synchronized
	public void killProcess(String pid) throws ProcessNotFoundException {
		UUID id = UUID.fromString(pid);
		Optional<Process> process = this.processQueue.stream().filter(p -> p.getPid().equals(id)).findFirst();
		if (process.isPresent()) {
			process.get().kill();
		} else {
			throw new ProcessNotFoundException(String.format("Process with id [%s] was not found", pid));
		}
	}

	@Override
	@Synchronized
	public void killGroup(Priority priority) {
		List<Process> groupToKill = this.processQueue.stream()
				.filter(process -> process.getPriority().equals(priority))
				.peek(this.processQueue::remove)
				.collect(Collectors.toList());
		groupToKill.forEach(Process::kill);
	}
}
