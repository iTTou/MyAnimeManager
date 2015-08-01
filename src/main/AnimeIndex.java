package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.pushingpixels.substance.api.skin.SubstanceGraphiteGlassLookAndFeel;

import util.AnimeData;
import util.AnimeIndexProperties;
import util.FileManager;
import util.SearchBar;
import util.SortedListModel;
import util.Updater;
import util.window.AddAnimeDialog;
import util.window.AddFansubDialog;
import util.window.AnimeInformation;
import util.window.ExitSaveDialog;
import util.window.PreferenceDialog;
import util.window.SetFilterDialog;
import util.window.UpdateDialog;
//import org.pushingpixels.substance.api.skin.SubstanceGraphiteGlassLookAndFeel;
//kemomimi OP
public class AnimeIndex extends JFrame
{

	public static final String VERSION = "1.0.0";
	public static JPanel mainFrame;
	public static JPanel cardContainer;
	public static AnimeInformation animeInformation;
	
	public static JList completedToSeeList;
	public static JList filmList;
	public static JList airingList;
	public static JList completedList;
	public static JList ovaList;
	private static JList searchList;
	private static JList filterList;
	
	public static SortedListModel completedModel = new SortedListModel();
	public static SortedListModel airingModel = new SortedListModel();
	public static SortedListModel ovaModel = new SortedListModel();
	public static SortedListModel filmModel = new SortedListModel();
	public static SortedListModel completedToSeeModel = new SortedListModel();
	private static SortedListModel searchModel = new SortedListModel();
	private static SortedListModel filterModel = new SortedListModel();
	
	private static String[] fansubList = {};
	public static TreeMap<String,String> fansubMap = new TreeMap<String,String>();
	public static Properties appProp;
	public static TreeMap<String,AnimeData> completedMap = new TreeMap<String,AnimeData>();
	public static TreeMap<String,AnimeData> airingMap = new TreeMap<String,AnimeData>();
	public static TreeMap<String,AnimeData> ovaMap = new TreeMap<String,AnimeData>();
	public static TreeMap<String,AnimeData> filmMap = new TreeMap<String,AnimeData>();
	public static TreeMap<String,AnimeData> completedToSeeMap = new TreeMap<String,AnimeData>();
	
	public static ArrayList<String> completedSessionAnime = new ArrayList();
	public static ArrayList<String> airingSessionAnime = new ArrayList();
	public static ArrayList<String> ovaSessionAnime = new ArrayList();
	public static ArrayList<String> filmSessionAnime = new ArrayList();
	public static ArrayList<String> completedToSeeSessionAnime = new ArrayList();
	
	public static ArrayList<String> completedDeletedAnime = new ArrayList();
	public static ArrayList<String> airingDeletedAnime = new ArrayList();
	public static ArrayList<String> ovaDeletedAnime = new ArrayList();
	public static ArrayList<String> filmDeletedAnime = new ArrayList();
	public static ArrayList<String> completedToSeeDeletedAnime = new ArrayList();
	
	private JButton addButton;
	private JButton deleteButton;
	public static JComboBox animeTypeComboBox;
	public static AddAnimeDialog animeDialog;
	private SearchBar searchBar;
	public static AddFansubDialog fansubDialog;
	public static JButton setFilterButton;
	private String list;
	public static boolean[] filterArray = {false, false, false, false, false, false, false, false, false};
	public static Font segui;
	public static String addToPreviousList;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		appProp = AnimeIndexProperties.createProperties();
		FileManager.loadAnime("completed.txt" , completedModel, completedMap);
		FileManager.loadAnime("airing.txt", airingModel, AnimeIndex.airingMap);
		FileManager.loadAnime("ova.txt", ovaModel, AnimeIndex.ovaMap);
		FileManager.loadAnime("film.txt", filmModel, AnimeIndex.filmMap);
		FileManager.loadAnime("toSee.txt", completedToSeeModel, AnimeIndex.completedToSeeMap);
		
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try {
			         UIManager.setLookAndFeel(new SubstanceGraphiteGlassLookAndFeel());
			        } catch (Exception e) {
			          System.out.println("Substance Graphite failed to initialize");
			        }
				try {
			          UIManager.setLookAndFeel(new SubstanceGraphiteGlassLookAndFeel());
			        } catch (Exception e) {
			          System.out.println("Substance Graphite failed to initialize");
			        }
				try {
					segui = segui();
					AnimeIndex frame = new AnimeIndex();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AnimeIndex()
	{
		setTitle("My Anime Manager");
		setIconImage(Toolkit.getDefaultToolkit().getImage(AnimeIndex.class.getResource("/image/icon.png")));
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {	
				try{
					saveModifiedInformation();
					ExitSaveDialog exitDialog = new ExitSaveDialog();
					exitDialog.setLocationRelativeTo(mainFrame);
					exitDialog.setVisible(true);
				}
				catch (Exception e)
				{
					ExitSaveDialog exitDialog = new ExitSaveDialog();
					exitDialog.setLocationRelativeTo(mainFrame);
					exitDialog.setVisible(true);
				}
				
			}
			@Override
			public void windowOpened(WindowEvent arg0) {
				File file = new File(FileManager.getAppDataPath() + File.separator + "Update" + File.separator + UpdateDialog.NEW_VERSION);
				if(file.isFile())
					file.delete();
				String updatedVersion = null;
				try {
					updatedVersion = Updater.getLatestVersion();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				if (!updatedVersion.equalsIgnoreCase(VERSION))
				{
				UpdateDialog update = new UpdateDialog(Updater.getWhatsNew());
				update.setLocationRelativeTo(AnimeIndex.mainFrame);
				update.setVisible(true);
				}
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//setBounds(100, 100, 700, 385);
		setBounds((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() /5, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() /7, 800, 520);
		this.setMinimumSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2));
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnMenu = new JMenu("Opzioni");
		menuBar.add(mnMenu);
		
		JMenuItem mntmPreferenze = new JMenuItem("Preferenze");
		mntmPreferenze.setIcon(new ImageIcon(AnimeIndex.class.getResource("/image/System-Preferences-icon.png")));
		mntmPreferenze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PreferenceDialog preference = new PreferenceDialog();
				preference.setLocationRelativeTo(mainFrame);
				preference.setVisible(true);
			}
		});
		mnMenu.add(mntmPreferenze);
		
		JSeparator separator = new JSeparator();
		mnMenu.add(separator);
		
		JMenu mnElimina = new JMenu("Elimina");
		mnElimina.setIcon(new ImageIcon(AnimeIndex.class.getResource("/image/DeleteRed.png")));
		mnMenu.add(mnElimina);
		
		JMenuItem mntmDeleteImage = new JMenuItem("Tutti i Dati");
		mnElimina.add(mntmDeleteImage);
		
