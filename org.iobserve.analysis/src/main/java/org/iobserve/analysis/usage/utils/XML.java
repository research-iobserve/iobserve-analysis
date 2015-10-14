/***************************************************************************
 * Copyright 2015 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis.usage.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public final class XML {

	// ********************************************************************
	// * STATIC FIELDS
	// ********************************************************************

	/**
	 * Get the validation warnings during XML creation.<br>
	 * Object is class {@link List} and values in list are class {@link SAXParseException}
	 *
	 */
	public static final String CTX_VALIDATION_WARNINGS = "ctx.validation.warnings";

	/**
	 * Get the validation errors during XML creation.<br>
	 * object is class {@link List} and values in list are class {@link SAXParseException}
	 *
	 */
	public static final String CTX_VALIDATION_ERRORS = "ctx.validation.errors";

	/**
	 * Get the validation fatals during XML creation.<br>
	 * object is class {@link List} and values in list are class {@link SAXParseException}
	 *
	 */
	public static final String CTX_VALIDATION_FATALS = "ctx.validation.fatals";

	/** Set a {@link NamespaceContext} for all filtering actions */
	public static final String CTX_NAMESPACE_CONTEXT = "ctx.namespace.context";

	// ********************************************************************
	// * VARIABLE FIELDS
	// ********************************************************************

	private Document doc;

	/** context of this XML object */
	private final Map<String, Object> context = new HashMap<String, Object>();

	/** namespace context */
	private final SimpleNameSpaceContext namespaceContext = new SimpleNameSpaceContext();

	private Node lastTouched;

	// ********************************************************************
	// * CONSTRUCTOR
	// ********************************************************************

	private XML() {

	}

	private XML(final Document document) {
		this.doc = document;
	}

	// ********************************************************************
	// * STATIC
	// ********************************************************************

	/**
	 * Create a {@link Schema} object based on the given input-stream
	 *
	 * @param input
	 * @return schema
	 */
	public static Schema createSchema(final InputStream input, final String lang) {
		try {
			final StreamSource _ss = new StreamSource(input);
			final SchemaFactory sf = SchemaFactory
					.newInstance(lang);
			final Schema _schema = sf.newSchema(_ss);
			return _schema;
		} catch (final SAXException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static XML valueOf(final InputStream input) {
		try {
			if ((input != null) && (input.available() != 0)) {
				final DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
				df.setNamespaceAware(true);
				final DocumentBuilder docBuilder = df.newDocumentBuilder();
				return new XML(docBuilder.parse(input));
			}
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final SAXException e) {
			e.printStackTrace();
		} catch (final ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static XML valueOfAndValidateDTD(final InputStream input, final InputStream schemaInput) {
		try {
			if ((input != null) && (input.available() != 0)) {

				// validate
				final Schema _schema = XML.createSchema(schemaInput, XMLConstants.XML_DTD_NS_URI);

				final DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
				df.setNamespaceAware(true);
				df.setValidating(true); // this is when validating with DTD
				df.setSchema(_schema);
				final DocumentBuilder docBuilder = df.newDocumentBuilder();
				final SimpleErrorHandler seh = new SimpleErrorHandler();
				docBuilder.setErrorHandler(seh);

				// read
				final XML _xml = new XML(docBuilder.parse(input));

				// eval errors/warnings
				if (!seh.getWarnings().isEmpty()) {
					_xml.getContext().put(CTX_VALIDATION_WARNINGS, seh.getWarnings());
				}
				if (!seh.getErrors().isEmpty()) {
					_xml.getContext().put(CTX_VALIDATION_ERRORS, seh.getErrors());
				}

				return _xml;
			}
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final SAXException e) {
			e.printStackTrace();
		} catch (final ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Create an XML and validate it with the given schema input.
	 *
	 * @param input
	 * @param schemaInput
	 * @return
	 */
	public static XML valueOfAndValidateSchema(final InputStream input, final InputStream schemaInput) {
		try {
			if ((input != null) && (input.available() != 0)) {

				// copy input data
				ByteArrayOutputStream _data = XML.convert(input);
				final ByteArrayInputStream _inputForRead = new ByteArrayInputStream(_data.toByteArray());
				final ByteArrayInputStream _inputForValidate = new ByteArrayInputStream(_data.toByteArray());
				_data.close();
				_data = null;

				// validate
				final Schema _schema = XML.createSchema(schemaInput, XMLConstants.W3C_XML_SCHEMA_NS_URI);
				final Validator _validator = _schema.newValidator();
				final StreamSource _xmlSource = new StreamSource(_inputForValidate);
				_validator.validate(_xmlSource);

				final DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
				df.setNamespaceAware(true);
				df.setSchema(_schema);
				final DocumentBuilder docBuilder = df.newDocumentBuilder();

				// read
				final XML _xml = new XML(docBuilder.parse(_inputForRead));
				return _xml;
			}
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final SAXException e) {
			e.printStackTrace();
		} catch (final ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static XML valueOf(final String xmlexpression) {
		try {
			final StringReader reader = new StringReader(xmlexpression);
			final DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
			df.setNamespaceAware(true);
			final DocumentBuilder docBuilder = df.newDocumentBuilder();
			return new XML(docBuilder.parse(new InputSource(reader)));
		} catch (final SAXException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static XML valueOf(final NodeList list, final String rootName) {
		final XML temp = XML.valueOf("<" + rootName + "/>");
		final Node root = temp.getRootNode();
		for (int i = 0; i < list.getLength(); i++) {
			temp.attachNode(list.item(i), root);
		}
		return temp;
	}

	public static XML valueOf(final NodeList list) {
		try {
			final DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
			df.setNamespaceAware(true);
			final DocumentBuilder docBuilder = df.newDocumentBuilder();
			final Document doc = docBuilder.newDocument();
			final XML temp = new XML();
			for (int i = 0; i < list.getLength(); i++) {
				temp.attachNode(list.item(i), doc);
			}
			return temp;
		} catch (final ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static XML valueOf(final Node node, final String rootName) {
		final XML temp = XML.valueOf("<" + rootName + "/>");
		final Node root = temp.getRootNode();
		temp.attachNode(node, root);
		return temp;
	}

	public static XML valueOf(final Node node) {
		try {
			final DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
			df.setNamespaceAware(true);
			final DocumentBuilder docBuilder = df.newDocumentBuilder();
			final Document doc = docBuilder.newDocument();

			final XML temp = new XML();
			temp.attachNode(node, doc);
			return temp;
		} catch (final ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static XML newInstance() {
		try {
			final DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
			df.setNamespaceAware(true);
			final DocumentBuilder docBuilder = df.newDocumentBuilder();
			return new XML(docBuilder.newDocument());
		} catch (final ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static XML newInstance(final String rootName) {
		final XML temp = XML.valueOf("<" + rootName + "/>");
		return temp;
	}

	/**
	 * Transform the given XML object using the given XSL transformation XML
	 * object.
	 *
	 * @param from
	 * @param xsl
	 * @return String with the transformation
	 */
	public static String transform(final XML from, final XML xsl) {
		try {
			final Document document = from.getDocument();
			final StringReader xslReader = new StringReader(xsl.toString());

			final DOMSource src = new DOMSource(document);
			final StringWriter output = new StringWriter();
			final StreamResult result = new StreamResult(output);

			final TransformerFactory tf = TransformerFactory.newInstance();
			final Transformer t = tf.newTransformer(new StreamSource(xslReader));
			t.transform(src, result);
			return output.getBuffer().toString();
		} catch (final TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (final TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (final TransformerException e) {
			e.printStackTrace();
		}
		return null;
	}

	// ********************************************************************
	// * MANIPULATE XML TREE
	// ********************************************************************

	public void addNode(final Node parent, final String name, final String content) {
		final Element elem = this.doc.createElement(name);
		if (content != null) {
			elem.setTextContent(content);
		}
		parent.appendChild(elem);
		this.lastTouched = elem;
	}

	public Node getLastTouched() {
		return this.lastTouched;
	}

	public void setLastTouched(final Node lastTouched) {
		this.lastTouched = lastTouched;
	}

	// ********************************************************************
	// * PUBLIC
	// ********************************************************************

	/**
	 * Get the context of this XML-Object
	 *
	 * @return
	 */
	public final Map<String, Object> getContext() {
		return this.context;
	}

	/**
	 * Get the name-space context
	 *
	 * @return
	 */
	public SimpleNameSpaceContext getNamespaceContext() {
		return this.namespaceContext;
	}

	/**
	 * Get a context object without casting. The casting is done by the method.
	 *
	 * @param type
	 *            class this object should be
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final <T> T getContext(final Class<T> type, final String key) {
		return (T) this.context.get(key);
	}

	/**
	 * Get the {@link Document} of this XML-Object. It is the source object
	 * which holds all the XML.
	 *
	 * @return
	 */
	public final Document getDocument() {
		return this.doc;
	}

	/**
	 * Validate the XML-Object against the given schema. Any warning, error or
	 * fatal error is saved into the context which can get by {@link #getContext()}.
	 * Following constants have to be used to retrieve such data.
	 * <ul>
	 * <li>{@link #CTX_VALIDATION_WARNINGS}</li>
	 * <li>{@link #CTX_VALIDATION_ERRORS}</li>
	 * <li>{@link #CTX_VALIDATION_FATALS}</li>
	 * </ul>
	 * The context will, if available, provide a List&lt;SAXParseException> or
	 * <b>null</b>
	 *
	 * @param schema.
	 *            All subclasses of {@link InputStream} are allowed. If you
	 *            have the schema as XML object, use {@link #getInputStream()}.
	 */
	public void validate(final InputStream schema) {

		try {
			// validate
			final Schema _schema = XML.createSchema(schema, XMLConstants.W3C_XML_SCHEMA_NS_URI);
			final Validator _validator = _schema.newValidator();
			final SimpleErrorHandler _errorHandler = new SimpleErrorHandler();
			_validator.setErrorHandler(_errorHandler);
			final StreamSource _xmlSource = new StreamSource(this.getInputStream());
			_validator.validate(_xmlSource);

			if (!_errorHandler.getWarnings().isEmpty()) {
				this.getContext().put(CTX_VALIDATION_WARNINGS, _errorHandler.getWarnings());
			}
			if (!_errorHandler.getErrors().isEmpty()) {
				this.getContext().put(CTX_VALIDATION_ERRORS, _errorHandler.getErrors());
			}
			if (!_errorHandler.getFatals().isEmpty()) {
				this.getContext().put(CTX_VALIDATION_FATALS, _errorHandler.getFatals());
			}

		} catch (final SAXException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public final Object search(final String xpathexpression, final Node node,
			final QName xpathconstants) {
		try {
			final XPathFactory xpathfac = XPathFactory.newInstance();
			final XPath path = xpathfac.newXPath();
			// NamespaceContext nsCtx = (NamespaceContext) getContext().get(CTX_NAMESPACE_CONTEXT);
			// if(nsCtx!=null) {
			// path.setNamespaceContext(nsCtx);
			// }
			path.setNamespaceContext(this.namespaceContext);
			return path.evaluate(xpathexpression, node,
					xpathconstants);
		} catch (final XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Search via xpath starting from root
	 *
	 * @param xpathExpr
	 *            ???
	 * @return
	 */
	public final NodeList search(final String xpathExpr) {
		return (NodeList) this.search(xpathExpr, this.getDocument(), XPathConstants.NODESET);
	}

	public final XML filter(final String xpathExpression, final String rootNewXML) {
		if ((xpathExpression == null) || xpathExpression.isEmpty()) {
			throw new IllegalArgumentException("param xpathExpression is either null or empty");
		}
		final NodeList list = (NodeList) this.search(xpathExpression, this.doc, XPathConstants.NODESET);
		return XML.valueOf(list, rootNewXML);
	}

	public final XML filter(final String xpathExpression) {
		if ((xpathExpression == null) || xpathExpression.isEmpty()) {
			throw new IllegalArgumentException("param xpathExpression is either null or empty");
		}
		final NodeList list = (NodeList) this.search(xpathExpression, this.doc, XPathConstants.NODESET);
		return XML.valueOf(list);
	}

	public final Node getNode(final String xpathExpression) {
		if ((xpathExpression == null) || xpathExpression.isEmpty()) {
			throw new IllegalArgumentException("param xpathExpression is either null or empty");
		}
		return (Node) this.search(xpathExpression, this.doc, XPathConstants.NODE);
	}

	public final NodeList getNodes(final String xpathExpression) {
		if ((xpathExpression == null) || xpathExpression.isEmpty()) {
			throw new IllegalArgumentException("param xpathExpression is either null or empty");
		}
		return (NodeList) this.search(xpathExpression, this.doc, XPathConstants.NODESET);
	}

	public final void write(final OutputStream out) {
		try {
			final TransformerFactory tf = TransformerFactory.newInstance();
			final Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.MEDIA_TYPE, "text/xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, "iso-8859-1");
			final DOMSource src = new DOMSource(this.doc);
			final StreamResult result = new StreamResult(out);
			transformer.transform(src, result);
		} catch (final TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (final TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (final TransformerException e) {
			e.printStackTrace();
		}
	}

	public final void write(final Writer writer) {
		try {
			final TransformerFactory tf = TransformerFactory.newInstance();
			final Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.MEDIA_TYPE, "text/xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, "iso-8859-1");
			final DOMSource src = new DOMSource(this.doc);
			final StreamResult result = new StreamResult(writer);
			transformer.transform(src, result);
		} catch (final TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (final TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (final TransformerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the data of the XML as {@link InputStream} object. The data is copied.
	 *
	 * @return
	 */
	public InputStream getInputStream() {
		// copy input data
		ByteArrayOutputStream _data = new ByteArrayOutputStream();
		this.write(_data);
		final ByteArrayInputStream _xmlData = new ByteArrayInputStream(_data.toByteArray());
		try {
			_data.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		_data = null;
		return _xmlData;
	}

	/**
	 * Walk through the XML-file using visitor-pattern approach
	 *
	 * @param start
	 *            use XPath to get the starting point of the walk through. The
	 *            starting point should be a node not a collection of node
	 * @param visitors
	 */
	public final void walkXML(final XMLOption strategy, final String start, final AbstractXMLVisitor... visitors) {
		// TODO
		/*
		 * Eventually on very big XML files the recursive way of this method fails!
		 * This can happen because of StackOverflow. To overcome this problem. Take
		 * a look in giusa.tools.algorithm.heuristics.HierarchyClustering there a Stack-Based
		 * recursive way was used!
		 */
		Node starting = this.getNode(start);
		if (starting.getNodeType() == Node.DOCUMENT_NODE) {
			starting = starting.getFirstChild().getFirstChild();
		}
		if (starting != null) {
			switch (strategy) {
			case BREADTH_FIRST_SEARCH:
				this.walkRecursivlyBreadthFirstSearch(new ArrayList<Node>(), starting, visitors);
				break;
			case DEPTH_FIRST_SEARCH:
				try {
					this.walkRecursivlyDepthFirstSearch(starting, starting, visitors);
				} catch (final XMLNotification e) {
					if (!e.getMessage().equals(XMLNotification.SUCCESSFULLY_FINISHED)) {
						e.printStackTrace();
					}
				}
				break;
			default:
				throw new IllegalArgumentException("passed invalide strategy");
			}

		} else {
			// TODO waring, that first node could not be found
		}
	}

	@Override
	public String toString() {
		String output = null;
		if (this.doc == null) {
			return "";
		}
		try {
			final TransformerFactory tf = TransformerFactory.newInstance();
			final Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			final StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(this.doc), new StreamResult(
					writer));
			output = writer.getBuffer().toString();
		} catch (final TransformerException e) {
			e.printStackTrace();
		}
		return output;
	}

	/**************************************************************************
	 * PRIVATE
	 *************************************************************************/

	public final Node getRootNode() {
		return this.getNode("/*"); //$NON-NLS-1$
	}

	/**
	 * Attach the Node n1 to the document
	 *
	 * @param n1
	 * @param document
	 * @throws DOMException
	 */
	private final void attachNode(final Node n1, final Node n2) {
		try {
			Node myNode = n1.cloneNode(true);
			myNode = this.doc.adoptNode(myNode);
			if (myNode != null) {
				n2.appendChild(myNode);
			}
		} catch (final DOMException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Walk through the document starting from the given start {@link Node}.
	 * Default strategy is a broad search algorithm.
	 *
	 * @param start
	 * @param visitors
	 */
	private void walkRecursivlyBreadthFirstSearch(final List<Node> visitedNodes, final Node node, final AbstractXMLVisitor... visitors) {
		if (node != null) {
			XMLOption option = XMLOption.CONTINUE;
			for (final AbstractXMLVisitor visitor : visitors) {
				try {
					// TODO this is super ugly. Use if-then-else instead
					switch (node.getNodeType()) {
					case Node.ELEMENT_NODE:
						option = visitor.visitNode(node);
						if ((option != XMLOption.BREAK) && !node.getTextContent().isEmpty()) {
							option = visitor.visitNode(node.getTextContent());
						}
						visitedNodes.add(node);

						// compute attributes
						if (node.hasAttributes()) {
							final int _lenAttr = node.getAttributes().getLength();
							Node _nodeAttr;
							for (int i = 0; i < _lenAttr; i++) {
								_nodeAttr = node.getAttributes().item(i);
								option = visitor.visitAttr(_nodeAttr);
								if ((option != XMLOption.BREAK) && !_nodeAttr.getTextContent().isEmpty()) {
									option = visitor.visitAttr(_nodeAttr.getTextContent());
								}
							}
						}
						break;
					default:
						break;
					}
					// TODO check if this can be replaced by a specific exception
				} catch (final Exception e) { // NOCS
					e.printStackTrace();
				}
				switch (option) {
				case CONTINUE:
					// do nothing
					break;
				case BREAK:
					// end with the recursive walk through
					return;
				case SKIP:
					// TODO no functionality yet
					break;
				case BREADTH_FIRST_SEARCH:
					break;
				case DEPTH_FIRST_SEARCH:
					break;
				default:
					break;
				}
			}
			this.walkRecursivlyBreadthFirstSearch(visitedNodes, node.getNextSibling(), visitors);
		}
		// here there is no sibling any more
		if (visitedNodes.size() > 0) {
			final int len = visitedNodes.size() - 1;
			for (int i = 0; i <= len; i++) {
				if (visitedNodes.get(i).hasChildNodes()) {
					final Node n = visitedNodes.get(i).getFirstChild();
					visitedNodes.remove(0);
					this.walkRecursivlyBreadthFirstSearch(visitedNodes, n, visitors);
					break;
				}
			}
		}
	}

	private void walkRecursivlyDepthFirstSearch(Node start, final Node node, final AbstractXMLVisitor... visitors) throws XMLNotification {
		if (node != null) {
			XMLOption option = XMLOption.CONTINUE;
			for (final AbstractXMLVisitor visitor : visitors) {
				try {
					// TODO this is ugly use if-then-else instead
					switch (node.getNodeType()) {
					case Node.ELEMENT_NODE:
						option = visitor.visitNode(node);
						if ((option != XMLOption.BREAK) && !node.getTextContent().isEmpty()) {
							option = visitor.visitNode(node.getTextContent());
						}

						// compute attributes
						if (node.hasAttributes()) {
							final int _lenAttr = node.getAttributes().getLength();
							Node _nodeAttr;
							for (int i = 0; i < _lenAttr; i++) {
								_nodeAttr = node.getAttributes().item(i);
								option = visitor.visitAttr(_nodeAttr);
								if ((option != XMLOption.BREAK) && !_nodeAttr.getTextContent().isEmpty()) {
									option = visitor.visitAttr(_nodeAttr.getTextContent());
								}
							}
						}
						break;
					default:
						break;
					}
					// TODO check if this can be replaced by a specific exception
				} catch (final Exception e) { // NOCS
					e.printStackTrace();
				}
				switch (option) {
				case CONTINUE:
					// do nothing
					break;
				case BREAK:
					// end with the recursive walk through
					return;
				case SKIP:
					// TODO no functionality yet
					break;
				case BREADTH_FIRST_SEARCH:
					break;
				case DEPTH_FIRST_SEARCH:
					break;
				default:
					break;
				}
			}
			this.walkRecursivlyDepthFirstSearch(node, node.getFirstChild(), visitors);
		}
		// here there is no sibling any more
		Node nextSibling = null;
		// TODO this is very ugly, as a parameter passed to the method is used as a variable
		while ((nextSibling = start.getNextSibling()) == null) {
			start = start.getParentNode(); // NOCS (please remove this)
			if (start.getNodeType() == Node.DOCUMENT_NODE) {
				throw new XMLNotification(XMLNotification.SUCCESSFULLY_FINISHED);
			}
		}
		if (nextSibling != null) {
			this.walkRecursivlyDepthFirstSearch(start, nextSibling, visitors);
		}
	}

	// ********************************************************************
	// * IO UTIL
	// ********************************************************************

	/**
	 * Get the bytes copied out of the {@link InputStream} in a virtual input
	 * stream. The given stream is not closed. This method is synchronized on
	 * the given input object. It is Thread-Save for the given input.
	 *
	 * @param input
	 * @return
	 */
	public static ByteArrayOutputStream convert(final InputStream input) {
		ByteArrayOutputStream _out = null;
		synchronized (input) {
			_out = new ByteArrayOutputStream();
			int _byte = -1;
			try {
				while ((_byte = input.read()) != -1) {
					_out.write(_byte);
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return _out;
	}

	/**
	 * Simple error handler for XML-Validation. Saves all occurring problems in
	 * different lists.
	 *
	 * @author Alessandro Giusa,alessandrogiusa@gmail.com
	 * @version 0.1 , 24.08.2014
	 */
	private static class SimpleErrorHandler extends DefaultHandler {

		private final List<SAXParseException> warnings = new ArrayList<SAXParseException>();

		private final List<SAXParseException> errors = new ArrayList<SAXParseException>();

		private final List<SAXParseException> fatals = new ArrayList<SAXParseException>();

		/**
		 * dummy default constructor.
		 */
		public SimpleErrorHandler() {
			super();
		}

		@Override
		public void error(final SAXParseException e) throws SAXException {
			this.errors.add(e);
		}

		@Override
		public void fatalError(final SAXParseException e) throws SAXException {
			this.fatals.add(e);
		}

		@Override
		public void warning(final SAXParseException e) throws SAXException {
			this.warnings.add(e);
		}

		public List<SAXParseException> getWarnings() {
			return this.warnings;
		}

		public List<SAXParseException> getErrors() {
			return this.errors;
		}

		public List<SAXParseException> getFatals() {
			return this.fatals;
		}
	}

	public class XMLNotification extends Throwable {
		public static final String SUCCESSFULLY_FINISHED = "exception.successfully.finished";

		private static final long serialVersionUID = 1L;

		public XMLNotification(final String string) {
			super(string);
		}
	}

	public enum XMLOption {
		CONTINUE, BREAK, SKIP, BREADTH_FIRST_SEARCH, DEPTH_FIRST_SEARCH;
	}

	// ********************************************************************
	// * LOCAL CLASSES
	// ********************************************************************

	// TODO it might be helpful to better understand XML.java to move these classes in separate files
	// and use a package to contain them together with the XML.java class
	public static class SimpleNameSpaceContext implements NamespaceContext {

		private final Map<String, String> ctx = new HashMap<String, String>();

		/**
		 * dummy constructor.
		 */
		public SimpleNameSpaceContext() {

		}

		@Override
		public String getNamespaceURI(final String prefix) {
			final String _ns = this.ctx.get(prefix);
			return _ns != null ? _ns : XMLConstants.NULL_NS_URI;
		}

		@Override
		public String getPrefix(final String namespaceURI) {
			String _prefix = null;
			for (final String nextKey : this.ctx.keySet()) {
				if (this.ctx.get(nextKey).equals(namespaceURI)) {
					_prefix = nextKey;
					break;
				}
			}
			return _prefix != null ? _prefix : XMLConstants.DEFAULT_NS_PREFIX;
		}

		@Override
		public Iterator<String> getPrefixes(final String namespaceURI) {
			final List<String> _list = new ArrayList<String>();
			_list.add(this.getPrefix(namespaceURI));
			return _list.iterator();
		}

		/**
		 * Add a new mapping to this context
		 *
		 * @param prefix
		 * @param namespace
		 */
		public void addMapping(final String prefix, final String namespace) {
			this.ctx.put(prefix, namespace);
		}

		/**
		 * Remove the prefix from the context
		 *
		 * @param prefix
		 */
		public void removePrefix(final String prefix) {
			this.ctx.remove(prefix);
		}

		/**
		 * Remove the prefix which has this name space
		 *
		 * @param ns
		 */
		public void removeNamespace(final String ns) {
			String _key2Rm = null;
			for (final String nextKey : this.ctx.keySet()) {
				if (this.ctx.get(nextKey).equals(ns)) {
					_key2Rm = nextKey;
					break;
				}
			}
			if (_key2Rm != null) {
				this.removePrefix(_key2Rm);
			}
		}
	}

	/**
	 *
	 * @author AlessandroGiusa@gmail.com
	 *
	 */
	public abstract static class AbstractXMLVisitor {

		protected XMLOption visitNode(final Node node) {
			return XMLOption.CONTINUE;
		}

		protected XMLOption visitNode(final String content) {
			return XMLOption.CONTINUE;
		}

		protected XMLOption visitAttr(final Node node) {
			return XMLOption.CONTINUE;
		}

		protected XMLOption visitAttr(final String content) {
			return XMLOption.CONTINUE;
		}

	}

}
