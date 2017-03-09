package no.lau.kompoback.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Komposition {
    //@JsonProperty
    //public long id; //Not in use yet
    @JsonProperty
    public String name;
    @JsonProperty
    public List<Segment> segments;

    /*
    public void addSegment(String id, int start, int end) {
        this.segments.add(new Segment(id, start, end));
}
     */
}