package ohm.roth.lbs;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mischarohlederer
 * Date: 25.10.11
 * Time: 19:39
 */

public class Path {
    protected String name;
    protected List<PathSegment> segments;

    public Path(String name) {
        this.segments = new ArrayList<PathSegment>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PathSegment> getSegments() {
        return segments;
    }

    public void setSegments(List<PathSegment> segments) {
        this.segments = segments;
    }

    public void addSegment(PathSegment segment) {
        this.segments.add(segment);
    }
}
