package ru.patseev.authenticationservice.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Конфигурация WebClient для взаимодействия с другими сервисами.
 */
@Configuration
@EnableWebFlux
public class WebClientConfiguration {

	/**
	 * Создает и возвращает объект WebClient.Builder с поддержкой балансировки нагрузки.
	 *
	 * @return Объект WebClient.Builder.
	 */
	@Bean
	@LoadBalanced
	public WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}
}