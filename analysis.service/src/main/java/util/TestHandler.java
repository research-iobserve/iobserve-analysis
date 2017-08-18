package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * This class handles the requests from the visualization stages for testing reasons.
 *
 * @author jweg
 *
 */
public final class TestHandler implements HttpHandler {

    private static String requestBody;

    @Override
    public void handle(final HttpExchange t) throws IOException {

        final InputStream is = t.getRequestBody();

        TestHandler.requestBody = IOUtils.toString(is, "UTF-8");

        final String response = "Changelog received";
        t.sendResponseHeaders(200, response.length());
        final OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }

    public static String getRequestBody() {
        return TestHandler.requestBody;
    }

}
