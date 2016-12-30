package no.lau.kompoback.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by stisse on 12/27/16.
 */
public class Segment {
    @JsonProperty
    public String id;
    @JsonProperty
    public int start;
    @JsonProperty
    public int end;
    public Segment() { }
    public Segment(String id, int start, int end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }
}