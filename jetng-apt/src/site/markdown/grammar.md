Grammar
=======

The following is a pseudo-ANTLR grammar

    grammar JetNg;
    template: jetDirective NEWLINE content;
    jetDirective: '<%@' SPACE* 'jet' SPACE+ attributes? '%>';
    attributes: (attribute SPACE+)*attribute;
    attribute: attributeName '=' attributeValue;
    attributeName: [A-Za-z0-9_-]+;
    attributeValue: '\'' [^\'\r\n]+ '\''  |
                    '"' [^"\r\n]+ '"' ;
    content: scriptElement | ANY
    scriptElement: comment | expression | directive | scriptlet;
    comment: SCRIPT_START_TAG '--' ANY SCRIPT_END_TAG;
    expressionElement: SCRIPT_START_TAG '=' ANY SCRIPT_END_TAG;
    directiveElement: SCRIPT_START_TAG '@' directive attributes* SCRIPT_END_TAG;
    scriptletElement: SCRIPT_START_TAG ANY SCRIPT_END_TAG; 
    directive: [A-Za-z0-9_-]+;

The first line must be a JET directive

JETNG Templates must be in UTF-8 encoding.