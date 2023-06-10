package com.hackathon.reffieclub.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JobFetchResponse {

    public JobFetchResponse() {
    }

    public JobFetchResponse(String entryCount, String message, JobData[] jobData) {
        this.entryCount = entryCount;
        this.message = message;
        this.jobData = jobData;
    }

    @JsonProperty("entry_count")
    private String entryCount;

    private String message;

    @JsonProperty("data")

    private JobData[] jobData;

    public String getEntryCount() {
        return entryCount;
    }

    public void setEntryCount(String entryCount) {
        this.entryCount = entryCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JobData[] getJobData() {
        return jobData;
    }

    public void setJobData(JobData[] jobData) {
        this.jobData = jobData;
    }
}
