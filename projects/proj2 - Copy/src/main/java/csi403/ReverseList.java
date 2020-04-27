package csi403;

// Import required java libraries
import java.io.*;
import java.util.Comparator;
import java.util.PriorityQueue;
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

      for(int i=0; i < inArray.size(); i++){
          if(inArray.getJsonObject(i).getString("cmd").equals("enqueue")) {
              name = inArray.getJsonObject(i).getString("name");
              priority = inArray.getJsonObject(i).getInt("pri");
              if(priority < 0){
                  out.println("{ \"message\" : \"Priority less than zero\"}");
                  return;
              }
              priorityQueue.add(new Job(name, priority));
          }
          else if(inArray.getJsonObject(i).getString("cmd").equals("dequeue")){
              if(priorityQueue.poll() == null){
                  out.println("{ \"message\" : \"Dequeued empty priority queue\"}");
                  return;
              }
          }
          else {
              out.println("{ \"message\" : \"Malformed JSON\"}");
              return;
          }
      }

      // Build array for printing
      JsonArrayBuilder outArrayBuilder = Json.createArrayBuilder();
      int size = priorityQueue.size();
      for (int i = 0; i < size; i++) {
          outArrayBuilder.add(priorityQueue.poll().getJobName());
      }

      //Print results
      out.println("{ \"outList\" : " + outArrayBuilder.build().toString() + "}");

  }
    
  // Standard Servlet method
  public void destroy()
  {
      // Do any required tear-down here, likely nothing.
  }
}

