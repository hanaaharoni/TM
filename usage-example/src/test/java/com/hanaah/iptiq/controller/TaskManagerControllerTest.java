package com.hanaah.iptiq.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanaah.iptiq.TaskManagerApplication;
import com.hanaah.iptiq.dto.PriorityDto;
import com.hanaah.iptiq.dto.ProcessDto;
import com.hanaah.iptiq.taskmanager.TaskManager;
import com.hanaah.iptiq.taskmanager.models.Priority;
import com.hanaah.iptiq.taskmanager.models.Process;
import com.hanaah.iptiq.taskmanager.models.SortBy;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TaskManagerApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = {
    "classpath:application.yml"
})
public class TaskManagerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TaskManager taskManager;

    @Before
    public void reset() {
        taskManager.killAll();
    }

    @Test
    public void When_addingNewProcess_Then_processCreatedWithCorrectPriority() throws Exception {

        mvc.perform(put("/task-manager/add")
                .param("priorityDto", String.valueOf(Priority.HIGH))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("priority").value("HIGH"));
    }

    @Test
    public void When_killingProcess_Then_processIsKilled() throws Exception {

        Process process = new Process(Priority.HIGH);
        taskManager.addProcess(process);

        mvc.perform(delete("/task-manager/kill-process/by-id/" + process.getProcessId().toString())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void When_killingNonexistentProcess_Then_properErrorReturned() throws Exception {

        mvc.perform(delete("/task-manager/kill-process/by-id/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void When_killingProcessesByPriority_Then_allProcessesOfPriorityAreKilled()
        throws Exception {

        taskManager.addProcess(new Process(Priority.HIGH));
        taskManager.addProcess(new Process(Priority.MEDIUM));
        taskManager.addProcess(new Process(Priority.MEDIUM));

        mvc.perform(delete("/task-manager/kill-process/by-priority/" + Priority.MEDIUM)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        Assertions.assertEquals(0, taskManager.listRunningProcess(SortBy.PROCESS_ID)
            .stream().filter(process -> process.getPriority().equals(Priority.MEDIUM)).count());
        Assertions.assertEquals(1, taskManager.listRunningProcess(SortBy.PROCESS_ID).size());
    }

    @Test
    public void When_killingAllProcesses_Then_allProcessesAreKilled()
        throws Exception {

        taskManager.addProcess(new Process(Priority.HIGH));
        taskManager.addProcess(new Process(Priority.MEDIUM));
        taskManager.addProcess(new Process(Priority.MEDIUM));

        mvc.perform(delete("/task-manager/kill-process/all")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        Assertions.assertTrue(taskManager.listRunningProcess(SortBy.PROCESS_ID).isEmpty());
    }


    @Test
    public void When_listingProcessesByPriority_Then_correctOrderIsReturned()
        throws Exception {

        taskManager.addProcess(new Process(Priority.LOW));
        taskManager.addProcess(new Process(Priority.HIGH));
        taskManager.addProcess(new Process(Priority.MEDIUM));
        taskManager.addProcess(new Process(Priority.MEDIUM));

        MvcResult result = mvc.perform(get("/task-manager/processes/PRIORITY")
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<ProcessDto> response = mapper.readValue(result.getResponse().getContentAsString(),
            new TypeReference<>() {
            });
        List<PriorityDto> actual = response.stream().map(ProcessDto::getPriority)
            .collect(Collectors.toList());
        List<PriorityDto> expected = actual.stream().sorted().collect(Collectors.toList());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void When_listingProcessesByCreationTime_Then_correctOrderIsReturned()
        throws Exception {

        taskManager.addProcess(new Process(Priority.LOW));
        taskManager.addProcess(new Process(Priority.HIGH));
        taskManager.addProcess(new Process(Priority.MEDIUM));
        taskManager.addProcess(new Process(Priority.MEDIUM));

        MvcResult result = mvc.perform(get("/task-manager/processes/CREATION_DATE")
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<ProcessDto> response = mapper.readValue(result.getResponse().getContentAsString(),
            new TypeReference<>() {
            });
        List<Date> actual = response.stream().map(ProcessDto::getCreationTimestamp)
            .collect(Collectors.toList());
        List<Date> expected = actual.stream().sorted().collect(Collectors.toList());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void When_listingProcessesByProcessId_Then_correctOrderIsReturned()
        throws Exception {

        taskManager.addProcess(new Process(Priority.LOW));
        taskManager.addProcess(new Process(Priority.HIGH));
        taskManager.addProcess(new Process(Priority.MEDIUM));
        taskManager.addProcess(new Process(Priority.MEDIUM));

        MvcResult result = mvc.perform(get("/task-manager/processes/PROCESS_ID")
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<ProcessDto> response = mapper.readValue(result.getResponse().getContentAsString(),
            new TypeReference<>() {
            });
        List<UUID> actual = response.stream().map(ProcessDto::getProcessId)
            .collect(Collectors.toList());
        List<UUID> expected = actual.stream().sorted().collect(Collectors.toList());

        Assertions.assertEquals(expected, actual);
    }

}

