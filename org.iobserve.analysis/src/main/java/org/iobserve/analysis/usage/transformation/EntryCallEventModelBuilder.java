package org.iobserve.analysis.usage.transformation;

import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.usage.EntryCallEventSession;
import org.iobserve.analysis.usage.EntryCallEventWrapper;
import org.iobserve.analysis.usage.modelprovider.UsageModelProvider;
import org.iobserve.analysis.usage.utils.FunctionalStream;
import org.iobserve.analysis.usage.utils.FunctionalStream.FunctionalBinaryOperation;
import org.iobserve.analysis.usage.utils.FunctionalStream.FunctionalUnaryOperation;

public final class EntryCallEventModelBuilder
		implements TokenSequenceAnalyserVisitor<EntryCallEventWrapper>, OnFinish<EntryCallEventWrapper> {

	private final TokenSequenceAnalyser<EntryCallEventWrapper> tokenAnalyser = new SimpleEntryCallEventTokenSequenceAnalyser();

	private final ICorrespondence correspondence;
	private final UsageModelProvider modelProvider;
	private final List<EntryCallEventWrapper> entryCallEventWrappers;

	public EntryCallEventModelBuilder(final ICorrespondence refCorrespondence,
			final UsageModelProvider refModelprovider,
			final List<EntryCallEventWrapper> events) {

		this.correspondence = refCorrespondence;
		this.modelProvider = refModelprovider;
		this.entryCallEventWrappers = events;
	}

	// ********************************************************************
	// * GETTER / SETTER
	// ********************************************************************

	public EObject getModel() {
		return this.modelProvider.getModel();
	}

	public UsageModelProvider getModelProvider() {
		return this.modelProvider;
	}

	// ********************************************************************
	// * INITIALIZATION
	// ********************************************************************

	private void init() {
		this.initEcoreModel();
		this.initTokenAnalyser();
	}

	private void initTokenAnalyser() {
		this.tokenAnalyser.setOnFinishHandler(this);
		this.tokenAnalyser.initTokens(this.entryCallEventWrappers);
		this.tokenAnalyser.addVisitor(this);
	}

	private void initEcoreModel() {
		// TODO this approach is only prototype
		this.modelProvider.createStart();

		final List<Long> interarrivalTimes = this.getInterarrivalTimes();
		final long averageInterarrivalTime = this.calcAverageInterarrivalTime(interarrivalTimes);
		this.modelProvider.createOpenWorkload(averageInterarrivalTime);
	}

	// ********************************************************************
	// * MODEL BUILDING
	// ********************************************************************

	@Override
	public void onFinish(final TokenSequenceAnalyser<EntryCallEventWrapper> tokenAnalyser) {
		this.modelProvider.createStop();
		// TODO this approach is only prototype
	}

	private List<Long> getInterarrivalTimes() {
		final FunctionalStream<EntryCallEventWrapper> stream = new FunctionalStream<EntryCallEventWrapper>(this.entryCallEventWrappers);

		final FunctionalStream<Long> streamInterarrivalTimes = stream
				.divide(new Comparator<EntryCallEventWrapper>() {
					@Override
					public int compare(final EntryCallEventWrapper o1,
							final EntryCallEventWrapper o2) {
						return (o1.event.getHostname() + o1.event.getSessionId())
								.compareTo((o2.event.getHostname() + o2.event.getSessionId()));
					}
				})
				.map(new FunctionalUnaryOperation<List<EntryCallEventWrapper>, EntryCallEventSession>() {
					@Override
					public EntryCallEventSession apply(
							final List<EntryCallEventWrapper> e1) {
						return new EntryCallEventSession(e1);
					}
				})
				.sort(new Comparator<EntryCallEventSession>() {
					@Override
					public int compare(final EntryCallEventSession o1, final EntryCallEventSession o2) {
						return (o1.getStartTime() > o2.getStartTime()) ? 1 : (o1.getStartTime() < o2.getStartTime()) ? -1 : 0;
					}
				})
				.map(new FunctionalBinaryOperation<EntryCallEventSession, Long>() {
					@Override
					public Long apply(final EntryCallEventSession e1,
							final EntryCallEventSession e2) {
						return e2.getStartTime() - e1.getStartTime();
					}
				});

		final List<Long> interarrivalTimes = streamInterarrivalTimes.getElementsAsSingleList();
		return interarrivalTimes;
	}

	/**
	 *
	 * @param interarrivalTimes
	 * @return
	 */
	private long calcAverageInterarrivalTime(final List<Long> interarrivalTimes) {
		if (interarrivalTimes.isEmpty()) {
			throw new IllegalArgumentException("the interarrival times list is empty1");
		}
		long averageInterarrivalTime = 0;
		for (final Long next : interarrivalTimes) {
			averageInterarrivalTime += next.longValue();
		}

		averageInterarrivalTime = averageInterarrivalTime / interarrivalTimes.size();

		System.out.println("Average Interarrival Times = " + averageInterarrivalTime);
		return averageInterarrivalTime;
	}

	// ********************************************************************
	// * PARSING
	// ********************************************************************

	public final void build() {
		System.out.println("[Start EntryCallEventModelBuilder] " + System.currentTimeMillis());
		this.init();
		this.tokenAnalyser.run();
		System.out.println("[Stop EntryCallEventModelBuilder] " + System.currentTimeMillis());
	}

	// ********************************************************************
	// * PARSING VISITOR METHODS
	// * This methods are called from the TokenSequenceAnalyser in time ordered manner
	// ********************************************************************

	private EntryCallEvent lastEvent;

	/**
	 * Check if the last processed event is same to the current. If yes this
	 * event should be skipped.
	 *
	 * @param event
	 * @return
	 */
	private boolean checkIfLastEventSame(final EntryCallEvent event) {
		if ((this.lastEvent != null)
				&& this.lastEvent.getClassSignature().equalsIgnoreCase(event.getClassSignature())
				&& this.lastEvent.getOperationSignature().equalsIgnoreCase(event.getOperationSignature())) {
			return true;
		}
		return false;
	}

	@Override
	public void visit(final EndModelLoop<EntryCallEventWrapper> item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(final ModelBranch<EntryCallEventWrapper> item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(final ModelLoop<EntryCallEventWrapper> item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(final ModelSystemCall<EntryCallEventWrapper> item) {
		final EntryCallEvent event = item.callEvent.event;
		if (!this.checkIfLastEventSame(event)) {
			final String classSig = event.getClassSignature();
			final String opSig = event.getOperationSignature();
			final String operationSig = this.correspondence.getCorrespondent(classSig, opSig);
			if (operationSig != null) {
				this.modelProvider.addAction(operationSig);
			} else {
				// TODO warning?
			}
		}
		this.lastEvent = event;
	}

	@Override
	public void visit(final StartModelLoop<EntryCallEventWrapper> item) {
		// TODO Auto-generated method stub

	}

}
