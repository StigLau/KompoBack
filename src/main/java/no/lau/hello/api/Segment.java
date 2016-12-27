package no.lau.hello.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by stisse on 12/27/16.
 */
public class Segment {
	public final String id;
	public final int start;
	public final int end;

	public Segment(String id, int start, int end) {

		this.id = id;
		this.start = start;
		this.end = end;
	}

	@JsonProperty
	String getId() {
		return id;
	}

	@JsonProperty
	int getStart() {
		return start;
	}

	@JsonProperty
	int getEnd() {
		return end;
	}
}
