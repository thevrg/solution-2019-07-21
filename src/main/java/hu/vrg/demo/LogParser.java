package hu.vrg.demo;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

public class LogParser {
    public static Collection<Integer> getIdsByMessage(String xml, String message) throws Exception {
        XPathExpression xPathExpression = XPathFactory.newInstance().newXPath().compile("//message[text() = '" + message + "']/../@id");
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(new InputSource(new StringReader(xml)));
        NodeList nodes = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
        ArrayList<Integer> response = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node item = nodes.item(i);
            response.add(Integer.parseInt(item.getTextContent()));
        }
        return response;
    }

    public static void main(String[] args) throws Exception {
        String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<log>\n" +
                        "    <entry id=\"1\">\n" +
                        "        <message>Application started</message>\n" +
                        "    </entry>\n" +
                        "    <entry id=\"2\">\n" +
                        "        <message>Application ended</message>\n" +
                        "    </entry>\n" +
                        "    <entry id=\"3\">\n" +
                        "        <message>Application ended</message>\n" +
                        "    </entry>\n" +
                        "</log>";

        Collection<Integer> ids = getIdsByMessage(xml, "Application ended");
        for (int id : ids) {
            System.out.println(id);
        }
    }
}
