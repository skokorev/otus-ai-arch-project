package ru.yahoondex.archhelper.crawler.services.helper;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.yahoondex.archhelper.crawler.repositories.dao.NameSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathNodes;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class ArxivSetHelper {
    private ArxivSetHelper() {}

    public static List<NameSet> getNameSets(String xmlSets) {
        List<NameSet> nameSets = new LinkedList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(xmlSets));
            Document document = builder.parse(inputSource);

            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathNodes sets = xPath.evaluateExpression("/OAI-PMH/ListSets/set", document, XPathNodes.class);
            for (Node set : sets) {
                Node spec = ((Element) set).getElementsByTagName("setSpec").item(0);
                Node name = ((Element) set).getElementsByTagName("setName").item(0);
                nameSets.add(new NameSet(spec.getTextContent(), name.getTextContent()));
            }
        } catch (ParserConfigurationException pce) {
            log.error("Unexpected error", pce);
        } catch (IOException | SAXException | XPathExpressionException e) {
            log.error("Parsing exception", e);
        }
        return nameSets;
    }
}
