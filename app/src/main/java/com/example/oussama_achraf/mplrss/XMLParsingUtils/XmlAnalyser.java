package com.example.oussama_achraf.mplrss.XMLParsingUtils;

import android.content.ClipData;
import android.util.Log;
import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XmlAnalyser {
    private XmlAnalyser(){}

    public static Document parseXml(String path){

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;

        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;
        try {
            document = db.parse(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return document;
    }

    public static ArrayList<ItemXml> getItemsArray(Document document){

        ArrayList<ItemXml> items = new ArrayList<>();

        NodeList nodeList = document.getElementsByTagName("*");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("item")) {
                //Log.d("parsing",node.getNodeName());
                final NodeList itemChild = node.getChildNodes();
                String title = null,link = null,description = null,image ="emptyimage";

                Log.d("parsing","this items contains :");
                for(int j=0; j< itemChild.getLength(); j++){

                    Log.d("parsing",itemChild.item(j).getNodeName());

                    if(itemChild.item(j).getNodeName().equals("title")){
                        title = itemChild.item(j).getTextContent();
                    }
                    if(itemChild.item(j).getNodeName().equals("link")){
                        link = itemChild.item(j).getTextContent();
                        Log.d("newdebug",link);
                    }
                    if(itemChild.item(j).getNodeName().equals("description")){
                        description = itemChild.item(j).getTextContent();
                        Log.d("newdebug",description);
                    }

                }

                ItemXml itemXml = new ItemXml(title,description,link);
                items.add(itemXml);
            }
        }

        // testing
        for(int i=0;i <items.size() ; i++){
            Log.d("items","items : "+i+ ":");
            Log.d("items","title : "+items.get(i).getTitle());
            Log.d("items","description : "+items.get(i).getDescription());
            Log.d("items","link : "+items.get(i).getLink());


        }
        return items;
    }
    public static String getPrincipalTitle(Document document){
        NodeList channelNode = document.getElementsByTagName("channel");
        Node channel = channelNode.item(0);
        final NodeList channelchild = channel.getChildNodes();
        for(int i =0;i<channelchild.getLength() ; i++){
            if(channelchild.item(i).getNodeName().equals("title")){
                return channelchild.item(i).getTextContent();
            }
        }
        return "None";
    }
    public static String getPrincipalDescription(Document document){
        NodeList channelNode = document.getElementsByTagName("channel");
        Node channel = channelNode.item(0);
        final NodeList channelchild = channel.getChildNodes();
        for(int i =0;i<channelchild.getLength() ; i++){
            if(channelchild.item(i).getNodeName().equals("description")){
                return channelchild.item(i).getTextContent();
            }
        }
        return "None";
    }
}
