package org.iobserve.analysis.model;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.iobserve.planning.changegroup.ChangeGroup;
import org.iobserve.planning.changegroup.ChangeGroupRepository;
import org.iobserve.planning.changegroup.ChangegroupFactory;

public class ChangeGroupModelBuilder {

	/**
	 * TODO
	 *
	 * @param designDecisionFolder
	 * @param fileName
	 * @param decisionSpace
	 * @throws IOException
	 */
	public static void saveChangeGroupRepository(final URI changeGroupFolder, final String fileName,
			final ChangeGroupRepository changeGroupRepository) throws IOException {
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet
				.createResource(changeGroupFolder.appendSegment(fileName).appendFileExtension("changegroup"));

		resource.getContents().add(changeGroupRepository);
		resource.save(Collections.EMPTY_MAP);
	}

	/**
	 * TODO
	 *
	 * @return
	 */
	public static ChangeGroupRepository createChangeGroupRepository() {
		ChangeGroupRepository repository = ChangegroupFactory.eINSTANCE.createChangeGroupRepository();
		return repository;
	}

	/**
	 * TODO
	 *
	 * @return
	 */
	public static ChangeGroup createChangeGroup() {
		ChangeGroup changeGroup = ChangegroupFactory.eINSTANCE.createChangeGroup();
		return changeGroup;
	}

}
