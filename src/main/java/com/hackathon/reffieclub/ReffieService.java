package com.hackathon.reffieclub;

import com.hackathon.reffieclub.model.*;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ReffieService {

    private static List<JobDetails> jobsDetails;
    private static Map<Long, List<JobReferral>> referralMap;

    private static Map<Long, Integer> referralScore;

    //Acts like a dummy db to initialize data
    {
        List<JobData> jobsList = initializeJobsList();
        jobsDetails = populateJobDetails(jobsList);
        referralMap = new HashMap<>();
        referralScore = new HashMap<>();
    }


    public List<JobDetails> getJobDetails() {
        return jobsDetails;
    }

    @Nullable
    public JobDetails getJobDetailsById(Long id) {
        return jobsDetails.stream().filter(job -> job.getJobId() == id).findFirst().orElse(null);
    }

    @Nullable
    public JobDetails referJob(Long jobId, Long fromUserId, Long toUserId) {
        JobDetails job = getJobDetailsById(jobId);
        if (job == null) {
            return null;
        }

        List<JobReferral> jobReferrals = referralMap.get(fromUserId);
        if (jobReferrals == null) {
            jobReferrals = new ArrayList<>();
        }
        jobReferrals.add(new JobReferral(jobId, toUserId));
        referralMap.put(fromUserId, jobReferrals);

        increaseReferralScore(fromUserId);

        job.setReferralCount(job.getReferralCount() + 1);
        return job;
    }

    public List<JobDetails> getJobsReferredToMe(Long userId) {
        List<JobDetails> jobsDetailsList = new ArrayList<>();
        referralMap.forEach((key, value) -> value.stream().filter(r -> Objects.equals(r.toUserId(), userId))
                .map(JobReferral::jobId).map(this::getJobDetailsById).findFirst().ifPresent(jobsDetailsList::add));
        return jobsDetailsList;
    }

    public List<JobReferral> getJobsReferredByMe(Long userId) {
        return referralMap.get(userId);
    }

    public int getMyReferralScore(Long userId) {
        return referralScore.getOrDefault(userId, 0);
    }

    public JobDetails commentOnJob(JobAdvocateComment jobAdvocateComment) {
        JobDetails jobDetails = getJobDetailsById(jobAdvocateComment.jobId());
        if (jobDetails == null) {
            return null;
        }
        List<JobAdvocateComment> jobAdvocateComments = jobDetails.getJobAdvocateComments();
        if (jobAdvocateComments == null || jobAdvocateComments.isEmpty()) {
            jobAdvocateComments = new ArrayList<>();
        }
        jobAdvocateComments.add(new JobAdvocateComment(jobAdvocateComment.jobId(), jobAdvocateComment.userId(), jobAdvocateComment.name(), jobAdvocateComment.position(), jobAdvocateComment.comment()));
        jobDetails.setJobAdvocateComments(jobAdvocateComments);

        increaseReferralScore(jobAdvocateComment.userId());

        return jobDetails;
    }

    private static void increaseReferralScore(long userId) {
        int score = referralScore.getOrDefault(userId, 0);
        referralScore.put(userId, score + 1);
    }

    private List<JobData> initializeJobsList() {
        var uri = "TO_BE_CHANGED_WITH_DATA_SOURCE_URL";
        var restTemplate = new RestTemplate();
        JobFetchResponse response = restTemplate.getForObject(uri, JobFetchResponse.class);
        if (response == null) {
            return Collections.emptyList();
        }

        return populateWithId(Arrays.asList(response.getJobData()));
    }

    private List<JobData> populateWithId(List<JobData> jobsList) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        jobsList.forEach(job -> {
            int id = atomicInteger.incrementAndGet();
            job.setId(id);
        });
        return jobsList;
    }

    private List<JobDetails> populateJobDetails(List<JobData> jobsList) {
        List<JobDetails> jobDetailsList = new ArrayList<>();
        jobsList.forEach(job -> {
            JobDetails jobDetails = new JobDetails(job.getId(), job, 0, 0, Collections.emptyList());
            jobDetailsList.add(jobDetails);
        });
        return jobDetailsList;
    }

}
