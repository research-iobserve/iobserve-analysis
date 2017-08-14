package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.json.JsonArray;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * This class handles the requests from the visualization stages for testing reasons.
 *
 * @author jweg
 *
 */
public final class TestHandler implements HttpHandler {

    @Override
    public void handle(final HttpExchange t) throws IOException {
        System.out.println("@Before in handle(HttpExchange)");
        final InputStream is = t.getRequestBody();
        System.out.printf("request body of HttpExchange as input stream: %s\n", is);
        // clazz = JSON Wurzel Objekt

        final ObjectMapper mapper = new ObjectMapper();
        //
        //// Einzel element: nodegroup- and node-changelog
        final JsonArray object = mapper.readValue(is, JsonArray.class);
        System.out.printf("JsonObject in AllocationVisualizationTestClass: %s\n", object.get(0));
        //
        //// Für listen
        // final List<T> list = mapper.readValue(response.getContent(),
        // mapper.getTypeFactory().constructCollectionType(List.class,
        // clazz));

        // read(is); // .. read the request body
        final String response = "This is the response";
        t.sendResponseHeaders(200, response.length());
        final OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }

}
