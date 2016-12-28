
package no.lau.hello.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Komposition {
    @JsonProperty
    public long id;
    @JsonProperty
    public String name;
    @JsonProperty
    public int start;
    @JsonProperty
    public int end;
    @JsonProperty
    public List<Segment> segments;

    /*
    public void addSegment(String id, int start, int end) {
        this.segments.add(new Segment(id, start, end));
}
     */
}
