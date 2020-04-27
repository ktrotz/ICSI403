package csi403;

import java.util.Comparator;

class JobComparator implements Comparator<Job> {
    public int compare(Job job1, Job job2)
    {
        return (job1.getPriority() - job2.getPriority());
    }
}
