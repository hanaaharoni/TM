package com.hanaah.iptiq.core.config;

import com.hanaah.iptiq.core.DefaultTaskManager;
import com.hanaah.iptiq.core.TaskManager;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class TaskManagerConfiguration {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public TaskManager taskManager() {
		return new DefaultTaskManager(5);
	}
}
