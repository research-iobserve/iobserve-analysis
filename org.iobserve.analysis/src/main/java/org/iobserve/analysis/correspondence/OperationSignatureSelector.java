package org.iobserve.analysis.correspondence;

import org.iobserve.analysis.utils.ToBoolBiFunction;

import protocom.extension.mapping.PcmCorrespondentMethod;
import protocom.extension.mapping.PcmOperationSignature;

@FunctionalInterface
public interface OperationSignatureSelector extends ToBoolBiFunction<PcmCorrespondentMethod, PcmOperationSignature> {
	
	default boolean select(final PcmCorrespondentMethod method, final PcmOperationSignature operation) {
		return this.applyAsBool(method, operation);
	}
}
