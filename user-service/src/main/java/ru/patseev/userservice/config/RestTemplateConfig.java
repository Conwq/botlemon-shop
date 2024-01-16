package ru.patseev.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Конфигурационный класс для получения бина RestTemplate
 */
@Configuration
public class RestTemplateConfig {

	/**
	 * Создает новый bean объекта RestTemplate.
	 *
	 * @return Новый объект RestTemplate.
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}