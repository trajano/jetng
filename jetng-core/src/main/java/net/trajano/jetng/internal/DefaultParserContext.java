package net.trajano.jetng.internal;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;

import net.trajano.jetng.CycleFoundException;
import net.trajano.jetng.ParseException;
import net.trajano.jetng.ParserContext;

/**
 * Configuration for the JET parser.
 *
 * @author Archimedes Trajano
 */
public class DefaultParserContext implements ParserContext {
    /**
     * Object class name. Defaults to "Object". If not fully qualified, then the
     * package must be placed in imports.
     */
    private String argumentsClassName;
    /**
     * Class name.
     */
    private String className;
    /**
     * Current file.
     */
    private final File currentFile;

    /**
     * End comment tag. This is "--" followed by the end tag.
     */
    private String endCommentTag;

    /**
     * End tag.
     */
    private String endTag;

    /**
     * File positions.
     */
    private final Map<File, FilePosition> filePositions = new HashMap<File, FilePosition>();

    /**
     * File stack.
     */
    private final Stack<File> fileStack = new Stack<File>();

    /**
     * Imports.
     */
    private final SortedSet<String> importedPackages = new TreeSet<String>();

    /**
     * Current indentation level.
     */
    private int indentLevel;

    /**
     * Package name.
     */
    private String packageName;

    /**
     * Start tag.
     */
    private String startTag;

    /**
     * Creates the configuration with the default start and end tags.
     */
    public DefaultParserContext() throws IOException {
        this(NullFile.get());
    }

    /**
     * Creates the configuration with the default start and end tags.
     *
     * @param file
     *            initial file
     */
    public DefaultParserContext(final File file) throws IOException {
        currentFile = file;
        pushFile(file);
        setStartTag("<%");
        setEndTag("%>");
        if (file != NullFile.get()) {
            className = file.getName().substring(0,
                    file.getName().lastIndexOf('.'));
        }
        argumentsClassName = "Object";
    }

    @Override
    public void addImports(final String... imports) {
        for (final String importPackage : imports) {
            importedPackages.add(importPackage);
        }
    }

    @Override
    public String getArgumentsClassName() {
        return argumentsClassName;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public FilePosition getCurrentFilePosition() {
        return filePositions.get(fileStack.peek());
    }

    @Override
    public String getEndCommentTag() {
        return endCommentTag;
    }

    @Override
    public String getEndTag() {
        return endTag;
    }

    @Override
    public Collection<String> getImports() {
        return importedPackages;
    }

    @Override
    public int getIndentLevel() {
        return indentLevel;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public String getStartTag() {
        return startTag;
    }

    @Override
    public String getTargetFile() {
        return packageName.replace('.', '/') + '/' + className + ".java";
    }

    /**
     * Increment current file column.
     */
    @Override
    public void inc() {
        filePositions.get(fileStack.peek()).inc();
    }

    @Override
    public void indent() {
        ++indentLevel;

    }

    @Override
    public boolean isTopFile() {
        return fileStack.size() == 1;
    }

    /**
     * Increment current file row and reset column.
     */
    @Override
    public void nl() {
        filePositions.get(fileStack.peek()).nl();
    }

    /**
     * Pops file from stack.
     */
    @Override
    public void popFile() {
        filePositions.remove(fileStack.pop());
    }

    /**
     * Pushes the file to the stack. If the file is already loaded, it will
     * throw a parse exception to indicate a cycle. File can only be null if the
     * stack is empty.
     *
     * @param file
     *            file to push.
     */
    @Override
    public void pushFile(final File file) throws IOException {
        if (file == NullFile.get() && !fileStack.isEmpty()) {
            throw new ParseException(
                    "Cannot pass a null file handle unless the parse context is empty",
                    this);
        }
        if (filePositions.containsKey(file)) {
            throw new CycleFoundException(file, this);
        }
        filePositions.put(file, new FilePosition(file));
        fileStack.push(file);
    }

    @Override
    public void pushFile(final String fileName) throws IOException {
        final File includedFile = new File(currentFile.getParentFile(),
                fileName);
        pushFile(includedFile);
    }

    @Override
    public void setArgumentsClassName(final String argumentsClassName) {
        this.argumentsClassName = argumentsClassName;
    }

    @Override
    public void setClassName(final String className) {
        this.className = className;
    }

    /**
     * Sets the end tag and related values.
     *
     * @param endTag
     *            end tag
     */
    @Override
    public void setEndTag(final String endTag) {
        this.endTag = endTag;
        endCommentTag = "--" + endTag;
    }

    @Override
    public void setPackageName(final String packageName) {
        this.packageName = packageName;
    }

    /**
     * Sets the start tag and related values.
     *
     * @param startTag
     *            start tag
     */
    @Override
    public void setStartTag(final String startTag) {
        this.startTag = startTag;
    }

    @Override
    public void unindent() throws IOException {
        if (indentLevel == 0) {
            throw new ParseException("Unable to reduce indent level", this);
        }
        --indentLevel;

    }

}
