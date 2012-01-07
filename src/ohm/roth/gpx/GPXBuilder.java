package ohm.roth.gpx;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import ohm.roth.Path;
import ohm.roth.PathNode;
import ohm.roth.PathSegment;
import ohm.roth.Waypoint;
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
import java.util.List;
import java.util.UUID;

/**
 * Erstellt GPX Dokumente aus verschiedenen klassen
 */
public class GPXBuilder {

    /**
     * Erstellt ein GPX Dokument aus einem path Objekt
     * @param path Der zu verwendende Pfad
     * @param name Der Name des Pfades
     * @return Das GPX Dokument als Text
     */
    public static String build(Path path, String name) {
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);
            doc.setXmlVersion("1.0");
            Element root = doc.createElement("gpx");
            root.setAttribute("version", "1.1");
            root.setAttribute("creator", "test2");
            root.setAttribute("xmlns", "http://www.topografix.com/GPX/1/1");
            root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            root.setAttribute("xsi:schemaLocation", "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd");
            doc.appendChild(root);

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

            for (PathSegment segment : path.getSegments()) {
                Element trackSegment = doc.createElement("trkseg");
                track.appendChild(trackSegment);
                Element trackPoint = doc.createElement("trkpt");
                trackname = doc.createElement("name");
                trackname.appendChild(doc.createTextNode(UUID.randomUUID().toString()));
                trackPoint.appendChild(trackname);
                trackPoint.setAttribute("lat", String.valueOf(segment.getStartWaypoint().y));
                trackPoint.setAttribute("lon", String.valueOf(-segment.getStartWaypoint().x));
                trackSegment.appendChild(trackPoint);
                PathNode currNode = segment.getFirstNode();
                while (currNode != null) {
                    if (currNode.getNextEdge() != null) {
                        for (Coordinate coord : currNode.getNextEdge().getEdgeGeo().getCoordinates()) {
                            trackPoint = doc.createElement("trkpt");
                            trackname = doc.createElement("name");
                            trackname.appendChild(doc.createTextNode(UUID.randomUUID().toString()));
                            trackPoint.appendChild(trackname);
                            trackPoint.setAttribute("lat", String.valueOf(coord.y));
                            trackPoint.setAttribute("lon", String.valueOf(-coord.x));
                            trackSegment.appendChild(trackPoint);
                        }
                    }
                    currNode = currNode.getNextNode();
                }
                trackPoint = doc.createElement("trkpt");
                trackname = doc.createElement("name");
                trackname.appendChild(doc.createTextNode(UUID.randomUUID().toString()));
                trackPoint.appendChild(trackname);
                trackPoint.setAttribute("lat", String.valueOf(segment.getEndWaypoint().y));
                trackPoint.setAttribute("lon", String.valueOf(-segment.getEndWaypoint().x));
                trackSegment.appendChild(trackPoint);
            }

            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);

            return sw.toString();

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * Erstellt ein Dorenda Dokument aus einer Liste von Wegpukten
     * @param waypoints Die Liste der Wegpunkte
     * @param name Der Name des daruas erstellten Pfades
     * @return Das GPX Dokument als String
     */
    public static String build(List<Waypoint> waypoints, String name) {
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);
            doc.setXmlVersion("1.0");

            Element root = doc.createElement("gpx");
            root.setAttribute("version", "1.1");
            root.setAttribute("creator", "test2");
            root.setAttribute("xmlns", "http://www.topografix.com/GPX/1/1");
            root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            root.setAttribute("xsi:schemaLocation", "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd");
            doc.appendChild(root);

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
                trackPoint.setAttribute("lat", Double.toString(currWaypoint.y));
                trackPoint.setAttribute("lon", Double.toString(-currWaypoint.x));
                trackSegment.appendChild(trackPoint);

            }


            Element trackPoint = doc.createElement("trkpt");
            trackname = doc.createElement("name");
            trackname.appendChild(doc.createTextNode(UUID.randomUUID().toString()));
            trackPoint.appendChild(trackname);
            trackPoint.setAttribute("lat", Double.toString(waypoints.get(0).y));
            trackPoint.setAttribute("lon", Double.toString(-waypoints.get(0).x));
            trackSegment.appendChild(trackPoint);

            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);

            return sw.toString();

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * Erstellt ein GPX Dokument aus einem LineString
     * @param line Der Line String
     * @param name Der Name des daraus gebildeten Pfades
     * @return Das GPX Dokument als String
     */
    public static String build(LineString line, String name) {
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);
            doc.setXmlVersion("1.0");

            Element root = doc.createElement("gpx");
            root.setAttribute("version", "1.1");
            root.setAttribute("creator", "test2");
            root.setAttribute("xmlns", "http://www.topografix.com/GPX/1/1");
            root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            root.setAttribute("xsi:schemaLocation", "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd");
            doc.appendChild(root);

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

            for (Coordinate currCoord : line.getCoordinates()) {

                Element trackPoint = doc.createElement("trkpt");
                trackname = doc.createElement("name");
                trackname.appendChild(doc.createTextNode(UUID.randomUUID().toString()));
                trackPoint.appendChild(trackname);
                trackPoint.setAttribute("lat", Double.toString(currCoord.y));
                trackPoint.setAttribute("lon", Double.toString(-currCoord.x));
                trackSegment.appendChild(trackPoint);

            }
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);

            return sw.toString();

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

}
