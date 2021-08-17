package com.hanaah.iptiq.dto;

import com.hanaah.iptiq.model.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProcessDto {

    private UUID processId;
    private Priority priority;
    private Date creationTimestamp;
}
