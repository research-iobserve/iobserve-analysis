package org.iobserve.analysis.model;

import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;

public class ResourceEnvironmentModelBuilder
        extends ModelBuilder<ResourceEnvironmentModelProvider, ResourceEnvironment> {

    public ResourceEnvironmentModelBuilder(final ResourceEnvironmentModelProvider modelToStartWith) {
        super(modelToStartWith);
    }

    // *****************************************************************
    //
    // *****************************************************************

    public ResourceEnvironmentModelBuilder save(final ModelSaveStrategy saveStrategy) {
        this.modelProvider.save(saveStrategy);
        return this;
    }

    public ResourceEnvironmentModelBuilder loadModel() {
        this.modelProvider.loadModel();
        return this;
    }

    public ResourceEnvironmentModelBuilder resetModel() {
        final ResourceEnvironment model = this.modelProvider.getModel();
        model.getResourceContainer_ResourceEnvironment().clear();
        model.getLinkingResources__ResourceEnvironment().clear();
        return this;
    }

    /**
     * Create a {@link ResourceContainer} with the given name, without checking if it already
     * exists. Use {@link #createResourceContainerIfAbsent(String)} instead if you wont create the
     * container if it is already available.
     * 
     * @param name
     *            name of the new container
     * @return builder
     */
    public ResourceContainer createResourceContainer(final String name) {
        final ResourceEnvironment model = this.modelProvider.getModel();
        final ResourceContainer resContainer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
        resContainer.setEntityName(name);
        model.getResourceContainer_ResourceEnvironment().add(resContainer);
        return resContainer;
    }

    /**
     * Creates a link between the given two container.
     * 
     * @param res1
     *            first container
     * @param res2
     *            second container
     * @return link instance, already added to the model
     */
    public LinkingResource connectResourceContainer(final ResourceContainer res1, final ResourceContainer res2) {
        final ResourceEnvironment model = this.modelProvider.getModel();
        final LinkingResource link = ResourceenvironmentFactory.eINSTANCE.createLinkingResource();
        link.getConnectedResourceContainers_LinkingResource().add(res1);
        link.getConnectedResourceContainers_LinkingResource().add(res2);
        model.getLinkingResources__ResourceEnvironment().add(link);
        return link;
    }

}
