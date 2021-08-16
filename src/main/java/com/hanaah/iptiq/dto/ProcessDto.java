package com.hanaah.iptiq.dto;

import com.hanaah.iptiq.model.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessDto {
	private UUID processId;
	private Priority priority;
	private Date creationTimestamp;
}
