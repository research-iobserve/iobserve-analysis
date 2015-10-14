package org.iobserve.analysis.usage.transformation;

import java.util.Comparator;
import java.util.List;

import org.eclipse.uml2.uml.CallEvent;
import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.usage.EntryCallEventWrapper;
import org.iobserve.analysis.usage.utils.FunctionalStream;

/**
 * An EntryCallEventLexer uses the idea of a Lexer normally invoked as a first
 * tool in a compiler tool chain to get the token stream formatted in a way that
 * the next tool can better handle it. In this case the next tool is the
 * {@link EntryCallEventModelBuilder}, which will build the model structure of the
 * implied usage model through EntryCallEvent objects into the Palladio-UsageModel
 * (http://www.palladio-simulator.com/science/).
 *
 * @author Alessandro Giusa, alessandrogiusa@gmail.com
 */
public final class SimpleEntryCallEventTokenSequenceAnalyser
		extends AbstractTokenSequenceAnalyser<EntryCallEventWrapper> {

	// ********************************************************************
	// * FIELDS
	// ********************************************************************

	// TODO why do we need these comparators?

	// /**
	// * Id-Provider for all {@link EntryCallEventWrapper}
	// */
	// private final IdProvider<EntryCallEventWrapper> tokenIdProvider = new IdProvider<EntryCallEventWrapper>() {
	// @Override
	// public String getId(final EntryCallEventWrapper element) {
	// final String id = SimpleEntryCallEventTokenSequenceAnalyser.getEntryCallEventId(element.event);
	// return id;
	// }
	// };

	// /**
	// * Comparator of {@link EntryCallEventWrapper}. It compares the Id's.
	// */
	// private final Comparator<EntryCallEventWrapper> tokenComparator = new Comparator<EntryCallEventWrapper>() {
	// @Override
	// public int compare(final EntryCallEventWrapper e1, final EntryCallEventWrapper e2) {
	// return SimpleEntryCallEventTokenSequenceAnalyser.this.tokenIdProvider.getId(e1)
	// .compareTo(SimpleEntryCallEventTokenSequenceAnalyser.this.tokenIdProvider.getId(e2));
	// }
	// };

	/**
	 * The last action called from the {@link TokenSequenceAnalyser} when processing the tokens
	 */
	private final OnFinish<EntryCallEventWrapper> finishAction = new OnFinish<EntryCallEventWrapper>() {
		@Override
		public void onFinish(final TokenSequenceAnalyser<EntryCallEventWrapper> lexer) {
			// is doing nothing
			System.out.println("[Stop TokenSequenceAnalyser] " + System.currentTimeMillis());
		}
	};

	private static final int SIZE_STATES = 5;

	// ********************************************************************
	// * CONSTRUCTORS
	// ********************************************************************

	public SimpleEntryCallEventTokenSequenceAnalyser() {
		super(SIZE_STATES);
		this.addState(this.state0);
		this.setState(0);
		this.setOnFinishHandler(this.finishAction);
	}

	// ********************************************************************
	// * HOOKS
	// ********************************************************************

	@Override
	protected void onReset() {
		// no reset
	}

	@Override
	public void run() {
		System.out.println("[Start TokenSequenceAnalyser] " + System.currentTimeMillis());
		super.run();
	}

	// ********************************************************************
	// * INITIALIZATION
	// ********************************************************************

	@Override
	public void initTokens(final List<EntryCallEventWrapper> origToken) {
		final List<EntryCallEventWrapper> sortedTokens = this.sortElements(origToken);
		this.setTokens(sortedTokens);
	}

	/**
	 * Sort EntryCallEventWrapper fist by entry time and if equal by exit time
	 *
	 * @param listToSort
	 * @return
	 */
	private List<EntryCallEventWrapper> sortElements(
			final List<EntryCallEventWrapper> listToSort) {

		final FunctionalStream<EntryCallEventWrapper> stream = new FunctionalStream<EntryCallEventWrapper>(listToSort);

		stream.sort(new Comparator<EntryCallEventWrapper>() {

			@Override
			public int compare(final EntryCallEventWrapper e1,
					final EntryCallEventWrapper e2) {

				// 1. Attempt of sort: By entry-time
				if (e1.event.getEntryTime() > e2.event.getEntryTime()) {
					return 1;
				} else if (e1.event.getEntryTime() < e2.event.getEntryTime()) {
					return -1;
				}

				// 2. Attempt of sort: By exit-time
				if (e1.event.getExitTime() > e2.event.getExitTime()) {
					return 1;
				} else if (e1.event.getExitTime() < e2.event.getExitTime()) {
					return -1;
				}

				// 3. Attempt of sort: By index of when the event was processed
				if (e1.index > e2.index) {
					return 1;
				} else if (e1.index < e2.index) {
					return -1;
				}

				// if the code reaches this point, the events are not distinguishable
				return 0;
			}
		});

		final List<EntryCallEventWrapper> sortedList = stream.getElementsAsSingleList();
		return sortedList;
	}

	// ********************************************************************
	// * GETTER / SETTER
	// ********************************************************************

	/**
	 * The Id is composed as followed:
	 *
	 * <pre>
	 *  {@link EntryCallEvent#getClassSignature()}.{@link EntryCallEvent#getOperationSignature()}
	 * </pre>
	 *
	 * @param event
	 * @return
	 */
	public static String getEntryCallEventId(final EntryCallEvent event) {
		return event.getClassSignature() + "." + event.getOperationSignature();
	}

	// ********************************************************************
	// * STATES - MACHINE
	// ********************************************************************

	/**
	 * Abstract class for the {@link TokenSequenceAnalyserState} interface. This class is acting
	 * as local base class for all states used. It overloads also the
	 * {@link TokenSequenceAnalyserState#run(TokenSequenceAnalyser)} method with the specific
	 * {@link SimpleEntryCallEventTokenSequenceAnalyser} type to facilitate the access of class
	 * specific methods.
	 *
	 * @author Alessandro Giusa, alessandrogiusa@gmail.com
	 *
	 */
	private abstract class EntryCallEventLexerState
			implements TokenSequenceAnalyserState<EntryCallEventWrapper> {
		@Override
		public final void run(final TokenSequenceAnalyser<EntryCallEventWrapper> lexer) {
			this.run((SimpleEntryCallEventTokenSequenceAnalyser) lexer);
		}

		/**
		 * @see TokenSequenceAnalyser#run()
		 * @param lexer
		 */
		public abstract void run(SimpleEntryCallEventTokenSequenceAnalyser lexer);
	}

	/**
	 * End-State to fire {@link CallEvent}
	 */
	private final EntryCallEventLexerState state0 = new EntryCallEventLexerState() {

		@Override
		public void run(final SimpleEntryCallEventTokenSequenceAnalyser lexer) {
			final EntryCallEventWrapper token = lexer.popToken();

			final ModelSystemCall<EntryCallEventWrapper> call = new ModelSystemCall<EntryCallEventWrapper>(token);
			SimpleEntryCallEventTokenSequenceAnalyser.this.callVisitors(call);
			SimpleEntryCallEventTokenSequenceAnalyser.this.setState(0);
		}
	};

}
