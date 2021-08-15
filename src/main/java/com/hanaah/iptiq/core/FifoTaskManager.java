package com.hanaah.iptiq.core;

import com.hanaah.iptiq.exception.ProcessNotFoundException;
import com.hanaah.iptiq.model.Priority;
import com.hanaah.iptiq.model.Process;
import lombok.Synchronized;

import java.util.*;
import java.util.stream.Collectors;

public class FifoTaskManager extends DefaultTaskManager {

	private final Queue<Process> processQueue;

	public FifoTaskManager(int capacity) {
		super(capacity);
		this.processQueue = new LinkedList<>();
	}

	@Override
	@Synchronized
	public void addProcess(Process process) {
		if (processQueue.size() >= this.capacity) {
			Process processToKill = processQueue.peek();
			if (processToKill != null) {
				processToKill.kill();
				processQueue.poll();
			}
		}
		processQueue.add(process);
	}

	@Override
	public List<Process> listRunningProcess(Comparator<Process> comparator) {
		return this.processQueue.stream().sorted(comparator).collect(Collectors.toList());
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
