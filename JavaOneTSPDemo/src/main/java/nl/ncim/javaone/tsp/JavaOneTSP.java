package nl.ncim.javaone.tsp;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import nl.ncim.javaone.tsp.util.TSPUtils;

public final class JavaOneTSP {
	
	private static final WorldWindowGLCanvas wwd = new WorldWindowGLCanvas();
	
	private static final JFrame frame = new AppFrame();
	
	private static class AppFrame extends JFrame {
		
		private static final long serialVersionUID = 6954901339876266277L;
		private RenderableLayer layer;

		public AppFrame() {
			initializeWWD();
			initializeMenu();
		}
		
		private void initializeWWD() {
			wwd.setPreferredSize(new Dimension(1200, 960));
			this.getContentPane().add(wwd, BorderLayout.CENTER);
			this.pack();
			wwd.setModel(new BasicModel());
			wwd.getView().setEyePosition(Position.fromDegrees(52.18958, 5.29524, 6e6));
			layer = TSPUtils.createCitiesLayer("Cities");
			wwd.getModel().getLayers().add(layer);
		}
		
		private void initializeMenu() {
			JMenuBar menuBar = new JMenuBar();
			JMenu menu = new JMenu("Actions");
			menuBar.add(menu);

			JMenuItem item = new JMenuItem("Randomize Cities");
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					TSPUtils.buildCitiesLayer(layer, TSPUtils.getRandomizedCities());
				}
			});
			menu.add(item);
			
			this.setJMenuBar(menuBar);
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.setTitle("JavaOne TSP Demo");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}