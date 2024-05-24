package com.dal.asdc.reconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class ReconnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReconnectApplication.class, args);
	}

}
