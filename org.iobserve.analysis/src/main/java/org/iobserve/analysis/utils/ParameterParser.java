package org.iobserve.analysis.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * This parser will parse arguments passed by the command line. It can parse 
 * 
 * <ul>
 * <li>unnamed arguments sorted by position, where the named arguments do not interfere the position value</li>
 * <li>named arguments --name=value or --name value or -name=value or -name value</li>
 * <li></li>
 * 
 * </ul>
 * 
 * 
 * @author Alessandro Giusa, alessandrogiusa@gmail.com, Robert Heinrich
 * @version 1.0
 *
 */
public class ParameterParser { 

	private final static String PREFIX_UNNAMED_PARAMETER = "$";
	private final static String PREFIX_PARAMETER_DOUBLE_DASH = "--";
	private final static String PREFIX_PARAMETER_SINGEL_DASH = "-";
	private final static String SEPARATOR_KEY_VALUE = "=";
	private final static String SINGLE_QUOATS = "'";
	private final static String DOUBLE_QUOATS = "\"";

	final static ParameterParserState initState = new InitState();
	final static ParameterParserState unnamedParameterState = new UnnamedParameterState();
	final static ParameterParserState doubleDashParameterState = 
			new DashParameterState(ParameterParser.PREFIX_PARAMETER_DOUBLE_DASH);
	final static ParameterParserState singleDashParameterState = 
			new DashParameterState(ParameterParser.PREFIX_PARAMETER_SINGEL_DASH);

	// ********************************************************************
	// * 
	// ********************************************************************

	private final Map<String, String> parameter;
	private ParameterParserState state = ParameterParser.initState;
	private int counterUnnamedParameter = 0;

	/**
	 * Simple constructor doing nothing
	 */
	public ParameterParser() {
		this.parameter =  new HashMap<String, String>();
	}

	/**
	 * Parse the given arguments
	 * @param args arguments to parse
	 */
	public void parse(final String[] args) {
		if (args != null && args.length > 0) {
			final int len = args.length;
			int i = 0;
			while(i < len) {
				final String next = args[i];
				final int retValue = this.state.run(this, next.trim());
				switch (retValue) {
				case ParameterParserState.NEXT_TOKEN:
					i++;
					break;
				case ParameterParserState.SAME_TOKEN:
					break;
				case ParameterParserState.STOP:
					return; // stop the loop
				default:
					break;
				}
			}
		}
	}

	/**
	 * Set the next state
	 * @param state state to set
	 */
	void setState(final ParameterParserState state) {
		this.state = state;
	}

	/**
	 * Get the internal map of parameter
	 * @return map of parameters
	 */
	Map<String, String> getParameter() {
		return this.parameter;
	}

	/**
	 * Add an unnamed parameter
	 * @param value parameter to add
	 */
	void addUnnamedParameter(final String value) {
		final String key = ParameterParser.PREFIX_UNNAMED_PARAMETER + String.valueOf(this.counterUnnamedParameter++);
		this.parameter.put(key, value);
	}

	/**
	 * Add a named parameter
	 * @param key name of the parameter
	 * @param value value of the parameter
	 */
	void addNamedParameter(final String key, final String value) {
		if (this.parameter.containsKey(key)) {
			throw new IllegalArgumentException(String.format("parameter %s already set", key));
		}
		this.parameter.put(key, value);
	}

	// ********************************************************************
	// * CHECK
	// ********************************************************************

	/**
	 * Check whether or not the given argument was passed
	 * @param key name of the argument
	 * @return true if present
	 */
	public boolean contains(final String key) {
		return this.parameter.containsKey(key);
	}

	/**
	 * Check whether or not an argument on the given position is present. Named arguments
	 * will not interfere with the position number.
	 * @param pos position starting by 0
	 * @return true if present
	 */
	public boolean contains(final int pos) {
		final String key = ParameterParser.PREFIX_UNNAMED_PARAMETER + pos;
		return this.contains(key);
	}

	// ********************************************************************
	// * GETTER NAMED
	// ********************************************************************

	/**
	 * Get the long value of the given argument name
	 * @param key argument name
	 * @return value value of parameter
	 * @throws IllegalArgumentException in case the value is not a long
	 */
	public long getNamedLong(final String key) {
		if (!this.parameter.containsKey(key)) {
			throw new IllegalArgumentException(String.format("argument with name %s not passed", key));
		}
		return Long.parseLong(this.parameter.get(key));
	}

