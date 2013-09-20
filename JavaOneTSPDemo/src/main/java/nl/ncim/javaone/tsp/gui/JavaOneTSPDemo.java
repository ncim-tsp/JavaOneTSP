/*
 * Copyright 2013 (C) NCIM Groep
 * 
 * Created on : 
 * Author     : Niels van Eijck
 * 
 * This class is used for the JavaOne Demo on 09/24/2013
 * for the following session: 
 * 
 * Evolutionary Algorithms: The Key to Solving Complex Java Puzzles [BOF2913]
 *
 */

package nl.ncim.javaone.tsp.gui;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import nl.ncim.javaone.tsp.domain.City;
import nl.ncim.javaone.tsp.ea.CandidateSolution;
import nl.ncim.javaone.tsp.util.TSPUtils;

public final class JavaOneTSPDemo
{

   private static final WorldWindowGLCanvas wwd = new WorldWindowGLCanvas();

   private static final AppFrame frame = new AppFrame();

   private AlgorithmUI algorithmUI;

   private double currentBestCandidateSolutionFound;
   private int currentGeneration;

   public JavaOneTSPDemo()
   {
      algorithmUI = new AlgorithmUI(this);
      startView(algorithmUI);
   }

   private static class AppFrame extends JFrame
   {

      private static final long serialVersionUID = 6954901339876266277L;
      private RenderableLayer layer;

      public AppFrame()
      {
         initializeWWD();
         initializeMenu();
      }

      private void initializeWWD()
      {
         wwd.setPreferredSize(new Dimension(900, 960));
         wwd.setModel(new BasicModel());
         wwd.getView().setEyePosition(Position.fromDegrees(52.18958, 5.29524, 6e6));
         layer = TSPUtils.createCitiesLayer("Cities");
         wwd.getModel().getLayers().add(layer);
      }

      private void initializeMenu()
      {
         JMenuBar menuBar = new JMenuBar();
         JMenu menu = new JMenu(JavaOneTSPDemoText.MENU_APPLICATION);
         menuBar.add(menu);

         JMenuItem quitMenuItem = new JMenuItem(JavaOneTSPDemoText.MENU_ITEM_QUIT);
         quitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae)
            {
               System.exit(0);
            }
         });
         menu.add(quitMenuItem);

         this.setJMenuBar(menuBar);
      }

      public void showRoute(List<City> route, double fitness)
      {

         wwd.getModel().getLayers().remove(layer);
         layer = TSPUtils.createCitiesLayer("Cities");
         wwd.getModel().getLayers().add(layer);
         TSPUtils.buildCitiesLayer(layer, route);
         frame.setTitle("JavaOne TSP Demo. Fitness: " + fitness);
      }

      public void reset()
      {
         wwd.getModel().getLayers().remove(layer);
         layer = TSPUtils.createCitiesLayer("Cities");
         wwd.getModel().getLayers().add(layer);

         frame.setTitle("JavaOne TSP Demo.");
      }
   }

   public void startView(final AlgorithmUI algorithmUI)
   {
      try
      {
         EventQueue.invokeAndWait(new Runnable() {
            public void run()
            {
               frame.setTitle("JavaOne TSP Demo");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);

               JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
               splitPane.add(algorithmUI, JSplitPane.LEFT);
               splitPane.add(wwd, JSplitPane.RIGHT);
               frame.add(splitPane, BorderLayout.CENTER);
               frame.pack();
            }
         });
      }
      catch(InterruptedException ex)
      {
         // ignore
      }
      catch(InvocationTargetException ex)
      {
         // ignore
      }
   }

   public void showLastGeneration(final CandidateSolution candidateSolution, final int generation)
   {
      EventQueue.invokeLater(new Runnable() {
         public void run()
         {
            frame.showRoute(candidateSolution.getRoute(), candidateSolution.getFitness());
            algorithmUI.showAlgorithmInfo(candidateSolution.getFitness(), generation);

         }
      });

      this.currentBestCandidateSolutionFound = candidateSolution.getFitness();
      this.currentGeneration = generation;
   }

   public static void main(String[] args)
   {
      new JavaOneTSPDemo();
   }

   public void enableStartButton(boolean b)
   {
      algorithmUI.enableStartButton(b);
   }

   public void enableStopButton(boolean b)
   {
      algorithmUI.enableStopButton(b);
   }

   public void reset()
   {
      enableStartButton(true);
      enableStopButton(false);

      EventQueue.invokeLater(new Runnable() {
         public void run()
         {
            frame.reset();
            algorithmUI.showAlgorithmInfo(Double.NaN, 0);
         }
      });
   }

   public void done()
   {
      enableStartButton(true);
      enableStopButton(false);

      JOptionPane.showMessageDialog(frame, "The Evolutionary Algorithm has finished.\n\n" +
         "Best Candidate Solution Found: " + currentBestCandidateSolutionFound + ".\n" + "In number of generations: " +
         currentGeneration + "\n\n", "Done!", JOptionPane.PLAIN_MESSAGE);

   }
}