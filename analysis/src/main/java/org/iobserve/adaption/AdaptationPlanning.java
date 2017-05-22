package org.iobserve.adaption;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.iobserve.adaption.data.AdaptationData;
import org.iobserve.planning.systemadaptation.AcquireAction;
import org.iobserve.planning.systemadaptation.Action;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;
import org.iobserve.planning.systemadaptation.TerminateAction;

import groovy.ui.Console;
import teetime.stage.basic.AbstractTransformation;

/**
 * This stage orderes the adaptation {@link Action}s into an executable
 * sequence.
 * 
 * @author Philipp Weimann
 *
 */
public class AdaptationPlanning extends AbstractTransformation<AdaptationData, AdaptationData> {

	Collection<ResourceContainerAction> aquires;
	Collection<ResourceContainerAction> terminates;

	Collection<AssemblyContextAction> allocations;
	Collection<AssemblyContextAction> migrations;
	Collection<AssemblyContextAction> changes;
	Collection<AssemblyContextAction> deallocations;

	@Override
	protected void execute(AdaptationData element) throws Exception {
		init(element);
		
		List<Action> adaptionSteps = new ArrayList<Action>();
		adaptionSteps.addAll(aquires);
		adaptionSteps.addAll(allocations);
		adaptionSteps.addAll(migrations);
		adaptionSteps.addAll(changes);
		adaptionSteps.addAll(deallocations);
		adaptionSteps.addAll(terminates);

		element.setExecutionOrder(adaptionSteps);
		
		this.printAdaptionSequence(adaptionSteps);
		this.outputPort.send(element);
	}

	private void init(AdaptationData data) {
		this.aquires = data.getRcActions().stream().filter(s -> s instanceof AcquireAction).collect(Collectors.toSet());
		this.terminates = data.getRcActions().stream().filter(s -> s instanceof TerminateAction).collect(Collectors.toSet());

		this.allocations = data.getAcActions().stream().filter(s -> s instanceof AllocateAction).collect(Collectors.toSet());
		this.migrations = data.getAcActions().stream().filter(s -> s instanceof MigrateAction).collect(Collectors.toSet());
		this.changes = data.getAcActions().stream().filter(s -> s instanceof ChangeRepositoryComponentAction).collect(Collectors.toSet());
		this.deallocations = data.getAcActions().stream().filter(s -> s instanceof DeallocateAction).collect(Collectors.toSet());
	}
	
	
	private void printAdaptionSequence(List<Action> adaptionSteps)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Adaption sequence:\n");
		
		for (int i = 0; i < adaptionSteps.size(); i++)
		{
			sb.append("\t" + i + "\t" + adaptionSteps.get(i).getClass().toString() + "\n");
		}
		
		System.out.println(sb.toString());
	}

}
