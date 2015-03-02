package com.petcare.demo1;


import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

public class SAXGetVersionService {
    public static String readRssXml(InputSource inStream)throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser saxParser = spf.newSAXParser(); // ´´½¨½âÎöÆ÷
        VesionXMLContent handler = new VesionXMLContent();
        saxParser.parse(inStream, handler);
        return handler.getVersion();
    }
}
