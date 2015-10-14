package org.iobserve.analysis.usage.modelprovider;

import org.iobserve.analysis.correspondence.ICorrespondence;
import org.palladiosimulator.pcm.core.entity.NamedElement;

import kieker.common.record.IMonitoringRecord;

public class UsageCorrespondenceModel implements ICorrespondence {

	private static final String BOOK_SALE_OPERATION = "orderProducts";
	private static final String BOOK_SALE_CLASS = "ServiceProviderBookSale";

	private static final String COCOME_BOOK_SALE_OPERATION_SIG = "bookSale";

	@Override
	public String getCorrespondent(final String classSig, final String operationSig) {
		if (classSig.contains(BOOK_SALE_CLASS) && operationSig.contains(BOOK_SALE_OPERATION)) {
			return COCOME_BOOK_SALE_OPERATION_SIG;
		}
		return null;
	}

	@Override
	public NamedElement getCorrespondingNode(final IMonitoringRecord record) {
		// TODO Auto-generated method stub
		return null;
	}

}
