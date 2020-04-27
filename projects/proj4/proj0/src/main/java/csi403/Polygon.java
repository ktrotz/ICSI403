package csi403;

public class Polygon
{
    private int n;
    private Point[] vertices;

    //Constructors
    public Polygon(Point[] vertices) 
    {
        this.n = vertices.length;
        this.vertices = vertices;
    }

    //Returns a double array containing all x points
    public double[] getXPoints()
    {
        double[] xPoints = new double[n];

        for(int i = 0; i < n; i++)
        {
            xPoints[i] = (double) vertices[i].getX();
        }

        return xPoints;
    }
    
    //Returns a double array containing all y points
    public double[] getYPoints()
    {
        double[] yPoints = new double[n];

        for(int i = 0; i < n; i++)
        {
            yPoints[i] = (double) vertices[i].getY();
        }

        return yPoints;
    }

    //Returns a string containing the vertices listed as pairs of points
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < n; i++)
        {
            stringBuilder.append("[").append(vertices[i].getX()).append(",").append(vertices[i].getY());
            
            if(i != (n-1))
            {
                stringBuilder.append("], ");
            }
            
            else
            {
                stringBuilder.append("]");
            }
        }

        return stringBuilder.toString();
    }

    //Returns true if the point is contained inside the polygon, not including sides
    public boolean containsPoint(Point test)
    {
        boolean result = false;
        int x1;
        int y1;
        int x2;
        int y2;
        double angle = 0;

        for(int i = 0; i < n; i++) 
        {
            x1 = vertices[i].getX() - test.getX();
            y1 = vertices[i].getY() - test.getY();
        
            if (i + 1 == n)
            {
                x2 = vertices[0].getX() - test.getX();
                y2 = vertices[0].getY() - test.getY();
            }
            
            else
            {
                x2 = vertices[i + 1].getX() - test.getX();
                y2 = vertices[i + 1].getY() - test.getY();
            }
            
            angle = angle + Angle2D(x1, y1, x2, y2);
        }
        
        if(Math.abs(angle) < Math.PI)
        {
            result = false;
        }
        
        else
        {
            result = !liesOnLines(test);
        }

        return result;
    }

    //Returns the angle between two points
    public double Angle2D (double x1, double y1, double x2, double y2)
    {
        double dtheta;
        double theta1;
        double theta2;
        
        theta1 = Math.atan2(y1, x1);
        theta2 = Math.atan2(y2, x2);
        dtheta = theta2 - theta1;

        while(dtheta > Math.PI)
        {
            dtheta = dtheta - (2 * Math.PI);
        }
        
        while(dtheta < -Math.PI)
        {
            dtheta = dtheta + (2 * Math.PI); //dtheta += (2*Math.PI);
        }

        return dtheta;
    }

    //Returns true if the point is intersected by any side of the polygon
    public boolean liesOnLines (Point test)
    {
        boolean result = false;
        Point A;
        Point B;
        double dxc;
        double dyc;
        double dxl;
        double dyl;
    
        for(int i = 0; i < n; i++)
        {
            A = vertices[i];
            
            if(i + 1 == n)
            {
                B = vertices[0];
            }
            
            else
            {
                B = vertices[i + 1];
            }

            dxc = test.getX() - A.getX();
            dyc = test.getY() - A.getY();
            dxl = B.getX() - A.getX();
            dyl = B.getY() - A.getY();

            if(Math.abs( ((dxc*dyl) - (dyc*dxl)) ) < 0.1)
            {
                if(Math.abs(dxl) >= Math.abs(dyl))
                {
                    result = dxl  > 0 ?    //wtfffff
                   
                    		A.getX() <= test.getX() && test.getX() <= B.getX():
                    		B.getX() <= test.getX() && test.getX() <= A.getX();
                }
                
                else
                {
                    result = dyl  > 0 ?  //whattttttt the fuckkkkk
                            A.getY() <= test.getY() && test.getY() <= B.getY():
                            B.getY() <= test.getY() && test.getY() <= A.getY();
                }
                
                break;
            }
        }

        return result;
    }

}
