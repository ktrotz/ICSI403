package csi403;


// Import required java libraries
import java.io.*;
import java.util.Map;
import java.util.HashMap;

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
		// Catch exceptions: I changed this
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


	// Our main worker method
	// Parses messages e.g. {"inList" : [5, 32, 3, 12]}
	// Returns the list reversed.   
	private void doService(HttpServletRequest request,
			HttpServletResponse response)
					throws ServletException, IOException
	{
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

		//JsonArrayBuilder outArrayBuilder = Json.createArrayBuilder();

		//new code
		JsonArray friends;

		JsonArrayBuilder outArrayBuilder = Json.createArrayBuilder();


		//try
		//{
		Map<String, String> dict = new HashMap<>(); 

		for(int index = 0; index < inArray.size(); index++)
		{  		    
			friends = inArray.getJsonObject(index).getJsonArray("friends");

			String key = friends.getString(0);
			String value = friends.getString(1);

			dict.put(key, value); //puts the values into the hash map dict1     		  
		}

		
		// Set response content type to be JSON
		response.setContentType("application/json");
		// Send back the response JSON message
		PrintWriter out = response.getWriter();
		out.println("{ \"outList\" : ");

		//  index++;
		//  } 

		//create a loop
		//the size of the loop will be the size of the keySet
		//call a function to link key1 to value2




		//in a hashmap find the value of the key
		// ---- "friends": ["Albert", "Betty"];
		//key = Albert, value = Betty 
		//key2 = Betty, value = Cathy

		//find all the keys associated with the initial key direct
		//if there any keys found link to the value of the original key and set the pair as foaf
		//pair = key1 and value2

		
		//Go through the all of the keys
		for(String k: dict.keySet())
		{
			//value
			String x = dict.get(k); 
					
			for(String key: dict.keySet()) //keySet method is used to get a Set view of the keys contained in this map.
			{						
				//if key = value
				if(key.equals(x))
				{					
					//out.println(k + ",");
					//out.println(dict.get(key) + ",");	
					
					String[] newArr = new String[2];
					
					newArr[0] = k;
					newArr[1] = dict.get(key);
					
					//if(newArr[0] != "Cathy" && newArr[1] != "Albert" )
					
					for(int index = 0; index < 2; index++)
					{
						outArrayBuilder.add(newArr[index]);				
					}
					
					
					out.println(outArrayBuilder.build().toString());
				}
			} 
		}
		
		out.println("}");



	} 
		



	// Standard Servlet method
	public void destroy()
	{
      // Do any required tear-down here, likely nothing.
  }

}

