package com.hanaah.iptiq.controller;


import com.hanaah.iptiq.core.TaskManager;
import com.hanaah.iptiq.dto.PriorityDto;
import com.hanaah.iptiq.dto.ProcessDto;
import com.hanaah.iptiq.dto.SortDto;
import com.hanaah.iptiq.exception.MaximumCapacityReachedException;
import com.hanaah.iptiq.exception.ProcessAlreadyExistsException;
import com.hanaah.iptiq.exception.ProcessNotFoundException;
import com.hanaah.iptiq.model.Priority;
import com.hanaah.iptiq.model.Process;
import com.hanaah.iptiq.model.Sort;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// TODO: remember to add exception handler (aka "advice" or "actuator") that spring offers, so that
//  you return a helpful message to the consumer when things go wrong.
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
    public List<ProcessDto> getRunningProcesses(@RequestParam SortDto sortDto) {
        Sort sortBy = this.modelMapper.map(sortDto, Sort.class);
        return taskManager.listRunningProcess()
            .stream()
            .sorted(sortBy.comparator())
            .map(process -> this.modelMapper.map(process, ProcessDto.class))
            .collect(Collectors.toList());
    }

    @PutMapping("/task-manager/add")
    @ExceptionHandler(value = MaximumCapacityReachedException.class)
    public void addProcess(@RequestParam PriorityDto priorityDto)
        throws MaximumCapacityReachedException, ProcessAlreadyExistsException {
        Priority priority = this.modelMapper.map(priorityDto, Priority.class);
        Process newProcess = new Process(priority);
        this.taskManager.addProcess(newProcess);
    }

    @DeleteMapping("/task-manager/kill-process/by-id/{processId}")
    public void killProcess(@PathVariable String processId) throws ProcessNotFoundException {
        UUID processUuid = UUID.fromString(processId);
        this.taskManager.killProcess(processUuid);
    }

    @DeleteMapping("/task-manager/kill-process/by-priority/{priority}")
    public void killByGroup(@PathVariable Priority priority) {
        this.taskManager.killGroup(priority);
    }

    @DeleteMapping("/task-manager/kill-process/all")
    public void killAllProcesses() {
        this.taskManager.killAll();
    }
}
