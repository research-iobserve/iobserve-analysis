package org.iobserve.analysis.service.updater;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;

import org.iobserve.analysis.service.services.NodeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Tests for AllocationVisualizationStage.
 *
 * @author jweg
 *
 */
public class AllocationVisualizationStageTest {

    @Before
    public void initServer() throws IOException {
        final class MyHandler implements HttpHandler {
            @Override
            public void handle(final HttpExchange t) throws IOException {

                final InputStream is = t.getRequestBody();
                // // clazz = JSON Wurzel Objekt
                //
                final ObjectMapper mapper = new ObjectMapper();
                //
                //// Einzel element
                final NodeService object = mapper.readValue(is, NodeService.class);
                //
                //// Für listen
                // final List<T> list = mapper.readValue(response.getContent(),
                // mapper.getTypeFactory().constructCollectionType(List.class,
                // clazz));

                // read(is); // .. read the request body
                // final String response = "This is the response";
                // t.sendResponseHeaders(200, response.length());
                // final OutputStream os = t.getResponseBody();
                // os.write(response.getBytes());
                // os.close();
            }
        }
        final HttpServer server = HttpServer.create(new InetSocketAddress(9090), 0);
        server.createContext("/test", new MyHandler());
        server.start();
    }

    @Test
    public void test() {
        Assert.fail("Not yet implemented");
    }

}
