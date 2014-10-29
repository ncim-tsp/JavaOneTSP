/*
 * NCIM Groep
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

package nl.ncim.bknopper.tspeademo.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.ncim.bknopper.tspeademo.ea.Algorithm;

public class AlgorithmUI extends JPanel implements ActionListener {

	/**
	 * Generated
	 */
	private static final long serialVersionUID = -8341220151024243512L;
	private static final int DEFAULT_LAUNCHER_WIDTH = 200;
	private static final int DEFAULT_LAUNCHER_HEIGHT = 960;

	/**
	 * the configuration panel of the algorithm UI. This panel consists of
	 * several configuration controls
	 */
	protected JPanel configPanel;

	/**
	 * the button panel holds several buttons for actions like starting/stopping
	 * the application
	 */
	protected JPanel buttonPanel;

	/**
	 * label reflecting the status of the algorithm
	 */
	protected JLabel status;

	/**
	 * start button which starts the algorithm
	 */
	protected JButton startButton;

	/**
	 * stop button which stops the algorithm
	 */
	protected JButton stopButton;

	private TSPEADemo demo;

	private Algorithm algorithm;

	private JLabel mutationProbLabel;
	private JSlider mutationProbability;

	private JLabel emptyLabel = new JLabel("");

	private JSlider populationSizeSlide;
	private JLabel populationSize;

	private JSlider nrOfGenerationsSlide;
	private JLabel nrOfGenerations;

	private JLabel fitnessThreshold;
	private JSlider fitnessThresholdsSlide;

	private JLabel parentSelectionSize;
	private JSlider parentSelectionSizeSlide;
	private JPanel algorithmInfoPanel;
	private JLabel currentGenerationLabel;
	private JLabel currentFitnessLabel;
	private JComboBox predefinedSetsComboBox;
	private JLabel parentPoolSize;
	private JSlider parentPoolSizeSlide;

	private enum PredefinedSet {
		NONE, LOW, MEDIUM, HIGH;
	}

	public AlgorithmUI(TSPEADemo demo) {
		this.demo = demo;
		buildUI();
	}

	private void buildUI() {
		Dimension dimension = new Dimension(DEFAULT_LAUNCHER_WIDTH,
				DEFAULT_LAUNCHER_HEIGHT);
		this.setSize(dimension);
		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());

		// Add the main panel
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

		JTabbedPane tabbedPane = new JTabbedPane();

		// Add the config panel to tabbedPane
		addConfigToMainPanel(tabbedPane);

		JPanel runPanelWrapper = new JPanel(new BorderLayout(10, 10));

		JPanel runPanel = new JPanel(new GridLayout(2, 1));
		runPanelWrapper.add(runPanel, BorderLayout.NORTH);
		runPanelWrapper.add(new JLabel(), BorderLayout.CENTER);

		// Add the algorithm info panel
		addAlgorithmInfoPanelToMainPanel(runPanel);

		// Add the buttons
		addButtonPanelToMainPanel(runPanel);

		tabbedPane.add("Run EA", runPanelWrapper);

		mainPanel.add(tabbedPane);
		this.add(mainPanel, BorderLayout.CENTER);

		this.setVisible(true);
	}

	private void addAlgorithmInfoPanelToMainPanel(JPanel runPanel) {

		JPanel algorithmPaddingPanel = new JPanel();
		algorithmPaddingPanel.setBorder(new EmptyBorder(10, 10, 50, 10));
		algorithmInfoPanel = new JPanel();
		algorithmInfoPanel.setPreferredSize(new Dimension(290, 100));

		algorithmInfoPanel.setBorder(BorderFactory
				.createTitledBorder("Evolutionary Algorithm Runtime Info"));

		algorithmInfoPanel.setLayout(new GridLayout(2, 1));

		currentGenerationLabel = new JLabel(
				TSPEADemoText.LABEL_CURRENT_GENERATION + ": " + 0);
		Font biggerFont = currentGenerationLabel.getFont().deriveFont(13f);
		currentGenerationLabel.setFont(biggerFont);
		algorithmInfoPanel.add(currentGenerationLabel);

		currentFitnessLabel = new JLabel(TSPEADemoText.LABEL_CURRENT_FITNESS
				+ ": ");
		currentFitnessLabel.setFont(biggerFont);
		algorithmInfoPanel.add(currentFitnessLabel);

		algorithmPaddingPanel.add(algorithmInfoPanel);
		runPanel.add(algorithmPaddingPanel);

	}

	private void addButtonPanelToMainPanel(JPanel runPanel) {
		buttonPanel = new JPanel();
		buttonPanel.setBorder(new EmptyBorder(10, 10, 50, 10));
		startButton = new JButton(TSPEADemoText.BUTTON_START_ALGORITHM);
		startButton.addActionListener(this);
		stopButton = new JButton(TSPEADemoText.BUTTON_STOP_ALGORITHM);
		stopButton.setEnabled(false);
		stopButton.addActionListener(this);

		buttonPanel.add(startButton);
		buttonPanel.add(stopButton);

		runPanel.add(buttonPanel);
	}

	private void addConfigToMainPanel(JTabbedPane tabbedPane) {
		JPanel configPaddingPanel = new JPanel();
		configPaddingPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		configPanel = new JPanel();
		configPanel.setBorder(BorderFactory
				.createTitledBorder("Evolutionary Algorithm Settings"));

		configPaddingPanel.add(configPanel);

		configPanel.setPreferredSize(new Dimension(300, 700));
		configPanel.setLayout(new GridLayout(16, 2));

		addMutationProbabilityToConfig();
		addPopulationSizeToConfig();
		addParentPoolSizeToConfig();
		addParentSelectionSizeToConfig();
		addNrOfGenerationsToConfig();
		addFitnessThresholdToConfig();

		addPredefinedSetsToConfig();

		tabbedPane.addTab("Settings", configPaddingPanel);
	}

	private void addMutationProbabilityToConfig() {
		mutationProbLabel = new JLabel(TSPEADemoText.LABEL_MUTATION_PROB + ": "
				+ 10);
		configPanel.add(mutationProbLabel);
		configPanel.add(emptyLabel);

		mutationProbability = new JSlider(JSlider.HORIZONTAL, 0, 100, 10);
		mutationProbability.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				JSlider slider = (JSlider) ce.getSource();
				if (slider.getValueIsAdjusting()) {
					predefinedSetsComboBox.setSelectedItem(PredefinedSet.NONE);
					String sliderValue = String.valueOf(slider.getValue());
					mutationProbLabel.setText(TSPEADemoText.LABEL_MUTATION_PROB
							+ ": " + sliderValue);
				}
			}
		});

		// Turn on labels at major tick marks.
		mutationProbability.setMajorTickSpacing(20);
		mutationProbability.setMinorTickSpacing(5);
		mutationProbability.setPaintTicks(true);
		mutationProbability.setPaintLabels(true);

		configPanel.add(mutationProbability);
	}

	private void addPopulationSizeToConfig() {
		populationSize = new JLabel(TSPEADemoText.LABEL_POPULATION_SIZE + ": "
				+ 100);
		configPanel.add(populationSize);
		configPanel.add(emptyLabel);

		populationSizeSlide = new JSlider(JSlider.HORIZONTAL, 0, 1000, 100);
		populationSizeSlide.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				JSlider slider = (JSlider) ce.getSource();
				if (slider.getValueIsAdjusting()) {
					predefinedSetsComboBox.setSelectedItem(PredefinedSet.NONE);
					String sliderValue = String.valueOf(slider.getValue());
					populationSize.setText(TSPEADemoText.LABEL_POPULATION_SIZE
							+ ": " + sliderValue);
				}
			}
		});

		// Turn on labels at major tick marks.
		populationSizeSlide.setMajorTickSpacing(250);
		populationSizeSlide.setMinorTickSpacing(50);
		populationSizeSlide.setPaintTicks(true);
		populationSizeSlide.setPaintLabels(true);

		configPanel.add(populationSizeSlide);
	}

	private void addParentSelectionSizeToConfig() {
		parentSelectionSize = new JLabel(
				TSPEADemoText.LABEL_PARENT_SELECTION_SIZE + ": " + 6);
		configPanel.add(parentSelectionSize);
		configPanel.add(emptyLabel);

		parentSelectionSizeSlide = new JSlider(JSlider.HORIZONTAL, 0, 100, 6);
		parentSelectionSizeSlide.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				JSlider slider = (JSlider) ce.getSource();
				final int selectedParentSelectionSize = slider.getValue();

				if (selectedParentSelectionSize % 2 != 0) {
					slider.setValue(selectedParentSelectionSize + 1); // up one
					// so
					// we're
					// always
					// even.
					return;
				}

				final int selectedParentPoolSize = parentPoolSizeSlide
						.getValue();
				if (selectedParentSelectionSize > selectedParentPoolSize) {

					/*
					 * the parentPoolSizeSlide value should not be smaller so
					 * set the parentPoolSizeSlide to this value
					 */
					parentPoolSizeSlide.setValue(selectedParentSelectionSize);
				}

				predefinedSetsComboBox.setSelectedItem(PredefinedSet.NONE);
				String sliderValue = String
						.valueOf(selectedParentSelectionSize);
				parentSelectionSize
						.setText(TSPEADemoText.LABEL_PARENT_SELECTION_SIZE
								+ ": " + sliderValue);

			}
		});

		// Turn on labels at major tick marks.
		parentSelectionSizeSlide.setMajorTickSpacing(20);
		parentSelectionSizeSlide.setMinorTickSpacing(2);
		parentSelectionSizeSlide.setPaintTicks(true);
		parentSelectionSizeSlide.setPaintLabels(true);
		parentSelectionSizeSlide.setSnapToTicks(true);

		configPanel.add(parentSelectionSizeSlide);
	}

	private void addParentPoolSizeToConfig() {
		parentPoolSize = new JLabel(TSPEADemoText.LABEL_PARENT_POOL_SIZE + ": "
				+ 10);
		configPanel.add(parentPoolSize);
		configPanel.add(emptyLabel);

		parentPoolSizeSlide = new JSlider(JSlider.HORIZONTAL, 0, 100, 10);
		parentPoolSizeSlide.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				JSlider slider = (JSlider) ce.getSource();

				final int selectedParentSize = parentSelectionSizeSlide
						.getValue();
				final int selectedParentPoolSize = slider.getValue();
				if (selectedParentSize > selectedParentPoolSize) {

					/*
					 * the parentPoolSizeSlide value should not be smaller so
					 * set the parentSelectionSizeSlide to this value
					 */
					parentSelectionSizeSlide.setValue(selectedParentPoolSize);
				}

				predefinedSetsComboBox.setSelectedItem(PredefinedSet.NONE);

				String sliderValue = String.valueOf(slider.getValue());
				parentPoolSize.setText(TSPEADemoText.LABEL_PARENT_POOL_SIZE
						+ ": " + sliderValue);
			}
		});

		// Turn on labels at major tick marks.
		parentPoolSizeSlide.setMajorTickSpacing(20);
		parentPoolSizeSlide.setMinorTickSpacing(6);
		parentPoolSizeSlide.setPaintTicks(true);
		parentPoolSizeSlide.setPaintLabels(true);

		configPanel.add(parentPoolSizeSlide);
	}

	private void addNrOfGenerationsToConfig() {
		nrOfGenerations = new JLabel(TSPEADemoText.LABEL_NR_OF_GENERATIONS
				+ ": " + 5000);
		configPanel.add(nrOfGenerations);
		configPanel.add(emptyLabel);

		nrOfGenerationsSlide = new JSlider(JSlider.HORIZONTAL, 0, 20000, 5000);
		nrOfGenerationsSlide.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				JSlider slider = (JSlider) ce.getSource();
				if (slider.getValueIsAdjusting()) {
					predefinedSetsComboBox.setSelectedItem(PredefinedSet.NONE);
					String sliderValue = String.valueOf(slider.getValue());
					nrOfGenerations
							.setText(TSPEADemoText.LABEL_NR_OF_GENERATIONS
									+ ": " + sliderValue);
				}
			}
		});

		// Turn on labels at major tick marks.
		nrOfGenerationsSlide.setMajorTickSpacing(5000);
		nrOfGenerationsSlide.setMinorTickSpacing(1000);
		nrOfGenerationsSlide.setPaintTicks(true);
		nrOfGenerationsSlide.setPaintLabels(true);

		configPanel.add(nrOfGenerationsSlide);
	}

	private void addFitnessThresholdToConfig() {
		fitnessThreshold = new JLabel(TSPEADemoText.LABEL_FITNESS_THRESHOLD
				+ ": " + 11000);
		configPanel.add(fitnessThreshold);
		configPanel.add(emptyLabel);

		fitnessThresholdsSlide = new JSlider(JSlider.HORIZONTAL, 0, 20000,
				11000);
		fitnessThresholdsSlide.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				JSlider slider = (JSlider) ce.getSource();
				if (slider.getValueIsAdjusting()) {
					predefinedSetsComboBox.setSelectedItem(PredefinedSet.NONE);
					String sliderValue = String.valueOf(slider.getValue());
					fitnessThreshold
							.setText(TSPEADemoText.LABEL_FITNESS_THRESHOLD
									+ ": " + sliderValue);
				}
			}
		});

		// Turn on labels at major tick marks.
		fitnessThresholdsSlide.setMajorTickSpacing(5000);
		fitnessThresholdsSlide.setMinorTickSpacing(1000);
		fitnessThresholdsSlide.setPaintTicks(true);
		fitnessThresholdsSlide.setPaintLabels(true);
		configPanel.add(fitnessThresholdsSlide);
		configPanel.add(emptyLabel);
	}

	private void addPredefinedSetsToConfig() {
		JLabel predefinedSet = new JLabel(TSPEADemoText.LABEL_PREDEFINED_SET);
		configPanel.add(predefinedSet);
		configPanel.add(emptyLabel);

		predefinedSetsComboBox = new JComboBox(PredefinedSet.values());
		predefinedSetsComboBox.addActionListener(this);

		configPanel.add(predefinedSetsComboBox);
		configPanel.add(emptyLabel);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object eventSource = event.getSource();
		if (eventSource.equals(startButton)) {

			startButton.setEnabled(false);
			stopButton.setEnabled(true);

			algorithm = new Algorithm(demo, mutationProbability.getValue(),
					populationSizeSlide.getValue(),
					nrOfGenerationsSlide.getValue(),
					fitnessThresholdsSlide.getValue(),
					parentSelectionSizeSlide.getValue(),
					parentPoolSizeSlide.getValue());
			algorithm.startAlgorithm();
		} else if (eventSource.equals(stopButton)) {
			algorithm.stopAlgorithm();
			demo.reset();
		} else if (eventSource.equals(predefinedSetsComboBox)) {
			setPredefinedSet((PredefinedSet) predefinedSetsComboBox
					.getSelectedItem());
		}
	}

	private void setPredefinedSet(PredefinedSet selectedItem) {
		switch (selectedItem) {
		case LOW:
			System.out.println("Selected LOW predefined set");
			setSettings(25, 500, 5000, 11000, 100, 60);
			break;

		case MEDIUM:
			System.out.println("Selected MEDIUM predefined set");
			setSettings(50, 750, 10000, 11000, 50, 30);
			break;

		case HIGH:
			System.out.println("Selected HIGH predefined set");
			setSettings(90, 1000, 20000, 11000, 10, 6);
			break;
		case NONE:
			// do nothing
		default:
		}
	}

	private void setSettings(int mutationProbabilityValue,
			int populationSizeValue, int nrOfGenerationsValue,
			int fitnessThresholdValue, int parentPoolSizeValue,
			int parentSelectionSizeValue) {

		mutationProbability.setValue(mutationProbabilityValue);
		mutationProbLabel.setText(TSPEADemoText.LABEL_MUTATION_PROB + ": "
				+ mutationProbabilityValue);

		populationSizeSlide.setValue(populationSizeValue);
		populationSize.setText(TSPEADemoText.LABEL_POPULATION_SIZE + ": "
				+ populationSizeValue);

		nrOfGenerationsSlide.setValue(nrOfGenerationsValue);
		nrOfGenerations.setText(TSPEADemoText.LABEL_NR_OF_GENERATIONS + ": "
				+ nrOfGenerationsValue);

		fitnessThresholdsSlide.setValue(fitnessThresholdValue);
		fitnessThreshold.setText(TSPEADemoText.LABEL_FITNESS_THRESHOLD + ": "
				+ fitnessThresholdValue);

		parentSelectionSizeSlide.setValue(parentSelectionSizeValue);
		parentSelectionSize.setText(TSPEADemoText.LABEL_PARENT_SELECTION_SIZE
				+ ": " + parentSelectionSizeValue);

		parentPoolSizeSlide.setValue(parentPoolSizeValue);
		parentPoolSize.setText(TSPEADemoText.LABEL_PARENT_POOL_SIZE + ": "
				+ parentPoolSizeValue);
	}

	public void enableStartButton(boolean enable) {
		startButton.setEnabled(enable);
	}

	public void enableStopButton(boolean enable) {
		stopButton.setEnabled(enable);
	}

	public void showAlgorithmInfo(double fitness, int generation) {
		currentGenerationLabel.setText(TSPEADemoText.LABEL_CURRENT_GENERATION
				+ ": " + generation);

		String fitnessString = (Double.isNaN(fitness)) ? "" : String
				.valueOf(fitness);
		currentFitnessLabel.setText(TSPEADemoText.LABEL_CURRENT_FITNESS + ": "
				+ fitnessString);
	}
}
