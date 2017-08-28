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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.iobserve.analysis.cdoruserbehavior.filter.models.BehaviorModel;
import org.iobserve.analysis.cdoruserbehavior.filter.models.CallInformation;
import org.iobserve.analysis.cdoruserbehavior.filter.models.EntryCallEdge;
import org.iobserve.analysis.cdoruserbehavior.filter.models.EntryCallNode;
import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.ISignatureCreationStrategy;
import org.iobserve.analysis.filter.writer.AbstractModelOutputFilter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Transform the behavior.
 *
 * @author Christoph Dornieden
 *
 */
public class TBehaviorModelVisualization extends AbstractModelOutputFilter {
    private static final Logger LOGGER = LogManager.getLogger(TBehaviorModelVisualization.class);
    private final ISignatureCreationStrategy signatureStrategy;
    private final String applicationUrl;
    private final Map<String, JsonNode> nodeMap;
    private final Pattern idPattern = Pattern.compile("\\\"@id\\\":\\\"1\\\",");

    private final ObjectMapper objectMapper;

    /**
     * constructor.
     *
     * @param baseUrl
     * @param signatureStrategy
     */
    public TBehaviorModelVisualization(final String baseUrl, final ISignatureCreationStrategy signatureStrategy) {
        this.objectMapper = new ObjectMapper();
        this.nodeMap = new HashMap<>();
        this.applicationUrl = baseUrl + "/applications";
        this.signatureStrategy = signatureStrategy;
        // TODO remove
        this.resetVisualization();

    }

    @Override
    protected void execute(final BehaviorModel model) {
        this.nodeMap.clear();
        final long modelId = this.createGraph(model.getName());
        if (modelId != -1L) {
            this.createNodes(model.getEntryCallNodes(), modelId);
            this.createEdges(model.getEntryCallEdges(), modelId);
        } else {
            TBehaviorModelVisualization.LOGGER.error("Failed to create behavior on server!");
        }

    }

    /**
     * Create graph at visualization backend.
     *
     * @param name
     *            name
     *
     * @return modelId
     */
    private long createGraph(final String name) {
        final ObjectNode graph = this.objectMapper.createObjectNode();
        graph.put("name", name);

        final String targetUrl = this.applicationUrl;

        final JsonNode json = this.postElement(graph, targetUrl);
        final JsonNode idJson = json.get("id");
        if (idJson != null) {
            return idJson.asLong();
        } else { // server error
            return -1L;
        }

    }

    /**
     * reset the visualisation.
     */
    private void resetVisualization() {
        final Optional<List<Long>> ids = this.getAllGraphsFromUI(this.applicationUrl);

        ids.ifPresent(idx -> idx.stream().forEach(id -> this.sendDelete(this.applicationUrl + "/" + id)));

    }

    /**
     * get all graph ids present in the ui.
     *
     * @param targetUrl
     *            targetUrl
     * @return ids as List
     */
    private Optional<List<Long>> getAllGraphsFromUI(final String targetUrl) {
        URL url;
        try {
            url = new URL(targetUrl);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            con.setRequestProperty("Accept", "*/*");
            con.setRequestMethod("GET");

            con.setDoInput(true);
            final InputStream response = con.getInputStream();
            @SuppressWarnings("resource")
            final Scanner scanner = new Scanner(response).useDelimiter("\\A");
            final String content = this.idPattern.matcher(scanner.next()).replaceAll("");

            final JsonNode contendNode = this.objectMapper.readTree(content);

            final List<Long> graphIds = new ArrayList<>();
            if (contendNode.isArray()) {
                for (final JsonNode graph : contendNode) {
                    graphIds.add(graph.get("id").asLong());
                }
                return Optional.of(graphIds);
            }

        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Send delete request.
     *
     * @param targetUrl
     *            targetUrl
     */
    private void sendDelete(final String targetUrl) {
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
     * Create new nodes at visualisation backend.
     *
     * @param entryCallNodes
     *            entryCallNodes
     */
    private void createNodes(final Set<EntryCallNode> entryCallNodes, final long modelId) {
        final ArrayNode nodes = this.objectMapper.createArrayNode();
        for (final EntryCallNode entryCallNode : entryCallNodes) {

            final ObjectNode json = this.objectMapper.createObjectNode();
            json.put("id", 0);
            json.put("name", this.signatureStrategy.getSignature(entryCallNode));

            final ObjectNode extras = this.objectMapper.createObjectNode();
            for (final CallInformation callInformation : entryCallNode.getEntryCallInformation()) {
                extras.put(callInformation.getInformationSignature(), callInformation.getInformationCode());
            }
            json.put("extra", extras);

            // TODO visualisations doesn't accept lists
            nodes.add(json);
            final JsonNode node = this.postElement(json, this.getNodeUrl(modelId));
            this.nodeMap.put(entryCallNode.getSignature(), node);
        }

    }

    /**
     * create new edges at visualization backend.
     *
     * @param entryCallEdges
     *            entryCallEdges
     * @param modelId
     *            modelId
     */
    private void createEdges(final Set<EntryCallEdge> entryCallEdges, final long modelId) {
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
        // TODO remove
        // this.postElements(edges, this.getNodeUrl(modelId));
    }

    /**
     * post elements to server.
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
            final String content = this.idPattern.matcher(scanner.next()).replaceAll("");

            final JsonNode contendNode = this.objectMapper.readTree(content);

            con.disconnect();
            return contendNode;

        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return this.objectMapper.createObjectNode();
    }

    /**
     * get graph url.
     *
     * @param modelId
     *            modelId
     * @return graph url
     */
    private String getGraphUrl(final long modelId) {
        return this.applicationUrl + "/" + modelId;
    }

    /**
     * get node url.
     *
     * @param modelId
     *            modelId
     * @return node url
     */
    private String getNodeUrl(final long modelId) {
        return this.getGraphUrl(modelId) + "/pages";
    }

    /**
     * get edge url.
     *
     * @param modelId
     *            modelId
     * @return edge url
     */
    private String getEdgeUrl(final long modelId) {
        return this.getGraphUrl(modelId) + "/visits";
    }

}
