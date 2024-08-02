package plotter;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Class representing a polyline, a sequence of points with 
 * line segments connecting them.  Each polyline contains a list of
 * points, a string representing a color, and a width in pixels.
 */
public class Polyline
{
  /**
   * The points in this polyline.
   */
  private ArrayList<Point> points;
  
  /**
   * The width of this polyline in pixels.
   */
  private int width;
  
  /**
   * The color of this polyline as a string. Recognized color names include 
   * "red", "green", "blue", "yellow", "orange", "magenta", "cyan", and "black".  
   */
  private String color;
  
  /**
   * Constructs an empty polyline with the given color and a 
   * width of one pixel.
   * @param givenColor
   *   name of the color for this polyline
   */
  public Polyline(String givenColor)
  {
    this(givenColor, 1);
  }
  
  /**
   * Constructs an empty polyline with the given color and the given
   * width, in pixels.
   * @param givenColor
   *   name of the color for this polyline
   * @param givenWidth
   *   width in pixels for this polyline
   */
  public Polyline(String givenColor, int givenWidth)
  {
    color = givenColor;
    width = givenWidth;
    points = new ArrayList<Point>();
  }
  
  /**
   * Adds a point to the polyline.
   * @param point
   *   point to be added
   */
  public void addPoint(Point point)
  {
    points.add(point);
  }
  
  /**
   * Returns the list of points in this polyline.
   * @return
   *   list of points
   */
  public ArrayList<Point> getPoints()
  {
    return points;
  }
  
  /**
   * Returns the color of this polyline.
   * @return
   *   string representing the color of this polyline
   */
  public String getColor()
  {
    return color;
  }
  
  /**
   * Returns the width of this polyline, in pixels.
   * @return
   *   width of the polyline
   */
  public int getWidth()
  {
    return width;
  }
}
