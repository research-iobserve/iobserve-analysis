package org.iobserve.analysis.service.updater;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import teetime.framework.AbstractConsumerStage;

// TODO the data type of the input element must be clarified

/**
 * This stage is triggered by an analysis update.
 *
 * @author Reiner Jung
 *
 */
public class VisualizationUpdateStage extends AbstractConsumerStage<Object> {

    private final static String USER_AGENT = "iObserve/0.0.1";

    private final URL outputURL;

    public VisualizationUpdateStage(URL outputURL) {
        this.outputURL = outputURL;
    }

    @Override
    protected void execute(Object element) {
        // TODO Auto-generated method stub
        try {
            this.sendPostRequest(element.toString());
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Send change log updates to the visualization.
     *
     * @param systemId
     * @param message
     * @throws IOException
     */
    private void sendPostRequest(final String message) throws IOException {

        final HttpURLConnection connection = (HttpURLConnection) this.outputURL.openConnection();

        // add request header
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", "application/json; charset=utf-8");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // Send post request
        connection.setDoOutput(true);
        final DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(message);
        wr.flush();
        wr.close();

        final int responseCode = connection.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + this.outputURL);
        System.out.println("Post parameters : " + message);
        System.out.println("Response Code : " + responseCode);

        final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        final StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result
        System.out.println(response.toString());

    }
}
