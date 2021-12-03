package com.hanaah.dto;

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
    private PriorityDto priority;
    private Date creationTimestamp;
}