	/**
	 * Get the int value of the given argument name
	 * @param key argument name
	 * @return value value of parameter
	 * @throws IllegalArgumentException in case the value is not an int
	 */
	public int getNamedInt(final String key) {
		if (!this.parameter.containsKey(key)) {
			throw new IllegalArgumentException(String.format("argument with name %s not passed", key));
		}
		return Integer.parseInt(this.parameter.get(key));
	}

	/**
	 * Get the double value of the given argument name
	 * @param key argument name
	 * @return value value of parameter
	 * @throws IllegalArgumentException in case the value is not a double
	 */
	public double getNamedDouble(final String key) {
		if (!this.parameter.containsKey(key)) {
			throw new IllegalArgumentException(String.format("argument with name %s not passed", key));
		}
		return Double.parseDouble(this.parameter.get(key));
	}

	/**
	 * Get the float value of the given argument name
	 * @param key argument name
	 * @return value value of parameter
	 * @throws IllegalArgumentException in case the value is not a float
	 */
	public float getNamedFloat(final String key) {
		if (!this.parameter.containsKey(key)) {
			throw new IllegalArgumentException(String.format("argument with name %s not passed", key));
		}
		return Float.parseFloat(this.parameter.get(key));
	}

	/**
	 * Get the boolean value of the given argument name
	 * @param key argument name
	 * @return value value of parameter
	 * @throws IllegalArgumentException in case the value is not a boolean
	 */
	public boolean getNamedBoolean(final String key) {
		if (!this.parameter.containsKey(key)) {
			throw new IllegalArgumentException(String.format("argument with name %s not passed", key));
		}
		return Boolean.parseBoolean(this.parameter.get(key));
	}

	/**
	 * Get the String value of the given argument name
	 * @param key argument name
	 * @return value value of parameter
	 * @throws IllegalArgumentException in case the value is not a string
	 */
	public String getNamedString(final String key) {
		if (!this.parameter.containsKey(key)) {
			throw new IllegalArgumentException(String.format("argument with name %s not passed", key));
		}
		return this.parameter.get(key);
	}

	// ********************************************************************
	// * GETER UNNAMED
	// ********************************************************************

	/**
	 * Get the long value of the given argument position. Named arguments will not interfere 
	 * with the position number
	 * @param pos position starting by 0
	 * @return value value of parameter
	 * @throws IllegalArgumentException in case the value is not a long
	 */
	public long getUnnamedLong(final int pos) {
		final String key = ParameterParser.PREFIX_UNNAMED_PARAMETER + pos;
		return this.getNamedLong(key);
	}

	/**
	 * Get the int value of the given argument position. Named arguments will not interfere 
	 * with the position number
	 * @param pos position starting by 0
	 * @return value value of parameter
	 * @throws IllegalArgumentException in case the value is not an int
	 */
	public int getUnnamedInt(final int pos) {
		final String key = ParameterParser.PREFIX_UNNAMED_PARAMETER + pos;
		return this.getNamedInt(key);
	}

	/**
	 * Get the double value of the given argument position. Named arguments will not interfere 
	 * with the position number
	 * @param pos position starting by 0
	 * @return value value of parameter
	 * @throws IllegalArgumentException in case the value is not a double
	 */
	public double getUnnamedDouble(final int pos) {
		final String key = ParameterParser.PREFIX_UNNAMED_PARAMETER + pos;
		return this.getNamedDouble(key);
	}

	/**
	 * Get the float value of the given argument position. Named arguments will not interfere 
	 * with the position number
	 * @param pos position starting by 0
	 * @return value value of parameter
	 * @throws IllegalArgumentException in case the value is not a float
	 */
	public float getUnnamedFloat(final int pos) {
		final String key = ParameterParser.PREFIX_UNNAMED_PARAMETER + pos;
		return this.getNamedFloat(key);
	}

	/**
	 * Get the boolean value of the given argument position. Named arguments will not interfere 
	 * with the position number
	 * @param pos position starting by 0
	 * @return value value of parameter
	 * @throws IllegalArgumentException in case the value is not a boolean
	 */
	public boolean getUnnamedBoolean(final int pos) {
		final String key = ParameterParser.PREFIX_UNNAMED_PARAMETER + pos;
		return this.getNamedBoolean(key);
	}

