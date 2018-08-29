package org.palladiosimulator.protocom.correspondencemodel;

import org.iobserve.model.correspondence2.LowLevelModelElement;

/**
 * Description class to describe a {@link LowLevelModelElement}.
 * 
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
public class LowLevelModelElemDescr {

	/**id of this model element*/
	private final String id;
	
	/**name of this model element*/
	private final String name;
	
	/**parent-id of this model element*/
	private final String parentId;

	/**
	 * Constructor initializes all internal fields which are immutable.
	 * @param theId the id of the low-level model element.
	 * @param theName the name.
	 * @param theParentId the id of the parent.
	 */
	public LowLevelModelElemDescr(final String theId, final String theName, final String theParentId) {
		super();
		this.id = theId;
		this.name = theName;
		this.parentId = theParentId;
	}

	/**
	 * @return the id.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @return the name.
	 */
	public String getName() {
		return this.name;
	}	
	
	/**
	 * @return the parent id.
	 */
	public String getParentId() {
		return this.parentId;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
}
