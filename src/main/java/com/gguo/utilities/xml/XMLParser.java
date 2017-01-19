/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gguo.utilities.xml;


import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
/**
 *
 * @author gguo
 */
public class XMLParser {
    
    Document XMLdoc;
    NodeList nodeList;
    NamedNodeMap attributeList;
    Node current, AttributeRoot;
    HashMap<String, String> attriMap;
    Element ElementRoot;
    
    public  XMLParser(String XML) throws Exception {
        XMLdoc= null;

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        ByteArrayInputStream stream = new ByteArrayInputStream(XML.getBytes());
        XMLdoc = docBuilder.parse (stream);
    }

    public  XMLParser(File XMLfile) throws Exception {
        XMLdoc= null;

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        XMLdoc = docBuilder.parse(XMLfile);
    }
        
    public XMLParser(Document newDoc) {
        XMLdoc= newDoc;
    }
    
    public NodeList getNodeList(String tag) {
        return XMLdoc.getElementsByTagName(tag); 
    }
    
    public String getContent(String tag) throws Exception {

        NodeList nodes= XMLdoc.getElementsByTagName(tag);    
        return nodes.item(0).getTextContent();

    }
    
    public String[] getContentList(String tag) throws Exception {

        String[] ret= null;
        NodeList nodes= XMLdoc.getElementsByTagName(tag);
        ret= new String[nodes.getLength()];
        for (int idx= 0;  idx< ret.length;  idx++) {
            ret[idx]= nodes.item(idx).getTextContent();
        }
        return ret;
    }
    
    public int locateContentNodeList(String tag) throws Exception {

        nodeList= XMLdoc.getElementsByTagName(tag);
        return nodeList.getLength();
    }
    
    public int getNodeAttributeList(int idx) {
        attributeList= nodeList.item(idx).getAttributes();
        return attributeList.getLength();
    }
    
    public String[][] getAttributeList1() {
        int size= attributeList.getLength();
        String[][] ret= new String[size][2];
        
        for (int idx= 0;  idx< size;  idx++) {
            ret[idx][0]= attributeList.item(idx).getNodeName();
            ret[idx][1]= attributeList.item(idx).getNodeValue();
        }
        return ret;
    }

    public String[][] getAttributeList() {
        makeAttributeMap();

        int size= attriMap.keySet().size();
        String[][] ret= new String[size][2];
        
        Iterator keys= attriMap.keySet().iterator();
        int idx= 0;
        while (keys.hasNext()) {
            String theKey= (String) keys.next();
            ret[idx][0]= theKey;
            ret[idx][1]= attriMap.get(theKey);
        }
        return ret;
    }
    
    public XMLParser makeAttributeMap() {
        int size= attributeList.getLength();
        attriMap= new HashMap();
        
        for (int idx= 0;  idx< size;  idx++) {
            attriMap.put(attributeList.item(idx).getNodeName(), attributeList.item(idx).getNodeValue());
        }
        return this;
    }
    
    public XMLParser setMapValue(String key, String value) {
        if (attriMap.containsKey(key)) {
            attriMap.put(key, value);
        }
        return this;
    }
    
    
    public NodeList getAttributeNodeList(String content, String attriName, String attriValue) {
        if (locateAttributeNodeList(content, attriName, attriValue)!= -1) {
            return nodeList;
        }
        return null;
    }
    
    public int locateAttributeNodeList(String content, String attriName, String attriValue) {
        try {
            int cIdx= locateContentNodeList(content);
       //     System.out.println("Size "+ cIdx);
            for (int idx= 0;  idx< cIdx;  idx++) {
                int size= getNodeAttributeList(idx);
                for (int aIdx= 0; aIdx< size; aIdx++) {
                    if (attributeList.item(aIdx).getNodeName().equalsIgnoreCase(attriName) && 
                        attributeList.item(aIdx).getNodeValue().equalsIgnoreCase(attriValue)) {
                        nodeList= nodeList.item(idx).getChildNodes();
                        ElementRoot= (Element) nodeList.item(idx);
                        return nodeList.getLength();
                    }
                }
            }
        } catch (Exception Ex) {
            Ex.printStackTrace();
        }
        return -1;
    }

    
    public Node getAttributeRoot() {
        return AttributeRoot;
    }
    
    public NodeList getChildNodeList(int idx) {
        return nodeList.item(idx).getChildNodes();
    }
    
    public NodeList loadChildNodeList(int idx) {
        nodeList= nodeList.item(idx).getChildNodes();
        return nodeList;
    }
    
    public XMLParser locateChildNode(int idx) {
        current= nodeList.item(idx);
        return this;
    }
    
    public XMLParser loadChildNodes() {
        nodeList= current.getChildNodes();
        return this;
    }
    
    public Node getChildNode(int idx) {
        return nodeList.item(idx);
    }
    
    public Element getElementRoot() {
     //   System.out.println("Element name: "+ ElementRoot.getTextContent());
        return ElementRoot;
    }
    
    public Document getDocument() {
        return XMLdoc;
    }
    
    public void writeXML(File outFile) {
        
        try {
            BufferedWriter BW= new BufferedWriter(new FileWriter(outFile));
    //        BW.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+ "\r\n");
            BW.write(XMLtoString());
            BW.close();
        } catch (Exception Ex) {
            JOptionPane.showMessageDialog(null, "Unable to save properties file.\n\n"+ Ex.getMessage());
        } finally {
            
        }
    }
    
    public String XMLtoString() {
            /////////////////
            //Output the XML

            try {
            //set up a transformer
                TransformerFactory transfac = TransformerFactory.newInstance();
                Transformer trans = transfac.newTransformer();
           //     trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
           //     trans.setOutputProperty(OutputKeys.INDENT, "yes");

                trans.setOutputProperty(OutputKeys.METHOD, "xml");
                trans.setOutputProperty(OutputKeys.STANDALONE, "yes");
                trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
           //     trans.setOutputProperty(OutputKeys.INDENT, "yes");
                
            //create string from xml tree
                StringWriter sw = new StringWriter();
                StreamResult result = new StreamResult(sw);
                DOMSource source = new DOMSource(XMLdoc);
                trans.transform(source, result);
                return sw.toString();
            } catch (Exception Ex) {
                Ex.printStackTrace();
                return "";
            }
    }
}
