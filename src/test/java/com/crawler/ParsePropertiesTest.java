package com.crawler;

import com.crawler.config.ParseProperties;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ParsePropertiesTest {

    @Test
    public void shouldSettersAndGettersBeMatched() {
        String from = "file";
        String url = "http://test.com";
        String resourcePath = "classpath:test.com";

        ParseProperties parseProperties = new ParseProperties();
        parseProperties.setFrom(from);
        parseProperties.setUrl(url);
        parseProperties.setResourcePath(resourcePath);

        Assert.assertEquals(from, parseProperties.getFrom());
        Assert.assertEquals(url, parseProperties.getUrl());
        Assert.assertEquals(resourcePath, parseProperties.getResourcePath());
    }
}
