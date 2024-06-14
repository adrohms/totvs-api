package com.totvs;

import com.totvs.infrastructure.config.TotvsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableConfigurationProperties(TotvsProperties.class)
@EnableJpaRepositories(basePackages =
		{"com.totvs.domain.account.repository",
				"com.totvs.domain.common.repository",
				"com.totvs.domain.crm.repository"})
public class TotvsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TotvsApplication.class, args);
	}

}
