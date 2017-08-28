package org.iobserve.analysis.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.analysis.service.services.CommunicationInstanceService;
import org.iobserve.analysis.service.services.CommunicationService;
import org.iobserve.analysis.service.services.NodeService;
import org.iobserve.analysis.service.services.NodegroupService;
import org.iobserve.analysis.service.services.ServiceInstanceService;
import org.iobserve.analysis.service.services.ServiceService;
import org.iobserve.analysis.service.services.SystemService;
import org.iobserve.analysis.service.services.UsergroupService;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.core.impl.PCMRandomVariableImpl;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.impl.LinkingResourceImpl;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.Branch;
import org.palladiosimulator.pcm.usagemodel.BranchTransition;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

import util.SendHttpRequest;

/**
 * Initializes the deployment visualization by mapping the initial palladio components to
 * visualization elements.
 *
 * @author jweg
 *
 */
public final class InitializeDeploymentVisualization {

    /** model provider for palladio models */
    private final ModelProvider<Allocation> allocationModelGraphProvider;
    private final ModelProvider<ResourceContainer> allocationResourceContainerModelGraphProvider;
    private final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider;
    private final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider;
    private final ModelProvider<UsageModel> usageScenarioModelGraphProvider;
    private final ModelProvider<Repository> repositoryModelGraphProvider;

    /** services for visualization elements */
    private final SystemService systemService = new SystemService();
    private final NodegroupService nodegroupService = new NodegroupService();
    private final NodeService nodeService = new NodeService();
    private final ServiceService serviceService = new ServiceService();
    private final ServiceInstanceService serviceinstanceService = new ServiceInstanceService();
    private final CommunicationService communicationService = new CommunicationService();
    private final CommunicationInstanceService communicationinstanceService = new CommunicationInstanceService();
    private final UsergroupService usergroupService = new UsergroupService();

    private final URL changelogUrl;
    private final URL systemUrl;

    /**
     * constructor
     *
     * @param systemUrl
     *            Url to send requests for creating a system to
     * @param changelogUrl
     *            Url to send requests for changelogs
     * @param allocationModelGraphProvider
     * @param allocationResourceContainerModelGraphProvider
     * @param systemModelGraphProvider
     * @param resourceEnvironmentModelGraphProvider
     */

    public InitializeDeploymentVisualization(final URL systemUrl, final URL changelogUrl,
            final ModelProvider<Allocation> allocationModelGraphProvider,
            final ModelProvider<ResourceContainer> allocationResourceContainerModelGraphProvider,
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider,
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider,
            final ModelProvider<UsageModel> usageScenarioModelGraphProvider,
            final ModelProvider<Repository> repositoryModelGraphProvider) {
        this.systemUrl = systemUrl;
        this.changelogUrl = changelogUrl;
        this.allocationModelGraphProvider = allocationModelGraphProvider;
        this.allocationResourceContainerModelGraphProvider = allocationResourceContainerModelGraphProvider;
        this.systemModelGraphProvider = systemModelGraphProvider;
        this.resourceEnvironmentModelGraphProvider = resourceEnvironmentModelGraphProvider;
        this.usageScenarioModelGraphProvider = usageScenarioModelGraphProvider;
        this.repositoryModelGraphProvider = repositoryModelGraphProvider;
    }

    /**
     * Populates the database of the deployment visualization initially and respects the changelog
     * constraints of iobserve-ui-deployment. It takes information from the system model, the
     * allocation model and the resource environment model and creates corresponding visualization
     * components, e.g. nodes and services.
     *
     * @throws Exception
     */
    protected void initialize() throws Exception {
        // set up the system model and take parts from it
        final org.palladiosimulator.pcm.system.System systemModel = this.systemModelGraphProvider
                .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class);

