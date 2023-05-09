package com.javarush.task.task33.task3309;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/* 
Комментарий внутри xml
*/

public class Solution {
    public static String toXmlWithComment(Object obj, String tagName, String comment) throws JAXBException, ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        StringWriter writer = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(obj, writer);
        Document document = builder.parse(new InputSource(new StringReader(writer.toString())));
        NodeList elements = document.getElementsByTagName(tagName);
        for (int i = 0; i < elements.getLength() ; i++) {
            Node element = elements.item(i);
            if (element.getNodeType() == Node.ELEMENT_NODE) {
                Node parent = element.getParentNode();
                Comment pasteComment = document.createComment(comment);
                parent.insertBefore(pasteComment, element);
            }

        }
        StringWriter result = new StringWriter();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no");

        transformer.transform(new DOMSource(document), new StreamResult(result));
        return result.toString();
    }

    public static void main(String[] args) throws JAXBException, ParserConfigurationException, IOException, TransformerException, SAXException {
        System.out.println(toXmlWithComment(new First(), "second", "it's a comment"));
    }
    //object for marshalling to XML
    @XmlType(name = "First")
    @XmlRootElement
    public static class First {
        @XmlElement
        public static List<String> second = new ArrayList<>();

        public First() {
            second.add("Data-1");
            second.add("Data-2");
            second.add("Data-3");
            second.add("");
        }
    }
}
