package prod;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;

import bus.Result;
import bus.ResultList;
import data.ConnectionFile;

import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

public class WindowsResult extends JDialog {

	private static final long serialVersionUID = 3240334426158761257L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private ResultList resultList = null;

	public WindowsResult(WindowsGame windowsGame, String input) {
		resultList = ConnectionFile.loadResult();
		if (resultList == null) {
			resultList = new ResultList();
		}
		if (windowsGame != null) {
			Result resul = new Result(windowsGame.getGame().getTimmer().getLastTimer(), input,
					windowsGame.getGame().getScore());
			resultList.add(resul);
			ConnectionFile.saveResult(resultList);
		}
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Kakuro - Leaderboard");
		setIconImage(ConnectionFile.loadIcon().getImage());
		setLocationRelativeTo(null);
		setSize(450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			Vector<String> columnNames = new Vector<String>(Arrays.asList("Fnish Time", "Name", "score"));

			Vector<Vector<String>> vs = new Vector<Vector<String>>();
			for (int i = 0; i < resultList.getResultList().size(); i++) {
				String[] arr = new String[3];
				Date date = new Date(resultList.getResultList().get(i).getFinishTime());
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String strDate = dateFormat.format(date);
				arr[0] = strDate;
				arr[1] = resultList.getResultList().get(i).getPlayer();
				arr[2] = resultList.getResultList().get(i).getScore().toString();
				Vector<String> v = new Vector<String>(Arrays.asList(arr[0], arr[1], arr[2]));
				vs.add(v);
			}

			table = new JTable(vs, columnNames);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

			JTableHeader head = table.getTableHeader();
			head.setPreferredSize(new Dimension(head.getWidth(), 25));
			head.setFont(new Font("", Font.BOLD, 12));
			table.setRowHeight(20);
			table.setEnabled(false);

			JScrollPane scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

			contentPanel.add(scrollPane);

		}
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
						dispose();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
//			{
//				JButton cancelButton = new JButton("Cancel");
//				cancelButton.setActionCommand("Cancel");
//				buttonPane.add(cancelButton);
//			}
		}
		setVisible(true);
	}

}
