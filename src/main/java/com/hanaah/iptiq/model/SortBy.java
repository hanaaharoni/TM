package com.hanaah.iptiq.model;

import java.util.Comparator;
import lombok.Getter;

@Getter
public enum SortBy {
    PROCESS_ID(Comparator.comparing(Process::getProcessId)),
    PRIORITY(Comparator.comparing(Process::getPriority)),
    CREATION_DATE(Comparator.comparing(Process::getCreationTimestamp));

    @Getter
    private final Comparator<Process> comparator;

    SortBy(Comparator<Process> comparator) {
        this.comparator = comparator;
    }


}
