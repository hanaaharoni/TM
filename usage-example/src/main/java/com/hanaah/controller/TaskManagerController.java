package com.hanaah.controller;


import com.hanaah.dto.ErrorDto;
import com.hanaah.dto.PriorityDto;
import com.hanaah.dto.ProcessDto;
import com.hanaah.taskmanager.TaskManager;
import com.hanaah.taskmanager.exceptions.MaximumCapacityReachedException;
import com.hanaah.taskmanager.exceptions.ProcessAlreadyExistsException;
import com.hanaah.taskmanager.exceptions.ProcessNotFoundException;
import com.hanaah.taskmanager.models.Priority;
import com.hanaah.taskmanager.models.Process;
import com.hanaah.taskmanager.models.SortBy;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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

    @ApiOperation(value = "Adds a new process to the task manager.",
        notes = "Provided with the process, priority, this endpoint adds the newly created process to the task manager.")
    @ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "success", response = ProcessDto.class),
            @ApiResponse(code = 400, message = "Bad request", response = ErrorDto.class),
            @ApiResponse(code = 500, message = "Server Error", response = ErrorDto.class),
        })
    @PutMapping("/task-manager/add")
    public ProcessDto addProcess(@RequestParam PriorityDto priorityDto)
        throws MaximumCapacityReachedException, ProcessAlreadyExistsException {
        Priority priority = this.modelMapper.map(priorityDto, Priority.class);
        Process newProcess = new Process(priority);
        this.taskManager.addProcess(newProcess);
        return this.modelMapper.map(newProcess, ProcessDto.class);
    }

    @ApiOperation(value = "Kill process by id.",
        notes = "Kills a process running in task manager by its id.")
    @ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "success", response = void.class),
            @ApiResponse(code = 400, message = "Bad request", response = ErrorDto.class),
            @ApiResponse(code = 500, message = "Server Error", response = ErrorDto.class),
        })
    @DeleteMapping("/task-manager/kill-process/by-id/{processId}")
    public void killProcess(@PathVariable String processId) throws

        ProcessNotFoundException {
        UUID processUuid = UUID.fromString(processId);
        this.taskManager.killProcess(processUuid);
    }

    @ApiOperation(value = "Kill process by priority.",
        notes = "Kills all processes running in task manager matching the provided priority.")
    @ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "success", response = void.class),
            @ApiResponse(code = 400, message = "Bad request", response = ErrorDto.class),
            @ApiResponse(code = 500, message = "Server Error", response = ErrorDto.class),
        })
    @DeleteMapping("/task-manager/kill-process/by-priority/{priority}")
    public void killByGroup(@PathVariable Priority priority) {
        this.taskManager.killGroup(priority);
    }

    @ApiOperation(value = "Kill all processes.",
        notes = "Kills all processes running in task manager."
            + "\n after this request is over, no processes will be running in task manager.")
    @ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "success", response = void.class),
            @ApiResponse(code = 400, message = "Bad request", response = ErrorDto.class),
            @ApiResponse(code = 500, message = "Server Error", response = ErrorDto.class),
        })
    @DeleteMapping("/task-manager/kill-process/all")
    public void killAllProcesses() {
        this.taskManager.killAll();
    }

    @ApiOperation(value = "Lists the running processes in the task manager.",
        notes = "An endpoint that offers the list of running processes inside the task manager. \n "
            + "The list can be sorted by: "
            + "\n - Creation date of the process"
            + "\n - The priority of the process"
            + "\n - The process id")
    @ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "success", response = ProcessDto.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad request", response = ErrorDto.class),
            @ApiResponse(code = 500, message = "Server Error", response = ErrorDto.class),
        })
    @GetMapping("/task-manager/processes/{sortBy}")
    public List<ProcessDto> getRunningProcesses(@PathVariable SortBy sortBy) {
        return taskManager.listRunningProcess(sortBy)
            .stream()
            .map(process -> this.modelMapper.map(process, ProcessDto.class))
            .collect(Collectors.toList());
    }

}
