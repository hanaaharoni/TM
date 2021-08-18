package com.hanaah.iptiq.taskmanager;

import com.hanaah.iptiq.taskmanager.exceptions.ProcessAlreadyExistsException;
import com.hanaah.iptiq.taskmanager.exceptions.ProcessNotFoundException;
import com.hanaah.iptiq.taskmanager.models.Priority;
import com.hanaah.iptiq.taskmanager.models.Process;
import com.hanaah.iptiq.taskmanager.models.SortBy;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Synchronized;

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
    public void addProcess(Process newProcess) throws ProcessAlreadyExistsException {
        if (findProcess(newProcess.getProcessId()).isPresent()) {
            throw new ProcessAlreadyExistsException(newProcess.getProcessId());
        }
        if (this.getSize() >= this.getCapacity()) {
            Optional<Priority> lowestPriority = this.getProcessQueue().entrySet()
                .stream()
                .filter(priorityMapEntry -> !priorityMapEntry.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .findFirst();
            lowestPriority.ifPresent(lowPrior -> {
                Optional<Process> processToKill = this.getProcessQueue().get(lowPrior).values()
                    .stream().findFirst();
                processToKill.ifPresent(pr -> {
                    pr.kill();
                    this.getProcessQueue().get(lowPrior).remove(pr.getProcessId());
                });
            });
        }
        this.getProcessQueue().get(newProcess.getPriority())
            .put(newProcess.getProcessId(), newProcess);
    }

    @Override
    public List<Process> listRunningProcess(SortBy sortedBy) {
        return this.getProcessQueue()
            .values()
            .stream()
            .map(Map::values)
            .flatMap(Collection::stream)
            .sorted(sortedBy.getComparator())
            .collect(Collectors.toList());
    }

    @Override
    @Synchronized
    public void killProcess(UUID processId) throws ProcessNotFoundException {
        Optional<Process> processToKill = this.findProcess(processId);
        if (!processToKill.isPresent()) {
            throw new ProcessNotFoundException(processId);
        }
        Process process = processToKill.get();
        process.kill();
        this.getProcessQueue().get(process.getPriority()).remove(process.getProcessId());
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

    private Optional<Process> findProcess(UUID processId) {
        return this.getProcessQueue().values().stream()
            .filter(processMap -> processMap.containsKey(processId))
            .map(Map::values)
            .flatMap(Collection::stream)
            .findFirst();
    }

    private int getSize() {
        return this.getProcessQueue().values().stream().mapToInt(Map::size).sum();
    }
}
