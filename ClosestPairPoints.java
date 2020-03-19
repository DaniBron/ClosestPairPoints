//package closestpairpoints;

import javafx.geometry.Point2D;
import static java.lang.Double.MAX_VALUE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import static javafx.scene.input.KeyCode.T;

/**
 **  Implement the divide-and-conquer closest-pair-of-points algorithm.  
 * Clearly comment your code.  Demonstrate that your code works by using a set of data of at least 10,000 points.  
 * Make it easy to change the data set.
 * @author Danny Bronshtein.
 */
public class ClosestPairPoints {

    /**
     * @param args In the main method, I just created an araay of points and allocated space
     * for it.
     * ********************** Variables *******************************
     * p: array of points with allocation to 10,000 points.
     */
    public static void main(String[] args) 
    {
        Point2D[] p = new Point2D[10000];
        p = randomPoints();
        System.out.println(closestPairPoints(p));
    }

    /**
     * This method generates 10,000 random points
     * @return randomPoints 
     */
    public static Point2D[] randomPoints() 
    {
        Point2D[] randomPoints = new Point2D[10000];

        for (int i = 0; i < 10000; i++) 
        {
            double x = Math.random() + 1;
            double y = Math.random() + 1;
            randomPoints[i] = new Point2D(x, y);
        }
        return randomPoints;
    }

    /**
     * This method gets input of array of points. With the input of multiple points
     * it checks for the smallest distance between two points. Run time: O(n log n)
     * 
     * @param points --> Array of points.
     * @return midDelta. midDelta --> Is the smallest distance between two points.
     */
    public static double closestPairPoints(Point2D[] points) 
    {
        /**
         * First, we want to sort the array of points by x cordinatce. This will make
         * sure when we divide the array by line L will be in the middle between all the
         * points.
         */
        int n = points.length;
        List<Point2D> xCoordinates = new ArrayList<Point2D>(n);
        for (int i = 0; i < points.length; i++) 
        {
            xCoordinates.add(points[i]);
        }
        /**
         * Sort the array by there x-coordinate.
         */
        Collections.sort(xCoordinates, new Comparator<Point2D>() 
        {
            public int compare(Point2D x1, Point2D x2) {
                int result = Double.compare(x1.getX(), x2.getX());
                if (result == 0) 
                {
                    // if x-coordinates are equal, compare y-coordinate.
                    result = Double.compare(x1.getY(), x2.getY());
                }
                return result;
            }
        });
        //Next, after we sorted the points by their x-coordinate
        //we want to generate the array with those loints.
        Point2D[] arraySortPoints = new Point2D[xCoordinates.size()];
        for (int i = 0; i < xCoordinates.size(); i++) 
        {
            Point2D p = new Point2D(xCoordinates.get(i).getX(), xCoordinates.get(i).getY());
            arraySortPoints[i] = p;
        }
        // Separation line L such that half the points are on one side and half
        // on the other side.
        Point2D[] rightSide = Arrays.copyOfRange(arraySortPoints, 0, n / 2);
        Point2D[] leftSide = Arrays.copyOfRange(arraySortPoints, n / 2, n);
        //Inishilize a min varibale with large number.
        //Compare min to the distance between two points.
        //If, new distance is the smallest --> make it as min.
        double min = MAX_VALUE;
        if (arraySortPoints.length <= 3) 
        {
            for (int i = 0; i < n; i++) 
            {
                for (int j = i + 1; j < n; j++) 
                {
                    if (arraySortPoints[i].distance(arraySortPoints[j]) < min) 
                    {
                        min = arraySortPoints[i].distance(arraySortPoints[j]);
                    }
                }
            }
            return min;
        }

        // recursive call to divide the points in each side.
        //The recursive call will accure till we have 3 points.
        double delta1 = closestPairPoints(leftSide);
        double delta2 = closestPairPoints(rightSide);
        //After the recursive call accure, compare between delta1 and delta 2.
        //minDelta --> Is the minimum distance.
        double minDelta = Math.min(delta1, delta2);

        // Add points so we can delete later.
        List<Point2D> arrayPoints = new ArrayList<Point2D>(n);
        for (int i = 0; i < n; i++) 
        {
            arrayPoints.add(arraySortPoints[i]);
        }

        // Delte all points further than minDelta from separation line L.
        for (int i = 1; i < arrayPoints.size(); i++) {
            if (arrayPoints.get(i).distance(arrayPoints.get(i - 1)) < minDelta) 
            {
                arrayPoints.remove(i);
            }
        }
        https: // stackoverflow.com/questions/5178092/sorting-a-list-of-points-with-java?fbclid=IwAR1dGJvYBAXcGmTMEYFzaGZe4y0L69lNWwFzYU3B2dimcn-GNlezGWPy5Kw
        Collections.sort(arrayPoints, new Comparator<Point2D>()
        {
            public int compare(Point2D p1, Point2D p2) 
            {
                return Double.compare(p1.getY(), p2.getY());
            }
        });

        // Scan points in y-order and compare distance between each point and
        // Next, 11 neighbors. If any of these distances is less than minDelta,
        // Update minDelta.
        for (int i = 1; i < 11; i++) {
            if (arrayPoints.get(i).distance(arrayPoints.get(i - 1)) < minDelta) {
                minDelta = arrayPoints.get(i).distance(arrayPoints.get(i - 1));
            }
        }
        //Returns the minimum distance between two points.
        return minDelta;
    }
}
