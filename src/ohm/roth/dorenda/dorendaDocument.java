package ohm.roth.dorenda;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mischarohlederer
 * Date: 12.12.11
 * Time: 12:24
 */
public class dorendaDocument {
    private List<dorendaLine> lines;
    private List<dorendaPoints> points;

    public dorendaDocument() {
        lines = new ArrayList<dorendaLine>();
        points = new ArrayList<dorendaPoints>();
    }

    public void addLine(dorendaLine line) {
        this.lines.add(line);
    }

    public void addPoints(dorendaPoints points) {
        this.points.add(points);
    }

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
