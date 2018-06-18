package org.iobserve.service.privacy.violation.transformation.privacycheck.policies;

import org.iobserve.service.privacy.violation.transformation.privacycheck.Policy;

/**
 *
 * @author Eric Schmieders
 *
 */

public class NoAnonymizedDataInRUSPolicy extends Policy {

    public NoAnonymizedDataInRUSPolicy() {
        super(Policy.DATACLASSIFICATION.ANONYMOUS, Policy.GEOLOCATION.RUS);
    }
}