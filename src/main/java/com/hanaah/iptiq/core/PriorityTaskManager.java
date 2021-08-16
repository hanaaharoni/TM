package com.hanaah.iptiq.core;

import com.hanaah.iptiq.exception.ProcessNotFoundException;
import com.hanaah.iptiq.model.Priority;
import com.hanaah.iptiq.model.Process;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.stream.Collectors;

@Getter(AccessLevel.PRIVATE)
public class PriorityTaskManager implements TaskManager {

	private final int capacity;
	private final Map<Priority, Map<UUID, Process>> processQueue;

	public PriorityTaskManager(int capacity) {
		this.capacity = capacity;
		this.processQueue = new TreeMap<>();
		for (Priority p : Priority.values()) {
			this.getProcessQueue().put(p, new LinkedHashMap<>());
		}
	}

	@Override
	@Synchronized
	public void addProcess(Priority priority) {
		if (this.getProcessQueue().size() >= this.getCapacity()) {
			Optional<Priority> lowestPriority = this.getProcessQueue().entrySet()
					.stream()
					.filter(priorityMapEntry -> !priorityMapEntry.getValue().isEmpty())
					.map(Map.Entry::getKey)
					.findFirst();
			lowestPriority.ifPresent(lowPrior -> {
				Optional<Process> processToKill = this.getProcessQueue().get(lowPrior).values().stream().findFirst();
				processToKill.ifPresent(pr -> {
					pr.kill();
					this.getProcessQueue().get(lowPrior).remove(pr.getProcessId());
				});
			});
		}
		Process process = new Process(priority);
		this.getProcessQueue().get(process.getPriority()).put(process.getProcessId(), process);
	}

	@Override
	public List<Process> listRunningProcess() {
		return this.getProcessQueue()
				.values()
				.stream()
				.map(Map::values)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	@Override
	@Synchronized
	public void killProcess(UUID processId) throws ProcessNotFoundException {
		//TODO: try to refactor this part to a fancier code
		Process processToKill = null;
		for (Priority priority : this.getProcessQueue().keySet()) {
			if (this.getProcessQueue().get(priority).containsKey(processId)) {
				processToKill = this.getProcessQueue().get(priority).get(processId);
				processToKill.kill();
				this.getProcessQueue().get(priority).remove(processToKill.getProcessId());
				break;
			}
		}
		if (processToKill == null) {
			throw new ProcessNotFoundException(processId);
		}
	}

	@Override
	@Synchronized
	public void killGroup(Priority priority) {
		this.getProcessQueue().get(priority).values().forEach(Process::kill);
		this.getProcessQueue().get(priority).clear();
	}

	@Override
	public void killAll() {
		for (Priority priority : this.getProcessQueue().keySet()) {
			killGroup(priority);
		}
	}
}
