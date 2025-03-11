package com.popov.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class AppApplication {
    @Value("${spring.application.name}")
    private String applicationName;

	@Value("${app.host}")
    private String host;
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(AppApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	@EventListener
    public void onApplicationEvent(WebServerInitializedEvent event) {
        int port = event.getWebServer().getPort();
		System.out.println("\n----------------------------------------------------------");
        System.out.println("Application '" + applicationName + "' is running at: " + host + ":" + port);
    }

}
