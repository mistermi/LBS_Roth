package ohm.roth.dorenda;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse die ein Dorenda Dokument beschreibt
 */
public class dorendaDocument {
    private List<dorendaLine> lines;
    private List<dorendaPoints> points;

    /**
     * Konstruktor eines Dorenda Dokuments
     */
    public dorendaDocument() {
        lines = new ArrayList<dorendaLine>();
        points = new ArrayList<dorendaPoints>();
    }

    /**
     * Fuegt dem Dokument eine Line Hinzu
     * @param line Die Linie
     */
    public void addLine(dorendaLine line) {
        this.lines.add(line);
    }

    /**
     * Fuegt dem Dokument Punkte hinzu
     * @param points Die Punkte
     */
    public void addPoints(dorendaPoints points) {
        this.points.add(points);
    }

    /**
     * Ausgabe des Dokuments als String
     * @return Das Dokument als String
     */
    @Override
    public String toString() {
        if (lines.size() == 0 && points.size() == 0) return "";
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");
        if (points.size() > 0) {
            for (dorendaPoints p : this.points) {
                result.append(p.toString());
                result.append(NEW_LINE);
            }
        }
        if (lines.size() > 0) {
            for (dorendaLine l : this.lines) {
                result.append(l.toString());
                result.append(NEW_LINE);
            }
        }

        return result.toString();
    }
}
