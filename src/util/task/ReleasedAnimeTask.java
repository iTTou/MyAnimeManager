package util.task;

import java.util.GregorianCalendar;

import javax.swing.SwingWorker;

import main.AnimeIndex;
import util.AnimeData;
import util.MAMUtil;
import util.window.ReleaseNotifierDialog;

public class ReleasedAnimeTask extends SwingWorker
{
	public static boolean enableFilm;
	public static boolean enableOav;
	private ReleaseNotifierDialog dial;
	
	@Override
	protected Object doInBackground() throws Exception
	{
		dial.ovaReleased.clear();
		dial.filmReleased.clear();
		String oggi = MAMUtil.today();
		GregorianCalendar today = MAMUtil.getDate(oggi);
		GregorianCalendar lastControlDate = null;
		if(!AnimeIndex.appProp.getProperty("Date_Release").equalsIgnoreCase("none"))
		{
			try {
				lastControlDate = MAMUtil.getDate(AnimeIndex.appProp.getProperty("Date_Release"));
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		}
		
		GregorianCalendar animeDate = null;
		Object[] ovaArray = AnimeIndex.ovaModel.toArray();
		for (int i = 0; i < ovaArray.length; i++)
		{
			String name = (String) ovaArray[i];
			AnimeData data = AnimeIndex.ovaMap.get(name);
			String releaseDate = data.getFinishDate();
			String secondaryReleaseDate = data.getReleaseDate();
			
			try {
				if(releaseDate.contains("?") || today.before(MAMUtil.getDate(releaseDate)))
				{
					try
					{
						animeDate = MAMUtil.getDate(secondaryReleaseDate);
					}
					catch (java.text.ParseException e){
					}
				}
				else
					animeDate = MAMUtil.getDate(releaseDate);
				
				if(AnimeIndex.openReleaseDialog == true)
				{
					if(AnimeIndex.exitDateMap.containsKey(name))
					{
						if(MAMUtil.getDate(AnimeIndex.exitDateMap.get(name)).equals(today))
						{
							if (animeDate.before(today)||animeDate.equals(today))
							{	
								dial.ovaReleased.addElement(name);
							}
						}
					}
				}
				
				if(lastControlDate!=null)
				{
					if(animeDate.after(lastControlDate))
					{
						if(AnimeIndex.exitDateMap.containsKey(name))
						{
							if(MAMUtil.getDate(AnimeIndex.exitDateMap.get(name)).before(animeDate))
							{
								if (animeDate.before(today)||animeDate.equals(today))
								{	
									dial.ovaReleased.addElement(name);
									AnimeIndex.exitDateMap.put(name, oggi);
								}
							}
						}
						else
						{
							if (animeDate.before(today)||animeDate.equals(today))
							{	
								dial.ovaReleased.addElement(name);
								AnimeIndex.exitDateMap.put(name, oggi);
							}
						}
					}
				}
				else
				{
					if(AnimeIndex.exitDateMap.containsKey(name))
					{
						if(MAMUtil.getDate(AnimeIndex.exitDateMap.get(name)).before(animeDate))
						{
							if (animeDate.before(today)||animeDate.equals(today))
							{	
								dial.ovaReleased.addElement(name);
								AnimeIndex.exitDateMap.put(name, oggi);
							}
						}
					}
					else
					{
						if (animeDate.before(today)||animeDate.equals(today))
						{	
							dial.ovaReleased.addElement(name);
							AnimeIndex.exitDateMap.put(name, oggi);
						}
					}
				}
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		}

		Object[] filmArray = AnimeIndex.filmModel.toArray();
		for (int i = 0; i < filmArray.length; i++)
		{
			String name = (String) filmArray[i];
			AnimeData data = AnimeIndex.filmMap.get(name);
			String releaseDate = data.getFinishDate();
			String secondaryReleaseDate = data.getReleaseDate();
			
			try {
				if(releaseDate.contains("?") || today.before(MAMUtil.getDate(releaseDate)))
				{
					try
					{
						animeDate = MAMUtil.getDate(secondaryReleaseDate);
					}
					catch (java.text.ParseException e){
					}
				}
				else
					animeDate = MAMUtil.getDate(releaseDate);
				
				if(AnimeIndex.openReleaseDialog == true)
				{
					if(AnimeIndex.exitDateMap.containsKey(name))
					{
						if(MAMUtil.getDate(AnimeIndex.exitDateMap.get(name)).equals(today))
						{
							if (animeDate.before(today)||animeDate.equals(today))
							{	
								dial.filmReleased.addElement(name);
							}
						}
					}
				}
				
				if(lastControlDate!=null)
				{
					if(animeDate.after(lastControlDate))
					{
						if(AnimeIndex.exitDateMap.containsKey(name))
						{
							if(MAMUtil.getDate(AnimeIndex.exitDateMap.get(name)).before(animeDate))
							{
								if (animeDate.before(today)||animeDate.equals(today))
								{	
									dial.filmReleased.addElement(name);
									AnimeIndex.exitDateMap.put(name, oggi);
								}
							}
						}
						else
						{
							if (animeDate.before(today)||animeDate.equals(today))
							{	
								dial.filmReleased.addElement(name);
								AnimeIndex.exitDateMap.put(name, oggi);
							}
						}
					}
				}
				else
				{
					if(AnimeIndex.exitDateMap.containsKey(name))
					{
						if(MAMUtil.getDate(AnimeIndex.exitDateMap.get(name)).before(animeDate))
						{
							if (animeDate.before(today)||animeDate.equals(today))
							{	
								dial.filmReleased.addElement(name);
								AnimeIndex.exitDateMap.put(name, oggi);
							}
						}
					}
					else
					{
						if (animeDate.before(today)||animeDate.equals(today))
						{	
							dial.filmReleased.addElement(name);
							AnimeIndex.exitDateMap.put(name, oggi);
						}
					}
				}
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		}
		if (dial.ovaReleased.isEmpty())
		{
			dial.ovaReleased.addElement("Nessun Anime Rilasciato");
			enableOav = false;
		}
		else
			enableOav = true;
		if (dial.filmReleased.isEmpty())
		{
			dial.filmReleased.addElement("Nessun Anime Rilasciato");
			enableFilm = false;
		}
		else
			enableFilm = true;
		return null;
	}

	@Override
	protected void done()
	{
		if (!ReleaseNotifierDialog.ovaReleased.contains("Nessun Anime Rilasciato") || !ReleaseNotifierDialog.filmReleased.contains("Nessun Anime Rilasciato"))
		{
			dial = new ReleaseNotifierDialog();
			dial.setLocationRelativeTo(AnimeIndex.mainFrame);
			dial.setVisible(true);
		}
		else if (AnimeIndex.openReleaseDialog==true)
		{
			AnimeIndex.openReleaseDialog = false;
			dial = new ReleaseNotifierDialog();
			dial.setLocationRelativeTo(AnimeIndex.mainFrame);
			dial.setVisible(true);
		}
	}

}
