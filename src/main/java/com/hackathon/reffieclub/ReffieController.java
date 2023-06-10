package com.hackathon.reffieclub;

import com.hackathon.reffieclub.model.JobAdvocateComment;
import com.hackathon.reffieclub.model.JobDetails;
import com.hackathon.reffieclub.model.JobReferral;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reffie")
public class ReffieController {

    private final ReffieService reffieService;

    public ReffieController(ReffieService reffieService) {
        this.reffieService = reffieService;
    }

    @GetMapping("/jobs")
    public List<JobDetails> getAllJobs() {
        return reffieService.getJobDetails();
    }

    @GetMapping("/jobs/{id}")
    public JobDetails getJobDetailsById(@PathVariable Long id) {
        return reffieService.getJobDetailsById(id);
    }

    @PostMapping("/refer/{jobId}/{fromUserId}/{toUserId}")
    public JobDetails getJobDetailsById(@PathVariable Long jobId, @PathVariable Long fromUserId, @PathVariable Long toUserId) {
        return reffieService.referJob(jobId, fromUserId, toUserId);
    }


    @GetMapping("/refer/referredToMe/{userId}")
    public List<JobDetails> getJobsReferredToMe(@PathVariable Long userId) {
        return reffieService.getJobsReferredToMe(userId);
    }

    @GetMapping("/refer/referredByMe/{userId}")
    public List<JobReferral> getJobsReferredByMe(@PathVariable Long userId) {
        return reffieService.getJobsReferredByMe(userId);
    }

    @GetMapping("/refer/score/{userId}")
    public int getMyReferralScore(@PathVariable Long userId) {
        return reffieService.getMyReferralScore(userId);
    }

    @PostMapping("/comment")
    public JobDetails commentToJob(@RequestBody JobAdvocateComment jobAdvocateComment){
        return reffieService.commentOnJob(jobAdvocateComment);
    }

}
