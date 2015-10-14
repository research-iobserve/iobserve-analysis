package org.iobserve.analysis.usage.transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.iobserve.analysis.usage.utils.CollectionUtility;


public abstract class AbstractTokenSequenceAnalyser<T> implements TokenSequenceAnalyser<T> {
	
	// ********************************************************************
	// * INTERNAL USED INTERFACES
	// ********************************************************************
	
	protected interface TokenSequenceAnalyserState<T> {
		public abstract void run(TokenSequenceAnalyser<T> lexer);
	}
	
	// ********************************************************************
	// * STATIC 
	// ********************************************************************
	
	public static int EOL_STATE = -1;
	
	// ********************************************************************
	// * FIELDS
	// ********************************************************************
	
	public final int lengthStates;
	
	private T[] tokens;
	
	private final TokenSequenceAnalyserState<T>[] states;
	
	private int statePointer = -1;
	private int tokenPointer = 0;
	
	private final List<OnFinish<T>> onFinishHandlers = new ArrayList<OnFinish<T>>();
	
	private final Stack<T> stack = new Stack<T>();
	
	private final List<TokenSequenceAnalyserVisitor<T>> visitors =
			new ArrayList<TokenSequenceAnalyserVisitor<T>>();
	
	// ********************************************************************
	// * CONSTRUCTORS
	// ********************************************************************
	
	@SuppressWarnings("unchecked")
	public AbstractTokenSequenceAnalyser(final int sizeStates) {
		this.states = new TokenSequenceAnalyserState[sizeStates];
		this.lengthStates = sizeStates;
	}
	
	
	// ********************************************************************
	// * GETTTER / SETTER / ADD
	// ********************************************************************
	
	public void setState(final int index, final TokenSequenceAnalyserState<T> state){
		this.states[index] = state;
	}
	
	public void addState(final TokenSequenceAnalyserState<T> state){
		this.setState(++this.statePointer, state);
	}
	
	protected void setTokens(final List<T> tokens) {
		this.tokens = CollectionUtility.toArray(tokens, tokens.get(0).getClass());
	}
	
	public Stack<T> getStack() {
		return this.stack;
	}
	
	// ********************************************************************
	// * VISITOR AND LISTENER METHODS
	// ********************************************************************
	
	protected final void callVisitors(final ModelLoop<T> event){
		for(final TokenSequenceAnalyserVisitor<T> next:getVisitors())
			event.accept(next);
	}
	
	protected final void callVisitors(final StartModelLoop<T> event){
		for(final TokenSequenceAnalyserVisitor<T> next:getVisitors())
			event.accept(next);
	}
	
	protected final void callVisitors(final EndModelLoop<T> event){
		for(final TokenSequenceAnalyserVisitor<T> next:getVisitors())
			event.accept(next);
	}
	
	protected final void callVisitors(final ModelBranch<T> event){
		for(final TokenSequenceAnalyserVisitor<T> next:getVisitors())
			event.accept(next);
	}
	
	protected final void callVisitors(final ModelSystemCall<T> event){
		for(final TokenSequenceAnalyserVisitor<T> next:getVisitors())
			event.accept(next);
	}
	
	// ********************************************************************
	// * STATE MACHINE WORKFLOW
	// ********************************************************************
	
	protected abstract void onReset();
	
	@Override
	public void reset(final int initState){
		this.tokenPointer = 0;
		this.statePointer = initState;
		this.onReset();
	}
	
	@Override
	public void setOnFinishHandler(final OnFinish<T> onFinishHandler) {
		if(!this.onFinishHandlers.contains(onFinishHandler)){
			this.onFinishHandlers.add(onFinishHandler);
		}
	}
	
	@Override
	public void addVisitor(final TokenSequenceAnalyserVisitor<T> visitor) {
		if(!this.visitors.contains(visitor)){
			this.visitors.add(visitor);
		}
	}
	
	protected List<TokenSequenceAnalyserVisitor<T>> getVisitors() {
		return this.visitors;
	}
	
	@Override
	public void removeVisitor(TokenSequenceAnalyserVisitor<T> visitor) {
		this.visitors.remove(visitor);
	}
	
	public void setState(final int index) {
		this.statePointer = index;
	}
	
	public T getToken(final int pos){
		return this.tokens[pos];
	}
	
	public int getTokenPointer(){
		return this.tokenPointer;
	}
	
	public int getTokenSize(){
		return this.tokens.length;
	}
	
	public T peekToken() {
		final T token = this.tokens[this.tokenPointer];
		return token;
	}
	
	public T popToken() {
		final T token = this.tokens[this.tokenPointer++];
		return token;
	}
	
	@Override
	public void run() {
		while(this.statePointer!=EOL_STATE && (this.tokenPointer) < this.tokens.length) {
			TokenSequenceAnalyserState<T> state = this.states[this.statePointer];
			state.run(this);
		}
		for(final OnFinish<T> nextFinishHandler:this.onFinishHandlers){
			try{
				nextFinishHandler.onFinish(this);
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
}
