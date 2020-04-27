package csi403;

// Import required java libraries
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;



// Extend HttpServlet class
public class ReverseList extends HttpServlet
{
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

		JsonArrayBuilder outArrayBuilder = Json.createArrayBuilder();

		out.println("{ \"outList\" : ");

		if(inArray.size() != 0)
		{
			for(int index = 0; index < inArray.size(); index++)
			{
				String val = inArray.getString(index); //json string at index
				int sum = sumASCII(val);

				JsonArrayBuilder jsonArray = Json.createArrayBuilder(); //to print outList


				for(int x = index + 1; x < inArray.size(); index++)
				{
					String val2 = inArray.getString(x); //next json string
					int sum2 = sumASCII(val2); //next string ascii value

					//Check if next index has same key as current index
					if(inArray.getString(x).length() > 0 && sum2 == sum)
					{
						//add two values to same outList array
						jsonArray.add(val); 
						jsonArray.add(val2);

						//JsonArray array = jsonArray.build();
						outArrayBuilder.add(jsonArray);

						//JsonArray outList = outArrayBuilder.build();     			 
					}
				}

				//Print results
				out.println(outArrayBuilder.build().toString());
			}
		}

		out.println( "}");
	}

	// Standard Servlet method
	public void destroy()
	{
		// Do any required tear-down here, likely nothing.
	}

	//Sum of lower case ASCII values
	public static int sumASCII(String str) 
	{
		String s = str.toLowerCase(); //json input to lower case
		int asc = 0;

		for(int i = 0; i < s.length(); i++) 
		{
			asc = asc + s.charAt(i); //adding ASCII
		}

		return asc;
	}
}


