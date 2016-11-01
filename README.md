Java Emitter Template NG
========================

[![Build Status](https://travis-ci.org/trajano/jetng.svg?branch=master)](https://travis-ci.org/trajano/jetng)

This is a rebuild of the [Java Emitter Templates][JET] project without the use
of any Eclipse dependencies.

[JET][] is a tool that can be used to generate Java code from JSP like
templates much like JSPs get converted to Servlet code.  Older versions of
[JET][] created code that used a simple StringBuilder and didn't have any
other dependencies outside of the JDK.

This implementation adds a few extra features to the JET specification.

## Sample .jet file

    <%@ jet package="foo" class="Bar" %>
    Hello world
    <%-- comments --%>
    <% Meta meta = (Meta)arguments; %>
    <%= meta.getValue()%>

It emits a [java file][1] that can be used to build a string or write to a
`java.io.Writer`.  The code itself is fully commented and does not itnroduce
any implicit `import` statement that may cause issues for template writers.
It will also pass most code quality checks from SonarQube, Checkstyle, FindBugs
and PMD.

## Other implementations of JET
The current Eclipse implementation of [JET][] does not work without loading up
in Eclipse.  There is another Maven plugin that does similar work using ANTLR,
but is not in active development.

The [JET2 project proposal][JET2] had indicated that it will be adding 
Eclipse dependencies to the generated code that makes it less feasible to use
outside of Eclipse.

[JET]: http://www.eclipse.org/modeling/m2t/?project=jet
[JET2]: http://www.eclipse.org/modeling/emf/docs/architecture/jet2/jet2.html
[1]: https://github.com/trajano/jetng/blob/master/jetng-core/src/test/resources/BlogPost.java
