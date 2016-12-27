
package no.lau.hello.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Komposition {
    private long id;

    public String name;
    public int start;
    public int end;
    private List<Segment> segments = new ArrayList<>();

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty public String getName() {
        return name;
    }
    @JsonProperty public int getStart() {
        return start;
    }
    @JsonProperty public int getEnd() {
        return end;
    }

    @JsonProperty List<Segment> getSegments() { return segments; }

    public void addSegment(String id, int start, int end) {
        this.segments.add(new Segment(id, start, end));
    }
}
