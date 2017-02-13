package com.crawler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "parse")
public class ParseProperties {
    private String from = "file";
    private String url;
    private String resourcePath = "burnley-chelsea.html";

    public void setFrom(String from) {
        this.from = from;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getFrom() {
        return from;
    }

    public String getUrl() {
        return url;
    }

    public String getResourcePath() {
        return resourcePath;
    }
}
