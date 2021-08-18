package com.hanaah.iptiq.taskmanager;

import com.hanaah.iptiq.taskmanager.exceptions.MaximumCapacityReachedException;
import com.hanaah.iptiq.taskmanager.exceptions.ProcessAlreadyExistsException;
import com.hanaah.iptiq.taskmanager.exceptions.ProcessNotFoundException;
import com.hanaah.iptiq.taskmanager.models.Priority;
import com.hanaah.iptiq.taskmanager.models.Process;
import com.hanaah.iptiq.taskmanager.models.SortBy;
import java.util.List;
import java.util.UUID;

public interface TaskManager {

    /**
     * Lists all running processes.
     *
     * @param sortedBy the type of sorting.
     * @return the list of running processes sorted.
     */
    List<Process> listRunningProcess(SortBy sortedBy);

    /**
     * Adds a process to the task manager.
     *
     * @param process the process to be added to the task manager.
     * @throws MaximumCapacityReachedException when the task manager reaches its capacity and can no
     *                                         longer take in more processes.
     */
    void addProcess(Process process)
        throws MaximumCapacityReachedException, ProcessAlreadyExistsException;

    /**
     * Kills a process by its id.
     *
     * @param processId the process id we want to kill.
     * @throws ProcessNotFoundException when the requested process is not found.
     */
    void killProcess(UUID processId) throws ProcessNotFoundException;

    /**
     * Kills all processes belonging to a specific group.
     *
     * @param priority the group by which we want to kill processes.
     */
    void killGroup(Priority priority);

    /**
     * Kills all process in task manager.
     */
    void killAll();
}
