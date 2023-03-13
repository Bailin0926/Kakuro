package prod;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import bus.EnumGameDifficulty;
import bus.Game;
import data.ConnectionFile;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class WindowsNew extends JDialog {

	private static final long serialVersionUID = 6591184288335055113L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textHeight;
	private JTextField textWidth;

	private JComboBox comboDifficulty;
	private int width = 8;
	private int height = 8;


	public WindowsNew(WindowsGame windowsGame, boolean isStart) {
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (isStart) {
                	System.exit(0);
                }
            }
        });
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setTitle("Kakuro - New Game");
		setIconImage(ConnectionFile.loadIcon().getImage());
		setSize( 200, 170);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		contentPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		JLabel lblHeight = new JLabel("Height: ");
		if (windowsGame.getGame() != null) {
			width = windowsGame.getGame().getColumnSize();
			height = windowsGame.getGame().getRowSize();
		}

		textHeight = new JTextField();
		textHeight.setColumns(5);
		textHeight.setText(String.valueOf(height));
		textHeight.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String num = textHeight.getText();
				if (isNumeric(num)) {
					height = Integer.parseInt(num);
				} else {
					if (num.length() == 0) {
						height = 0;
					}
				}

				textHeight.setText(String.valueOf(height));
			}
		});
		lblHeight.setLabelFor(textHeight);

		JLabel lblWidth = new JLabel("Width: ");

		textWidth = new JTextField();
		textWidth.setColumns(5);
		textWidth.setText(String.valueOf(width));
		textWidth.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String num = textWidth.getText();
				if (isNumeric(num)) {
					width = Integer.parseInt(num);
				} else {
					if (num.length() == 0) {
						width = 0;
					}
				}
				textWidth.setText(String.valueOf(width));
			}
		});
		lblWidth.setLabelFor(textWidth);

		JLabel lblDifficulty = new JLabel("Difficulty: ");

		comboDifficulty = new JComboBox(EnumGameDifficulty.values());
		int i = 0;
		if (windowsGame.getGame() != null) {
			for (i = 0; i < EnumGameDifficulty.values().length; i++) {
				if (comboDifficulty.getItemAt(i).toString() == windowsGame.getGame().getDifficulty().toString()) {
					break;
				}
			}
		}

		comboDifficulty.setSelectedIndex(i);

		lblDifficulty.setLabelFor(comboDifficulty);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;

		c.gridx = 0;
		c.gridy = 0;
		contentPanel.add(lblHeight, c);
		c.gridx = 1;
		c.gridy = 0;
		contentPanel.add(textHeight, c);
		c.gridx = 0;
		c.gridy = 1;
		contentPanel.add(lblWidth, c);
		c.gridx = 1;
		c.gridy = 1;
		contentPanel.add(textWidth, c);
		c.gridx = 0;
		c.gridy = 2;
		contentPanel.add(lblDifficulty, c);
		c.gridx = 1;
		c.gridy = 2;
		contentPanel.add(comboDifficulty, c);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (width < 4 || height < 4 || width > 20 || height > 20) {
							JOptionPane.showMessageDialog(okButton,
									"The side length of the game grid should less than 20 and greater than 4.",
									"Invalid Game Grid Size", JOptionPane.ERROR_MESSAGE);
						} else {
							windowsGame.setGame(new Game(height, width,
									EnumGameDifficulty.valueOf(comboDifficulty.getSelectedItem().toString())));
							windowsGame.loadPane();
							dispose();
						}

					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				if (!isStart) {
					JButton cancelButton = new JButton("Cancel");
					cancelButton.setActionCommand("Cancel");
					cancelButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							dispose();
						}
					});
					buttonPane.add(cancelButton);
				}
			}
		}
		setVisible(true);
	}

	private static boolean isNumeric(String str) {
		return str != null && str.matches("[0-9]+");
	}
}
