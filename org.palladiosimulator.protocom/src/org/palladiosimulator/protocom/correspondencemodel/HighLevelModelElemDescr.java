package org.palladiosimulator.protocom.correspondencemodel;

import org.palladiosimulator.pcm.core.entity.Entity;

import org.iobserve.model.correspondence2.HighLevelModelElement;

/**
 * Description class to describe a {@link HighLevelModelElement}.
 * 
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
public class HighLevelModelElemDescr {

	/**entity reference from where later more information can be get*/
	private final Entity enity;
	
	/**id of this model element*/
	private final String id;
	
	/**name of this model element*/
	private final String name;

	public HighLevelModelElemDescr(final Entity theEntity) {
		super();
		this.enity = theEntity;
		this.id = theEntity.getId();
		this.name = theEntity.getEntityName();
	}

	/**
	 * @return the id.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @return the parent id.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return the entity
	 */
	public Entity getEnity() {
		return this.enity;
	}
}
