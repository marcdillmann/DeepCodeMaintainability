package def.dom;

import def.js.Object;

public class XPathExpression extends def.js.Object {
    native public XPathExpression evaluate(Node contextNode, double type, XPathResult result);
    public static XPathExpression prototype;
    public XPathExpression(){}
}

