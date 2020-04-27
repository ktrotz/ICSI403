package csi403;

class Job 
{
    private String name;
    private int pri;

    // Constructor
    Job(String n, int p)
    {
        this.name = n;
        this.pri = p;
    }

    //Get Job name
    String getName() 
    {
        return name;
    }
    
    //Get Job priority
    int getPri()
    {
        return pri;
    }
}
