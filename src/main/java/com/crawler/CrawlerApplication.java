package com.crawler;

import com.crawler.config.ParseProperties;
import com.crawler.service.JsoupService;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import java.util.NoSuchElementException;

@SpringBootApplication
@EnableConfigurationProperties(ParseProperties.class)
public class CrawlerApplication {

	private final JsoupService jsoupService;
	private final ParseProperties parseProperties;

	@Autowired
	public CrawlerApplication(JsoupService jsoupService, ParseProperties parseProperties) {
		this.jsoupService = jsoupService;
		this.parseProperties = parseProperties;
	}

	public static void main(String[] args) {
		SpringApplication.run(CrawlerApplication.class, args);
	}

	@Bean
	@Profile("default")
	CommandLineRunner init() {
		return args -> {
			Document page;

			switch (parseProperties.getFrom()) {
				case "file":
					page = jsoupService.getPage(new ClassPathResource(parseProperties.getResourcePath()));
					break;
				case "web":
					page = jsoupService.getPage(parseProperties.getUrl());
					break;
				default:
					throw new NoSuchElementException();
			}

			String sportJson = jsoupService.getSportJson(page);
			System.out.println(sportJson);
		};
	}


}
