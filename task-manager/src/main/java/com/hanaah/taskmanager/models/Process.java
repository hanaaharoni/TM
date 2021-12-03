package com.hanaah.taskmanager.models;


import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
public class Process {

    private final UUID processId;
    private final Priority priority;
    private final Date creationTimestamp;

    public Process(Priority priority) {
        this.priority = priority;
        this.processId = UUID.randomUUID();
        this.creationTimestamp = Calendar.getInstance().getTime();
    }

    public void kill() {
        log.info("[pid: {}] was terminated.", this.getProcessId());
    }
}
