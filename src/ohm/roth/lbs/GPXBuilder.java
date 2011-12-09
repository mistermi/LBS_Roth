package ohm.roth.lbs;


import com.vividsolutions.jts.geom.Coordinate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * User: mischarohlederer
 * Date: 25.10.11
 * Time: 20:39
 */
public class GPXBuilder {
    public static String build(String filename, Path path) {
        try {
            /////////////////////////////
            //Creating an empty XML Document

            //We need a Document
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);
            doc.setXmlVersion("1.0");

            ////////////////////////
            //Creating the XML tree
            FileOutputStream fos = null;
            fos = new FileOutputStream("tmppath.txt");
            OutputStreamWriter out2 = new OutputStreamWriter(fos);
            out2.write("LINE mode=1 col=0,0,255,75\n");
            //create the root element and add it to the document
            Element root = doc.createElement("gpx");
            root.setAttribute("version", "1.1");
            root.setAttribute("creator", "test2");
            root.setAttribute("xmlns", "http://www.topografix.com/GPX/1/1");
            root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            root.setAttribute("xsi:schemaLocation", "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd");
            doc.appendChild(root);

            //create child element, add an attribute, and add to root
            Element meta = doc.createElement("metadata");
            root.appendChild(meta);

            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode("FOO"));
            meta.appendChild(name);


            Element track = doc.createElement("trk");
            root.appendChild(track);
            Element trackname = doc.createElement("name");
            trackname.appendChild(doc.createTextNode(path.getName()));
            track.appendChild(trackname);

            for (PathSegment segment : path.getSegments()) {
                Element trackSegment = doc.createElement("trkseg");
                track.appendChild(trackSegment);
                PathNode currNode = segment.getFirstNode();
                while (currNode != null) {
                    if (currNode.getNextEdge() != null) {
                    Edge e = currNode.getNextEdge();
                    for (Coordinate coord : currNode.getNextEdge().getEdgeGeo().getCoordinates()) {
                        out2.write(coord.x + "," + coord.y + "\n");
                        Element trackPoint = doc.createElement("trkpt");
                        trackname = doc.createElement("name");
                        trackname.appendChild(doc.createTextNode(UUID.randomUUID().toString()));
                        trackPoint.appendChild(trackname);
                       trackPoint.setAttribute("lat", String.valueOf(coord.y));
                        trackPoint.setAttribute("lon", String.valueOf(-coord.x));
                        trackSegment.appendChild(trackPoint);
                    }
                    out2.flush();
                    }

                    currNode = currNode.getNextNode();
                }
            }
            out2.close();
                fos.close();

            /////////////////
            //Output the XML

            //set up a transformer
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            //create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);

