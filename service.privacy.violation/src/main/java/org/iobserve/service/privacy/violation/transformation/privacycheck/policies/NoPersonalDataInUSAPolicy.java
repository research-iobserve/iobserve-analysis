package org.iobserve.service.privacy.violation.transformation.privacycheck.policies;

import org.iobserve.service.privacy.violation.transformation.privacycheck.Policy;

public class NoPersonalDataInUSAPolicy extends Policy{

	public NoPersonalDataInUSAPolicy() {
		super(Policy.DATACLASSIFICATION.PersonalInformation, Policy.GEOLOCATION.USA);
	}
}