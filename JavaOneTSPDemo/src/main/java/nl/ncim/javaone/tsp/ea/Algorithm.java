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
import java.util.Collections;
import java.util.List;
import java.util.Random;

import nl.ncim.javaone.tsp.gui.JavaOneTSPDemo;
import nl.ncim.javaone.tsp.util.TSPUtils;

public class Algorithm
{

   /**
    * Our connection to the demo (view)
    */
   private JavaOneTSPDemo view;

   /**
    * Population of the algorithm
    */
   private List<CandidateSolution> population = new ArrayList<CandidateSolution>();

   /**
    * Thread in which the algorithm runs
    */
   private Thread algorithmThread;

   /**
    * Probability with which a new child mutates
    */
   private int mutationProbability;

   /**
    * Size of the population (as given by the GUI)
    */
   private int populationSize;

   /**
    * number of generations to run max.
    */
   private int nrOfGenerations;

   /**
    * When this fitness threshold is reached the algorithm can be stopped
    */
   private int fitnessThreshold;

   /**
    * Number of parents to be selected for reproduction (each generation)
    */
   private int parentSelectionSize;

   /**
    * Pool of CandidateSolution from which a number of parents will be selected for reproduction (each generation)
    */
   private int parentPoolSize;

   /**
    * Constructor for the Evolutionary Algorithm
    * 
    * @param view
    * @param mutationProbability
    * @param populationSize
    * @param nrOfGenerations
    * @param fitnessThreshold
    * @param parentSelectionSize
    * @param parentPoolSize 
    */
   public Algorithm(JavaOneTSPDemo view, int mutationProbability, int populationSize, int nrOfGenerations,
         int fitnessThreshold, int parentSelectionSize, int parentPoolSize)
   {
      this.view = view;
      this.mutationProbability = mutationProbability;
      this.populationSize = populationSize;
      this.nrOfGenerations = nrOfGenerations;
      this.fitnessThreshold = fitnessThreshold;
      this.parentSelectionSize = parentSelectionSize;
      this.parentPoolSize = parentPoolSize;
   }

   /**
    * Starts the algorithm using the settings given through the
    * constructor.
    */
   public void startAlgorithm()
   {

      /* let the algorithm run in a Thread */
      algorithmThread = new Thread(new Runnable() {
         public void run()
         {
            population = initialisation();

            /* 
             * implemented a Comparable for CandidateSolution
             * using its fitness. So best fitness (lowest)
             * first.
             */
            Collections.sort(population);
            CandidateSolution bestCandidateSolution = population.get(0);

            int generations = 0;

            /* show the current best candidate solution on the demo screen */
            view.showLastGeneration(bestCandidateSolution, generations);

            /* start the iterative part of the algorithm */
            while(generations != nrOfGenerations && population.get(0).getFitness() > fitnessThreshold)
            {

               /* Select the parents for reproduction */
               List<CandidateSolution> parents = parentSelection();

               /* Let the selected parents reproduce (recombine) */
               for(int i = 0; i < parents.size(); i += 2)
               {
                  CandidateSolution parent1 = parents.get(i);
                  CandidateSolution parent2 = parents.get(i + 1);

                  List<CandidateSolution> children = parent1.recombine(parent2);

                  /* 
                   * let the children mutate with probability mutationProbability
                   * and add them to the population 
                   */
                  for(CandidateSolution child : children)
                  {

                     /* probability to mutate */
                     if(new Random().nextInt(101) <= mutationProbability)
                     {
                        child.mutate();
                     }

                     population.add(child);
                  }
               }

               /* 
                * Since evaluation of candidate solutions is done within
                * the CandidateSolution itself, there is no need to evaluate
                * seperately here (although that is a part of the Evolutionary
                * Algorithm)
                */

               /* 
                * Survivor selection: which individuals (CandidateSolutions)
                * progress to the next generation
                */
               selectSurvivors();

               /* Sort the population so that the best candidates are up front */
               Collections.sort(population);

               generations++;
               view.showLastGeneration(population.get(0), generations);

               /* 
                * Sleep, so the Thread can be interrupted if needed
                * and to make the progression of the algorithm easy
                * on the eyes on the demo screen
                */
               try
               {
                  Thread.sleep(1);
               }
               catch(InterruptedException e)
               {
                  view.reset();
                  return;
               }
            }

            /* we're done here */
            view.done();
         }
      });

      /* start the above defined algorithm */
      algorithmThread.start();
   }

   /**
    * Selects the survivors by removing the worst candidate 
    * solutions from the list, so we have the original 
    * population size again
    */
   private void selectSurvivors()
   {

      /* Sort the population so that the best candidates are up front */
      Collections.sort(population);

      /* 
       * cut back the population to the original size by dropping the worst
       * candidates
       */
      population = new ArrayList<CandidateSolution>(population.subList(0, populationSize));

   }

   /**
    * Select the x best candidate solutions from the population
    * 
    * @param nrParentsToBeSelected
    *            the number of parents to be selected
    * @return parents a list of the chosen parents
    */
   private List<CandidateSolution> parentSelection()
   {

      List<CandidateSolution> randomCandidates = new ArrayList<CandidateSolution>();

      Random random = new Random();
      for(int i = 0; i <= parentPoolSize; i++)
      {
         int randomlySelectedIndex = random.nextInt(population.size());
         CandidateSolution randomSelection = population.get(randomlySelectedIndex);
         randomCandidates.add(randomSelection);
      }

      /* Sort the population so that the best candidates are up front */
      Collections.sort(randomCandidates);

      /* 
       * return a list with size parentSelectionSize with the best
       * CandidateSolutions
       */
      return randomCandidates.subList(0, parentSelectionSize);

   }

   private List<CandidateSolution> initialisation()
   {

      /* initialize population list of CandidateSolutions */
      List<CandidateSolution> populationTemp = new ArrayList<CandidateSolution>();

      /* create a populationSize amount of random CandidateSolutions (routes) */
      for(int i = 0; i < populationSize; i++)
      {
         CandidateSolution candidateSolution = new CandidateSolution(TSPUtils.getBaseCity(),
               TSPUtils.getRandomizedCities());
         populationTemp.add(candidateSolution);
      }

      return populationTemp;
   }

   private void printPopulation()
   {

      /*
       * for (CandidateSolution candidateSolution : population) {
       * 
       * System.out.println("CandidateSolution added to population: " +
       * candidateSolution.toString()); }
       * 
       * System.out.println("Population size: " + population.size());
       */
   }

   /**
    * Stops the algorithm
    */
   public void stopAlgorithm()
   {
      algorithmThread.interrupt();
   }

}
