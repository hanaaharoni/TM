package com.hanaah.iptiq.model;


import lombok.Getter;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Getter
public class Process {
	private final UUID pid;
	private final Priority priority;
	private final Date creationTimestamp;

	public Process(Priority priority) {
		this.priority = priority;
		this.pid = UUID.randomUUID();
		this.creationTimestamp = Calendar.getInstance().getTime();
	}

	public void kill(){
	}
}
