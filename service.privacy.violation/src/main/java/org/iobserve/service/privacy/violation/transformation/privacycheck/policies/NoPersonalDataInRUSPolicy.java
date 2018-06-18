package org.iobserve.service.privacy.violation.transformation.privacycheck.policies;

import org.iobserve.service.privacy.violation.transformation.privacycheck.Policy;

/**
 *
 * @author Eric Schmieders
 *
 */

public class NoPersonalDataInRUSPolicy extends Policy {

    public NoPersonalDataInRUSPolicy() {
        super(Policy.DATACLASSIFICATION.PERSONAL, Policy.GEOLOCATION.RUS);
    }
}