package csi403;

import java.util.Comparator;

class JobComparator implements Comparator<Job> //implements defined object from ReverseList
{
    public int compare(Job job1, Job job2)
    {
        return (job1.getPri() - job2.getPri());
    } //field declaration
}
