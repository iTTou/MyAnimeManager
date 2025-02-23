package util;

import java.io.File;

public class AnimeData
{
	String currentEpisode;
	String totalEpisode;
	String fansub;
	String note;
	String imageName;
	String day;
	String id;
	String linkName;
	String link;
	String animeType;
	String releaseDate;
	String finishDate;
	String durationEp;
	Boolean bd;
	//tipo anime, data di inizio e fine

	private final static String IMAGE_PATH = FileManager.getImageFolderPath();
		
	public AnimeData(String currentEpisode, String totalEpisode, String fansub, String note, String image, 
					 String day, String id, String linkName, String link, String animeType, String releaseDate, 
					 String finishDate, String durationEp, Boolean bd)
	{
		this.currentEpisode = currentEpisode;
		this.totalEpisode = totalEpisode;
		this.fansub = fansub;
		this.note = note;
		this.imageName = image;
		this.day = day;
		this.id = id;
		this.linkName = linkName;
		this.link = link;
		this.animeType = animeType;
		this.releaseDate = releaseDate;
		this.finishDate = finishDate;
		this.durationEp = durationEp;
		this.bd = bd;
	}
	
	public String getLinkName()
	{
		return linkName;
	}

	public String getLink()
	{
		return link;
	}

	public String getAnimeType()
	{
		return animeType;
	}

	public String getReleaseDate()
	{
		return releaseDate;
	}

	public String getFinishDate()
	{
		return finishDate;
	}

	public String getCurrentEpisode()
	{
		return currentEpisode;
	}

	public String getTotalEpisode()
	{
		return totalEpisode;
	}

	public String getFansub()
	{
		return fansub;
	}

	public String getNote()
	{
		return note;
	}

	public String getImageName()
	{
		return imageName;
	}
	
	public String getImagePath(String listName)
	{
		String folder = "";
		if (listName.equalsIgnoreCase("anime completati"))
			folder = "Completed";
		else if (listName.equalsIgnoreCase("anime in corso"))
			folder = "Airing";
		else if (listName.equalsIgnoreCase("oav"))
			folder = "Ova";
		else if (listName.equalsIgnoreCase("film"))
			folder = "Film";
		else if (listName.equalsIgnoreCase("completi da vedere"))
			folder = "Completed to See";
		
		return IMAGE_PATH + folder + File.separator + this.getImageName();
	}

	public String getDay()
	{
		return day;
	}
	
	public String getId()
	{
		return id;
	}

	public String getDurationEp()
	{
		return durationEp;
	}
	public boolean getBd()
	{
		return bd;
	}
	public String toString()
	{
		String string = this.getCurrentEpisode() + "||" + this.getTotalEpisode() + "||" + this.getFansub() + "||" + this.getNote() + "||" + this.getImageName() + "||" + this.getDay() + "||" + this.getId() + "||" + this.getLinkName() + "||" + this.getLink() + "||" + this.getAnimeType() + "||" + this.getReleaseDate() + "||" + this.getFinishDate() + "||" + this.getDurationEp() + "||" + this.getBd();
		return string;
	}
}
