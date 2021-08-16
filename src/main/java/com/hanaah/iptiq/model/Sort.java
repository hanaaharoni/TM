package com.hanaah.iptiq.model;

import java.util.Comparator;
import lombok.Getter;

@Getter
public enum Sort {
    PROCESS_ID,
    CREATION_DATE,
    PRIORITY;

    public Comparator<Process> comparator() {
        switch (this) {
            case PROCESS_ID:
                return Comparator.comparing(Process::getProcessId);
            case PRIORITY:
                return Comparator.comparing(Process::getPriority);
            default:
                return Comparator.comparing(Process::getCreationTimestamp);
        }
    }
}
