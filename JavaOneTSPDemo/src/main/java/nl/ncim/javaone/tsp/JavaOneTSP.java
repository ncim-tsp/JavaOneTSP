package nl.ncim.javaone.tsp;

import gov.nasa.worldwind.View;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwindx.examples.ApplicationTemplate;
import nl.ncim.javaone.tsp.util.TSPUtils;

public class JavaOneTSP extends ApplicationTemplate {

	public static class AppFrame extends ApplicationTemplate.AppFrame {
		private static final long serialVersionUID = 5875574025750544247L;

		public AppFrame() {
			super(true, true, false);

			try {
				RenderableLayer layer = TSPUtils.createCitiesLayer("Cities", TSPUtils.getCities());
				System.out.println("Total distance: " + TSPUtils.calculateTotalDistance(TSPUtils.getCities()));
				
				insertBeforeCompass(this.getWwd(), layer);

				this.getLayerPanel().update(this.getWwd());

				// Move the view to the line locations.
				View view = getWwd().getView();
				// Put The Netherlands into view but with enough altitude to display Europe :)
				view.setEyePosition(Position.fromDegrees(52.18958, 5.29524, 6e6));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		ApplicationTemplate.start("JavaOne TSP Demo", AppFrame.class);
	}
}