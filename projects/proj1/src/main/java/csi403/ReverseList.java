package csi403;

// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;

// Extend HttpServlet class
public class ReverseList extends HttpServlet 
{

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
			return;
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

		// Extract numbers from JSONArray
		int[] array = new int[inArray.size()];
		for(int i = 0; i < inArray.size(); i++)
		{          
			array[i] = inArray.getInt(i);
		}

		// Sort the array and find time taken in MS
		long timeMS = System.currentTimeMillis();
		array = insertionSort(array);
		timeMS = System.currentTimeMillis() - timeMS;

		// Build array for printing
		JsonArrayBuilder outArrayBuilder = Json.createArrayBuilder();
		for (int index = 0; index < array.length; index++)
		{
			outArrayBuilder.add(array[index]);
		}

		//Print results
		out.println("{ \"outList\" : " + outArrayBuilder.build().toString() + ",");
		out.println(" \"algorithm\" : \"Insertion Sort\",");
		out.println(" \"timeMS\" : " + Long.toString(timeMS) + "}");
	}

	// Accepts an array and returns that array sorted
	private int[] insertionSort(int[] input)
	{
		int temp;
		// Assume first number in the right place
		// Loop (n-1) times
		for (int i = 1; i < input.length; i++)
		{
			// Loop i times
			for(int j = i ; j > 0 ; j--)
			{
				if(input[j] < input[j-1])
				{
					temp = input[j];
					input[j] = input[j-1];
					input[j-1] = temp;
				}
			}
		}
		return input;
	}


	public void destroy()
	{
		// Do any required tear-down here, likely nothing.
	}
}