		JMenuItem mntmEliminaFansub = new JMenuItem("Tutti i Fansub");
		mntmEliminaFansub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int shouldCancel = JOptionPane.showConfirmDialog(mainFrame, "Vuoi cancellare tutti i Fansub?", "Attenzione!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (shouldCancel == 0)
				{
				try {
					FileManager.deleteData(new File(System.getenv("APPDATA") + File.separator + "MyAnimeManager" + File.separator + "Fansub.txt"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				String[] newFansub = {};
				fansubList = newFansub;
				fansubMap.clear();
				animeInformation.fansubComboBox.removeAllItems();
				addToFansub("?????");
				animeInformation.setFansubComboBox();
				animeInformation.setBlank();
				JOptionPane.showMessageDialog(mainFrame, "Fansub eliminati", "Attenzione", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		mnElimina.add(mntmEliminaFansub);		
		JMenu mnEliminaLista = new JMenu("Tutta la Lista");
		mnElimina.add(mnEliminaLista);
		
		JMenuItem mntmAnimeCompletati = new JMenuItem("Anime Completati");
		mntmAnimeCompletati.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int shouldCancel = JOptionPane.showConfirmDialog(mainFrame, "Vuoi cancellare tutti gli Anime Completati?", "Attenzione!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (shouldCancel == 0)
				{
				try {
					FileManager.deleteData(new File(System.getenv("APPDATA") + File.separator + "MyAnimeManager" + File.separator + "Anime" + File.separator + "completed.txt"));
					FileManager.deleteData(new File(FileManager.getImageFolderPath() + "Completed"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				completedModel.clear();
				
				completedMap.clear();
				
				animeInformation.setBlank();
				JOptionPane.showMessageDialog(mainFrame, "Anime Completati eliminati", "Attenzione", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		mnEliminaLista.add(mntmAnimeCompletati);
		
		JMenuItem mntmAnimeInCorso = new JMenuItem("Anime in Corso");
		mntmAnimeInCorso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int shouldCancel = JOptionPane.showConfirmDialog(mainFrame, "Vuoi cancellare tutti gli Anime in Corso?", "Attenzione!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (shouldCancel == 0)
				{
				try {
					FileManager.deleteData(new File(System.getenv("APPDATA") + File.separator + "MyAnimeManager" + File.separator + "Anime" + File.separator + "airing.txt"));
					FileManager.deleteData(new File(FileManager.getImageFolderPath() + "Airing"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				airingModel.clear();
				
				airingMap.clear();
				
				animeInformation.setBlank();
				JOptionPane.showMessageDialog(mainFrame, "Anime in Corso eliminati", "Attenzione", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		mnEliminaLista.add(mntmAnimeInCorso);
		
		JMenuItem mntmOav = new JMenuItem("OAV");
		mntmOav.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int shouldCancel = JOptionPane.showConfirmDialog(mainFrame, "Vuoi cancellare tutti gli OAV?", "Attenzione!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (shouldCancel == 0)
				{
				try {
					FileManager.deleteData(new File(System.getenv("APPDATA") + File.separator + "MyAnimeManager" + File.separator + "Anime" + File.separator + "ova.txt"));
					FileManager.deleteData(new File(FileManager.getImageFolderPath() + "Ova"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				ovaModel.clear();
				
				ovaMap.clear();
				
				animeInformation.setBlank();
				JOptionPane.showMessageDialog(mainFrame, "OAV eliminati", "Attenzione", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		mnEliminaLista.add(mntmOav);
		
		JMenuItem mntmFilm = new JMenuItem("Film");
		mntmFilm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int shouldCancel = JOptionPane.showConfirmDialog(mainFrame, "Vuoi cancellare tutti i Film", "Attenzione!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (shouldCancel == 0)
				{
				try {
					FileManager.deleteData(new File(System.getenv("APPDATA") + File.separator + "MyAnimeManager" + File.separator + "Anime" + File.separator + "film.txt"));
					FileManager.deleteData(new File(FileManager.getImageFolderPath() + "Film"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				filmModel.clear();
				
				filmMap.clear();
				
				animeInformation.setBlank();
				JOptionPane.showMessageDialog(mainFrame, "Film eliminati", "Attenzione", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		mnEliminaLista.add(mntmFilm);
		
		JMenuItem mntmCompletiDaVedere = new JMenuItem("Completi da Vedere");
		mntmCompletiDaVedere.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int shouldCancel = JOptionPane.showConfirmDialog(mainFrame, "Vuoi cancellare tutti gli Anime Completi da Vedere", "Attenzione!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (shouldCancel == 0)
				{
				try {
					FileManager.deleteData(new File(System.getenv("APPDATA") + File.separator + "MyAnimeManager" + File.separator + "Anime" + File.separator + "toSee.txt"));
					FileManager.deleteData(new File(FileManager.getImageFolderPath() + "Completed to See"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				completedToSeeModel.clear();
				
				completedToSeeMap.clear();
				
				animeInformation.setBlank();
				JOptionPane.showMessageDialog(mainFrame, "Completi da Vedere eliminati", "Attenzione", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		mnEliminaLista.add(mntmCompletiDaVedere);
		mntmDeleteImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int shouldCancel = JOptionPane.showConfirmDialog(mainFrame, "Vuoi cancellare tutti i dati?", "Attenzione!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (shouldCancel == 0)
				{
				try {
					FileManager.deleteData(new File(System.getenv("APPDATA") + File.separator + "MyAnimeManager"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				completedModel.clear();
				airingModel.clear();
				completedToSeeModel.clear();
				filmModel.clear();
				ovaModel.clear();
				
				completedMap.clear();
				airingModel.clear();
				completedToSeeMap.clear();
				filmMap.clear();
				ovaMap.clear();

				String[] newFansub = {};
				fansubList = newFansub;
				
				JList list = getJList();
				list.clearSelection();
				animeInformation.setBlank();
				animeInformation.fansubComboBox.removeAllItems();
				JOptionPane.showMessageDialog(mainFrame, "Dati eliminati", "Attenzione", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		JMenu mnAggiungi = new JMenu("Aggiungi");
		menuBar.add(mnAggiungi);
		
		JMenuItem mntmAggiungiFansub = new JMenuItem("Nuovo Fansub");
		mntmAggiungiFansub.setIcon(new ImageIcon(AnimeIndex.class.getResource("/image/Aegisub.png")));
		mnAggiungi.add(mntmAggiungiFansub);
		mntmAggiungiFansub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fansubDialog = new AddFansubDialog();
				fansubDialog.setLocationRelativeTo(mainFrame);
				fansubDialog.setVisible(true);
			}
		});
		
		JMenu mnAnichart = new JMenu("Apri");
		menuBar.add(mnAnichart);
		
		JMenuItem mntmAnichart = new JMenuItem("AniChart");
		mntmAnichart.setIcon(new ImageIcon(AnimeIndex.class.getResource("/image/AniC.png")));
		mnAnichart.add(mntmAnichart);
		mntmAnichart.setHorizontalAlignment(SwingConstants.LEFT);
		
		JSeparator separator_4 = new JSeparator();
		mnAnichart.add(separator_4);
		
		JMenuItem mntmAnilist = new JMenuItem("AniList");
		mntmAnilist.setIcon(new ImageIcon(AnimeIndex.class.getResource("/image/anilist.png")));
		mntmAnilist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String link = "https://anilist.co/";
				try {
					URI uriLink = new URI(link);
					Desktop.getDesktop().browse(uriLink);
				} catch (URISyntaxException a) {
				} catch (IOException a) {
			}
			}
		});
		mnAnichart.add(mntmAnilist);
		
		JSeparator separator_5 = new JSeparator();
		mnAnichart.add(separator_5);
		
		JMenuItem mntmMyAnimeList = new JMenuItem("MyAnimeList");
		mntmMyAnimeList.setIcon(new ImageIcon(AnimeIndex.class.getResource("/image/MAL.png")));
		mntmMyAnimeList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String link = "http://myanimelist.net/";
				try {
					URI uriLink = new URI(link);
					Desktop.getDesktop().browse(uriLink);
				} catch (URISyntaxException a) {
				} catch (IOException a) {
			}
			}
		});
		mnAnichart.add(mntmMyAnimeList);
		
		JSeparator separator_6 = new JSeparator();
		mnAnichart.add(separator_6);
		
		JMenuItem mntmAnimeclick = new JMenuItem("AnimeClick");
		mntmAnimeclick.setIcon(new ImageIcon(AnimeIndex.class.getResource("/image/AC.png")));
		mntmAnimeclick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String link = "http://www.animeclick.it/";
				try {
					URI uriLink = new URI(link);
					Desktop.getDesktop().browse(uriLink);
				} catch (URISyntaxException a) {
				} catch (IOException a) {
			}
			}
		});
		mnAnichart.add(mntmAnimeclick);
		
		JSeparator separator_8 = new JSeparator();
		mnAnichart.add(separator_8);
		
		JMenuItem mntmHummingbird = new JMenuItem("Hummingbird");
		mntmHummingbird.setIcon(new ImageIcon(AnimeIndex.class.getResource("/image/hummingbird.me.png")));
		mntmHummingbird.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String link = "https://hummingbird.me/";
				try {
					URI uriLink = new URI(link);
					Desktop.getDesktop().browse(uriLink);
				} catch (URISyntaxException a) {
				} catch (IOException a) {
			}
			}
		});
		mnAnichart.add(mntmHummingbird);
		
		JMenuItem mntmAnidb = new JMenuItem("AniDB");
		mntmAnidb.setIcon(new ImageIcon(AnimeIndex.class.getResource("/image/anidb_icon.png")));
		mntmAnidb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String link = "http://anidb.net/";
				try {
					URI uriLink = new URI(link);
					Desktop.getDesktop().browse(uriLink);
				} catch (URISyntaxException a) {
				} catch (IOException a) {
			}
			}
		});
		
		JSeparator separator_3 = new JSeparator();
		mnAnichart.add(separator_3);
		mnAnichart.add(mntmAnidb);
		
		JMenu mnInfo = new JMenu("Info");
		menuBar.add(mnInfo);
		
		JMenuItem mntmControlloAggiornamenti = new JMenuItem("Controllo aggiornamenti");
		mntmControlloAggiornamenti.setIcon(new ImageIcon(AnimeIndex.class.getResource("/image/Update.png")));
		mnInfo.add(mntmControlloAggiornamenti);
		
		JSeparator separator_1 = new JSeparator();
		mnInfo.add(separator_1);
		
		JMenu mnHelp = new JMenu("Aiuto");
		mnHelp.setIcon(new ImageIcon(AnimeIndex.class.getResource("/image/icon2.png")));
		mnInfo.add(mnHelp);
		
		JMenuItem mntmAboutMyAnimeManager = new JMenuItem("Versione Programma");
		mntmAboutMyAnimeManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(mainFrame, "My Anime Manager          "+VERSION, "Versione", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnHelp.add(mntmAboutMyAnimeManager);
		
		JMenuItem mntmCredit = new JMenuItem("Crediti");
		mnHelp.add(mntmCredit);
		mntmCredit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = System.getProperty("user.home");
				if (name.equalsIgnoreCase("C:\\Users\\Denis"))
					JOptionPane.showMessageDialog(mainFrame, "Creato da tu sai chi, merdina che non fa i dizionari", "Crediti", JOptionPane.INFORMATION_MESSAGE);
				else
					JOptionPane.showMessageDialog(mainFrame, "Creato da Yesod30", "Crediti", JOptionPane.INFORMATION_MESSAGE);
				
			}
		});
		mntmControlloAggiornamenti.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					String updatedVersion = null;
					try {
						updatedVersion = Updater.getLatestVersion();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					if (updatedVersion.equalsIgnoreCase(VERSION))
						JOptionPane.showMessageDialog(mainFrame, "Nessun Aggiornamento Trovato", "Aggiornamento", JOptionPane.INFORMATION_MESSAGE);
					else
					{
					UpdateDialog update = new UpdateDialog(Updater.getWhatsNew());
					update.setLocationRelativeTo(AnimeIndex.mainFrame);
					update.setVisible(true);
					}
			}
		});
		mntmAnichart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String link = "http://anichart.net/";
				try {
					URI uriLink = new URI(link);
					Desktop.getDesktop().browse(uriLink);
				} catch (URISyntaxException a) {
				} catch (IOException a) {
			}
		  }
		});
				
		mainFrame = new JPanel();
		mainFrame.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainFrame);
		mainFrame.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setMaximumSize(new Dimension(138, 233));
		mainFrame.add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel animeSelectionPanel = new JPanel();
		animeSelectionPanel.setMaximumSize(new Dimension(138, 233));
		panel.add(animeSelectionPanel, BorderLayout.NORTH);
		animeSelectionPanel.setLayout(new BorderLayout(0, 0));
		
		animeTypeComboBox = new JComboBox();
		animeTypeComboBox.setMaximumSize(new Dimension(138, 20));
		animeTypeComboBox.setMaximumRowCount(2139120439);
		animeTypeComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				CardLayout cl = (CardLayout)(cardContainer.getLayout());
		        cl.show(cardContainer, (String)evt.getItem());
		        saveModifiedInformation(list);
		        if (deleteButton.isEnabled())
		        { 
		        	deleteButton.setEnabled(false);	
		        	ovaList.clearSelection();
		        	filmList.clearSelection();
		        	airingList.clearSelection();
		        	completedList.clearSelection();
		        	completedToSeeList.clearSelection();
		        	searchBar.setText("");
		        	}
		        AnimeIndex.animeInformation.setBlank();
		        String type = getList();
		        list = type;
		        appProp.setProperty("Last_list", type);
		        if(type.equalsIgnoreCase("anime completati"))
		        	{
		        		AnimeIndex.animeInformation.exitDaycomboBox.setSelectedItem("Concluso");
		        		AnimeIndex.animeInformation.exitDaycomboBox.setEnabled(false);
		        	}
		        if(animeInformation.lblAnimeName.getText().equals("Anime"))
		        {
		        	AnimeIndex.animeInformation.addToSeeButton.setEnabled(false);
		        	AnimeIndex.animeInformation.finishedButton.setEnabled(false);
		        }
		        else
		        {
		        if (type.equalsIgnoreCase("anime in corso") || type.equalsIgnoreCase("anime completati") || type.equalsIgnoreCase("oav") || type.equalsIgnoreCase("film"))
		        	AnimeIndex.animeInformation.addToSeeButton.setEnabled(true);
		        else
		        	AnimeIndex.animeInformation.addToSeeButton.setEnabled(false);
		        
		        if (type.equalsIgnoreCase("anime in corso") || type.equalsIgnoreCase("oav") || type.equalsIgnoreCase("film") || type.equalsIgnoreCase("completi da vedere"))
		        	AnimeIndex.animeInformation.finishedButton.setEnabled(true);
		        else
		        	AnimeIndex.animeInformation.finishedButton.setEnabled(false);
		         }
		     }
		});
		animeTypeComboBox.setModel(new DefaultComboBoxModel(new String[] {"Anime Completati", "Anime in Corso", "OAV", "Film", "Completi Da Vedere"}));
		animeSelectionPanel.add(animeTypeComboBox, BorderLayout.NORTH);
		
		searchBar = new SearchBar();
		searchBar.setFont(segui.deriveFont(11f));
		ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(AnimeIndex.class.getResource("/image/search.png")));
        searchBar.setIcon(icon);
		searchBar.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent documentEvent) {
				
			}

			@Override
			public void insertUpdate(DocumentEvent e)
			{
				String search = searchBar.getText();
				String listName = getList();
				SortedListModel model = getModel();
				
				CardLayout cl = (CardLayout)(cardContainer.getLayout());
		        cl.show(cardContainer, "Ricerca");
				SearchInList(search, model);
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				String search = searchBar.getText();
				String listName = getList();
				SortedListModel model = null;
				
				if (listName.equalsIgnoreCase("anime completati"))
				{	
					model = AnimeIndex.completedModel;						
				}				
				else if (listName.equalsIgnoreCase("anime in corso"))
				{
					model = AnimeIndex.airingModel;
				}
				else if (listName.equalsIgnoreCase("oav"))
				{
					model = AnimeIndex.ovaModel;
				}
				else if (listName.equalsIgnoreCase("film"))
				{
					model = AnimeIndex.filmModel;
				}
				else if (listName.equalsIgnoreCase("completi da vedere"))
				{
					model = AnimeIndex.completedToSeeModel;
				}
				if (!search.isEmpty())
				{	
				CardLayout cl = (CardLayout)(cardContainer.getLayout());
		        cl.show(cardContainer, "Ricerca");
				SearchInList(search, model);
				}
				else
				{
					CardLayout cl = (CardLayout)(cardContainer.getLayout());
			        cl.show(cardContainer, listName);
				}
			}
			});
		searchBar.setDisabledTextColor(Color.LIGHT_GRAY);
		searchBar.setBackground(Color.BLACK);
		searchBar.setForeground(Color.LIGHT_GRAY);
		animeSelectionPanel.add(searchBar, BorderLayout.SOUTH);
		
		cardContainer = new JPanel();
		cardContainer.setPreferredSize(new Dimension(159, 235));
		cardContainer.setMaximumSize(new Dimension(159, 135));
		panel.add(cardContainer, BorderLayout.CENTER);
		cardContainer.setLayout(new CardLayout(0, 0));
		
		JPanel completedAnime = new JPanel();
		cardContainer.add(completedAnime, "Anime Completati");
		completedAnime.setLayout(new BorderLayout(0, 0));
		
		JScrollPane completedAnimeScroll = new JScrollPane();
		completedAnimeScroll.setMaximumSize(new Dimension(138, 233));
		completedAnime.add(completedAnimeScroll, BorderLayout.CENTER);

		completedList = new JList(completedModel);
		completedList.setFont(segui.deriveFont(12f));
		completedList.setMaximumSize(new Dimension(157, 233));
		completedList.setMinimumSize(new Dimension(138, 233));
		completedList.setPreferredSize(new Dimension(138, 233));
		completedList.setSize(new Dimension(138, 233));
		completedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		completedList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
			applyListSelectionChange(AnimeIndex.completedList);
			AnimeIndex.animeInformation.minusButton.setEnabled(false);
			AnimeIndex.animeInformation.currentEpisodeField.setEnabled(false);
			AnimeIndex.animeInformation.totalEpisodeText.setEnabled(false);
			AnimeIndex.animeInformation.addToSeeButton.setEnabled(true);
			AnimeIndex.animeInformation.releaseDateField.setEnabled(false);
			AnimeIndex.animeInformation.finishDateField.setEnabled(false);
			AnimeIndex.animeInformation.durationField.setEnabled(false);
			AnimeIndex.animeInformation.noteTextArea.setEnabled(true);
			AnimeIndex.animeInformation.typeComboBox.setEnabled(true);
			AnimeIndex.animeInformation.btnAnilistInfo.setEnabled(true);
			AnimeIndex.animeInformation.setLinkButton.setEnabled(true);
			AnimeIndex.animeInformation.exitDaycomboBox.setEnabled(false);
			AnimeIndex.animeInformation.checkDataButton.setEnabled(false);
			AnimeInformation.fansubComboBox.setEnabled(true);
			String name = (String) completedList.getSelectedValue();
			if(name!=null && !name.equalsIgnoreCase("Anime"))
			{
				if(completedMap.get(name).getLink() !=null && !completedMap.get(name).getLink().isEmpty())
					AnimeIndex.animeInformation.btnOpen.setEnabled(true);
			}
			String link = (String)animeInformation.fansubComboBox.getSelectedItem();
			if(link!=null && !link.isEmpty())
			{
				if(fansubMap.get(link) != null && !fansubMap.get(link).isEmpty())
					AnimeIndex.animeInformation.fansubButton.setEnabled(true);
			}
			
			}
		});

		completedAnimeScroll.setViewportView(completedList);

		
		JPanel airingAnime = new JPanel();
		cardContainer.add(airingAnime, "Anime in Corso");
		airingAnime.setLayout(new BorderLayout(0, 0));
		
		JScrollPane airingAnimeScroll = new JScrollPane();
		airingAnime.add(airingAnimeScroll, BorderLayout.CENTER);
		
		airingList = new JList(airingModel);
		airingList.setFont(segui.deriveFont(12f));
		airingList.setMaximumSize(new Dimension(157, 233));
		airingList.setMinimumSize(new Dimension(138, 233));
		airingList.setPreferredSize(new Dimension(138, 233));
		airingList.setSize(new Dimension(138, 233));
		airingList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				applyListSelectionChange(AnimeIndex.airingList);
				AnimeIndex.animeInformation.minusButton.setEnabled(true);
				AnimeIndex.animeInformation.currentEpisodeField.setEnabled(true);
				AnimeIndex.animeInformation.totalEpisodeText.setEnabled(true);
				AnimeIndex.animeInformation.addToSeeButton.setEnabled(true);
				AnimeIndex.animeInformation.releaseDateField.setEnabled(true);
				AnimeIndex.animeInformation.finishDateField.setEnabled(true);
				AnimeIndex.animeInformation.durationField.setEnabled(true);
				AnimeIndex.animeInformation.noteTextArea.setEnabled(true);
				AnimeIndex.animeInformation.typeComboBox.setEnabled(true);
				AnimeIndex.animeInformation.btnAnilistInfo.setEnabled(true);
				AnimeIndex.animeInformation.setLinkButton.setEnabled(true);
				AnimeIndex.animeInformation.exitDaycomboBox.setEnabled(true);
				AnimeIndex.animeInformation.checkDataButton.setEnabled(true);
				String ep = animeInformation.currentEpisodeField.getText();
				String fep = animeInformation.totalEpisodeText.getText();
				if(ep!=null && fep!=null && !fep.equalsIgnoreCase("??") && ep.equalsIgnoreCase(fep))
					AnimeIndex.animeInformation.finishedButton.setEnabled(true);
				else
					AnimeIndex.animeInformation.finishedButton.setEnabled(false); 
				AnimeInformation.fansubComboBox.setEnabled(true);
				String name = (String) airingList.getSelectedValue();
				if(name!=null && !name.equalsIgnoreCase("Anime"))
				{
					if(airingMap.get(name).getLink() !=null && !airingMap.get(name).getLink().isEmpty())
						AnimeIndex.animeInformation.btnOpen.setEnabled(true);
				}
				String link = (String)animeInformation.fansubComboBox.getSelectedItem();
				if(link!=null && !link.isEmpty())
				{
					if(fansubMap.get(link) != null && !fansubMap.get(link).isEmpty())
						AnimeIndex.animeInformation.fansubButton.setEnabled(true);
				}
			}
		});
		airingAnimeScroll.setViewportView(airingList);
		
		JPanel ova = new JPanel();
		cardContainer.add(ova, "OAV");
		ova.setLayout(new BorderLayout(0, 0));
		
		JScrollPane ovaScroll = new JScrollPane();
		ova.add(ovaScroll, BorderLayout.CENTER);
		
		ovaList = new JList(ovaModel);
		ovaList.setFont(segui.deriveFont(12f));
		ovaList.setMaximumSize(new Dimension(138, 233));
		ovaList.setMinimumSize(new Dimension(138, 233));
		ovaList.setPreferredSize(new Dimension(138, 233));
		ovaList.setSize(new Dimension(138, 233));
		ovaList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				applyListSelectionChange(AnimeIndex.ovaList);
				AnimeIndex.animeInformation.minusButton.setEnabled(true);
				AnimeIndex.animeInformation.currentEpisodeField.setEnabled(true);
				AnimeIndex.animeInformation.totalEpisodeText.setEnabled(true);
				AnimeIndex.animeInformation.addToSeeButton.setEnabled(true);
				AnimeIndex.animeInformation.releaseDateField.setEnabled(true);
				AnimeIndex.animeInformation.finishDateField.setEnabled(true);
				AnimeIndex.animeInformation.durationField.setEnabled(true);
				AnimeIndex.animeInformation.noteTextArea.setEnabled(true);
				AnimeIndex.animeInformation.typeComboBox.setEnabled(true);
				AnimeIndex.animeInformation.btnAnilistInfo.setEnabled(true);
				AnimeIndex.animeInformation.setLinkButton.setEnabled(true);
				AnimeIndex.animeInformation.exitDaycomboBox.setEnabled(true);
				AnimeIndex.animeInformation.checkDataButton.setEnabled(true);
				String ep = animeInformation.currentEpisodeField.getText();
				String fep = animeInformation.totalEpisodeText.getText();
				if(ep!=null && fep!=null && !fep.equalsIgnoreCase("??") && ep.equalsIgnoreCase(fep))
					AnimeIndex.animeInformation.finishedButton.setEnabled(true);
				else
					AnimeIndex.animeInformation.finishedButton.setEnabled(false);
				AnimeInformation.fansubComboBox.setEnabled(true);
				String name = (String) ovaList.getSelectedValue();
				if(name!=null && !name.equalsIgnoreCase("Anime"))
				{
					if(ovaMap.get(name).getLink() !=null && !ovaMap.get(name).getLink().isEmpty())
						AnimeIndex.animeInformation.btnOpen.setEnabled(true);
				}
				String link = (String)animeInformation.fansubComboBox.getSelectedItem();
				if(link!=null && !link.isEmpty())
				{
					if(fansubMap.get(link) != null && !fansubMap.get(link).isEmpty())
						AnimeIndex.animeInformation.fansubButton.setEnabled(true);
				}
			}
		});
		ovaScroll.setViewportView(ovaList);
		
