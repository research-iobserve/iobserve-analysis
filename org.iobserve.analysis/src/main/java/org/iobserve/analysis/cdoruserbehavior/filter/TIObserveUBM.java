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

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.Set;

import org.iobserve.analysis.cdoruserbehavior.filter.models.BehaviorModel;
import org.iobserve.analysis.cdoruserbehavior.filter.models.CallInformation;
import org.iobserve.analysis.cdoruserbehavior.filter.models.EntryCallEdge;
import org.iobserve.analysis.cdoruserbehavior.filter.models.EntryCallNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
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

    private final ObjectMapper objectMapper;

    /**
     * consturctor
     */
    public TIObserveUBM() {
        this.objectMapper = new ObjectMapper();

    }

    @Override
    protected void execute(BehaviorModel model) {
        final long modelId = this.generateModelId();
        this.createGraph(model, modelId);
        this.createNodes(model.getEntryCallNodes(), modelId);
        this.createEdges(model.getEntryCallEdges(), modelId);

    }

    /**
     * create graph at visualisation backend
     *
     * @param model
     * @param modelId
     */
    private void createGraph(BehaviorModel model, long modelId) {
        final ObjectNode graph = this.objectMapper.createObjectNode();
        graph.put("id", modelId);
        graph.put("name", "jpetstore");

        this.postElements(graph, this.baseUrl + "/applications");
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
            json.put("id", this.createID(modelId, entryCallNode.getSignature()));
            json.put("name", entryCallNode.getSignature());

            final ArrayNode extras = this.objectMapper.createArrayNode();
            for (final CallInformation callInformation : entryCallNode.getEntryCallInformation()) {
                final ObjectNode information = this.objectMapper.createObjectNode();
                information.put("key", callInformation.getInformationSignature());
                information.put("key2", callInformation.getInformationCode());
                extras.add(information);
            }

            json.put("extra", extras);
            nodes.add(json);
        }
        this.postElements(nodes, this.getNodeUrl(modelId));

    }

    /**
     * create new edges at visualisation backend
     *
     * @param entryCallEdges
     *            entryCallEdges
     */
    private void createEdges(Set<EntryCallEdge> entryCallEdges, long modelId) {
        final ArrayNode edges = this.objectMapper.createArrayNode();
        for (final EntryCallEdge entryCallEdge : entryCallEdges) {

            final ObjectNode json = this.objectMapper.createObjectNode();
            final String sourceSignature = entryCallEdge.getSource().getSignature();
            final String targetSignature = entryCallEdge.getTarget().getSignature();

            json.put("id", this.createID(modelId, sourceSignature + "->" + targetSignature));
            json.put("start", this.createID(modelId, sourceSignature));
            json.put("end", targetSignature);
            json.put("action", sourceSignature + "->" + targetSignature);
            json.put("count", entryCallEdge.getCalls());

            edges.add(json);
        }
        this.postElements(edges, this.getNodeUrl(modelId));
    }

    /**
     * post elements to server
     *
     * @param elems
     *            elements
     * @param targetUrl
     *            targetUrl
     */
    private void postElements(final BaseJsonNode elems, final String targetUrl) {

        try {
            final String json = this.objectMapper.writeValueAsString(elems);
            System.out.println(targetUrl);
            // TODO System.out.println(json);

            final URL url = new URL(targetUrl);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            // final OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            // wr.write(json);
            // wr.flush();
            // wr.close();

            System.out.println(json);
            final OutputStream os = con.getOutputStream();
            final byte[] outputBytes = json.getBytes("UTF-8");
            os.write(outputBytes);
            os.flush();

            System.out.println(con.getResponseCode());
            System.out.println(con.getResponseMessage());

            con.disconnect();

        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * create id
     *
     * @param id
     *            model id
     * @param signature
     *            signature
     * @return id
     */
    private long createID(long id, String signature) {
        final String string = "" + id + signature.hashCode();

        return Long.valueOf(string.replaceAll("-", ""));
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

    /**
     * generate id
     *
     * @return id
     */
    private long generateModelId() {
        final Random random = new Random();
        return random.nextInt(99999);
    }

}
