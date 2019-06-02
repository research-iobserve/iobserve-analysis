package org.iobserve.service.behavior.analysis;

import java.util.Iterator;
import java.util.List;

import org.iobserve.analysis.session.data.UserSession;
import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.iobserve.service.behavior.analysis.model.BehaviorModelNode;
import org.iobserve.stages.general.data.EntryCallEvent;
import org.iobserve.stages.general.data.PayloadAwareEntryCallEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import teetime.stage.basic.AbstractTransformation;

public class UserSessionToModelConverter extends AbstractTransformation<UserSession, BehaviorModelGED> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSessionToModelConverter.class);

    @Override
    protected void execute(final UserSession session) throws Exception {

        UserSessionToModelConverter.LOGGER.info("Received user session");
        final BehaviorModelGED model = new BehaviorModelGED();

        final List<EntryCallEvent> entryCalls = session.getEvents();
        final Iterator<EntryCallEvent> iterator = entryCalls.iterator();

        if (iterator.hasNext()) {
            PayloadAwareEntryCallEvent event = (PayloadAwareEntryCallEvent) iterator.next();
            BehaviorModelNode lastNode = new BehaviorModelNode(event.getOperationSignature());
            model.getNodes().put(event.getOperationSignature(), lastNode);
            BehaviorModelNode currentNode;

            while (iterator.hasNext()) {
                event = (PayloadAwareEntryCallEvent) iterator.next();
                currentNode = new BehaviorModelNode(event.getOperationSignature());

                model.getNodes().put(event.getOperationSignature(), currentNode);

                lastNode.addEdge(event, currentNode);
                lastNode = currentNode;
            }
        }
        this.outputPort.send(model);
        UserSessionToModelConverter.LOGGER.info("Created BehaviorModelGED");

    }
}
