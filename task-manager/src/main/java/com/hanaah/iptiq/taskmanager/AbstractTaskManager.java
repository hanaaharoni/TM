package com.hanaah.iptiq.taskmanager;

import com.hanaah.iptiq.taskmanager.exceptions.ProcessNotFoundException;
import com.hanaah.iptiq.taskmanager.models.Priority;
import com.hanaah.iptiq.taskmanager.models.Process;
import com.hanaah.iptiq.taskmanager.models.SortBy;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter(AccessLevel.PROTECTED)
public abstract class AbstractTaskManager implements TaskManager {

    protected int capacity;
    protected Map<UUID, Process> processMap;

    public AbstractTaskManager(int capacity, Map<UUID, Process> processMap) {
        this.capacity = capacity;
        this.processMap = processMap;
    }

    @Override
    public List<Process> listRunningProcess(SortBy sortedBy) {
        return this.getProcessMap().values().stream().sorted(sortedBy.getComparator())
            .collect(Collectors.toList());
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
        List<Process> processesToKill = this.getProcessMap().values().stream()
            .filter(process -> process.getPriority().equals(priority))
            .collect(Collectors.toList());
        processesToKill.forEach(process -> {
            try {
                this.killProcess(process.getProcessId());
            } catch (ProcessNotFoundException ignoredException) {
                log.warn("The exception is ignored because if the "
                    + "process doesn't exist it means it was already killed. however, we "
                    + "would want to know if it ever happens", ignoredException);
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
