package com.hanaah.config;

import com.hanaah.taskmanager.FifoTaskManager;
import com.hanaah.taskmanager.PrefixCapacityTaskManager;
import com.hanaah.taskmanager.PriorityTaskManager;
import com.hanaah.taskmanager.TaskManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class TaskManagerConfiguration {

    private final int capacity;
    private final String type;

    public TaskManagerConfiguration(@Value("${task.manager.capacity}") int capacity,
        @Value("${task.manager.type}") String type) {
        this.capacity = capacity;
        this.type = type;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public TaskManager taskManager() {
        if (("fifo").equals(this.type)) {
            return new FifoTaskManager(this.capacity);
        }
        if (("prefixed").equals(this.type)) {
            return new PrefixCapacityTaskManager(this.capacity);
        }
        if (("priority").equals(this.type)) {
            return new PriorityTaskManager(this.capacity);
        }
        throw new IllegalArgumentException(
            "Please specify task manager management type in application.yml. [fifo/prefixed/priority]");
    }
}
