import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MondrianGUI extends JFrame {
    private JTextField widthField, heightField;
    private JComboBox<String> modeSelector;
    private JLabel imageLabel;
    private JButton generateButton;

    public MondrianGUI() {
        setTitle("Mondrian Art Generator");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel: Mode Selector and Canvas Size
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        inputPanel.add(new JLabel("Select Mode:"));
        modeSelector = new JComboBox<>(new String[]{"Basic", "Complex"});
        inputPanel.add(modeSelector);

        inputPanel.add(new JLabel("Canvas Width:"));
        widthField = new JTextField("300");
        inputPanel.add(widthField);

        inputPanel.add(new JLabel("Canvas Height:"));
        heightField = new JTextField("300");
        inputPanel.add(heightField);

        add(inputPanel, BorderLayout.NORTH);

        // Center: Image Display Area
        imageLabel = new JLabel("Your artwork will appear here", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(300, 300));
        add(imageLabel, BorderLayout.CENTER);

        // Bottom: Generate Button
        generateButton = new JButton("Generate Art");
        generateButton.addActionListener(new GenerateArtListener());
        add(generateButton, BorderLayout.SOUTH);
    }

    private class GenerateArtListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                String mode = (String) modeSelector.getSelectedItem();

                // Create the canvas as a 2D Color array
                Color[][] pixels = new Color[width][height];
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        pixels[i][j] = Color.WHITE;  // Default color
                    }
                }

                // Create an instance of Mondrian to generate the artwork
                Mondrian mondrian = new Mondrian();
                if ("Basic".equals(mode)) {
                    mondrian.paintBasicMondrian(pixels);
                } else {
                    mondrian.paintComplexMondrian(pixels);
                }

                // Convert the Color[][] array to a BufferedImage
                BufferedImage image = createImageFromPixels(pixels);

                // Display the generated image in the JLabel
                ImageIcon icon = new ImageIcon(image);
                imageLabel.setIcon(icon);
                imageLabel.repaint();  // Ensure the label updates

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(MondrianGUI.this,
                        "Invalid canvas size. Please enter valid integers.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Helper method to convert a Color[][] array to a BufferedImage
        private BufferedImage createImageFromPixels(Color[][] pixels) {
            int width = pixels.length;
            int height = pixels[0].length;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    image.setRGB(i, j, pixels[i][j].getRGB());
                }
            }

            return image;
        }
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MondrianGUI gui = new MondrianGUI();
            gui.setVisible(true);
        });
    }
}