		JPanel film = new JPanel();
		cardContainer.add(film, "Film");
		film.setLayout(new BorderLayout(0, 0));
		
		JScrollPane filmScroll = new JScrollPane();
		film.add(filmScroll, BorderLayout.CENTER);
		
		filmList = new JList(filmModel);
		filmList.setFont(segui.deriveFont(12f));
		filmList.setMaximumSize(new Dimension(138, 233));
		filmList.setMinimumSize(new Dimension(138, 233));
		filmList.setPreferredSize(new Dimension(138, 233));
		filmList.setSize(new Dimension(138, 233));
		filmList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				applyListSelectionChange(AnimeIndex.filmList);
				AnimeIndex.animeInformation.minusButton.setEnabled(true);
				AnimeIndex.animeInformation.currentEpisodeField.setEnabled(true);
				AnimeIndex.animeInformation.totalEpisodeText.setEnabled(true);
				AnimeIndex.animeInformation.addToSeeButton.setEnabled(true);
				AnimeIndex.animeInformation.releaseDateField.setEnabled(true);
				AnimeIndex.animeInformation.finishDateField.setEnabled(true);
				AnimeIndex.animeInformation.durationField.setEnabled(true);
				AnimeIndex.animeInformation.noteTextArea.setEnabled(true);
				AnimeIndex.animeInformation.typeComboBox.setEnabled(true);
				AnimeIndex.animeInformation.btnAnilistInfo.setEnabled(true);
				AnimeIndex.animeInformation.setLinkButton.setEnabled(true);
				AnimeIndex.animeInformation.exitDaycomboBox.setEnabled(true);
				AnimeIndex.animeInformation.checkDataButton.setEnabled(true);
				String ep = animeInformation.currentEpisodeField.getText();
				String fep = animeInformation.totalEpisodeText.getText();
				if(ep!=null && fep!=null && !fep.equalsIgnoreCase("??") && ep.equalsIgnoreCase(fep))
					AnimeIndex.animeInformation.finishedButton.setEnabled(true);
				else
					AnimeIndex.animeInformation.finishedButton.setEnabled(false); 
				AnimeInformation.fansubComboBox.setEnabled(true);
				String name = (String) filmList.getSelectedValue();
				if(name!=null && !name.equalsIgnoreCase("Anime"))
				{
					if(filmMap.get(name).getLink() !=null && !filmMap.get(name).getLink().isEmpty())
						AnimeIndex.animeInformation.btnOpen.setEnabled(true);
				}
				String link = (String)animeInformation.fansubComboBox.getSelectedItem();
				if(link!=null && !link.isEmpty())
				{
					if(fansubMap.get(link) != null && !fansubMap.get(link).isEmpty())
						AnimeIndex.animeInformation.fansubButton.setEnabled(true);
				}
			}
		});
		filmScroll.setViewportView(filmList);
		
		JPanel completedToSeeAnime = new JPanel();
		cardContainer.add(completedToSeeAnime, "Completi Da Vedere");
		completedToSeeAnime.setLayout(new BorderLayout(0, 0));
		
		JScrollPane completedToSeeScroll = new JScrollPane();
		completedToSeeAnime.add(completedToSeeScroll, BorderLayout.CENTER);
		
		completedToSeeList = new JList(completedToSeeModel);
		completedToSeeList.setFont(segui.deriveFont(12f));
		completedToSeeList.setMaximumSize(new Dimension(138, 233));
		completedToSeeList.setMinimumSize(new Dimension(138, 233));
		completedToSeeList.setPreferredSize(new Dimension(138, 233));
		completedToSeeList.setSize(new Dimension(138, 233));
		completedToSeeList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				applyListSelectionChange(AnimeIndex.completedToSeeList);
				AnimeIndex.animeInformation.minusButton.setEnabled(true);
				AnimeIndex.animeInformation.currentEpisodeField.setEnabled(true);
				AnimeIndex.animeInformation.totalEpisodeText.setEnabled(false);
				AnimeIndex.animeInformation.addToSeeButton.setEnabled(false);
				AnimeIndex.animeInformation.releaseDateField.setEnabled(false);
				AnimeIndex.animeInformation.finishDateField.setEnabled(false);
				AnimeIndex.animeInformation.durationField.setEnabled(true);
				AnimeIndex.animeInformation.noteTextArea.setEnabled(true);
				AnimeIndex.animeInformation.typeComboBox.setEnabled(true);
				AnimeIndex.animeInformation.btnAnilistInfo.setEnabled(true);
				AnimeIndex.animeInformation.setLinkButton.setEnabled(true);
				AnimeIndex.animeInformation.exitDaycomboBox.setEnabled(false);
				AnimeIndex.animeInformation.checkDataButton.setEnabled(false);
				String ep = animeInformation.currentEpisodeField.getText();
				String fep = animeInformation.totalEpisodeText.getText();
				if(ep!=null && fep!=null && !fep.equalsIgnoreCase("??") && ep.equalsIgnoreCase(fep))
					AnimeIndex.animeInformation.finishedButton.setEnabled(true);
				else
					AnimeIndex.animeInformation.finishedButton.setEnabled(false); 
				AnimeInformation.fansubComboBox.setEnabled(true);
				String name = (String) completedToSeeList.getSelectedValue();
				if(name!=null && !name.equalsIgnoreCase("Anime"))
				{
					if(completedToSeeMap.get(name).getLink() !=null && !completedToSeeMap.get(name).getLink().isEmpty())
						AnimeIndex.animeInformation.btnOpen.setEnabled(true);
				}
				String link = (String)animeInformation.fansubComboBox.getSelectedItem();
				if(link!=null && !link.isEmpty())
				{
					if(fansubMap.get(link) != null && !fansubMap.get(link).isEmpty())
						AnimeIndex.animeInformation.fansubButton.setEnabled(true);
				}
			}
		});
		completedToSeeScroll.setViewportView(completedToSeeList);
		
		
		Date now = new Date();
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE"); // the day of the week abbreviated
		String currentDay = simpleDateformat.format(now);
		
		Object[] airingArray = airingModel.toArray();
		for (int i = 0; i < airingArray.length; i++)
		{
			String anime = (String) airingArray[i];
			AnimeData data = airingMap.get(anime);
		}
