package com.crawler.model;

import java.util.List;
import java.util.Map;

public class Event {
    private final String home;
    private final String away;
    private final String started_at;
    private final Map<String, Map<String, List<Ratio>>> periods;

    public Event(String home, String away, String started_at, Map<String, Map<String, List<Ratio>>> periods) {
        this.home = home;
        this.away = away;
        this.started_at = started_at;
        this.periods = periods;
    }
}
