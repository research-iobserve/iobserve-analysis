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
package org.iobserve.analysis.behavior.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import teetime.framework.AbstractConsumerStage;

import org.iobserve.analysis.behavior.models.extended.BehaviorModel;
import org.iobserve.analysis.behavior.models.extended.EntryCallEdge;
import org.iobserve.analysis.behavior.models.extended.EntryCallNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transform the behavior.
 *
 * @author Christoph Dornieden
 *
 */

public class IObserveUIServerStage extends AbstractConsumerStage<BehaviorModel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IObserveUIServerStage.class);

    private static final String OPERATION = "operation";

    private static final String ATTRIBUTE_IP = "ip";

    private static final String ATTRIBUTE_HOSTNAME = "hostname";

    private static final String ATTRIBUTE_NODE_GROUP_ID = "nodeGroupId";

    private static final String ATTRIBUTE_NODE_ID = "nodeId";

    private static final String ATTRIBUTE_SERVICE_ID = "serviceId";

    private static final String ATTRIBUTE_NAME = "name";

    private static final String ATTRIBUTE_DATA = "data";

    private static final String ATTRIBUTE_WORKLOAD = "workload";

    private static final String ATTRIBUTE_COMMUNICATION_ID = "communicationId";

    private static final String ATTRIBUTE_TARGET_ID = "targetId";

    private static final String ATTRIBUTE_SOURCE_ID = "sourceId";

    private static final String ATTRIBUTE_TECHNOLOGY = "technology";

    private static final String ATTRIBUTE_ID = "id";

    private static final String ATTRIBUTE_SYSTEM_ID = "systemId";

    private static final String ATTRIBUTE_TYPE = "type";

    private final String systemId;
    private final String changelogUrl;
    private final String resetUrl;
    private final String behaviorModelGroupId; // = "Behavior-Model-Group";

    private final ObjectMapper objectMapper;
    private final String visualizationHost;

    /**
     * enum.
     */
    private enum ChangelogType {
        CREATE, APPEND
    }

    /**
     * constructor.
     *
     * @param visualizationHost
     *            host where the visualization info is send to
     * @param port
     *            port for the visualizationHost
     * @param systemId
     *            system id
     * @param behaviorModelGroupId
     *            group id for the behavior model
     */
    public IObserveUIServerStage(final String visualizationHost, final String port, final String systemId,
            final String behaviorModelGroupId) {
        this.visualizationHost = visualizationHost;
        this.systemId = systemId;
        this.changelogUrl = "http://" + visualizationHost + ":" + port + "/v1/systems/" + this.systemId + "/changelogs";
        this.resetUrl = "http://" + visualizationHost + ":" + port + "/v1/systems/createBehavior";
        this.behaviorModelGroupId = behaviorModelGroupId;
        this.objectMapper = new ObjectMapper();
        this.resetSystem();
    }

    @Override
    protected void execute(final BehaviorModel behaviorModel) {
        final List<ObjectNode> changelogs = new ArrayList<>();
        final List<ObjectNode> instanceChangelogs = new ArrayList<>();
        final String nodeID = this.createBasicChangelogs(changelogs);
        this.postChangelogs(changelogs);
        changelogs.clear();

        behaviorModel.getNodes().stream()
                .forEach(node -> this.toCreateNodeChangelog(node, changelogs, instanceChangelogs, nodeID));
        this.postChangelogs(changelogs);
        this.postChangelogs(instanceChangelogs);
        instanceChangelogs.clear();
        changelogs.clear();

        behaviorModel.getEdges().stream()
                .forEach(edge -> this.toCreateEdgeChangelog(edge, changelogs, instanceChangelogs, nodeID));
        this.postChangelogs(changelogs);
        this.postChangelogs(instanceChangelogs);
        instanceChangelogs.clear();
        changelogs.clear();

    }

    /**
     * create changelog for entry call edge.
     *
     * @param edge
     *            edge
     * @param changelogs
     *            changelogs
     * @param instanceChangelogs
     *            instanceChangelogs *
     * @param nodeID
     *            nodeID
     */
    private void toCreateEdgeChangelog(final EntryCallEdge edge, final List<ObjectNode> changelogs,
            final List<ObjectNode> instanceChangelogs, final String nodeID) {
        final String fromSignature = edge.getSource().getSignature();
        final String toSignature = edge.getTarget().getSignature();
        final String signature = fromSignature + ">" + toSignature;

        final ObjectNode communication = this.objectMapper.createObjectNode();
        final String communicationId = this.createID(nodeID, signature);

        final ObjectNode communicationInstance = this.objectMapper.createObjectNode();
        final String communicationInstanceId = this.createID(communicationId, "-I");

        communication.put(IObserveUIServerStage.ATTRIBUTE_TYPE, "communication");
        communication.put(IObserveUIServerStage.ATTRIBUTE_ID, communicationId);
        communication.put(IObserveUIServerStage.ATTRIBUTE_SYSTEM_ID, this.systemId);
        communication.put(IObserveUIServerStage.ATTRIBUTE_TECHNOLOGY, "");
        communication.put(IObserveUIServerStage.ATTRIBUTE_SOURCE_ID, this.createID(nodeID, fromSignature));
        communication.put(IObserveUIServerStage.ATTRIBUTE_TARGET_ID, this.createID(nodeID, toSignature));

        final ObjectNode communicationChangelog = this.getChangelog(ChangelogType.CREATE);
        communicationChangelog.put(IObserveUIServerStage.ATTRIBUTE_DATA, communication);
        changelogs.add(communicationChangelog);

        communicationInstance.put(IObserveUIServerStage.ATTRIBUTE_TYPE, "communicationInstance");
        communicationInstance.put(IObserveUIServerStage.ATTRIBUTE_ID, communicationInstanceId);
        communicationInstance.put(IObserveUIServerStage.ATTRIBUTE_COMMUNICATION_ID, communicationId);
        communicationInstance.put(IObserveUIServerStage.ATTRIBUTE_SYSTEM_ID, this.systemId);
        communicationInstance.put(IObserveUIServerStage.ATTRIBUTE_SOURCE_ID,
                this.createID(this.createID(nodeID, fromSignature), "-I"));
        communicationInstance.put(IObserveUIServerStage.ATTRIBUTE_TARGET_ID,
                this.createID(this.createID(nodeID, toSignature), "-I"));
        communicationInstance.put(IObserveUIServerStage.ATTRIBUTE_WORKLOAD, edge.getCalls());

        final ObjectNode communicationInstanceChangelog = this.getChangelog(ChangelogType.CREATE);
        communicationInstanceChangelog.put(IObserveUIServerStage.ATTRIBUTE_DATA, communicationInstance);
        instanceChangelogs.add(communicationInstanceChangelog);

    }

    /**
     * create changelogs for entry call nodes.
     *
     * @param entryCallNode
     *            entryCallNode
     * @param changelogs
     *            changelogs
     * @param instanceChangelogs
     *            instanceChangelogs
     *
     * @param nodeID
     *            nodeID
     *
     * @return
     */
    private void toCreateNodeChangelog(final EntryCallNode entryCallNode, final List<ObjectNode> changelogs,
            final List<ObjectNode> instanceChangelogs, final String nodeID) {
        final String signature = entryCallNode.getSignature();
        final ObjectNode service = this.objectMapper.createObjectNode();
        final String serviceID = this.createID(nodeID, signature);
        final ObjectNode serviceInstance = this.objectMapper.createObjectNode();
        final String serviceInstanceID = this.createID(serviceID, "-I");

        service.put(IObserveUIServerStage.ATTRIBUTE_TYPE, "service");
        service.put(IObserveUIServerStage.ATTRIBUTE_SYSTEM_ID, this.systemId);
        service.put(IObserveUIServerStage.ATTRIBUTE_ID, serviceID);
        service.put(IObserveUIServerStage.ATTRIBUTE_NAME, signature);
        final ObjectNode serviceChangelog = this.getChangelog(ChangelogType.CREATE);

        serviceInstance.put(IObserveUIServerStage.ATTRIBUTE_TYPE, "serviceInstance");
        serviceInstance.put(IObserveUIServerStage.ATTRIBUTE_SYSTEM_ID, this.systemId);
        serviceInstance.put(IObserveUIServerStage.ATTRIBUTE_ID, serviceInstanceID);
        serviceInstance.put(IObserveUIServerStage.ATTRIBUTE_NAME, signature);
        serviceInstance.put(IObserveUIServerStage.ATTRIBUTE_SERVICE_ID, serviceID);
        serviceInstance.put(IObserveUIServerStage.ATTRIBUTE_NODE_ID, nodeID);

        serviceChangelog.put(IObserveUIServerStage.ATTRIBUTE_DATA, service);
        changelogs.add(serviceChangelog);

        final ObjectNode serviceInstanceChangelog = this.getChangelog(ChangelogType.CREATE);
        serviceInstanceChangelog.put(IObserveUIServerStage.ATTRIBUTE_DATA, serviceInstance);
        instanceChangelogs.add(serviceInstanceChangelog);

    }

    /**
     * creates needed basic system via change logs.
     *
     * @param list
     *            list
     *
     * @return nodeID
     *
     */
    private String createBasicChangelogs(final List<ObjectNode> list) {
        final String nodeID = UUID.randomUUID().toString();
        final ObjectNode behaviorModelAsNode = this.objectMapper.createObjectNode();
        behaviorModelAsNode.put(IObserveUIServerStage.ATTRIBUTE_TYPE, "node");
        behaviorModelAsNode.put(IObserveUIServerStage.ATTRIBUTE_ID, nodeID);
        // behaviorModelAsNode.put(revisionNumber,0);
        // behaviorModelAsNode.put(changelogSequence,0)
        // behaviorModelAsNode.put(lastUpdate,new Date().getTime());
        behaviorModelAsNode.put(IObserveUIServerStage.ATTRIBUTE_NAME, "behaviorModel");
        behaviorModelAsNode.put(IObserveUIServerStage.ATTRIBUTE_NODE_GROUP_ID, this.behaviorModelGroupId);
        behaviorModelAsNode.put(IObserveUIServerStage.ATTRIBUTE_HOSTNAME, "");
        behaviorModelAsNode.put(IObserveUIServerStage.ATTRIBUTE_IP, this.visualizationHost);

        final ObjectNode changelog = this.getChangelog(ChangelogType.CREATE);
        changelog.put(IObserveUIServerStage.ATTRIBUTE_DATA, behaviorModelAsNode);
        list.add(changelog);

        return nodeID;
    }

    /**
     * creates changelog object node.
     *
     * @param type
     *            type
     * @return object node of changelog
     */
    private ObjectNode getChangelog(final ChangelogType type) {
        final ObjectNode changelog = this.objectMapper.createObjectNode();
        changelog.put(IObserveUIServerStage.ATTRIBUTE_TYPE, "changelog");
        changelog.put(IObserveUIServerStage.ATTRIBUTE_SYSTEM_ID, this.systemId);
        changelog.put(IObserveUIServerStage.ATTRIBUTE_ID, UUID.randomUUID().toString());

        switch (type) {
        case CREATE:
            changelog.put(IObserveUIServerStage.OPERATION, "CREATE");
            break;
        case APPEND:
            changelog.put(IObserveUIServerStage.OPERATION, "APPEND");
            break;
        default:
            changelog.put(IObserveUIServerStage.OPERATION, "DELETE");
            break;
        }

        return changelog;
    }

    /**
     * reset the system in the ui.
     */
    private void resetSystem() {
        try {
            final URL url = new URL(this.resetUrl);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("GET");
            con.disconnect();
        } catch (final IOException ex) {
            IObserveUIServerStage.LOGGER.warn("Could not send reset request {}", this.resetUrl);
        }

        final ObjectNode group = this.objectMapper.createObjectNode();
        group.put(IObserveUIServerStage.ATTRIBUTE_SYSTEM_ID, this.systemId);
        group.put(IObserveUIServerStage.ATTRIBUTE_TYPE, "nodeGroup");
        group.put(IObserveUIServerStage.ATTRIBUTE_ID, this.behaviorModelGroupId);
        // behaviorModelAsNode.put(revisionNumber,0);
        // behaviorModelAsNode.put(changelogSequence,0)
        // behaviorModelAsNode.put(lastUpdate,new Date().getTime());
        group.put(IObserveUIServerStage.ATTRIBUTE_NAME, "behaviorModels");

        final ObjectNode changelog = this.getChangelog(ChangelogType.CREATE);
        changelog.put(IObserveUIServerStage.ATTRIBUTE_DATA, group);

        final List<ObjectNode> list = new ArrayList<>();
        list.add(changelog);

        this.postChangelogs(list);
    }

    /**
     * post changelogs to server.
     *
     * @param changelogs
     *            changelogs
     */
    private void postChangelogs(final List<ObjectNode> changelogs) {

        try {
            final String json = this.objectMapper.writeValueAsString(changelogs);

            final URL url = new URL(this.changelogUrl);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            // final OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            // wr.write(json);
            // wr.flush();
            // wr.close();

            final OutputStream os = con.getOutputStream();
            final byte[] outputBytes = json.getBytes("UTF-8");
            os.write(outputBytes);
            os.flush();

            con.disconnect();

        } catch (final Exception ex) { // NOCS NOPMD
            IObserveUIServerStage.LOGGER.warn("Could not post change logs {}", this.resetUrl);
        }
    }

    /**
     * create id.
     *
     * @param nodeID
     *            nodeID
     * @param signature
     *            signature
     * @return id
     */
    private String createID(final String nodeID, final String signature) {
        return nodeID + signature;
    }

}