        final List<AssemblyContext> assemblyContexts = systemModel.getAssemblyContexts__ComposedStructure();
        // set up the allocation model and take parts from it
        final List<String> allocationIds = this.allocationModelGraphProvider.readComponentByType(Allocation.class);
        // an allocation model contains exactly one allocation, therefore .get(0)
        final String allocationId = allocationIds.get(0);
        final Allocation allocation = this.allocationModelGraphProvider.readOnlyComponentById(Allocation.class,
                allocationId);
        final List<AllocationContext> allocationContexts = allocation.getAllocationContexts_Allocation();
        // set up the resource environment model and take parts from it
        final ResourceEnvironment resourceEnvironmentModel = this.resourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class);
        final List<LinkingResource> linkingResources = resourceEnvironmentModel
                .getLinkingResources__ResourceEnvironment();
        final List<ResourceContainer> resourceContainers = resourceEnvironmentModel
                .getResourceContainer_ResourceEnvironment();
        // set up the usage model and take parts from it
        final UsageModel usageModel = this.usageScenarioModelGraphProvider.readOnlyRootComponent(UsageModel.class);
        final List<UsageScenario> usageScenarios = usageModel.getUsageScenario_UsageModel();

        // set up the repsoitory model
        final Repository repositoryModel = this.repositoryModelGraphProvider.readOnlyRootComponent(Repository.class);

        // sending created components to visualization (in predefined order stated in changelog
        // constraints)
        // SendHttpRequest.post(this.systemService.createSystem(systemModel), this.systemUrl,
        // this.changelogUrl);

        for (int i = 0; i < resourceContainers.size(); i++) {
            final ResourceContainer resourceContainer = resourceContainers.get(i);
            // SendHttpRequest.post(
            // Changelog.create(this.nodegroupService.createNodegroup(this.systemService.getSystemId())),
            // this.systemUrl, this.changelogUrl);
            // SendHttpRequest.post(Changelog.create(this.nodeService.createNode(resourceContainer,
            // this.systemService.getSystemId(), this.nodegroupService.getNodegroupId())),
            // this.systemUrl,
            // this.changelogUrl);
        }
        // architecture view in deployment visualization shows services and
        // communication
        for (int i = 0; i < assemblyContexts.size(); i++) {
            final AssemblyContext assemblyContext = assemblyContexts.get(i);
            // SendHttpRequest.post(
            // Changelog.create(
            // this.serviceService.createService(assemblyContext,
            // this.systemService.getSystemId())),
            // this.systemUrl, this.changelogUrl);
        }

        // deployment view in deployment visualization shows nodegroups, nodes, serviceinstances and
        // communicationinstances
        for (int i = 0; i < allocationContexts.size(); i++) {
            final AllocationContext allocationContext = allocationContexts.get(i);

            final String resourceContainerId = allocationContext.getResourceContainer_AllocationContext().getId();
            final AssemblyContext assemblyContext = allocationContext.getAssemblyContext_AllocationContext();
            final String assemblyContextId = allocationContext.getAssemblyContext_AllocationContext().getId();

            // SendHttpRequest.post(
            // Changelog.create(this.serviceinstanceService.createServiceInstance(assemblyContext,
            // this.systemService.getSystemId(), resourceContainerId, assemblyContextId)),
            // this.systemUrl, this.changelogUrl);
        }

        /**
         * ID of resource container on which source (regarding communication) assembly context is
         * deployed
         */
        String resourceSourceId = null;
        /**
         * ID of resource container on which target (regarding communication) assembly context is
         * deployed
         */
        String resourceTargetId = null;
        /** technology of communication */
        String technology = null;
        final List<Connector> connectors = systemModel.getConnectors__ComposedStructure();

        for (int i = 0; i < connectors.size(); i++) {
            final Connector connector = connectors.get(i);
            // we are only interested in AssemblyConnectors
            if (connector instanceof AssemblyConnector) {

                // aim: find out which technology is used for communication
                // howTo: (AllocationModel) get the resourceContainer(s) on which the
                // source/targetAssemblyContext are deployed, (resourceEnvModel) get the
                // linkingResource which includes the resourceContainers in
                // connectedResourceContainers, entityName of linkingResource=technology
                final String assemContSourceId = ((AssemblyConnector) connector)
                        .getProvidingAssemblyContext_AssemblyConnector().getId();
                final String assemContTargetId = ((AssemblyConnector) connector)
                        .getRequiringAssemblyContext_AssemblyConnector().getId();

                final List<EObject> allocationContextsWithSource = this.allocationModelGraphProvider
                        .readOnlyReferencingComponentsById(AssemblyContext.class, assemContSourceId);
                if (allocationContextsWithSource.get(0) instanceof AllocationContext) {
                    final AllocationContext allocationContext = (AllocationContext) allocationContextsWithSource.get(0);
                    resourceSourceId = allocationContext.getResourceContainer_AllocationContext().getId();
                }

                final List<EObject> allocationContextsWithTarget = this.allocationModelGraphProvider
                        .readOnlyReferencingComponentsById(AssemblyContext.class, assemContTargetId);
                if (allocationContextsWithTarget.get(0) instanceof AllocationContext) {
                    final AllocationContext allocationContext = (AllocationContext) allocationContextsWithTarget.get(0);
                    resourceTargetId = allocationContext.getResourceContainer_AllocationContext().getId();
                }

                if ((resourceSourceId != null) && (resourceTargetId != null)) {
                    for (int l = 0; l < linkingResources.size(); l++) {
                        final LinkingResource linkingResource = linkingResources.get(l);
                        if (linkingResource instanceof LinkingResourceImpl) {
                            final List<ResourceContainer> connectedResourceConts = linkingResource
                                    .getConnectedResourceContainers_LinkingResource();
                            final List<String> connectedResourceContsIds = new ArrayList<>();
                            for (int k = 0; k < connectedResourceConts.size(); k++) {
                                connectedResourceContsIds.add(connectedResourceConts.get(k).getId());
                            }
                            if (connectedResourceContsIds.contains(resourceSourceId)) {
                                if (connectedResourceContsIds.contains(resourceTargetId)) {
                                    technology = linkingResource.getEntityName();
                                }
                            }
                        }
                    }
                }

                // SendHttpRequest.post(
                // Changelog.create(this.communicationService.createCommunication((AssemblyConnector)
                // connector,
                // this.systemService.getSystemId(), technology)),
                // this.systemUrl, this.changelogUrl);
                //
                // SendHttpRequest.post(
                // Changelog.create(this.communicationinstanceService.createCommunicationInstance(
                // (AssemblyConnector) connector, this.systemService.getSystemId(),
                // this.communicationService.getCommunicationId())),
                // this.systemUrl, this.changelogUrl);

            } else {
                System.out.printf("no AssemblyConnector: %s\n", connector.getEntityName());
            }

        }

        // Collect all EntryLevelSystemCalls. This list will be passed to the
        // userGroupService().
        final List<EntryLevelSystemCall> userSteps = new ArrayList<>();
        for (int h = 0; h < usageScenarios.size(); h++) {
            final UsageScenario usageScenario = usageScenarios.get(h);

            final ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
            final List<AbstractUserAction> usageActions = scenarioBehaviour.getActions_ScenarioBehaviour();

            for (int i = 0; i < usageActions.size(); i++) {
                final AbstractUserAction actualUsageAction = usageActions.get(i);

                if (actualUsageAction instanceof EntryLevelSystemCall) {
                    userSteps.add((EntryLevelSystemCall) actualUsageAction);

                }
                if (actualUsageAction instanceof Branch) {
                    final List<BranchTransition> branches = ((Branch) actualUsageAction).getBranchTransitions_Branch();
                    AbstractUserAction branchAction;
                    List<AbstractUserAction> branchActions;

                    for (int j = 0; j < branches.size(); j++) {
                        final BranchTransition branchTransition = branches.get(j);
                        branchActions = branchTransition.getBranchedBehaviour_BranchTransition()
                                .getActions_ScenarioBehaviour();
                        for (int k = 0; k < branchActions.size(); k++) {
                            branchAction = branchActions.get(k);
                            System.out.printf("branchAction:%s\n", branchAction);
                            if (branchAction instanceof EntryLevelSystemCall) {
                                userSteps.add((EntryLevelSystemCall) branchAction);
                            }
                        }
                    }
                }
                if (actualUsageAction instanceof Loop) {
                    final PCMRandomVariableImpl loopIteration = (PCMRandomVariableImpl) ((Loop) actualUsageAction)
                            .getLoopIteration_Loop();
                    final ScenarioBehaviour loopBehaviour = ((Loop) actualUsageAction).getBodyBehaviour_Loop();
                    final List<AbstractUserAction> loopActions = loopBehaviour.getActions_ScenarioBehaviour();

                    for (int l = 0; l < loopActions.size(); l++) {
                        final AbstractUserAction loopAction = loopActions.get(l);
                        System.out.printf("loopAction:%s\n", loopAction);
                        if (loopAction instanceof EntryLevelSystemCall) {
                            userSteps.add((EntryLevelSystemCall) loopAction);
                        }
                    }
                }

            }
        }

        final List<AssemblyContext> userInvokedServices = new ArrayList<>();
        // map userSteps to assemblyContexts/services
        for (int m = 0; m < userSteps.size(); m++) {
            final EntryLevelSystemCall userStep = userSteps.get(m);
            final String operationSignatureId = userStep.getOperationSignature__EntryLevelSystemCall().getId();

            // get the components__Repository, that contains a providedRole with the Id
            // operationSignatureId
            final List<EObject> repositoryComponentsWithOperationSignature = this.repositoryModelGraphProvider
                    .readOnlyReferencingComponentsById(ProvidedRole.class, operationSignatureId);
            final RepositoryComponent repositoryComponent = (RepositoryComponent) repositoryComponentsWithOperationSignature
                    .get(0);

            // get the assemblyContext, that contains the repository id
            final List<EObject> assemblyContextsWithComponent = this.repositoryModelGraphProvider
                    .readOnlyReferencingComponentsById(RepositoryComponent.class, repositoryComponent.getId());
            for (int n = 0; n < assemblyContextsWithComponent.size(); n++) {
                final AssemblyContext assemblyContextWithComponent = (AssemblyContext) assemblyContextsWithComponent
                        .get(n);
                userInvokedServices.add(assemblyContextWithComponent);
            }

        }
        if (userInvokedServices.size() > 0) {
            SendHttpRequest.post(
                    this.usergroupService.createUsergroup(this.systemService.getSystemId(), userInvokedServices),
                    this.systemUrl, this.changelogUrl);

        }

    }

}
