package net.trajano.jetng;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Default handler. Comments are ignored.
 *
 * @author Archimedes
 *
 */
public class DefaultParseEventHandler implements ParseEventHandler {
	/**
	 * Flag indicating the {@link #header(ParserContext)} has been called.
	 */
	private boolean headerSent;

	@Override
	public final void characters(final ParserContext context, final String characters, final boolean eol,
			final boolean aloneOnLine) throws IOException {
		if (!isContextReadyForWriting(context)) {
			throw new ContextNotReadyException(context);
		}
		doCharacters(context, characters, eol, aloneOnLine);

	}

	/**
	 * Does nothing.
	 *
	 * @param context
	 *            ignored
	 * @param comment
	 *            ignored
	 * @param eol
	 *            ignored
	 */
	@Override
	public final void comment(final ParserContext context, final String comment, final boolean eol) throws IOException {
	}

	@Override
	public void directive(final ParserContext context, final String directiveName, final Map<String, String> attributes)
			throws IOException {
		if ("jet".equals(directiveName)) {
			handleJetDirective(context, attributes);
			if (!headerSent) {
				if (!isContextReadyForWriting(context)) {
					throw new ContextNotReadyException(context);
				}
				header(context);
				headerSent = true;
			}
		} else if ("include".equals(directiveName)) {
			handleIncludeDirective(context, attributes);
		} else if ("taglib".equals(directiveName)) {
			// TODO implement taglib support
			throw new ParseException(MessageFormat.format("Unsupported directive: {0}", directiveName), context);
		} else {
			throw new ParseException(MessageFormat.format("Unsupported directive: {0}", directiveName), context);
		}
	}

	/**
	 * Method to handle characters.
	 *
	 * @param context
	 *            context
	 * @param characters
	 *            characters
	 * @param eol
	 *            end of line
	 * @param aloneOnLine
	 *            alone on line indicator
	 */
	protected void doCharacters(final ParserContext context, final String characters, final boolean eol,
			final boolean aloneOnLine) {
	}

	/**
	 * Method to handle end of the document.
	 *
	 * @param context
	 *            context.
	 */
	protected void doEndDocument(final ParserContext context) {
	}

	/**
	 * Does nothing.
	 *
	 * @param context
	 *            ignored
	 */
	@Override
	public final void endComment(final ParserContext context) {
	}

	@Override
	public final void endDocument(final ParserContext context) throws IOException {
		if (context.getIndentLevel() != 0) {
			throw new ParseException("Indent level is not at zero at the end of the document", context);
		}
		doEndDocument(context);
	}

	@Override
	public void endExpression(final ParserContext context) {
	}

	@Override
	public void endScriptlet(final ParserContext context) {
	}

	@Override
	public void expression(final ParserContext context, final String expression) {
	}

	/**
	 * Handle include directive.
	 *
	 * @param context
	 *            context
	 * @param attributes
	 *            attributes
	 * @throws IOException
	 */
	private void handleIncludeDirective(final ParserContext context, final Map<String, String> attributes)
			throws IOException {
		if (!(attributes.containsKey("file") && attributes.size() == 1)) {
			throw new ParseException("Unexpected JET directives attributes found " + attributes.keySet(), context);
		}

		context.pushFile(attributes.get("file"));
		new JetNgParser(context.getCurrentFilePosition().getFile(), this, 6).parse(context);
		context.popFile();
		context.setStartTag(context.getCurrentFilePosition().getStartTag());
		context.setEndTag(context.getCurrentFilePosition().getEndTag());
	}

	/**
	 * This will throw a parse exception if anything but startTag or endTag is
	 * set.
	 *
	 * @param context
	 *            context
	 * @param attributes
	 *            attributes
	 */
	private void handleJetDirective(final ParserContext context, final Map<String, String> attributes)
			throws IOException {
		if (attributes.get("startTag") != null) {
			context.setStartTag(attributes.get("startTag"));
		}
		if (attributes.get("endTag") != null) {
			context.setEndTag(attributes.get("endTag"));
		}
		final Set<String> attributeNames = new HashSet<String>(attributes.keySet());
		attributeNames.remove("startTag");
		attributeNames.remove("endTag");
		if (!attributeNames.isEmpty() && headerSent) {
			throw new ParseException("Unexpected JET directives attributes found " + attributeNames, context);
		}
		if (attributes.get("imports") != null) {
			context.addImports(attributes.get("imports").split("\\s"));
			attributeNames.remove("imports");
		}
		if (attributes.get("package") != null) {
			context.setPackageName(attributes.get("package"));
			attributeNames.remove("package");
		}
		if (attributes.get("class") != null) {
			context.setClassName(attributes.get("class"));
			attributeNames.remove("class");
		}
		if (attributes.get("argumentsClass") != null) {
			context.setArgumentsClassName(attributes.get("argumentsClass"));
			attributeNames.remove("argumentsClass");
		}
		if (!attributeNames.isEmpty()) {
			throw new ParseException("Unsupported jet directive attributes  " + attributeNames, context);
		}
	}

	/**
	 * This is called by {@link #directive(ParserContext, String, Map)} just
	 * after the first jet directive is handled.
	 *
	 * @param context
	 *            context
	 */
	public void header(final ParserContext context) {
	}

	/**
	 * Check if the context is ready for writing. The class and package names
	 * have to be set for it to be ready for writing.
	 *
	 * @param context
	 *            context to evaluate
	 * @return <code>true</code> if the context is ready for writing.
	 */
	private boolean isContextReadyForWriting(final ParserContext context) {
		return context.getClassName() != null && context.getPackageName() != null;
	}

	@Override
	public void scriptlet(final ParserContext context, final String scriptlet, final boolean eol) {
	}

	/**
	 * Does nothing.
	 *
	 * @param context
	 *            ignored
	 */
	@Override
	public final void startComment(final ParserContext context) {
	}

	@Override
	public void startDocument(final ParserContext context) {
	}

	@Override
	public void startExpression(final ParserContext context) {
	}

	@Override
	public void startScriptlet(final ParserContext context) {
	}
}
