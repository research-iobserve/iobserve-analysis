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
package org.iobserve.analysis.clustering.filter;

import java.io.IOException;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.iobserve.analysis.clustering.filter.models.BehaviorModel;
import org.iobserve.analysis.clustering.filter.models.CallInformation;
import org.iobserve.analysis.clustering.filter.models.EntryCallEdge;
import org.iobserve.analysis.clustering.filter.models.EntryCallNode;
import org.iobserve.analysis.clustering.filter.models.configuration.ISignatureCreationStrategy;
import org.iobserve.analysis.sink.AbstractModelOutputSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transform the behavior.
 *
 * @author Christoph Dornieden
 *
 */
public class TBehaviorModelVisualization extends AbstractModelOutputSink {
    private static final Logger LOGGER = LoggerFactory.getLogger(TBehaviorModelVisualization.class);
    private final ISignatureCreationStrategy signatureStrategy;
    private final String applicationUrl;
    private final Map<String, JsonNode> nodeMap;
    private final Pattern idPattern = Pattern.compile("\\\"@id\\\":\\\"1\\\",");

    private final ObjectMapper objectMapper;

    /**
     * constructor.
     *
     * @param baseUrl
     *            visualization URL
     * @param signatureStrategy
     *            strategy for signature creation
     */
    public TBehaviorModelVisualization(final String baseUrl, final ISignatureCreationStrategy signatureStrategy) {
        this.objectMapper = new ObjectMapper();
        this.nodeMap = new HashMap<>();
        this.applicationUrl = baseUrl + "/applications";
        this.signatureStrategy = signatureStrategy;
        /** cleanup the visualization service. Note: This might not be necessary. */
        this.resetVisualization();

    }

    @Override
    protected void execute(final BehaviorModel model) {
        this.nodeMap.clear();
        final long modelId = this.createGraph(model.getName());
        if (modelId != -1L) {
            this.createNodes(model.getNodes(), modelId);
            this.createEdges(model.getEdges(), modelId);
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
        } else { /** server error. */
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
        final URL url;
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

        } catch (final IOException e) {
            TBehaviorModelVisualization.LOGGER.error("Fetching data from visualization service failed.", e);
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
        final URL url;
        try {
            url = new URL(targetUrl);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestMethod("DELETE");

            con.getResponseCode();

        } catch (final IOException e) {
            TBehaviorModelVisualization.LOGGER.error("HTTP DELETE failed.", e);
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

            // TODO visualizations doesn't accept lists.
            // TODO is this a requirement request?
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
            json.put("action", String.format("%s->%s", sourceSignature, targetSignature));
            json.put("count", entryCallEdge.getCalls());

            this.postElement(json, this.getEdgeUrl(modelId));
            edges.add(json);
        }
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

        } catch (final IOException ex) {
            TBehaviorModelVisualization.LOGGER.error("Cannot post element.", ex);
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
