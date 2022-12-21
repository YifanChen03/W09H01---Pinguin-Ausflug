package pgdp.pingutrip;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

final public class PinguTrip {

    // To hide constructor in utility class.
    private PinguTrip() {}

    public static Stream<WayPoint> readWayPoints(String pathToWayPoints) {
        // TODO: Task 1
        try {
            return Files.lines(Path.of(pathToWayPoints))
                    .filter(s -> !s.contains("//"))
                    .takeWhile(s -> !s.contains("---"))
                    .map(s -> new WayPoint(Double.parseDouble(s.substring(0, s.indexOf(";"))),
                            Double.parseDouble(s.substring(s.indexOf(";") + 1, s.length()))));
        } catch (IOException e) {
            return Stream.empty();
        }
    }

    public static Stream<OneWay> transformToWays(List<WayPoint> wayPoints) {
        // TODO: Task 2
        return IntStream.range(0, wayPoints.size() - 1)
                .mapToObj(in -> new OneWay(wayPoints.get(in), wayPoints.get(in + 1)));
    }

    public static double pathLength(Stream<OneWay> oneWays) {
        // TODO: Task 3
        return oneWays
                .map(way -> way.getLength())
                .reduce(0.0, (acc, wl) -> acc + wl);
    }

    public static List<OneWay> kidFriendlyTrip(List<OneWay> oneWays) {
        // TODO: Task 4
        double avg_length = oneWays.stream()
                .map(way -> way.getLength())
                .reduce(0.0, (acc, wl) -> acc + wl) / oneWays.size();
        return oneWays.stream()
                .takeWhile(way -> way.getLength() <= avg_length)
                .collect(Collectors.toList());
    }

    public static WayPoint furthestAwayFromHome(Stream<WayPoint> wayPoints, WayPoint home) {
        // TODO: Task 5
        try {
            List<WayPoint> wayPoints_save = wayPoints
                    .collect(Collectors.toList());
            double maxlength = wayPoints_save.stream()
                    .map(wp -> wp.distanceTo(home))
                    .max(Double::compare)
                    .get();
            List<WayPoint> out = wayPoints_save.stream()
                    .filter(wp -> wp.distanceTo(home) == maxlength)
                    .collect(Collectors.toList());
            return out.get(0);
        } catch (Exception e) {
            return home;
        }
    }

    public static boolean onTheWay(Stream<OneWay> oneWays, WayPoint visit) {
        // TODO: Task 6
        return false;
    }

    public static String prettyDirections(Stream<OneWay> oneWays) {
        // TODO: Task 7
        return "";
    }

    public static void main(String[] args) {
        List<WayPoint> wayPoints = readWayPoints("test_paths/path.txt").toList();
        //List.of(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2));

        List<OneWay> oneWays = transformToWays(wayPoints).toList();
        //System.out.println(oneWays);
        //List.of(new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2)));

        double length = pathLength(oneWays.stream());
        //System.out.println(length);
        // 17.230 ...

        List<OneWay> kidFriendly = kidFriendlyTrip(oneWays);
        //System.out.println(kidFriendly);
        // List.of(new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2)));

        WayPoint furthest = furthestAwayFromHome(wayPoints.stream(), wayPoints.get(0));
        System.out.println(furthest);
        // new WayPoint(19.1, 3.2);

        //boolean onTheWay = onTheWay(oneWays.stream(), new WayPoint(0.0, 0.0));
        // false

        //onTheWay = onTheWay(oneWays.stream(), new WayPoint(19.1, 3.2));
        // true

        //String directions = prettyDirections(oneWays.stream());
        // "25 Schritte Richtung 331 Grad."
    }

}
