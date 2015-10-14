package org.iobserve.analysis.usage.transformation;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import org.eclipse.uml2.uml.CallEvent;
import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.usage.EntryCallEventSession;
import org.iobserve.analysis.usage.EntryCallEventWrapper;
import org.iobserve.analysis.usage.utils.FunctionalStream;
import org.iobserve.analysis.usage.utils.FunctionalStream.FunctionalUnaryOperation;
import org.iobserve.analysis.usage.utils.IdProvider;

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
public final class EntryCallEventTokenSequenceAnalyser
		extends AbstractTokenSequenceAnalyser<UsagePathElement<EntryCallEventWrapper>> {

	// ********************************************************************
	// * DEBUGGING
	// ********************************************************************
	// TODO has to be removed
	private static final String PLANT_UML_DIAGRAM_FILE = "test_resources/PlantUML.txt";

	// ********************************************************************
	// * FIELDS
	// ********************************************************************

	/**
	 * Id-Provider for all {@link EntryCallEventWrapper}
	 */
	private final IdProvider<EntryCallEventWrapper> tokenIdProvider = new IdProvider<EntryCallEventWrapper>() {
		@Override
		public String getId(final EntryCallEventWrapper element) {
			final String id = EntryCallEventTokenSequenceAnalyser.getEntryCallEventId(element.event);
			return id;
		}
	};

	/**
	 * Comparator of {@link EntryCallEventWrapper}. It compares the Id's.
	 */
	private final Comparator<EntryCallEventWrapper> tokenComparator = new Comparator<EntryCallEventWrapper>() {
		@Override
		public int compare(final EntryCallEventWrapper e1, final EntryCallEventWrapper e2) {
			return EntryCallEventTokenSequenceAnalyser.this.tokenIdProvider.getId(e1)
					.compareTo(EntryCallEventTokenSequenceAnalyser.this.tokenIdProvider.getId(e2));
		}
	};

	/**
	 * The last action called from the {@link TokenSequenceAnalyser} when processing the tokens
	 */
	private final OnFinish<UsagePathElement<EntryCallEventWrapper>> finishAction = new OnFinish<UsagePathElement<EntryCallEventWrapper>>() {
		@Override
		public void onFinish(final TokenSequenceAnalyser<UsagePathElement<EntryCallEventWrapper>> lexer) {
			EntryCallEventTokenSequenceAnalyser.this.state1.run(lexer);
			// TODO debugging
			System.out.println("Finish-Action");
		}
	};

	private final UsagePath<EntryCallEventWrapper> usagePath;

	private static final int SIZE_STATES = 5;

	// ********************************************************************
	// * CONSTRUCTORS
	// ********************************************************************

	public EntryCallEventTokenSequenceAnalyser() {
		super(SIZE_STATES);
		this.addState(this.state0);
		this.addState(this.state1);
		this.addState(this.state2);
		this.addState(this.state3);
		this.addState(this.state4);
		this.setState(0);
		this.setOnFinishHandler(this.finishAction);

		this.usagePath = new UsagePath<EntryCallEventWrapper>(
				this.tokenIdProvider, this.tokenComparator);
	}

	// ********************************************************************
	// * HOOKS
	// ********************************************************************

	@Override
	protected void onReset() {
		this.usagePath.reset();
	}

	@Override
	public void run() {
		System.out.println("[TimeStamp Start TokenSequenceAnalyser] " + System.currentTimeMillis());
		super.run();
	}

	// ********************************************************************
	// * INITIALIZATION
	// ********************************************************************

	@Override
	public void initTokens(
			final List<UsagePathElement<EntryCallEventWrapper>> origToken) {
		// TODO Auto-generated method stub

	}

	/**
	 * Initialize the usage path given through the {@link EntryCallEvent} objects. This method
	 * will initialize the internally used {@link UsagePath} object. This object is used as an
	 * advanced data structure of the later lexing phase
	 *
	 *
	 *
	 * @param entryCallEvents
	 */
	// TODO this method has to be externalized
	public void initUsagePath(final List<EntryCallEventWrapper> entryCallEvents) {
		final FunctionalStream<EntryCallEventWrapper> stream = new FunctionalStream<EntryCallEventWrapper>(entryCallEvents);

		final List<EntryCallEventSession> sessions = stream
				.divide(new Comparator<EntryCallEventWrapper>() {
					@Override
					public int compare(final EntryCallEventWrapper o1,
							final EntryCallEventWrapper o2) {
						return (o1.event.getHostname() + o1.event.getSessionId())
								.compareTo(o2.event.getHostname() + o2.event.getSessionId());
					}
				})
				.map(new FunctionalUnaryOperation<List<EntryCallEventWrapper>, EntryCallEventSession>() {
					@Override
					public EntryCallEventSession apply(final List<EntryCallEventWrapper> e1) {
						return new EntryCallEventSession(e1);
					}
				}).getElementsAsSingleList();

		for (final EntryCallEventSession nextSession : sessions) {
			for (final EntryCallEventWrapper nextEvent : nextSession.getListEntryCallEvents()) {
				this.usagePath.addElement(nextEvent);
			}
			this.usagePath.reset();
		}

		final List<UsagePathElement<EntryCallEventWrapper>> unsortedElements = this.usagePath.getElements();

		final List<UsagePathElement<EntryCallEventWrapper>> sortedElements = this.sortElements(unsortedElements);

		this.setTokens(sortedElements);

		try {
			final PrintWriter writer = new PrintWriter(PLANT_UML_DIAGRAM_FILE);
			writer.append(this.usagePath.toString());
			writer.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sort EntryCallEventWrapper fist by entry time and if equal by exit time
	 *
	 * @param listToSort
	 * @return
	 */
	private List<UsagePathElement<EntryCallEventWrapper>> sortElements(
			final List<UsagePathElement<EntryCallEventWrapper>> listToSort) {

		final FunctionalStream<UsagePathElement<EntryCallEventWrapper>> stream = new FunctionalStream<UsagePathElement<EntryCallEventWrapper>>(listToSort);

		stream.sort(new Comparator<UsagePathElement<EntryCallEventWrapper>>() {

			@Override
			public int compare(final UsagePathElement<EntryCallEventWrapper> o1,
					final UsagePathElement<EntryCallEventWrapper> o2) {
				final EntryCallEventWrapper e1 = o1.getUserData();
				final EntryCallEventWrapper e2 = o2.getUserData();

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

		final List<UsagePathElement<EntryCallEventWrapper>> sortedList = stream.getElementsAsSingleList();
		return sortedList;
	}

	/**
	 * In case where the entry- and exit time is same, only the index attribute
	 * of the {@link EntryCallEventWrapper} can help to get an order of the events
	 *
	 * @param listToSort
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<UsagePathElement<EntryCallEventWrapper>> sortElementsByIndex(
			final List<UsagePathElement<EntryCallEventWrapper>> listToSort) {

		final FunctionalStream<UsagePathElement<EntryCallEventWrapper>> stream = new FunctionalStream<UsagePathElement<EntryCallEventWrapper>>(listToSort);

		stream.sort(new Comparator<UsagePathElement<EntryCallEventWrapper>>() {

			@Override
			public int compare(final UsagePathElement<EntryCallEventWrapper> o1,
					final UsagePathElement<EntryCallEventWrapper> o2) {
				final EntryCallEventWrapper e1 = o1.getUserData();
				final EntryCallEventWrapper e2 = o2.getUserData();
				if (e1.index > e2.index) {
					return 1;
				} else if (e1.index < e2.index) {
					return -1;
				}
				return 0;
			}
		});

		final List<UsagePathElement<EntryCallEventWrapper>> sortedList = stream.getElementsAsSingleList();
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

	/**
	 * Get the internally used data representation for holding the {@link EntryCallEvent} objects
	 *
	 * @return
	 */
	public UsagePath<EntryCallEventWrapper> getUsagePath() {
		return this.usagePath;
	}

	// ********************************************************************
	// * STATES - MACHINE
	// ********************************************************************

	/**
	 * Abstract class for the {@link TokenSequenceAnalyserState} interface. This class is acting
	 * as local base class for all states used. It overloads also the
	 * {@link TokenSequenceAnalyserState#run(TokenSequenceAnalyser)} method with the specific
	 * {@link EntryCallEventTokenSequenceAnalyser} type to facilitate the access of class
	 * specific methods.
	 *
	 * @author Alessandro Giusa, alessandrogiusa@gmail.com
	 *
	 */
	private abstract class EntryCallEventLexerState
			implements TokenSequenceAnalyserState<UsagePathElement<EntryCallEventWrapper>> {
		@Override
		public final void run(final TokenSequenceAnalyser<UsagePathElement<EntryCallEventWrapper>> lexer) {
			this.run((EntryCallEventTokenSequenceAnalyser) lexer);
		}

		/**
		 * @see TokenSequenceAnalyser#run()
		 * @param lexer
		 */
		public abstract void run(EntryCallEventTokenSequenceAnalyser lexer);
	}

	/**
	 *
	 */
	private final EntryCallEventLexerState state0 = new EntryCallEventLexerState() {

		@Override
		public void run(final EntryCallEventTokenSequenceAnalyser lexer) {
			final UsagePathElement<EntryCallEventWrapper> token = lexer.peekToken();

			if (token.getPredecessors().isEmpty()) {
				// no predecessors means this is the first token and it is
				// not in a loop
				lexer.setState(1);
			} else {
				// it has predecessors, now loop has to be tested
				final Stack<UsagePathElement<EntryCallEventWrapper>> s = EntryCallEventTokenSequenceAnalyser.this.getStack();

				if (s.isEmpty()) {
					lexer.popToken();
					s.push(token);
					EntryCallEventTokenSequenceAnalyser.this.setState(2);
				} else {
					// TODO
				}
			}

		}
	};

	/**
	 * End-State to fire {@link CallEvent}
	 */
	private final EntryCallEventLexerState state1 = new EntryCallEventLexerState() {

		@Override
		public void run(final EntryCallEventTokenSequenceAnalyser lexer) {
			final Stack<UsagePathElement<EntryCallEventWrapper>> s = EntryCallEventTokenSequenceAnalyser.this.getStack();
			UsagePathElement<EntryCallEventWrapper> token = null;
			if (s.isEmpty()) {
				token = lexer.popToken();
			} else {
				token = s.pop();
			}

			if (token.getSuccessors().size() > 1) {
				final ModelBranch<UsagePathElement<EntryCallEventWrapper>> branch = new ModelBranch<UsagePathElement<EntryCallEventWrapper>>(token);
				EntryCallEventTokenSequenceAnalyser.this.callVisitors(branch);
			} else {
				final ModelSystemCall<UsagePathElement<EntryCallEventWrapper>> call = new ModelSystemCall<UsagePathElement<EntryCallEventWrapper>>(token);
				EntryCallEventTokenSequenceAnalyser.this.callVisitors(call);
			}
			EntryCallEventTokenSequenceAnalyser.this.setState(0);
		}
	};

	/**
	 *
	 */
	private final EntryCallEventLexerState state2 = new EntryCallEventLexerState() {

		@Override
		public void run(final EntryCallEventTokenSequenceAnalyser lexer) {
			final UsagePathElement<EntryCallEventWrapper> token = lexer.peekToken();
			final Stack<UsagePathElement<EntryCallEventWrapper>> s = EntryCallEventTokenSequenceAnalyser.this.getStack();
			final UsagePathElement<EntryCallEventWrapper> lastToken = s.peek();

			final boolean pathAvailable = lexer.getUsagePath()
					.existsPath(token, lastToken);
			if (pathAvailable) {
				s.push(token);
				lexer.popToken();
				// could have been a transition to state3
			} else {
				if (s.size() < 2) {
					// stack has only one element, this can not be a loop
					EntryCallEventTokenSequenceAnalyser.this.setState(1);
				} else {
					// stack has more than one element, those are loop
					EntryCallEventTokenSequenceAnalyser.this.setState(4);
				}

			}
		}
	};

	/**
	 *
	 */
	private final EntryCallEventLexerState state3 = new EntryCallEventLexerState() {

		@Override
		public void run(final EntryCallEventTokenSequenceAnalyser lexer) {
			// TODO implementation missing
		}
	};

	/**
	 * End-State, Loop detected, is now extracted
	 */
	private final EntryCallEventLexerState state4 = new EntryCallEventLexerState() {

		@Override
		public void run(final EntryCallEventTokenSequenceAnalyser lexer) {
			final Stack<UsagePathElement<EntryCallEventWrapper>> s = lexer.getStack();

			List<UsagePathElement<EntryCallEventWrapper>> loopItems = new ArrayList<UsagePathElement<EntryCallEventWrapper>>();

			UsagePathElement<EntryCallEventWrapper> item = null;
			while (!s.isEmpty()) {
				item = s.pop();
				loopItems.add(item);
			}

			loopItems = EntryCallEventTokenSequenceAnalyser.this.sortElements(loopItems);
			// TODO sorted by time problem
			/*
			 * sorted by index of arrival time order of each event, since the
			 * entry- and exit-time can be same
			 */

			// send the information that
			final StartModelLoop<UsagePathElement<EntryCallEventWrapper>> startLoop = new StartModelLoop<UsagePathElement<EntryCallEventWrapper>>(loopItems);
			EntryCallEventTokenSequenceAnalyser.this.callVisitors(startLoop);

			for (final UsagePathElement<EntryCallEventWrapper> next : loopItems) {
				if (next.getSuccessors().size() > 1) {
					// and the successors in loop itself
					final ModelBranch<UsagePathElement<EntryCallEventWrapper>> branch = new ModelBranch<UsagePathElement<EntryCallEventWrapper>>(next);
					EntryCallEventTokenSequenceAnalyser.this.callVisitors(branch);
				} else {
					final ModelSystemCall<UsagePathElement<EntryCallEventWrapper>> call = new ModelSystemCall<UsagePathElement<EntryCallEventWrapper>>(next);
					EntryCallEventTokenSequenceAnalyser.this.callVisitors(call);
				}
			}

			final EndModelLoop<UsagePathElement<EntryCallEventWrapper>> endLoop = new EndModelLoop<UsagePathElement<EntryCallEventWrapper>>(loopItems);
			EntryCallEventTokenSequenceAnalyser.this.callVisitors(endLoop);

			lexer.setState(0);

		}
	};

}
