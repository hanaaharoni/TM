package com.hanaah.iptiq.core;

import com.hanaah.iptiq.exception.MaximumCapacityReachedException;
import com.hanaah.iptiq.exception.ProcessNotFoundException;
import com.hanaah.iptiq.model.Priority;
import com.hanaah.iptiq.model.Process;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

// TODO: you can document the methods here to make it easier for the implemented of this interface.
public interface TaskManager {

	List<Process> listRunningProcess();

	void addProcess(Priority Priority) throws MaximumCapacityReachedException;

	void killProcess(UUID processId) throws ProcessNotFoundException;

	void killGroup(Priority priority);

	void killAll();
}
