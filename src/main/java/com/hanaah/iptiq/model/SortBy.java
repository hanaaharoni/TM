package com.hanaah.iptiq.model;

import lombok.Getter;

import java.util.Comparator;

@Getter
public enum SortBy {
	PID,
	CREATION_DATE,
	PRIORITY;

	public Comparator<Process> comparator(){
		switch (this){
			case PID:
				return Comparator.comparing(Process::getPid);
			case PRIORITY:
				return Comparator.comparing(Process::getPriority);
			default:
				return Comparator.comparing(Process::getCreationTimestamp);
		}
	}
}