	/**
	 * Get the String value of the given argument position. Named arguments will not interfere 
	 * with the position number
	 * @param pos position starting by 0
	 * @return value value of parameter
	 * @throws IllegalArgumentException in case the value is not a String
	 */
	public String getUnnamedString(final int pos) {
		final String key = ParameterParser.PREFIX_UNNAMED_PARAMETER + pos;
		return this.getNamedString(key);
	}

	// ********************************************************************
	// * STATES AND HELPER
	// ********************************************************************

	static String removeQuoats(final String string) {

		if (string.contains(ParameterParser.DOUBLE_QUOATS)) {
			return string.substring(1, string.length() - 1);

		} else if(string.contains(ParameterParser.SINGLE_QUOATS)) {
			return string.substring(1,string.length() - 1);
		} 
		return string;
	}


	/**
	 * State of parsing process
	 * 
	 * @author Alessandro Giusa
	 * @version 1.0
	 *
	 */
	private interface ParameterParserState {

		/**option to get next token*/
		int NEXT_TOKEN = 0;
		/**option to get same token*/
		int SAME_TOKEN = 1;
		/**option to stop*/
		int STOP = 2;

		/**
		 * Run the state
		 * @param parser parser instance
		 * @param next next token to parse
		 * @return value to tell internal loop what to do next see
		 *         {@link #NEXT_TOKEN}, {@link #SAME_TOKEN},{@link #STOP}
		 */
		int run(ParameterParser parser, String next);
	}

	// ********************************************************************
	// * STATES IMPL.
	// ********************************************************************

	/**
	 * The basic dispatcher state
	 * 
	 * @author Alessandro Giusa
	 * @version 1.0
	 *
	 */
	static class InitState implements ParameterParserState {
		@Override
		public int run(final ParameterParser parser, final String next) {
			int retVal = ParameterParserState.SAME_TOKEN;
			if (next.startsWith(ParameterParser.PREFIX_PARAMETER_DOUBLE_DASH)) {
				parser.setState(ParameterParser.doubleDashParameterState);

			} else if (next.startsWith(ParameterParser.PREFIX_PARAMETER_SINGEL_DASH)) {
				parser.setState(ParameterParser.singleDashParameterState);

			} else {
				parser.setState(ParameterParser.unnamedParameterState);
			}
			return retVal;
		}
	}

	/**
	 * State which handles unnamed args but in order they were typed in
	 * 
	 * @author Alessandro Giusa
	 * @version 1.0
	 *
	 */
	static class UnnamedParameterState implements ParameterParserState {

		@Override
		public int run(final ParameterParser parser, final String next) {
			parser.addUnnamedParameter(ParameterParser.removeQuoats(next));
			parser.setState(ParameterParser.initState);
			return ParameterParserState.NEXT_TOKEN;
		}
	}

	/**
	 * State which handles dash named arguments
	 * 
	 * @author Alessandro Giusa
	 * @version 1.0
	 *
	 */
	static class DashParameterState  implements ParameterParserState {

		private final String dashType;
		private String key;
		private String value;

		/**
		 * Create {@link DashParameterState} object with the given dash type
		 * @param dash dash type
		 */
		public DashParameterState(final String dash) {
			this.dashType = dash;
		}

		@Override
		public int run(final ParameterParser parser, final String next) {
			int retVal = ParameterParserState.NEXT_TOKEN;
			if (this.key == null) {
				if (next.contains(ParameterParser.SEPARATOR_KEY_VALUE)) {
					this.key = next.substring(next.indexOf(this.dashType) 
							+ this.dashType.length(), next.indexOf(ParameterParser.SEPARATOR_KEY_VALUE));
					this.value = next.substring(next.indexOf(ParameterParser.SEPARATOR_KEY_VALUE) 
							+ ParameterParser.SEPARATOR_KEY_VALUE.length() ,next.length());
					this.value = ParameterParser.removeQuoats(this.value);
					this.flush(parser);
				} else {
					this.key = next.substring(next.indexOf(this.dashType) 
							+ this.dashType.length(), next.length());
				}

			} else {
				this.value = next;
				this.value = ParameterParser.removeQuoats(this.value);
				this.flush(parser);
			}
			return retVal;
		}

		/**
		 * Flush the key, value to the parser
		 * @param parser parser instance
		 */
		private void flush(final ParameterParser parser) {
			parser.addNamedParameter(this.key, this.value);
			parser.setState(ParameterParser.initState);
			this.key = null;
			this.value = null;
		}
	}
}
