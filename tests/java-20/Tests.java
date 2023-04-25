import java.util.Map;
import java.util.HashMap;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.google.gson.Gson;

final Gson gson = new Gson();

public RuntimeResponse main(RuntimeRequest req, RuntimeResponse res) throws Exception {
    String payloadString = req.getPayload() == null || req.getPayload().isEmpty() 
        ? "{}" 
        : req.getPayload();

    Map<String, Object> payload = gson.fromJson(payloadString, Map.class);

    String header = "";
    Map<String, String> headers = req.getHeaders();
    if (headers != null && headers.containsKey("x-test-header")) {
        header = headers.get("x-test-header");
    }

    String varData = "";
    Map<String, String> variables = req.getVariables();
    if (variables != null && variables.containsKey("test-variable")) {
        varData = variables.get("test-variable");
    }

    String id = "1";
    if (payload.containsKey("id") && payload.get("id") != null) {
        id = payload.get("id").toString();
    }

    URL url = new URL("https://jsonplaceholder.typicode.com/todos/" + id);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");
    con.getResponseCode();

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer todoBuffer = new StringBuffer();
    while ((inputLine = in.readLine()) != null) {
        todoBuffer.append(inputLine);
    }
    in.close();
    con.disconnect();

    Map<String, Object> todo = gson.fromJson(todoBuffer.toString(), Map.class);

    Map<String, Object> data = new HashMap<>();
    data.put("isTest", true);
    data.put("message", "Hello Open Runtimes 👋");
    data.put("header", header);
    data.put("variable", varData);
    data.put("todo", todo);

    System.out.println("String1");
    System.out.println(42);
    System.out.println(4.2);
    System.out.println(true);

    System.out.println("String2");
    System.out.println("String3");
    System.out.println("String4");
    System.out.println("String5");

    return res.json(data);
}
