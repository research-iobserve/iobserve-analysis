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
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.iobserve.analysis.cdoruserbehavior.filter.models.BehaviorModel;
import org.iobserve.analysis.cdoruserbehavior.filter.models.CallInformation;
import org.iobserve.analysis.cdoruserbehavior.filter.models.EntryCallEdge;
import org.iobserve.analysis.cdoruserbehavior.filter.models.EntryCallNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import teetime.framework.AbstractConsumerStage;

/**
 * Transform the behavior.
 *
 * @author Christoph Dornieden
 *
 */

public class TIObserveUIServer extends AbstractConsumerStage<BehaviorModel> {
    // TODO extract predefined string and convert them to settings
    private final String systemId = "behaviormodelsystem";
    private final String changelogUrl = "http://localhost:8080/v1/systems/" + this.systemId + "/changelogs";
    private final String resetUrl = "http://localhost:8080/v1/systems/createBehavior";
    private final String behaviorModelGroupID = "Behavior-Model-Group";

    private final ObjectMapper objectMapper;

    /**
     * enum.
     */
    private enum ChangelogType {
        CREATE, APPEND
    }

    /**
     * constructor.
     */
    public TIObserveUIServer() {
        this.objectMapper = new ObjectMapper();
        this.resetSystem();
    }

    @Override
    protected void execute(final BehaviorModel behaviorModel) {
        final ArrayList<ObjectNode> changelogs = new ArrayList<>();
        final ArrayList<ObjectNode> instanceChangelogs = new ArrayList<>();
        final String nodeID = this.createBasicChangelogs(changelogs);
        this.postChangelogs(changelogs);
        changelogs.clear();

        behaviorModel.getNodes().stream().forEach(
                node -> this.toCreateNodeChangelog(node, changelogs, instanceChangelogs, nodeID, behaviorModel));
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
    private void toCreateEdgeChangelog(final EntryCallEdge edge, final ArrayList<ObjectNode> changelogs,
            final ArrayList<ObjectNode> instanceChangelogs, final String nodeID) {
        final String fromSignature = edge.getSource().getSignature();
        final String toSignature = edge.getTarget().getSignature();
        final String signature = fromSignature + ">" + toSignature;

        final ObjectNode communication = this.objectMapper.createObjectNode();
        final String communicationId = this.createID(nodeID, signature);

        final ObjectNode communicationInstance = this.objectMapper.createObjectNode();
        final String communicationInstanceId = this.createID(communicationId, "-I");

        communication.put("type", "communication");
        communication.put("id", communicationId);
        communication.put("systemId", this.systemId);
        communication.put("technology", "");
        communication.put("sourceId", this.createID(nodeID, fromSignature));
        communication.put("targetId", this.createID(nodeID, toSignature));

        final ObjectNode communicationChangelog = this.getChangelog(ChangelogType.CREATE);
        communicationChangelog.put("data", communication);
        changelogs.add(communicationChangelog);

        communicationInstance.put("type", "communicationInstance");
        communicationInstance.put("id", communicationInstanceId);
        communicationInstance.put("communicationId", communicationId);
        communicationInstance.put("systemId", this.systemId);
        communicationInstance.put("sourceId", this.createID(this.createID(nodeID, fromSignature), "-I"));
        communicationInstance.put("targetId", this.createID(this.createID(nodeID, toSignature), "-I"));
        communicationInstance.put("workload", edge.getCalls());

        final ObjectNode communicationInstanceChangelog = this.getChangelog(ChangelogType.CREATE);
        communicationInstanceChangelog.put("data", communicationInstance);
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
    private void toCreateNodeChangelog(final EntryCallNode entryCallNode, final ArrayList<ObjectNode> changelogs,
            final ArrayList<ObjectNode> instanceChangelogs, final String nodeID, final BehaviorModel behaviorModel) {
        final String signature = entryCallNode.getSignature();
        final ObjectNode service = this.objectMapper.createObjectNode();
        final String serviceID = this.createID(nodeID, signature);
        final ObjectNode serviceInstance = this.objectMapper.createObjectNode();
        final String serviceInstanceID = this.createID(serviceID, "-I");

        service.put("type", "service");
        service.put("systemId", this.systemId);
        service.put("id", serviceID);
        service.put("name", signature);
        final ObjectNode serviceChangelog = this.getChangelog(ChangelogType.CREATE);

        serviceInstance.put("type", "serviceInstance");
        serviceInstance.put("systemId", this.systemId);
        serviceInstance.put("id", serviceInstanceID);
        serviceInstance.put("name", signature);
        serviceInstance.put("serviceId", serviceID);
        serviceInstance.put("nodeId", nodeID);

        serviceChangelog.put("data", service);
        changelogs.add(serviceChangelog);

        final ObjectNode serviceInstanceChangelog = this.getChangelog(ChangelogType.CREATE);
        serviceInstanceChangelog.put("data", serviceInstance);
        instanceChangelogs.add(serviceInstanceChangelog);

        // entryCallNode.getEntryCallInformation().stream()
        // .forEach(info -> this.appendInformation(changelogs, serviceInstanceID, info));

    }

    /**
     * append information to node.
     *
     * @param changelogs
     *            changelogs
     * @param serviceInstanceID
     *            serviceInstanceID
     * @param info
     *            info
     */
    private void appendInformation(final ArrayList<ObjectNode> changelogs, final String serviceInstanceID,
            final CallInformation info) {
        final String signature = info.getInformationSignature();
        final ObjectNode statusInfo = this.objectMapper.createObjectNode();

        statusInfo.put("type", "statusInfo");
        statusInfo.put("id", this.createID(serviceInstanceID, signature));
        statusInfo.put("parentType", "serviceInstance");
        statusInfo.put("parentId", serviceInstanceID);
        statusInfo.put("timestamp", new Date().getTime());
        statusInfo.put("key", signature);
        statusInfo.put("value", info.getInformationCode());

        final ObjectNode infoChangelog = this.getChangelog(ChangelogType.APPEND);
        infoChangelog.put("data", statusInfo);
        changelogs.add(infoChangelog);
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
    private String createBasicChangelogs(final ArrayList<ObjectNode> list) {
        final String nodeID = UUID.randomUUID().toString();
        final ObjectNode behaviorModelAsNode = this.objectMapper.createObjectNode();
        behaviorModelAsNode.put("type", "node");
        behaviorModelAsNode.put("id", nodeID);
        // behaviorModelAsNode.put(revisionNumber,0);
        // behaviorModelAsNode.put(changelogSequence,0)
        // behaviorModelAsNode.put(lastUpdate,new Date().getTime());
        behaviorModelAsNode.put("name", "behaviorModel");
        behaviorModelAsNode.put("nodeGroupId", this.behaviorModelGroupID);
        behaviorModelAsNode.put("hostname", "");
        behaviorModelAsNode.put("ip", "127.0.0.1");

        final ObjectNode changelog = this.getChangelog(ChangelogType.CREATE);
        changelog.put("data", behaviorModelAsNode);
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
        changelog.put("type", "changelog");
        changelog.put("systemId", this.systemId);
        changelog.put("id", UUID.randomUUID().toString());

        switch (type) {
        case CREATE:
            changelog.put("operation", "CREATE");
            break;
        case APPEND:
            changelog.put("operation", "APPEND");
            break;
        default:
            changelog.put("operation", "DELETE");
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
        } catch (final Exception ex) {
            ex.printStackTrace();
        }

        final ObjectNode group = this.objectMapper.createObjectNode();
        group.put("systemId", this.systemId);
        group.put("type", "nodeGroup");
        group.put("id", this.behaviorModelGroupID);
        // behaviorModelAsNode.put(revisionNumber,0);
        // behaviorModelAsNode.put(changelogSequence,0)
        // behaviorModelAsNode.put(lastUpdate,new Date().getTime());
        group.put("name", "behaviorModels");

        final ObjectNode changelog = this.getChangelog(ChangelogType.CREATE);
        changelog.put("data", group);

        final ArrayList<ObjectNode> list = new ArrayList<>();
        list.add(changelog);

        this.postChangelogs(list);
    }

    /**
     * post changelogs to server.
     *
     * @param changelogs
     *            changelogs
     */
    private void postChangelogs(final ArrayList<ObjectNode> changelogs) {

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

        } catch (final Exception ex) {
            ex.printStackTrace();
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
