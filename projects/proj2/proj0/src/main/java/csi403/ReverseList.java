package csi403;

// Import required java libraries
import java.io.*;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;

// Extend HttpServlet class
public class ReverseList extends HttpServlet {

  // Standard servlet method 
  public void init() throws ServletException
  {
      // Do any required initialization here - likely none
  }

  // Standard servlet method - we will handle a POST operation
  public void doPost(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      // Catch exceptions
      try
      {
          doService(request, response);
      }
      catch(Exception e)
      {
          response.setContentType("application/json");
          PrintWriter out = response.getWriter();
          out.println("{ \"message\" : \"Malformed JSON\"}");
      }
  }

  // Standard servlet method - we will not respond to GET
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      // Set response content type and return an error message
      response.setContentType("application/json");
      PrintWriter out = response.getWriter();
      out.println("{ 'message' : 'Use POST!'}");
  }

  // Parses lists e.g. {"inList" : [5, 32, 3, 12]}
  // Returns the list sorted using insertion sort
  private void doService(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      // Set response content type to be JSON
      response.setContentType("application/json");
      // Send back the response JSON message
      PrintWriter out = response.getWriter();

      // Get received JSON data from HTTP request
      BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
      String jsonStr = "";
      if(br != null){
          jsonStr = br.readLine();
      }
      
      // Create JsonReader object
      StringReader strReader = new StringReader(jsonStr);
      JsonReader reader = Json.createReader(strReader);

      // Get the singular JSON object (name:value pair) in this message.    
      JsonObject obj = reader.readObject();
      // From the object get the array named "inList"
      JsonArray inArray = obj.getJsonArray("inList");

      Comparator<Job> comparator = new JobComparator();
      PriorityQueue<Job> priorityQueue = new PriorityQueue<>(inArray.size(),comparator);
      String name;
      int priority;

      for(int index = 0; index < inArray.size(); index++)
      {
          if(inArray.getJsonObject(index).getString("cmd").equals("enqueue"))
          {
              name = inArray.getJsonObject(index).getString("name");
              priority = inArray.getJsonObject(index).getInt("pri");
              
              if(priority < 0)
              {
                  out.println("{ \"message\" : \"Invalid Priority\"}");
                  return;
              }
              
              priorityQueue.add(new Job(name, priority));
          }
          
          else if(inArray.getJsonObject(index).getString("cmd").equals("dequeue"))
          {
              if(priorityQueue.poll() == null)
              {
                  out.println("{ \"message\" : \"Empty Queue\"}");
                  return;
              }
          }
          
          else 
          {
              out.println("{ \"message\" : \"Malformed JSON\"}");
              return;
          }
      }

      // Build array for printing
      JsonArrayBuilder outArrayBuilder = Json.createArrayBuilder();
      int size = priorityQueue.size();
      
      for (int index = 0; index < size; index++) 
      {
          outArrayBuilder.add(priorityQueue.poll().getName());
      }


      //Print results
      out.println("{ \"outList\" : " + outArrayBuilder.build().toString() + "}");
  }
    

  public void destroy()
  {
      // Do any required tear-down here, likely nothing.
  }
}

