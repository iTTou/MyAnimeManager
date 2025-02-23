package util.window;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import main.AnimeIndex;
import net.miginfocom.swing.MigLayout;
import util.task.DownloadUpdateTask;

public class DownloadingDialog extends JDialog
{

	DownloadUpdateTask task = new DownloadUpdateTask();
	private JProgressBar progressBar;
	private JLabel lblDownloadInCorso;
	/**
	 * Create the dialog.
	 */
	public DownloadingDialog()
	{
		super(AnimeIndex.frame,true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(DownloadingDialog.class.getResource("/image/Update.png")));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
			        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			        task.execute();
			}
			});	
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setModal(true);
		setResizable(false);
		setTitle("Download Aggiornamento...");
		setBounds(100, 100, 328, 85);
		
		getContentPane().setLayout(new MigLayout("", "[320.00px]", "[14px][14px]"));
		lblDownloadInCorso = new JLabel("Avvio download in corso...");
		lblDownloadInCorso.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblDownloadInCorso, "cell 0 0,growx,aligny center");
		
		progressBar = new JProgressBar(0,100);
		progressBar.setStringPainted(true);
		getContentPane().add(progressBar, "cell 0 1,growx,aligny center");
		
		task.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("progress"))
				{
				int progress = task.getProgress();
				progressBar.setValue(progress);
				if (task.totalSize != 0)
					lblDownloadInCorso.setText("Scaricati " + ((task.currentSize/1024)) + "/" + ((task.totalSize/1024)) +" Kb");
				}
				else if (evt.getPropertyName().equals("state"))
					{
						if(evt.getNewValue().toString().equalsIgnoreCase("done"))
							DownloadingDialog.this.dispose();
					}
			}
		});

		
		
}	
}