//TODO nn mostra i dati degli anime
		JPanel searchCard = new JPanel();
		cardContainer.add(searchCard, "Ricerca");
		searchCard.setLayout(new BorderLayout(0, 0));
		
		JScrollPane searchScroll = new JScrollPane();
		searchCard.add(searchScroll, BorderLayout.CENTER);	
		searchList = new JList(searchModel);
		searchList.setFont(segui.deriveFont(12f));
		searchList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				applyListSelectionChange(AnimeIndex.searchList);
				String cBox = (String)animeTypeComboBox.getSelectedItem();
				if(cBox.equalsIgnoreCase("Anime Completati"))
				{
					AnimeIndex.animeInformation.minusButton.setEnabled(false);
					AnimeIndex.animeInformation.currentEpisodeField.setEnabled(false);
					AnimeIndex.animeInformation.totalEpisodeText.setEnabled(false);
					AnimeIndex.animeInformation.addToSeeButton.setEnabled(true);
					AnimeIndex.animeInformation.releaseDateField.setEnabled(false);
					AnimeIndex.animeInformation.finishDateField.setEnabled(false);
					AnimeIndex.animeInformation.durationField.setEnabled(false);
					AnimeIndex.animeInformation.noteTextArea.setEnabled(true);
					AnimeIndex.animeInformation.typeComboBox.setEnabled(true);
					AnimeIndex.animeInformation.btnAnilistInfo.setEnabled(true);
					AnimeIndex.animeInformation.setLinkButton.setEnabled(true);
					AnimeIndex.animeInformation.exitDaycomboBox.setEnabled(false);
					AnimeIndex.animeInformation.checkDataButton.setEnabled(false);
					AnimeInformation.fansubComboBox.setEnabled(true);
					String name = (String) completedList.getSelectedValue();
					if(name!=null && !name.equalsIgnoreCase("Anime"))
					{
						if(completedMap.get(name).getLink() !=null && !completedMap.get(name).getLink().isEmpty())
							AnimeIndex.animeInformation.btnOpen.setEnabled(true);
					}
						String link = (String)animeInformation.fansubComboBox.getSelectedItem();
						if(link!=null && !link.isEmpty())
					{
						if(fansubMap.get(link) != null && !fansubMap.get(link).isEmpty())
							AnimeIndex.animeInformation.fansubButton.setEnabled(true);
					}
				}
					else if(cBox.equalsIgnoreCase("Anime in Corso"))
					{
						AnimeIndex.animeInformation.minusButton.setEnabled(true);
						AnimeIndex.animeInformation.currentEpisodeField.setEnabled(true);
						AnimeIndex.animeInformation.totalEpisodeText.setEnabled(true);
						AnimeIndex.animeInformation.addToSeeButton.setEnabled(true);
						AnimeIndex.animeInformation.releaseDateField.setEnabled(true);
						AnimeIndex.animeInformation.finishDateField.setEnabled(true);
						AnimeIndex.animeInformation.durationField.setEnabled(true);
						AnimeIndex.animeInformation.noteTextArea.setEnabled(true);
						AnimeIndex.animeInformation.typeComboBox.setEnabled(true);
						AnimeIndex.animeInformation.btnAnilistInfo.setEnabled(true);
						AnimeIndex.animeInformation.setLinkButton.setEnabled(true);
						AnimeIndex.animeInformation.exitDaycomboBox.setEnabled(true);
						AnimeIndex.animeInformation.checkDataButton.setEnabled(true);
						String ep = animeInformation.currentEpisodeField.getText();
						String fep = animeInformation.totalEpisodeText.getText();
						if(ep!=null && fep!=null && !fep.equalsIgnoreCase("??") && ep.equalsIgnoreCase(fep))
							AnimeIndex.animeInformation.finishedButton.setEnabled(true);
						else
							AnimeIndex.animeInformation.finishedButton.setEnabled(false);
						AnimeInformation.fansubComboBox.setEnabled(true);
						String name = (String) airingList.getSelectedValue();
						if(name!=null && !name.equalsIgnoreCase("Anime"))
						{
							if(airingMap.get(name).getLink() !=null && !airingMap.get(name).getLink().isEmpty())
								AnimeIndex.animeInformation.btnOpen.setEnabled(true);
						}
						String link = (String)animeInformation.fansubComboBox.getSelectedItem();
						if(link!=null && !link.isEmpty())
						{
							if(fansubMap.get(link) != null && !fansubMap.get(link).isEmpty())
								AnimeIndex.animeInformation.fansubButton.setEnabled(true);
						}
						
					}
					else if(cBox.equalsIgnoreCase("OAV"))
					{
						AnimeIndex.animeInformation.minusButton.setEnabled(true);
						AnimeIndex.animeInformation.currentEpisodeField.setEnabled(true);
						AnimeIndex.animeInformation.totalEpisodeText.setEnabled(true);
						AnimeIndex.animeInformation.addToSeeButton.setEnabled(true);
						AnimeIndex.animeInformation.releaseDateField.setEnabled(true);
						AnimeIndex.animeInformation.finishDateField.setEnabled(true);
						AnimeIndex.animeInformation.durationField.setEnabled(true);
						AnimeIndex.animeInformation.noteTextArea.setEnabled(true);
						AnimeIndex.animeInformation.typeComboBox.setEnabled(true);
						AnimeIndex.animeInformation.btnAnilistInfo.setEnabled(true);
						AnimeIndex.animeInformation.setLinkButton.setEnabled(true);
						AnimeIndex.animeInformation.exitDaycomboBox.setEnabled(true);
						AnimeIndex.animeInformation.checkDataButton.setEnabled(true);
						String ep = animeInformation.currentEpisodeField.getText();
						String fep = animeInformation.totalEpisodeText.getText();
						if(ep!=null && fep!=null && !fep.equalsIgnoreCase("??") && ep.equalsIgnoreCase(fep))
							AnimeIndex.animeInformation.finishedButton.setEnabled(true);
						else
							AnimeIndex.animeInformation.finishedButton.setEnabled(false);
						AnimeInformation.fansubComboBox.setEnabled(true);
						String name = (String) ovaList.getSelectedValue();
						if(name!=null && !name.equalsIgnoreCase("Anime"))
						{
							if(ovaMap.get(name).getLink() !=null && !ovaMap.get(name).getLink().isEmpty())
								AnimeIndex.animeInformation.btnOpen.setEnabled(true);
						}
						String link = (String)animeInformation.fansubComboBox.getSelectedItem();
						if(link!=null && !link.isEmpty())
						{
							if(fansubMap.get(link) != null && !fansubMap.get(link).isEmpty())
								AnimeIndex.animeInformation.fansubButton.setEnabled(true);
						}
					}
					else if(cBox.equalsIgnoreCase("Film"))
					{
						AnimeIndex.animeInformation.minusButton.setEnabled(true);
						AnimeIndex.animeInformation.currentEpisodeField.setEnabled(true);
						AnimeIndex.animeInformation.totalEpisodeText.setEnabled(true);
						AnimeIndex.animeInformation.addToSeeButton.setEnabled(true);
						AnimeIndex.animeInformation.releaseDateField.setEnabled(true);
						AnimeIndex.animeInformation.finishDateField.setEnabled(true);
						AnimeIndex.animeInformation.durationField.setEnabled(true);
						AnimeIndex.animeInformation.noteTextArea.setEnabled(true);
						AnimeIndex.animeInformation.typeComboBox.setEnabled(true);
						AnimeIndex.animeInformation.btnAnilistInfo.setEnabled(true);
						AnimeIndex.animeInformation.setLinkButton.setEnabled(true);
						AnimeIndex.animeInformation.exitDaycomboBox.setEnabled(true);
						AnimeIndex.animeInformation.checkDataButton.setEnabled(true);
						String ep = animeInformation.currentEpisodeField.getText();
						String fep = animeInformation.totalEpisodeText.getText();
						if(ep!=null && fep!=null && !fep.equalsIgnoreCase("??") && ep.equalsIgnoreCase(fep))
							AnimeIndex.animeInformation.finishedButton.setEnabled(true);
						else
							AnimeIndex.animeInformation.finishedButton.setEnabled(false); 
						AnimeInformation.fansubComboBox.setEnabled(true);
						String name = (String) filmList.getSelectedValue();
						if(name!=null && !name.equalsIgnoreCase("Anime"))
						{
							if(filmMap.get(name).getLink() !=null && !filmMap.get(name).getLink().isEmpty())
								AnimeIndex.animeInformation.btnOpen.setEnabled(true);
						}
						String link = (String)animeInformation.fansubComboBox.getSelectedItem();
						if(link!=null && !link.isEmpty())
						{
							if(fansubMap.get(link) != null && !fansubMap.get(link).isEmpty())
								AnimeIndex.animeInformation.fansubButton.setEnabled(true);
						}
					}
					else
					{
						AnimeIndex.animeInformation.minusButton.setEnabled(true);
						AnimeIndex.animeInformation.currentEpisodeField.setEnabled(true);
						AnimeIndex.animeInformation.totalEpisodeText.setEnabled(false);
						AnimeIndex.animeInformation.addToSeeButton.setEnabled(false);
						AnimeIndex.animeInformation.releaseDateField.setEnabled(false);
						AnimeIndex.animeInformation.finishDateField.setEnabled(false);
						AnimeIndex.animeInformation.durationField.setEnabled(true);
						AnimeIndex.animeInformation.noteTextArea.setEnabled(true);
						AnimeIndex.animeInformation.typeComboBox.setEnabled(true);
						AnimeIndex.animeInformation.btnAnilistInfo.setEnabled(true);
						AnimeIndex.animeInformation.setLinkButton.setEnabled(true);
						AnimeIndex.animeInformation.exitDaycomboBox.setEnabled(false);
						AnimeIndex.animeInformation.checkDataButton.setEnabled(false);
						String ep = animeInformation.currentEpisodeField.getText();
						String fep = animeInformation.totalEpisodeText.getText();
						if(ep!=null && fep!=null && !fep.equalsIgnoreCase("??") && ep.equalsIgnoreCase(fep))
							AnimeIndex.animeInformation.finishedButton.setEnabled(true);
						else
							AnimeIndex.animeInformation.finishedButton.setEnabled(false); 
						AnimeInformation.fansubComboBox.setEnabled(true);
						String name = (String) completedToSeeList.getSelectedValue();
						if(name!=null && !name.equalsIgnoreCase("Anime"))
						{
							if(completedToSeeMap.get(name).getLink() !=null && !completedToSeeMap.get(name).getLink().isEmpty())
								AnimeIndex.animeInformation.btnOpen.setEnabled(true);
						}
						String link = (String)animeInformation.fansubComboBox.getSelectedItem();
						if(link!=null && !link.isEmpty())
						{
							if(fansubMap.get(link) != null && !fansubMap.get(link).isEmpty())
								AnimeIndex.animeInformation.fansubButton.setEnabled(true);
						}
					}
			}
		});
		searchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		searchList.setSize(new Dimension(138, 233));
		searchList.setPreferredSize(new Dimension(138, 233));
		searchList.setMinimumSize(new Dimension(138, 233));
		searchList.setMaximumSize(new Dimension(138, 233));
		searchScroll.setViewportView(searchList);
		
		JPanel filterCard = new JPanel();
		cardContainer.add(filterCard, "Filtri");
		filterCard.setLayout(new BorderLayout(0, 0));
		
		JScrollPane filterScroll = new JScrollPane();
		filterCard.add(filterScroll, BorderLayout.CENTER);
		
		filterList = new JList(filterModel);
		filterList.setFont(segui.deriveFont(12f));
		filterList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				applyListSelectionChange(AnimeIndex.filterList);
				String cBox = (String)animeTypeComboBox.getSelectedItem();
				if(cBox.equalsIgnoreCase("Anime Completati"))
				{
					AnimeIndex.animeInformation.minusButton.setEnabled(false);
					AnimeIndex.animeInformation.currentEpisodeField.setEnabled(false);
					AnimeIndex.animeInformation.totalEpisodeText.setEnabled(false);
					AnimeIndex.animeInformation.addToSeeButton.setEnabled(true);
					AnimeIndex.animeInformation.releaseDateField.setEnabled(false);
					AnimeIndex.animeInformation.finishDateField.setEnabled(false);
					AnimeIndex.animeInformation.durationField.setEnabled(false);
					AnimeIndex.animeInformation.noteTextArea.setEnabled(true);
					AnimeIndex.animeInformation.typeComboBox.setEnabled(true);
					AnimeIndex.animeInformation.btnAnilistInfo.setEnabled(true);
					AnimeIndex.animeInformation.setLinkButton.setEnabled(true);
					AnimeIndex.animeInformation.exitDaycomboBox.setEnabled(false);
					AnimeIndex.animeInformation.checkDataButton.setEnabled(false);
					AnimeInformation.fansubComboBox.setEnabled(true);
					String name = (String) completedList.getSelectedValue();
					if(name!=null && !name.equalsIgnoreCase("Anime"))
					{
						if(completedMap.get(name).getLink() !=null && !completedMap.get(name).getLink().isEmpty())
							AnimeIndex.animeInformation.btnOpen.setEnabled(true);
					}
						String link = (String)animeInformation.fansubComboBox.getSelectedItem();
						if(link!=null && !link.isEmpty())
					{
						if(fansubMap.get(link) != null && !fansubMap.get(link).isEmpty())
							AnimeIndex.animeInformation.fansubButton.setEnabled(true);
					}
				}
					else if(cBox.equalsIgnoreCase("Anime in Corso"))
					{
						AnimeIndex.animeInformation.minusButton.setEnabled(true);
						AnimeIndex.animeInformation.currentEpisodeField.setEnabled(true);
						AnimeIndex.animeInformation.totalEpisodeText.setEnabled(true);
						AnimeIndex.animeInformation.addToSeeButton.setEnabled(true);
						AnimeIndex.animeInformation.releaseDateField.setEnabled(true);
						AnimeIndex.animeInformation.finishDateField.setEnabled(true);
						AnimeIndex.animeInformation.durationField.setEnabled(true);
						AnimeIndex.animeInformation.noteTextArea.setEnabled(true);
						AnimeIndex.animeInformation.typeComboBox.setEnabled(true);
						AnimeIndex.animeInformation.btnAnilistInfo.setEnabled(true);
						AnimeIndex.animeInformation.setLinkButton.setEnabled(true);
						AnimeIndex.animeInformation.exitDaycomboBox.setEnabled(true);
						AnimeIndex.animeInformation.checkDataButton.setEnabled(true);
						String ep = animeInformation.currentEpisodeField.getText();
						String fep = animeInformation.totalEpisodeText.getText();
						if(ep!=null && fep!=null && !fep.equalsIgnoreCase("??") && ep.equalsIgnoreCase(fep))
							AnimeIndex.animeInformation.finishedButton.setEnabled(true);
						else
							AnimeIndex.animeInformation.finishedButton.setEnabled(false); 
						AnimeInformation.fansubComboBox.setEnabled(true);
						String name = (String) airingList.getSelectedValue();
						if(name!=null && !name.equalsIgnoreCase("Anime"))
						{
							if(airingMap.get(name).getLink() !=null && !airingMap.get(name).getLink().isEmpty())
								AnimeIndex.animeInformation.btnOpen.setEnabled(true);
						}
						String link = (String)animeInformation.fansubComboBox.getSelectedItem();
						if(link!=null && !link.isEmpty())
						{
							if(fansubMap.get(link) != null && !fansubMap.get(link).isEmpty())
								AnimeIndex.animeInformation.fansubButton.setEnabled(true);
						}
						
					}
					else if(cBox.equalsIgnoreCase("OAV"))
					{
						AnimeIndex.animeInformation.minusButton.setEnabled(true);
						AnimeIndex.animeInformation.currentEpisodeField.setEnabled(true);
						AnimeIndex.animeInformation.totalEpisodeText.setEnabled(true);
						AnimeIndex.animeInformation.addToSeeButton.setEnabled(true);
						AnimeIndex.animeInformation.releaseDateField.setEnabled(true);
						AnimeIndex.animeInformation.finishDateField.setEnabled(true);
						AnimeIndex.animeInformation.durationField.setEnabled(true);
						AnimeIndex.animeInformation.noteTextArea.setEnabled(true);
						AnimeIndex.animeInformation.typeComboBox.setEnabled(true);
						AnimeIndex.animeInformation.btnAnilistInfo.setEnabled(true);
						AnimeIndex.animeInformation.setLinkButton.setEnabled(true);
						AnimeIndex.animeInformation.exitDaycomboBox.setEnabled(true);
						AnimeIndex.animeInformation.checkDataButton.setEnabled(true);
						String ep = animeInformation.currentEpisodeField.getText();
						String fep = animeInformation.totalEpisodeText.getText();
						if(ep!=null && fep!=null && !fep.equalsIgnoreCase("??") && ep.equalsIgnoreCase(fep))
							AnimeIndex.animeInformation.finishedButton.setEnabled(true);
						else
							AnimeIndex.animeInformation.finishedButton.setEnabled(false); 
						AnimeInformation.fansubComboBox.setEnabled(true);
						String name = (String) ovaList.getSelectedValue();
						if(name!=null && !name.equalsIgnoreCase("Anime"))
						{
							if(ovaMap.get(name).getLink() !=null && !ovaMap.get(name).getLink().isEmpty())
								AnimeIndex.animeInformation.btnOpen.setEnabled(true);
						}
						String link = (String)animeInformation.fansubComboBox.getSelectedItem();
						if(link!=null && !link.isEmpty())
						{
							if(fansubMap.get(link) != null && !fansubMap.get(link).isEmpty())
								AnimeIndex.animeInformation.fansubButton.setEnabled(true);
						}
					}
					else if(cBox.equalsIgnoreCase("Film"))
					{
						AnimeIndex.animeInformation.minusButton.setEnabled(true);
						AnimeIndex.animeInformation.currentEpisodeField.setEnabled(true);
						AnimeIndex.animeInformation.totalEpisodeText.setEnabled(true);
						AnimeIndex.animeInformation.addToSeeButton.setEnabled(true);
						AnimeIndex.animeInformation.releaseDateField.setEnabled(true);
						AnimeIndex.animeInformation.finishDateField.setEnabled(true);
						AnimeIndex.animeInformation.durationField.setEnabled(true);
						AnimeIndex.animeInformation.noteTextArea.setEnabled(true);
						AnimeIndex.animeInformation.typeComboBox.setEnabled(true);
						AnimeIndex.animeInformation.btnAnilistInfo.setEnabled(true);
						AnimeIndex.animeInformation.setLinkButton.setEnabled(true);
						AnimeIndex.animeInformation.exitDaycomboBox.setEnabled(true);
						AnimeIndex.animeInformation.checkDataButton.setEnabled(true);
						String ep = animeInformation.currentEpisodeField.getText();
						String fep = animeInformation.totalEpisodeText.getText();
						if(ep!=null && fep!=null && !fep.equalsIgnoreCase("??") && ep.equalsIgnoreCase(fep))
							AnimeIndex.animeInformation.finishedButton.setEnabled(true);
						else
							AnimeIndex.animeInformation.finishedButton.setEnabled(false);
						AnimeInformation.fansubComboBox.setEnabled(true);
						String name = (String) filmList.getSelectedValue();
						if(name!=null && !name.equalsIgnoreCase("Anime"))
						{
							if(filmMap.get(name).getLink() !=null && !filmMap.get(name).getLink().isEmpty())
								AnimeIndex.animeInformation.btnOpen.setEnabled(true);
						}
						String link = (String)animeInformation.fansubComboBox.getSelectedItem();
						if(link!=null && !link.isEmpty())
						{
							if(fansubMap.get(link) != null && !fansubMap.get(link).isEmpty())
								AnimeIndex.animeInformation.fansubButton.setEnabled(true);
						}
					}
					else
					{
						AnimeIndex.animeInformation.minusButton.setEnabled(true);
						AnimeIndex.animeInformation.currentEpisodeField.setEnabled(true);
						AnimeIndex.animeInformation.totalEpisodeText.setEnabled(false);
						AnimeIndex.animeInformation.addToSeeButton.setEnabled(false);
						AnimeIndex.animeInformation.releaseDateField.setEnabled(false);
						AnimeIndex.animeInformation.finishDateField.setEnabled(false);
						AnimeIndex.animeInformation.durationField.setEnabled(true);
						AnimeIndex.animeInformation.noteTextArea.setEnabled(true);
						AnimeIndex.animeInformation.typeComboBox.setEnabled(true);
						AnimeIndex.animeInformation.btnAnilistInfo.setEnabled(true);
						AnimeIndex.animeInformation.setLinkButton.setEnabled(true);
						AnimeIndex.animeInformation.exitDaycomboBox.setEnabled(false);
						AnimeIndex.animeInformation.checkDataButton.setEnabled(false);
						String ep = animeInformation.currentEpisodeField.getText();
						String fep = animeInformation.totalEpisodeText.getText();
						if(ep!=null && fep!=null && !fep.equalsIgnoreCase("??") && ep.equalsIgnoreCase(fep))
							AnimeIndex.animeInformation.finishedButton.setEnabled(true);
						else
							AnimeIndex.animeInformation.finishedButton.setEnabled(false);
						AnimeInformation.fansubComboBox.setEnabled(true);
						String name = (String) completedToSeeList.getSelectedValue();
						if(name!=null && !name.equalsIgnoreCase("Anime"))
						{
							if(completedToSeeMap.get(name).getLink() !=null && !completedToSeeMap.get(name).getLink().isEmpty())
								AnimeIndex.animeInformation.btnOpen.setEnabled(true);
						}
						String link = (String)animeInformation.fansubComboBox.getSelectedItem();
						if(link!=null && !link.isEmpty())
						{
							if(fansubMap.get(link) != null && !fansubMap.get(link).isEmpty())
								AnimeIndex.animeInformation.fansubButton.setEnabled(true);
						}
					}
			}
		});
		filterList.setSize(new Dimension(138, 233));
		filterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		filterList.setPreferredSize(new Dimension(138, 233));
		filterList.setMinimumSize(new Dimension(138, 233));
		filterList.setMaximumSize(new Dimension(138, 233));
		filterScroll.setViewportView(filterList);
		
		JPanel buttonPanel = new JPanel();
		panel.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new BorderLayout(0, 0));

		deleteButton = new JButton("Elimina Anime");
		deleteButton.setPreferredSize(new Dimension(159, 21));
		deleteButton.setMaximumSize(new Dimension(159, 21));
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String type = (String) animeTypeComboBox.getSelectedItem();

				SortedListModel model = getModel();
				JList list = getJList();
				String listName = getList();
				TreeMap<String,AnimeData> map = getMap();
				ArrayList<String> arrayList = getDeletedAnimeArray();
				int index = list.getSelectedIndex();
				String name = (String) model.getElementAt(index);
				model.removeElementAt(index);
				index -= 1;
				list.clearSelection();
				list.setSelectedIndex(index);
				
				String image = map.get(name).getImagePath(listName);
				arrayList.add(image);
								
				map.remove(name);
				if (!list.isSelectionEmpty())					
					deleteButton.setEnabled(true);
				else
				{
					deleteButton.setEnabled(false);
					animeInformation.setBlank();
				}
			}
		});
		deleteButton.setEnabled(false);
		buttonPanel.add(deleteButton, BorderLayout.CENTER);
		
		
		addButton = new JButton("Aggiungi Anime");
		addButton.setPreferredSize(new Dimension(159, 21));
		addButton.setMaximumSize(new Dimension(159, 21));
		addButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {	
				animeDialog = new AddAnimeDialog();
				animeDialog.setLocationRelativeTo(mainFrame);
				animeDialog.setVisible(true);
			}
		});
		buttonPanel.add(addButton, BorderLayout.SOUTH);
		
	    setFilterButton = new JButton("Filtro");
	    setFilterButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		SetFilterDialog filterDialog = new SetFilterDialog();
				filterDialog.setLocationRelativeTo(animeInformation.animeImage);
				filterDialog.setVisible(true);
	    	}
	    });
	    setFilterButton.setIcon(new ImageIcon(AnimeIndex.class.getResource("/image/ellipse_icon3.png")));
		buttonPanel.add(setFilterButton, BorderLayout.NORTH);
		
		fansubList = FileManager.loadFansubList();
		animeInformation = new AnimeInformation();
		mainFrame.add(animeInformation, BorderLayout.CENTER);
		addToFansub("?????");
		AnimeIndex.animeInformation.setFansubComboBox();
		animeInformation.setBlank();
		if (appProp.getProperty("List_to_visualize_at_start").equalsIgnoreCase("Last list"))
		{
			list = appProp.getProperty("Last_list");
			animeTypeComboBox.setSelectedItem(list);
		}
		else
			list = appProp.getProperty("List_to_visualize_at_start");
		animeTypeComboBox.setSelectedItem(list);
		
		
		}
	
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	public static  String[] getFansubList()
	{
		return fansubList;
	}
	
	public void addToFansub(String fansub)
	
	{
		String[] oldList = fansubList;
		String[] newList = new String[oldList.length + 1];
		for (int i = 0; i < oldList.length; i++)
		{
			newList[i] = oldList[i];
		}

		newList[oldList.length] = fansub; 
		fansubList = newList;
	}
	
	public static void setFansubList(Object[] arrayToSet)
	{
		String[] fansubArray = new String[arrayToSet.length];
		for (int i = 0; i < fansubArray.length; i++) {
			if (arrayToSet[i]!= null)
				fansubArray[i] = arrayToSet[i].toString();
		}
		fansubList = fansubArray;
		AnimeInformation.setFansubComboBox();
	}
	
	public void addToAnime(String animeName)
	{
		completedModel.addElement(animeName);
	}
	
	public static String getList()
	{
		return (String) animeTypeComboBox.getSelectedItem();
	}
	
	public void SearchInList(String searchedVal, SortedListModel modelToSearch) 
	{
		Object[] mainArray = modelToSearch.toArray();			
			searchModel.clear();
			for (int i = 0; i < mainArray.length; i++) {
				String value = (String) mainArray[i];
				value = value.toLowerCase();
				searchedVal = searchedVal.toLowerCase();
				if (value.contains(searchedVal))
					searchModel.addElement((String)mainArray[i]);
			}
		if (searchModel.isEmpty())
			searchModel.addElement("Nessun Anime Corrispondente");
		}

	public static JList getJList()
	{
		String listName = getList();
		JList list= null;
		if (listName.equalsIgnoreCase("anime completati"))
		{	
			list = AnimeIndex.completedList;						
		}				
		else if (listName.equalsIgnoreCase("anime in corso"))
		{
			list = AnimeIndex.airingList;
		}
		else if (listName.equalsIgnoreCase("oav"))
		{
			list = AnimeIndex.ovaList;
		}
		else if (listName.equalsIgnoreCase("film"))
		{
			list = AnimeIndex.filmList;
		}
		else if (listName.equalsIgnoreCase("completi da vedere"))
		{
			list = AnimeIndex.completedToSeeList;
		}
		
		return list;
	}
	
	public static TreeMap getMap()
	{
		String listName = getList();
		TreeMap map= null;
		if (listName.equalsIgnoreCase("anime completati"))
		{	
			map = AnimeIndex.completedMap;						
		}				
		else if (listName.equalsIgnoreCase("anime in corso"))
		{
			map = AnimeIndex.airingMap;
		}
		else if (listName.equalsIgnoreCase("oav"))
		{
			map = AnimeIndex.ovaMap;
		}
		else if (listName.equalsIgnoreCase("film"))
		{
			map = AnimeIndex.filmMap;
		}
		else if (listName.equalsIgnoreCase("completi da vedere"))
		{
			map = AnimeIndex.completedToSeeMap;
		}
		
		return map;
	}
	
	public static SortedListModel getModel()
	{
		String listName = getList();
		SortedListModel model= null;
		if (listName.equalsIgnoreCase("anime completati"))
		{	
			model = AnimeIndex.completedModel;						
		}				
		else if (listName.equalsIgnoreCase("anime in corso"))
		{
			model = AnimeIndex.airingModel;
		}
		else if (listName.equalsIgnoreCase("oav"))
		{
			model = AnimeIndex.ovaModel;
		}
		else if (listName.equalsIgnoreCase("film"))
		{
			model = AnimeIndex.filmModel;
		}
		else if (listName.equalsIgnoreCase("completi da vedere"))
		{
			model = AnimeIndex.completedToSeeModel;
		}
		
		return model;
	}
	
	public static ArrayList<String> getDeletedAnimeArray()
	{
		String listName = getList();
		ArrayList<String> arrayList= null;
		if (listName.equalsIgnoreCase("anime completati"))
		{	
			arrayList = AnimeIndex.completedDeletedAnime;						
		}				
		else if (listName.equalsIgnoreCase("anime in corso"))
		{
			arrayList = AnimeIndex.airingDeletedAnime;
		}
		else if (listName.equalsIgnoreCase("oav"))
		{
			arrayList = AnimeIndex.ovaDeletedAnime;
		}
		else if (listName.equalsIgnoreCase("film"))
		{
			arrayList = AnimeIndex.filmDeletedAnime;
		}
		else if (listName.equalsIgnoreCase("completi da vedere"))
		{
			arrayList = AnimeIndex.completedToSeeDeletedAnime;
		}
		
		return arrayList;
	}

	public static void saveModifiedInformation()
	{
		if(!animeInformation.lblAnimeName.getText().equalsIgnoreCase("Anime"))
		{
			String name = animeInformation.lblAnimeName.getText();
			String currEp = animeInformation.currentEpisodeField.getText();
			String totEp = animeInformation.totalEpisodeText.getText();
			String fansub = (String) animeInformation.fansubComboBox.getSelectedItem();
			String fansubLink = animeInformation.getLink();
			String note = animeInformation.noteTextArea.getText();
			String day = (String) animeInformation.exitDaycomboBox.getSelectedItem();
			String linkName = animeInformation.setLinkButton.getText();
			String animeType = (String)animeInformation.typeComboBox.getSelectedItem();
			String releaseDate = animeInformation.releaseDateField.getText();
			String finishDate = animeInformation.finishDateField.getText();
			String durationEp = animeInformation.durationField.getText();
			
			String list = AnimeIndex.getList();
			if (list.equalsIgnoreCase("Anime Completati"))
				{
				String image = AnimeIndex.completedMap.get(name).getImageName();
				String id = AnimeIndex.completedMap.get(name).getId();
				String link = AnimeIndex.completedMap.get(name).getLink();
				Boolean bd = AnimeIndex.completedMap.get(name).getBd();
				AnimeData data = new AnimeData(currEp, totEp, fansub, note, image, day, id, linkName, link, animeType, releaseDate, finishDate, durationEp, bd);
				AnimeIndex.completedMap.put(name, data);
				}
			else if (list.equalsIgnoreCase("Anime in Corso"))
				{
				String image = AnimeIndex.airingMap.get(name).getImageName();
				String id = AnimeIndex.airingMap.get(name).getId();
				String link = AnimeIndex.airingMap.get(name).getLink();
				Boolean bd = AnimeIndex.airingMap.get(name).getBd();
				AnimeData data = new AnimeData(currEp, totEp, fansub, note, image, day, id, linkName, link, animeType, releaseDate, finishDate, durationEp, bd);
				AnimeIndex.airingMap.put(name, data);
				}
			else if (list.equalsIgnoreCase("OAV"))
				{
				String image = AnimeIndex.ovaMap.get(name).getImageName();
				String id = AnimeIndex.ovaMap.get(name).getId();
				String link = AnimeIndex.ovaMap.get(name).getLink();
				Boolean bd = AnimeIndex.ovaMap.get(name).getBd();
				AnimeData data = new AnimeData(currEp, totEp, fansub, note, image, day, id, linkName, link, animeType, releaseDate, finishDate, durationEp, bd);
				AnimeIndex.ovaMap.put(name, data);
				}
			else if (list.equalsIgnoreCase("Film"))
			{
				String image = AnimeIndex.filmMap.get(name).getImageName();
				String id = AnimeIndex.filmMap.get(name).getId();
				String link = AnimeIndex.filmMap.get(name).getLink();
				Boolean bd = AnimeIndex.filmMap.get(name).getBd();
				AnimeData data = new AnimeData(currEp, totEp, fansub, note, image, day, id, linkName, link, animeType, releaseDate, finishDate, durationEp, bd);
				AnimeIndex.filmMap.put(name, data);
			}
			else if (list.equalsIgnoreCase("Completi Da Vedere"))
			{
				String image = AnimeIndex.completedToSeeMap.get(name).getImageName();
				String id = AnimeIndex.completedToSeeMap.get(name).getId();
				String link = AnimeIndex.completedToSeeMap.get(name).getLink();
				Boolean bd = AnimeIndex.completedToSeeMap.get(name).getBd();
				AnimeData data = new AnimeData(currEp, totEp, fansub, note, image, day, id, linkName, link, animeType, releaseDate, finishDate, durationEp, bd);
				AnimeIndex.completedToSeeMap.put(name, data);				
			}
		}
	}

	public void saveModifiedInformation(String list)
	{
		if(!animeInformation.lblAnimeName.getText().equalsIgnoreCase("Anime"))
		{
			String name = animeInformation.lblAnimeName.getText();
			String currEp = animeInformation.currentEpisodeField.getText();
			String totEp = animeInformation.totalEpisodeText.getText();
			String fansub = (String) animeInformation.fansubComboBox.getSelectedItem();
			String fansubLink = animeInformation.getLink();
			String note = animeInformation.noteTextArea.getText();
			String day = (String) animeInformation.exitDaycomboBox.getSelectedItem();
			String linkName = animeInformation.setLinkButton.getText();
			String animeType = (String)animeInformation.typeComboBox.getSelectedItem();
			String releaseDate = animeInformation.releaseDateField.getText();
			String finishDate = animeInformation.finishDateField.getText();
			String durationEp = animeInformation.durationField.getText();
			
			if (list.equalsIgnoreCase("Anime Completati"))
				{
				String image = AnimeIndex.completedMap.get(name).getImageName();
				String id = AnimeIndex.completedMap.get(name).getId();
				String link = AnimeIndex.completedMap.get(name).getLink();
				Boolean bd = AnimeIndex.completedMap.get(name).getBd();
				AnimeData data = new AnimeData(currEp, totEp, fansub, note, image, day, id, linkName, link, animeType, releaseDate, finishDate, durationEp, bd);
				AnimeIndex.completedMap.put(name, data);
				}
			else if (list.equalsIgnoreCase("Anime in Corso"))
				{
				String image = AnimeIndex.airingMap.get(name).getImageName();
				String id = AnimeIndex.airingMap.get(name).getId();
				String link = AnimeIndex.airingMap.get(name).getLink();
				Boolean bd = AnimeIndex.airingMap.get(name).getBd();
				AnimeData data = new AnimeData(currEp, totEp, fansub, note, image, day, id, linkName, link, animeType, releaseDate, finishDate, durationEp, bd);
				AnimeIndex.airingMap.put(name, data);
				}
			else if (list.equalsIgnoreCase("OAV"))
				{
				String image = AnimeIndex.ovaMap.get(name).getImageName();
				String id = AnimeIndex.ovaMap.get(name).getId();
				String link = AnimeIndex.ovaMap.get(name).getLink();
				Boolean bd = AnimeIndex.ovaMap.get(name).getBd();
				AnimeData data = new AnimeData(currEp, totEp, fansub, note, image, day, id, linkName, link, animeType, releaseDate, finishDate, durationEp, bd);
				AnimeIndex.ovaMap.put(name, data);
				}
			else if (list.equalsIgnoreCase("Film"))
			{
				String image = AnimeIndex.filmMap.get(name).getImageName();
				String id = AnimeIndex.filmMap.get(name).getId();
				String link = AnimeIndex.filmMap.get(name).getLink();
				Boolean bd = AnimeIndex.filmMap.get(name).getBd();
				AnimeData data = new AnimeData(currEp, totEp, fansubLink, note, image, day, id, linkName, link, animeType, releaseDate, finishDate, durationEp, bd);
				AnimeIndex.filmMap.put(name, data);
			}
			else if (list.equalsIgnoreCase("Completi Da Vedere"))
			{
				String image = AnimeIndex.completedToSeeMap.get(name).getImageName();
				String id = AnimeIndex.completedToSeeMap.get(name).getId();
				String link = AnimeIndex.completedToSeeMap.get(name).getLink();
				Boolean bd = AnimeIndex.completedToSeeMap.get(name).getBd();
				AnimeData data = new AnimeData(currEp, totEp, fansubLink, note, image, day, id, linkName, link, animeType, releaseDate, finishDate, durationEp, bd);
				AnimeIndex.completedToSeeMap.put(name, data);
			}
		}
	}
	
	public void setFansubMap(TreeMap<String, String> fansubMap) 
	{
		AnimeIndex.fansubMap.putAll(fansubMap);
	}
	
	private void applyListSelectionChange(JList jList)
	{
		try
		{
			saveModifiedInformation();
		}
		catch (NullPointerException e1)
		{
			deleteButton.setEnabled(true);
			JList list = jList;
			String anime = (String) list.getSelectedValue();
			if (anime != null)
			{
			TreeMap<String,AnimeData> map = getMap();
			AnimeData data = map.get(anime);
			animeInformation.setAnimeName(anime);
			animeInformation.setCurrentEp(data.getCurrentEpisode());
			animeInformation.setTotalEp(data.getTotalEpisode());
			animeInformation.setDurationEp(data.getDurationEp());
			animeInformation.setFansub(data.getFansub());
			animeInformation.setLink(data.getLink());
			if (data.getLink() != null && !(data.getLink().isEmpty()))
			{
				if (data.getLinkName() != null && !(data.getLinkName().isEmpty()))
					animeInformation.setlinkName(data.getLinkName());
				else
					animeInformation.setlinkName("Link");
			}
			else
				animeInformation.setlinkName("Imposta Link");
			animeInformation.setReleaseDate(data.getReleaseDate());
			animeInformation.setFinishDate(data.getFinishDate());
			animeInformation.setDay(data.getDay());
			animeInformation.setType(data.getAnimeType());
			String listName = getList();
			String path = data.getImagePath(listName);
			File file = new File(path);
			if (file.exists())
				animeInformation.setImage(data.getImagePath(listName));
			else
			{
				animeInformation.setImage("default");
			}
			
			if(data.getCurrentEpisode().equals(data.getTotalEpisode()))
				animeInformation.plusButton.setEnabled(false);
			else
				animeInformation.plusButton.setEnabled(true);
			}
		}
		
		deleteButton.setEnabled(true);
		JList list = jList;
		String anime = (String) list.getSelectedValue();
		if (anime != null)
		{	
			TreeMap<String,AnimeData> map = getMap();
			AnimeData data = map.get(anime);
			animeInformation.setAnimeName(anime);
			animeInformation.setCurrentEp(data.getCurrentEpisode());
			animeInformation.setTotalEp(data.getTotalEpisode());
			animeInformation.setDurationEp(data.getDurationEp());
			animeInformation.setFansub(data.getFansub());
			animeInformation.setLink(data.getLink());
			if (data.getLink() != null && !(data.getLink().isEmpty()))
			{
				if (data.getLinkName() != null && !(data.getLinkName().isEmpty()))
					animeInformation.setlinkName(data.getLinkName());
				else
					animeInformation.setlinkName("Link");
			}
			else
				animeInformation.setlinkName("Imposta Link");
			animeInformation.setReleaseDate(data.getReleaseDate());
			animeInformation.setFinishDate(data.getFinishDate());
			animeInformation.setDay(data.getDay());
			animeInformation.setType(data.getAnimeType());
			String listName = getList();
			String path = data.getImagePath(listName);
			File file = new File(path);
			if (file.exists())
				animeInformation.setImage(data.getImagePath(listName));
			else
			{
				animeInformation.setImage("deafult");
			}
		
			if(data.getCurrentEpisode().equals(data.getTotalEpisode()))
				animeInformation.plusButton.setEnabled(false);
			else
				animeInformation.plusButton.setEnabled(true);
		
			if (data.getFansub() != null)
			{
				if(fansubMap.get(data.getFansub()) != null && !(fansubMap.get(data.getFansub())).isEmpty())
	                AnimeIndex.animeInformation.fansubButton.setEnabled(true);
				else
					 AnimeIndex.animeInformation.fansubButton.setEnabled(false);
			} 
			else
			{
				 AnimeIndex.animeInformation.fansubButton.setEnabled(false);
			}
		}
		
	}
	
	private static Font segui()
	{
		InputStream is = AnimeIndex.class.getResourceAsStream("/font/seguisym.ttf");
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT,is);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		return font;
}
}
//AnimeIndex.animeInformation.minusButton.setEnabled(false);
//AnimeIndex.animeInformation.currentEpisodeField.setEnabled(false);
//AnimeIndex.animeInformation.totalEpisodeText.setEnabled(false);
//AnimeIndex.animeInformation.addToSeeButton.setEnabled(true);
//AnimeIndex.animeInformation.releaseDateField.setEnabled(false);
//AnimeIndex.animeInformation.finishDateField.setEnabled(false);
//AnimeIndex.animeInformation.durationField.setEnabled(false);
