package com.example.pharmacy.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.net.URI;

@Component
public class BrowserLauncher implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(BrowserLauncher.class);

    @Value("${server.port:8081}")
    private int port;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String url = "http://localhost:" + port + "/";
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
                logger.info("Browser opened at {}", url);
            } else {
                logger.warn("Desktop not supported. Please open {} manually.", url);
            }
        } catch (Exception e) {
            logger.error("Failed to open browser at {}", url, e);
        }
    }
}

