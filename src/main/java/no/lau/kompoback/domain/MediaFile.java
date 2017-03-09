package no.lau.kompoback.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Stig@Lau.no
 */
public class MediaFile {

    @JsonProperty
    public String fileName;
    @JsonProperty
    public long startingOffset;
    @JsonProperty
    public String checksum;
    //@JsonProperty
    //public String type; //youtube
    //@JsonProperty
    //public String extension; //mp4
}