            //print xml
            return sw.toString();

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static String build(String filename, List<Waypoint> waypoints, String name) {
        try {
            /////////////////////////////
            //Creating an empty XML Document

            //We need a Document
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);
            doc.setXmlVersion("1.0");

            ////////////////////////
            //Creating the XML tree

            //create the root element and add it to the document
            Element root = doc.createElement("gpx");
            root.setAttribute("version", "1.1");
            root.setAttribute("creator", "test2");
            root.setAttribute("xmlns", "http://www.topografix.com/GPX/1/1");
            root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            root.setAttribute("xsi:schemaLocation", "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd");
            doc.appendChild(root);

            //create child element, add an attribute, and add to root
            Element meta = doc.createElement("metadata");
            root.appendChild(meta);

            Element docname;
            docname = doc.createElement("name");
            docname.appendChild(doc.createTextNode("FOO"));
            meta.appendChild(docname);


            Element track = doc.createElement("trk");
            root.appendChild(track);
            Element trackname = doc.createElement("name");
            trackname.appendChild(doc.createTextNode(name));
            track.appendChild(trackname);

            Element trackSegment = doc.createElement("trkseg");
            track.appendChild(trackSegment);

            for (Waypoint currWaypoint : waypoints) {

                Element trackPoint = doc.createElement("trkpt");
                trackname = doc.createElement("name");
                trackname.appendChild(doc.createTextNode(UUID.randomUUID().toString()));
                trackPoint.appendChild(trackname);
                trackPoint.setAttribute("lat", Double.toString(currWaypoint.getLat()));
                trackPoint.setAttribute("lon", Double.toString(-currWaypoint.getLon()));
                trackSegment.appendChild(trackPoint);

            }


            Element trackPoint = doc.createElement("trkpt");
            trackname = doc.createElement("name");
            trackname.appendChild(doc.createTextNode(UUID.randomUUID().toString()));
            trackPoint.appendChild(trackname);
            trackPoint.setAttribute("lat", Double.toString(waypoints.get(0).getLat()));
            trackPoint.setAttribute("lon", Double.toString(-waypoints.get(0).getLon()));
            trackSegment.appendChild(trackPoint);

            /////////////////
            //Output the XML

            //set up a transformer
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            //create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);

            //print xml
            return sw.toString();

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

        public static String build(String filename, NavGraph graph) {
        try {
            /////////////////////////////
            //Creating an empty XML Document

            //We need a Document
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);
            doc.setXmlVersion("1.0");

            ////////////////////////
            //Creating the XML tree

            //create the root element and add it to the document
            Element root = doc.createElement("gpx");
            root.setAttribute("version", "1.1");
            root.setAttribute("creator", "test2");
            root.setAttribute("xmlns", "http://www.topografix.com/GPX/1/1");
            root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            root.setAttribute("xsi:schemaLocation", "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd");
            doc.appendChild(root);

            //create child element, add an attribute, and add to root
            Element meta = doc.createElement("metadata");
            root.appendChild(meta);

            Element docname;
            docname = doc.createElement("name");
            docname.appendChild(doc.createTextNode("FOO"));
            meta.appendChild(docname);


            Element track = doc.createElement("trk");
            root.appendChild(track);
            Element trackname = doc.createElement("name");
            trackname.appendChild(doc.createTextNode("FOO"));
            track.appendChild(trackname);


            Element trackPoint;
            ArrayList<String> visitedNodes = new ArrayList<String>();
            for (Edge currNode : graph.getEdgeList().values()) {

                if (graph.getNode(currNode.getTo()).getPosition().getLat() > 49.49452 ||
                    graph.getNode(currNode.getTo()).getPosition().getLat() < 49.44930 ||
                    -graph.getNode(currNode.getTo()).getPosition().getLon() < 11.07744 ||
                    -graph.getNode(currNode.getTo()).getPosition().getLon() > 11.082 ||
                    graph.getNode(currNode.getFrom()).getPosition().getLat() > 49.49452 ||
                    graph.getNode(currNode.getFrom()).getPosition().getLat() < 49.44930 ||
                    -graph.getNode(currNode.getFrom()).getPosition().getLon() < 11.07744 ||
                    -graph.getNode(currNode.getFrom()).getPosition().getLon() > 11.082) {
                    continue;
                }
                Element trackSegment = doc.createElement("trkseg");
                track.appendChild(trackSegment);

                trackPoint = doc.createElement("trkpt");
                trackPoint.setAttribute("lat", Double.toString(
                        graph.getNode(currNode.getFrom()).getPosition().getLat()
                ));
                trackPoint.setAttribute("lon", Double.toString(
                        graph.getNode(currNode.getFrom()).getPosition().getLon()
                ));
                trackSegment.appendChild(trackPoint);
                trackPoint = doc.createElement("trkpt");
                trackPoint.setAttribute("lat", Double.toString(
                        graph.getNode(currNode.getTo()).getPosition().getLat()
                ));
                trackPoint.setAttribute("lon", Double.toString(
                        -graph.getNode(currNode.getTo()).getPosition().getLon()
                ));
                trackSegment.appendChild(trackPoint);
            }

            /////////////////
            //Output the XML

            //set up a transformer
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            //create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);

            //print xml
            return sw.toString();

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
