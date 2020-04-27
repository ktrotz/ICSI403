package csi403;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class JsonParser {

    public JsonParser(){
    }

    //Given points in json return a Polygon object with those points as vertices (Assuming 19x19 grid, 0-18)
    public Polygon ParsePolygon(String jsonStr) throws Exception
    {
        StringReader strReader = new StringReader(jsonStr);
        JsonReader reader = Json.createReader(strReader);

        // Get the singular JSON object (name:value pair) in this message.
        JsonObject obj = reader.readObject();
        // From the object get the array named "inList"
        JsonArray inArray = obj.getJsonArray("inList");

        Point[] vertices = new Point[inArray.size()];
        
        int x;
        int y;

        if(inArray.size() < 3) 
        {
            throw new Exception("Less than three sides");
        }

        for(int i = 0; i < inArray.size(); i++)
        {
            x = inArray.getJsonObject(i).getInt("x");
            y = inArray.getJsonObject(i).getInt("y");
        
            //Erroneous input
            if( !(x >= 0 && x <= 18 && y >= 0 && y <= 18)) 
            {
                throw new Exception("Point outside bounds");
            }
            
            vertices[i] = new Point(x,y);
        }

        return new Polygon(vertices);
    }
}
