package org.iobserve.analysis.filter;

import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * 
 * @author Robert Heinrich
 * @author Nicolas Boltz
 */
public class TDeallocation extends AbstractConsumerStage<ResourceContainer> {
	
	/** reference to allocation model provider. */
    private final AllocationModelProvider allocationModelProvider;
	/** reference to {@link ResourceEnvironment} provider. */
    private final ResourceEnvironmentModelProvider resourceEnvModelProvider;
    /** output port. */
    private final OutputPort<ResourceContainer> outputPort = this.createOutputPort();

    /**
     * TDeallocation checks whether the ResourceContainer affected by the execution ofTUndeployment
     * is empty and removes it.
     *
     * @param resourceEvnironmentModelProvider
     *            the resource environment model provider
     */
    public TDeallocation(final AllocationModelProvider allocationModelProvider, final ResourceEnvironmentModelProvider resourceEvnironmentModelProvider) {
    	this.allocationModelProvider = allocationModelProvider;
        this.resourceEnvModelProvider = resourceEvnironmentModelProvider;
    }

	@Override
	protected void execute(ResourceContainer container) throws Exception {
		for(AllocationContext context : this.allocationModelProvider.getModel().getAllocationContexts_Allocation()) {
			if(context.getResourceContainer_AllocationContext().getId().equals(container.getId())) {
				/*There is still an allocation context affiliated with the resource container.*/
				return;
			}
		}
		/*No allocation on the resource container -> remove container.*/
		ResourceEnvironmentModelBuilder.removeResourceContainer(this.resourceEnvModelProvider.getModel(), container);
		this.resourceEnvModelProvider.save();
	}
	
    /**
     * @return the OutputPort
     */
    public OutputPort<ResourceContainer> getOutputPort() {
        return this.outputPort;
    }

}
