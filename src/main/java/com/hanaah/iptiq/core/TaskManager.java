package com.hanaah.iptiq.core;

import com.hanaah.iptiq.exception.MaximumCapacityReachedException;
import com.hanaah.iptiq.exception.ProcessNotFoundException;
import com.hanaah.iptiq.model.Priority;
import com.hanaah.iptiq.model.Process;
import com.hanaah.iptiq.model.SortBy;

import java.util.List;

public interface TaskManager {
	List<Process> listRunningProcess(SortBy sortBy);

	void addProcess(Process process) throws MaximumCapacityReachedException;

	void killProcess(String pid) throws ProcessNotFoundException;

	void killGroup(Priority priority);

	void killAll();
}
