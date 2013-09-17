/*
 * Copyright 2013 (C) NCIM Groep
 * 
 * Created on : 
 * Author     : Bas W. Knopper
 * 
 * This class is used for the JavaOne Demo on 09/24/2013
 * for the following session: 
 * 
 * Evolutionary Algorithms: The Key to Solving Complex Java Puzzles [BOF2913]
 *
 */

package nl.ncim.javaone.tsp.ea;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nl.ncim.javaone.tsp.domain.City;

public class CandidateSolution implements Comparable<CandidateSolution>
{

   /**
    * The route for the TSP
    */
   private List<City> visitingCities;

   /**
    * The route for the TSP
    */
   private City baseCity;

   /**
    * The fitness of this CandidateSolution which consists of the total
    * distance of its route
    */
   private double fitness;

   private List<City> route;

   /**
    * CandidateSolution constructor
    * @param baseCity 
    * 
    * @param visitingCities
    *            the route for the TSP
    */
   public CandidateSolution(City baseCity, List<City> visitingCities)
   {
      this.baseCity = baseCity;
      this.visitingCities = visitingCities;

      /* create the route */
      route = new ArrayList<City>();
      route.add(baseCity); // start at base
      route.addAll(visitingCities); // visit all cities
      route.add(baseCity); // return to base
   }

   /**
    * Gets the route of this CandidateSolution
    * 
    * @return route
    */
   public List<City> getRoute()
   {
      return route;
   }

   /**
    * Returns the fitness of this Candidate Solution. Calculated by summing up
    * the distance of the whole route.
    * 
    * @return distance over whole route
    */
   public double getFitness()
   {

      /* doing some caching here... */
      if(this.fitness == 0)
      {

         calculateFitness();
      }

      return fitness;
   }

   /**
    * Calculates the total distance of the whole route and stores it as this
    * candidate solution's fitness
    */
   private void calculateFitness()
   {

      /* initialize total distance */
      double totalDistance = 0;

      /* 
       * For all Cities in the route (except the last one) get the distance between this 
       * City and the next and add it to the totalDistance
       */
      for(int i = 0; i < route.size() - 1; i++)
      {

         City city = route.get(i);
         City nextCity = route.get(i + 1);
         totalDistance += city.calculateDistance(nextCity);
      }

      /* store totalDistance as this candidate solution's fitness */
      this.fitness = totalDistance;

   }

   /**
    * Recombines given CandidateSolution with current CandidateSolution by
    * using the "cross-and-fill" technique.
    * 
    * @param otherParent
    * @return list of new children
    */
   public List<CandidateSolution> recombine(CandidateSolution otherParent)
   {

      /* get routes of both parents */
      List<City> parentRoute1 = getVisitingCities();
      List<City> parentRoute2 = otherParent.getVisitingCities();

      /* initialize the routes for the children */
      List<City> childRoute1 = new ArrayList<City>();
      List<City> childRoute2 = new ArrayList<City>();

      /* randomize cutIndex for "cross-and-fill point" */
      int cutIndex = new Random().nextInt(parentRoute1.size());

      /* get the first part of both parent routes using the cut index */
      List<City> partRoute1 = parentRoute1.subList(0, cutIndex);
      List<City> partRoute2 = parentRoute2.subList(0, cutIndex);

      /* copy the first part of the parents cut into the children */
      childRoute1.addAll(partRoute1);
      childRoute2.addAll(partRoute2);

      /*
       * Now, the "difficult part". Check the rest of the route in the
       * crossing parent and add the cities that are not yet in the child (in
       * the order of the route of the crossing parent)
       */
      crossFill(childRoute1, parentRoute2, cutIndex);
      crossFill(childRoute2, parentRoute1, cutIndex);

      /* create new children using the new children routes */
      CandidateSolution child1 = new CandidateSolution(baseCity, childRoute1);
      CandidateSolution child2 = new CandidateSolution(baseCity, childRoute2);

      /* put the children in a list and return it */
      List<CandidateSolution> children = new ArrayList<CandidateSolution>();
      children.add(child1);
      children.add(child2);
      return children;
   }

   /**
    */
   private List<City> getVisitingCities()
   {
      return visitingCities;
   }

   /**
    * Check the rest of the route in the crossing parent and add the cities
    * that are not yet in the child (in the order of the route of the crossing
    * parent)
    * 
    * @param childRoute
    *            route to be filled with crossing parent
    * @param parentRoute
    *            crossing parent of the child
    * @param cutIndex
    *            index where both parent routes were cut
    */
   private void crossFill(List<City> childRoute, List<City> parentRoute, int cutIndex)
   {

      /*
       * traverse the parent route from the cut index on and add every city
       * not yet in the child to the child
       */
      for(int i = cutIndex; i < parentRoute.size(); i++)
      {
         City nextCityOnRoute = parentRoute.get(i);

         if(!childRoute.contains(nextCityOnRoute))
         {
            childRoute.add(nextCityOnRoute);
         }
      }

      /*
       * traverse the parent route from the start of the route and add every
       * city not yet in the child to the child
       */
      for(int i = 0; i < cutIndex; i++)
      {
         City nextCityOnRoute = parentRoute.get(i);

         if(!childRoute.contains(nextCityOnRoute))
         {
            childRoute.add(nextCityOnRoute);
         }
      }
   }

   /**
    * Mutates the current individual by swapping two random cities in its
    * route.
    */
   public void mutate()
   {
      // System.out.println(toString());

      Random random = new Random();

      /* randomly select two indices in the route */
      int indexFirstCity = random.nextInt(visitingCities.size());
      int indexSecondCity = random.nextInt(visitingCities.size());

      /* Make sure they are different */
      while(indexFirstCity == indexSecondCity)
      {
         indexSecondCity = random.nextInt(visitingCities.size());
      }

      /* retrieve the Cities on the given indices */
      City firstCity = visitingCities.get(indexFirstCity);
      City secondCity = visitingCities.get(indexSecondCity);

      /* Changer! */
      visitingCities.set(indexFirstCity, secondCity);
      visitingCities.set(indexSecondCity, firstCity);

      calculateFitness(); // fitness changes. Since we are doing caching:
      // recalculate!
      // System.out.println(toString());
   }

   @Override
   public String toString()
   {
      StringBuilder builder = new StringBuilder();

      builder.append("CandidateSolution [fitness=");
      builder.append(getFitness());
      builder.append("] [route=");

      for(City city : visitingCities)
      {
         builder.append(city);
         builder.append(",");
      }

      builder.deleteCharAt(builder.length() - 1); // delete trailing comma

      builder.append("]");

      return builder.toString();
   }

   @Override
   public int compareTo(CandidateSolution o)
   {

      double ownFitness = this.getFitness();
      double otherFitness = o.getFitness();

      /* Remember: the lower the fitness the better in our case! */

      if(ownFitness > otherFitness)
      {
         return 1;
      }
      else if(ownFitness < otherFitness)
      {
         return -1;
      }
      else
      {
         return 0; // equal
      }
   }
}
