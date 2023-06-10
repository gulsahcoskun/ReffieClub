package com.hackathon.reffieclub.model;

import java.util.List;

public class JobDetails {

    public JobDetails() {
    }

    public JobDetails(long jobId, JobData data, int referralCount, int numberOfLikes, List<JobAdvocateComment> jobAdvocateComments) {
        this.jobId = jobId;
        this.data = data;
        this.referralCount = referralCount;
        this.numberOfLikes = numberOfLikes;
        this.jobAdvocateComments = jobAdvocateComments;
    }

    private long jobId;
    private JobData data;
    private int referralCount;
    private int numberOfLikes;
    private List<JobAdvocateComment> jobAdvocateComments;

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public JobData getData() {
        return data;
    }

    public void setData(JobData data) {
        this.data = data;
    }

    public int getReferralCount() {
        return referralCount;
    }

    public void setReferralCount(int referralCount) {
        this.referralCount = referralCount;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public List<JobAdvocateComment> getJobAdvocateComments() {
        return jobAdvocateComments;
    }

    public void setJobAdvocateComments(List<JobAdvocateComment> jobAdvocateComments) {
        this.jobAdvocateComments = jobAdvocateComments;
    }
}
