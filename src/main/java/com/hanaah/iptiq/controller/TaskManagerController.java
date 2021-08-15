package com.hanaah.iptiq.controller;


import com.hanaah.iptiq.core.TaskManager;
import com.hanaah.iptiq.exception.MaximumCapacityReachedException;
import com.hanaah.iptiq.exception.ProcessNotFoundException;
import com.hanaah.iptiq.model.Priority;
import com.hanaah.iptiq.model.Process;
import com.hanaah.iptiq.model.SortBy;
import com.hanaah.iptiq.model.dto.PriorityDto;
import com.hanaah.iptiq.model.dto.ProcessDto;
import com.hanaah.iptiq.model.dto.SortByDto;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
public class TaskManagerController {

	private final TaskManager taskManager;
	private final ModelMapper modelMapper;

	@Autowired
	public TaskManagerController(TaskManager taskManager, ModelMapper objectMapper) {
		this.taskManager = taskManager;
		this.modelMapper = objectMapper;
	}

	@GetMapping("/task-manager/processes")
	public List<ProcessDto> getRunningProcesses(@RequestParam SortByDto sortByDto) {
		SortBy sortBy = this.modelMapper.map(sortByDto, SortBy.class);
		return taskManager.listRunningProcess(sortBy.comparator()).stream()
				.map(process -> this.modelMapper.map(process, ProcessDto.class))
				.collect(Collectors.toList());
	}

	@PutMapping("/task-manager/add")
	@ExceptionHandler(value = MaximumCapacityReachedException.class)
	public void addProcess(@RequestParam PriorityDto priorityDto) throws MaximumCapacityReachedException {
		Priority priority = this.modelMapper.map(priorityDto, Priority.class);
		this.taskManager.addProcess(new Process(priority));
	}

	@DeleteMapping("/task-manager/kill-process/{pid}")
	public void deleteProcess(@PathVariable String pid) throws ProcessNotFoundException {
		this.taskManager.killProcess(pid);
	}
}
