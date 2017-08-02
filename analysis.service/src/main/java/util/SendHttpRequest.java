package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonWriter;

public final class SendHttpRequest {

    private static final String USER_AGENT = "iObserve/0.0.2";

    private SendHttpRequest() {

    }

    /**
     * Send change log updates to the visualization.
     *
     * @param modelData
     * @param systemUrl
     * @param changelogUrl
     * @throws IOException
     */
    public static void post(final JsonObject modelData, final URL systemUrl, final URL changelogUrl)
            throws IOException {
        final HttpURLConnection connection;
        // prepare data for changelog constraint of deployment visualization
        final JsonArray dataArray = Json.createArrayBuilder().add(modelData).build();

        final JsonString type = (JsonString) modelData.get("type");
        if (type.getString() == "system") {
            connection = (HttpURLConnection) systemUrl.openConnection();
        } else {
            connection = (HttpURLConnection) changelogUrl.openConnection();
        }

        // add request header
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", "application/json; charset=utf-8");
        connection.setRequestProperty("User-Agent", SendHttpRequest.USER_AGENT);
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        // Send post request
        connection.setDoOutput(true);
        final JsonWriter jsonWriter = Json.createWriter(connection.getOutputStream());
        if (type.getString() == "system") {
            jsonWriter.write(modelData);

            System.out.println("\nSending 'POST' request to URL : " + systemUrl);
            System.out.println("Post parameters : " + modelData);

        } else {
            jsonWriter.writeArray(dataArray); // work in progress

            System.out.println("\nSending 'POST' request to URL : " + changelogUrl);
            System.out.println("Post parameters : " + dataArray);
        }

        jsonWriter.close();
        final int responseCode = connection.getResponseCode();
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

    /**
     * Send change log updates to the visualization.
     *
     * @param dataArray
     * @param changelogUrl
     * @throws IOException
     */
    public static void post(final JsonArray dataArray, final URL changelogUrl) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) changelogUrl.openConnection();

        // add request header
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", "application/json; charset=utf-8");
        connection.setRequestProperty("User-Agent", SendHttpRequest.USER_AGENT);
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // Send post request
        connection.setDoOutput(true);

        final JsonWriter jsonWriter = Json.createWriter(connection.getOutputStream());

        jsonWriter.writeArray(dataArray);
        jsonWriter.close();

        final int responseCode = connection.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + changelogUrl);
        System.out.println("Post parameters : " + dataArray);
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
