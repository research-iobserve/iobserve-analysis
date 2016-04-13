/***************************************************************************
 * Copyright 2014 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
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
 * @author Alessandro Giusa, alessandrogiusa@gmail.com
 * @author Robert Heinrich
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
	 * Get the number of unnamed parameter
	 * @return
	 */
	public int getCountUnnamedParameter() {
		return this.counterUnnamedParameter;
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
	Map<String, String> getParameters() {
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

	// *****************************************************************
	// CONVENIENT GETTER
	// *****************************************************************

	/**
	 * Get the parameter either by the name or by its position
	 * 
	 * @param name
	 *            name of the parameter. Do not use dash or other, just the name
	 * @param pos
	 *            position like 0 or 1 or other integer.
	 * @param required
	 *            true if the parameter is absolute required. If true and parameter does
	 *            not exist, the runtime exception below is thrown. If false and parameter does not
	 *            exist <b>null</b> is returned.
	 * @throws IllegalArgumentException
	 *             if parameter required and not existent
	 * @return the value of the parameter
	 */
	public String getParameterString(final String name, final int pos, final boolean required) {
		final String value;
		if (this.contains(name)) {
			value = this.getNamedString(name);
		} else if (this.contains(pos)) {
			value = this.getUnnamedString(pos);
			Terminal.printf("read argument %s from %s.slot", name, String.valueOf(pos));

		} else {
			if (required) {
				final String errorMessage = String.format(
						"the argument %s does not exist as "
								+ "named argument nor in position %s.",
								name,String.valueOf(pos));
				throw new IllegalArgumentException(errorMessage);
			} 
			value = null;
		}
		return value;
	}

	/**
	 * Get the parameter either by the name or by its position
	 * 
	 * @param name
	 *            name of the parameter. Do not use dash or other, just the name
	 * @param pos
	 *            position like 0 or 1 or other integer.
	 * @param required
	 *            true if the parameter is absolute required. If true and parameter does
	 *            not exist, the runtime exception below is thrown. If false and parameter does not
	 *            exist <b>0</b> is returned.
	 * @throws IllegalArgumentException
	 *             if parameter required and not existent
	 * @return the value of the parameter
	 */
	public int getParameterInt(final String name, final int pos, final boolean required) {
		final int value;
		if (this.contains(name)) {
			value = this.getNamedInt(name);
		} else if (this.contains(pos)) {
			value = this.getUnnamedInt(pos);
			Terminal.printf("read argument %s from %s.slot", name, String.valueOf(pos));

		} else {
			if (required) {
				final String errorMessage = String.format(
						"the argument %s does not exist as "
								+ "named argument nor in position %s.",
								name,String.valueOf(pos));
				throw new IllegalArgumentException(errorMessage);
			} 
			value = 0;
		}
		return value;
	}

	/**
	 * Get the parameter either by the name or by its position
	 * 
	 * @param name
	 *            name of the parameter. Do not use dash or other, just the name
	 * @param pos
	 *            position like 0 or 1 or other integer.
	 * @param required
	 *            true if the parameter is absolute required. If true and parameter does
	 *            not exist, the runtime exception below is thrown. If false and parameter does not
	 *            exist <b>0</b> is returned.
	 * @throws IllegalArgumentException
	 *             if parameter required and not existent
	 * @return the value of the parameter
	 */
	public long getParameterLong(final String name, final int pos, final boolean required) {
		final long value;
		if (this.contains(name)) {
			value = this.getNamedLong(name);
		} else if (this.contains(pos)) {
			value = this.getUnnamedLong(pos);
			Terminal.printf("read argument %s from %s.slot", name, String.valueOf(pos));

		} else {
			if (required) {
				final String errorMessage = String.format(
						"the argument %s does not exist as "
								+ "named argument nor in position %s.",
								name,String.valueOf(pos));
				throw new IllegalArgumentException(errorMessage);
			} 
			value = 0l;
		}
		return value;
	}

	/**
	 * Get the parameter either by the name or by its position
	 * 
	 * @param name
	 *            name of the parameter. Do not use dash or other, just the name
	 * @param pos
	 *            position like 0 or 1 or other integer.
	 * @param required
	 *            true if the parameter is absolute required. If true and parameter does
	 *            not exist, the runtime exception below is thrown. If false and parameter does not
	 *            exist <b>0</b> is returned.
	 * @throws IllegalArgumentException
	 *             if parameter required and not existent
	 * @return the value of the parameter
	 */
	public float getParameterFloat(final String name, final int pos, final boolean required) {
		final float value;
		if (this.contains(name)) {
			value = this.getNamedFloat(name);
		} else if (this.contains(pos)) {
			value = this.getUnnamedFloat(pos);
			Terminal.printf("read argument %s from %s.slot", name, String.valueOf(pos));

		} else {
			if (required) {
				final String errorMessage = String.format(
						"the argument %s does not exist as "
								+ "named argument nor in position %s.",
								name,String.valueOf(pos));
				throw new IllegalArgumentException(errorMessage);
			} 
			value = 0f;
		}
		return value;
	}

	/**
	 * Get the parameter either by the name or by its position
	 * 
	 * @param name
	 *            name of the parameter. Do not use dash or other, just the name
	 * @param pos
	 *            position like 0 or 1 or other integer.
	 * @param required
	 *            true if the parameter is absolute required. If true and parameter does
	 *            not exist, the runtime exception below is thrown. If false and parameter does not
	 *            exist <b>0</b> is returned.
	 * @throws IllegalArgumentException
	 *             if parameter required and not existent
	 * @return the value of the parameter
	 */
	public double getParameterDouble(final String name, final int pos, final boolean required) {
		final double value;
		if (this.contains(name)) {
			value = this.getNamedDouble(name);
		} else if (this.contains(pos)) {
			value = this.getUnnamedDouble(pos);
			Terminal.printf("read argument %s from %s.slot", name, String.valueOf(pos));

		} else {
			if (required) {
				final String errorMessage = String.format(
						"the argument %s does not exist as "
								+ "named argument nor in position %s.",
								name,String.valueOf(pos));
				throw new IllegalArgumentException(errorMessage);
			} 
			value = 0d;
		}
		return value;
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
