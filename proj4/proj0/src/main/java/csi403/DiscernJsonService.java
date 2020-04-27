package csi403;


// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


// Extend HttpServlet class
public class DiscernJsonService extends HttpServlet { //EndpointHandler

	 // Standard servlet method 
	  public void init() throws ServletException
	  {
	      // Do any required initialization here - likely none
	  }

	  // Standard servlet method - we will handle a POST operation
	  public void doPost(HttpServletRequest request,
	                    HttpServletResponse response)
	            throws ServletException, IOException {
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

	  private void doService(HttpServletRequest request,
	                    HttpServletResponse response)
	          throws Exception {
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

	      JsonParser parser = new JsonParser();
	      Polygon poly = parser.ParsePolygon(jsonStr);
	      int n = 0;

	      for(int i = 0; i < 19; i++)
	      {
	          for(int j = 0; j < 19; j++)
	          {
	              if(poly.containsPoint(new Point(i,j)) )
	              {
	                  n++;
	              }
	          }
	      }

	      out.println("{ \"count\" : "+ n +" }");

	  }
	}

