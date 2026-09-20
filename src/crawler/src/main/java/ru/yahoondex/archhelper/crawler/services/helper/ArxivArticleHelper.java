package ru.yahoondex.archhelper.crawler.services.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.iterators.NodeListIterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.yahoondex.archhelper.commons.contracts.kafka.ArticleDto;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathNodes;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class ArxivArticleHelper {
    private ArxivArticleHelper() {}

    public static List<ArticleDto> getArticles(String xmlArticles) {
        List<ArticleDto> articleDtos = new LinkedList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(xmlArticles));
            Document document = builder.parse(inputSource);

            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathNodes records = xPath.evaluateExpression("/OAI-PMH/ListRecords/record", document, XPathNodes.class);
            for (Node record : records) {
                Node header = ((Element) record).getElementsByTagName("header").item(0);
                Node metadata = ((Element) record).getElementsByTagName("metadata").item(0);

                List<String> specs = new LinkedList<>();
                new NodeListIterator(((Element) header).getElementsByTagName("setSpec"))
                        .forEachRemaining(specNode -> specs.add(specNode.getTextContent()));

                String lastPublished = ((Element) header).getElementsByTagName("datestamp").item(0).getTextContent();

                Element arxiv = (Element) ((Element) metadata).getElementsByTagName("arXiv").item(0);
                String id = arxiv.getElementsByTagName("id").item(0).getTextContent();
                String abstr = arxiv.getElementsByTagName("abstract").item(0).getTextContent();
                String title = arxiv.getElementsByTagName("title").item(0).getTextContent();

                articleDtos.add(new ArticleDto(id, LocalDate.parse(lastPublished, DateTimeFormatter.ISO_DATE), abstr, title, specs.toArray(new String[0])));
            }
        } catch (ParserConfigurationException pce) {
            log.error("Unexpected error", pce);
        } catch (IOException | SAXException | XPathExpressionException e) {
            log.error("Parsing exception", e);
        }
        return articleDtos;
    }
}
