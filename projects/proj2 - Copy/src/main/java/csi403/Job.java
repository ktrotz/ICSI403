package csi403;

class Job {
    // Variables
    private String jobName;
    private int priority;

    // Constructors
    Job(String argJob, int argPrio){
        this.jobName = argJob;
        this.priority = argPrio;
    }

    // Getters/Setters
    String getJobName() {
        return jobName;
    }
    int getPriority() {
        return priority;
    }
}
