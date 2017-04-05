/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.analysis.cdoruserbehavior.filter;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.iobserve.analysis.cdoruserbehavior.filter.models.BehaviorModel;
import org.iobserve.analysis.cdoruserbehavior.filter.models.CallInformation;
import org.iobserve.analysis.cdoruserbehavior.filter.models.EntryCallEdge;
import org.iobserve.analysis.cdoruserbehavior.filter.models.EntryCallNode;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import teetime.framework.AbstractConsumerStage;

/**
 * Transform the behavior
 *
 * @author Christoph Dornieden
 *
 */
public class TIObserveUBM extends AbstractConsumerStage<BehaviorModel> {
    private final String modelName = "JPetstore Behavior Model";
    private final String baseUrl = "http://localhost:8080/ubm-backend/v1";
    private final Map<String, JsonNode> nodeMap;

    private final ObjectMapper objectMapper;

    /**
     * consturctor
     */
    public TIObserveUBM() {
        this.objectMapper = new ObjectMapper();
        this.nodeMap = new HashMap<>();
        // TODO remove
        this.resetVisualization();

    }

    @Override
    protected void execute(BehaviorModel model) {
        this.nodeMap.clear();
        final long modelId = this.createGraph(this.modelName);
        this.createNodes(model.getEntryCallNodes(), modelId);
        this.createEdges(model.getEntryCallEdges(), modelId);

    }

    /**
     * create graph at visualisation backend
     *
     * @param name
     *            name
     *
     * @return modelId
     */
    private long createGraph(String name) {
        final ObjectNode graph = this.objectMapper.createObjectNode();
        graph.put("name", name);

        final String targetUrl = this.baseUrl + "/applications";

        final JsonNode json = this.postElement(graph, targetUrl);
        final String id = json.get("id").asText();

        return Long.valueOf(id);
    }

    /**
     * reset the visualisation
     */
    private void resetVisualization() {
        for (int i = 900; i < 1200; i++) {
            this.sendDelete("http://localhost:8080/ubm-backend/v1/applications/" + i);
        }
    }

    /**
     * Send delete request
     *
     * @param targetUrl
     *            targetUrl
     */
    private void sendDelete(String targetUrl) {
        URL url;
        try {
            url = new URL(targetUrl);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestMethod("DELETE");

            con.getResponseCode();

        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Create new nodes at visualisation backend
     *
     * @param entryCallNodes
     *            entryCallNodes
     */
    private void createNodes(Set<EntryCallNode> entryCallNodes, long modelId) {
        final ArrayNode nodes = this.objectMapper.createArrayNode();
        for (final EntryCallNode entryCallNode : entryCallNodes) {

            final ObjectNode json = this.objectMapper.createObjectNode();
            json.put("id", 0);
            json.put("name", entryCallNode.getSignature());

            final ArrayNode extras = this.objectMapper.createArrayNode();
            for (final CallInformation callInformation : entryCallNode.getEntryCallInformation()) {
                final ObjectNode information = this.objectMapper.createObjectNode();
                information.put("key", callInformation.getInformationSignature());
                information.put("key2", callInformation.getInformationCode());
                extras.add(information);
            }
            // TODO caused by ui - not relevant for petstore
            if (extras.has(0)) {
                json.put("extra", extras.get(0));
            } else {
                json.put("extra", this.objectMapper.createObjectNode());
            }

            // TODO visualisations doesn't accept lists
            nodes.add(json);
            final JsonNode node = this.postElement(json, this.getNodeUrl(modelId));
            this.nodeMap.put(entryCallNode.getSignature(), node);
        }

    }

    /**
     * create new edges at visualisation backend
     *
     * @param entryCallEdges
     *            entryCallEdges
     * @param modelId
     *            modelId
     */
    private void createEdges(Set<EntryCallEdge> entryCallEdges, long modelId) {
        final ArrayNode edges = this.objectMapper.createArrayNode();
        for (final EntryCallEdge entryCallEdge : entryCallEdges) {

            final ObjectNode json = this.objectMapper.createObjectNode();
            final String sourceSignature = entryCallEdge.getSource().getSignature();
            final String targetSignature = entryCallEdge.getTarget().getSignature();

            json.put("id", 0);
            json.put("start", this.nodeMap.get(sourceSignature));
            json.put("end", this.nodeMap.get(targetSignature));
            json.put("action", sourceSignature + "->" + targetSignature);
            json.put("count", entryCallEdge.getCalls());

            this.postElement(json, this.getEdgeUrl(modelId));
            edges.add(json);
        }
        // this.postElements(edges, this.getNodeUrl(modelId));
    }

    /**
     * post elements to server
     *
     * @param elem
     *            element
     * @param targetUrl
     *            targetUrl
     * @return id
     */
    private JsonNode postElement(final ObjectNode elem, final String targetUrl) {

        try {
            final String json = this.objectMapper.writeValueAsString(elem);

            final URL url = new URL(targetUrl);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            final OutputStream os = con.getOutputStream();
            final byte[] outputBytes = json.getBytes("UTF-8");
            os.write(outputBytes);
            os.flush();

            final InputStream response = con.getInputStream();
            @SuppressWarnings("resource")
            final Scanner scanner = new Scanner(response).useDelimiter("\\A");
            final String content = scanner.next().replaceAll("\\\"@id\\\":\\\"1\\\",", "");

            final JsonNode contendNode = this.objectMapper.readTree(content);

            con.disconnect();
            return contendNode;

        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return this.objectMapper.createObjectNode();
    }

    /**
     * get graph url
     *
     * @param modelId
     *            modelId
     * @return graph url
     */
    private String getGraphUrl(long modelId) {
        return this.baseUrl + "/applications/" + modelId;
    }

    /**
     * get node url
     *
     * @param modelId
     *            modelId
     * @return node url
     */
    private String getNodeUrl(long modelId) {
        return this.getGraphUrl(modelId) + "/pages";
    }

    /**
     * get edge url
     *
     * @param modelId
     *            modelId
     * @return edge url
     */
    private String getEdgeUrl(long modelId) {
        return this.getGraphUrl(modelId) + "/visits";
    }

}
