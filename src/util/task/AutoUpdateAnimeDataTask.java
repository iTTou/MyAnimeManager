package util.task;

import java.io.File;
import java.util.TreeMap;

import javax.swing.SwingWorker;

import org.json.JSONObject;

import main.AnimeIndex;
import util.AnimeData;
import util.ConnectionManager;
import util.FileManager;
import util.window.AnimeInformation;

public class AutoUpdateAnimeDataTask extends SwingWorker
{

	@Override
	protected Object doInBackground() throws Exception
	{
		if (AnimeIndex.appProp.getProperty("Update_system").equalsIgnoreCase("true"))
		{
			System.out.println("prova");
			ConnectionManager.ConnectAndGetToken();
			String nome = "";
			if (AnimeIndex.filtro != 9)
			{
				nome = (String) AnimeIndex.filterList.getSelectedValue();
				AnimeIndex.getJList().setSelectedValue(nome, true);
			}
			if (!AnimeIndex.searchBar.getText().isEmpty())
			{
				nome = (String) AnimeIndex.searchList.getSelectedValue();
				AnimeIndex.getJList().setSelectedValue(nome, true);
			}
			String name = (String) AnimeIndex.getJList().getSelectedValue();
			TreeMap<String, AnimeData> map = AnimeIndex.getMap();
			AnimeData oldData = map.get(name);
			
			if (!AnimeIndex.exclusionAnime.containsKey(name))
			{				
				int id = Integer.parseInt(oldData.getId());
				JSONObject data = ConnectionManager.getAnimeInformation(id);
				
				String totalEp = Integer.toString(data.getInt("total_episodes"));
				if(totalEp.equals("null")||totalEp.equals("0"))
					totalEp = "??";
				String duration = Integer.toString(data.getInt("duration"));
				if(duration.equals("null")||duration.equals("0"))
					duration = "?? min";
				else
					duration += " min";
				String startDate = data.getString("start_date");
				if (startDate.equals("null"))
					startDate = "??/??/????";
				else if(startDate.length()==4)
					startDate = "??/??/" + startDate;
				else if(startDate.length()==7)
				{
					String monthStart = startDate.substring(5, 7);
					String yearStart = startDate.substring(0, 4);
					startDate = "??/" + monthStart + "/" + yearStart;
				}
				else if (startDate.length() > 7)
				{
					String dayStart = startDate.substring(8, 10);
					String monthStart = startDate.substring(5, 7);
					String yearStart = startDate.substring(0, 4);
					startDate = dayStart + "/" + monthStart + "/" + yearStart;
				}
				
				String finishDate;
				if (totalEp.equals("1"))
					finishDate = startDate;
				else
				{
					finishDate = data.getString("end_date");
					if (finishDate.equals("null"))
						finishDate = "??/??/????";
					else if(finishDate.length()==4)
						finishDate = "??/??/" + finishDate;
					else if(finishDate.length()==7)
					{
						String monthEnd = finishDate.substring(5, 7);
						String yearEnd = finishDate.substring(0, 4);
						finishDate = "??/" + monthEnd + "/" + yearEnd;
					}
					else if (finishDate.length() > 7)
					{
						String dayEnd = finishDate.substring(8, 10);
						String monthEnd= finishDate.substring(5, 7);
						String yearEnd = finishDate.substring(0, 4);
						finishDate = dayEnd + "/" + monthEnd + "/" + yearEnd;
					}
				}
				
				String type = (String)AnimeIndex.animeInformation.typeComboBox.getSelectedItem();
				if(!type.equals("Blu-ray"))
					type = data.getString("type");
				
				String imageLink = data.getString("image_url_lge");
				imageLink = imageLink.replaceAll("\\\\/", "/");
				String imageName = name.replaceAll("\\\\", "_");
				imageName = imageName.replaceAll("/", "_");
				imageName = imageName.replaceAll(":", "_");
				imageName = imageName.replaceAll("\\*", "_");
				imageName = imageName.replaceAll("\\?", "_");
				imageName = imageName.replaceAll("\"", "_");
				imageName = imageName.replaceAll(">", "_");
				imageName = imageName.replaceAll("<", "_");
				String list = AnimeIndex.getList();
				if (list.equalsIgnoreCase("anime completati"))
					FileManager.saveImage(imageLink, imageName, "Completed");
				if (list.equalsIgnoreCase("anime in corso"))
					FileManager.saveImage(imageLink, imageName, "Airing");
				if (list.equalsIgnoreCase("oav"))
					FileManager.saveImage(imageLink, imageName, "Ova");
				if (list.equalsIgnoreCase("film"))
					FileManager.saveImage(imageLink, imageName, "Film");
				if (list.equalsIgnoreCase("completi da vedere"))
					FileManager.saveImage(imageLink, imageName, "Completed to See");
				
				String path = oldData.getImagePath(list);
				File imgFile = new File(path);
				if(imgFile.exists())
					AnimeIndex.animeInformation.setImage(path);
				else
					AnimeIndex.animeInformation.setImage("default");
				
				AnimeData newData = new AnimeData(oldData.getCurrentEpisode(), totalEp, oldData.getFansub(), oldData.getNote(),
												  oldData.getImageName(), oldData.getDay(), oldData.getId(), oldData.getLinkName(),
												  oldData.getLink(), type, startDate, finishDate, duration, oldData.getBd());
				
				map.put(name, newData);
			}
			else if (AnimeIndex.exclusionAnime.containsKey(name))
			{
				String totalEp = oldData.getTotalEpisode();
				String duration = oldData.getDurationEp();
				String startDate = oldData.getReleaseDate();
				String finishDate = oldData.getFinishDate();
				String type = oldData.getAnimeType();
				int id = Integer.parseInt(oldData.getId());
				
				boolean[] exclusionArray = AnimeIndex.exclusionAnime.get(name);
				JSONObject dataAni = ConnectionManager.getAnimeInformation(id);
				
				if(exclusionArray[1]==false)
				{
					totalEp = Integer.toString(dataAni.getInt("total_episodes"));
					if(totalEp.equals("null")||totalEp.equals("0"))
						totalEp = "??";
				}
				
				if(exclusionArray[2]==false)
				{
					 duration = Integer.toString(dataAni.getInt("duration"));
					if(duration.equals("null")||duration.equals("0"))
						duration = "?? min";
					else
						duration += " min";
				}
				
				if (exclusionArray[3]==false)
				{
					startDate = dataAni.getString("start_date");
					
					if (startDate.equals("null"))
						startDate = "??/??/????";
					else if(startDate.length()==4)
						startDate = "??/??/" + startDate;
					else if(startDate.length()==7)
					{
						String monthStart = startDate.substring(5, 7);
						String yearStart = startDate.substring(0, 4);
						startDate = "??/" + monthStart + "/" + yearStart;
					}
					else if (startDate.length() > 7)
					{
						String dayStart = startDate.substring(8, 10);
						String monthStart = startDate.substring(5, 7);
						String yearStart = startDate.substring(0, 4);
						startDate = dayStart + "/" + monthStart + "/" + yearStart;
					}
				}
				
				if(exclusionArray[4]==false)
				{
					if (totalEp.equals("1"))
					{
						if (exclusionArray[3]==false)
							finishDate = startDate;
						else
						{
							startDate = dataAni.getString("start_date");
							
							if (startDate.equals("null"))
								startDate = "??/??/????";
							else if(startDate.length()==4)
								startDate = "??/??/" + startDate;
							else if(startDate.length()==7)
							{
								String monthStart = startDate.substring(5, 7);
								String yearStart = startDate.substring(0, 4);
								startDate = "??/" + monthStart + "/" + yearStart;
							}
							else if (startDate.length() > 7)
							{
								String dayStart = startDate.substring(8, 10);
								String monthStart = startDate.substring(5, 7);
								String yearStart = startDate.substring(0, 4);
								startDate = dayStart + "/" + monthStart + "/" + yearStart;
							}
							finishDate = startDate;
						}
					}
					else
					{
						finishDate = dataAni.getString("end_date");
						
						if (finishDate.equals("null"))
							finishDate = "??/??/????";
						else if(finishDate.length()==4)
							finishDate = "??/??/" + finishDate;
						else if(finishDate.length()==7)
						{
							String monthEnd = finishDate.substring(5, 7);
							String yearEnd = finishDate.substring(0, 4);
							finishDate = "??/" + monthEnd + "/" + yearEnd;
						}
						else if (finishDate.length() > 7)
						{
							String dayEnd = finishDate.substring(8, 10);
							String monthEnd= finishDate.substring(5, 7);
							String yearEnd = finishDate.substring(0, 4);
							finishDate = dayEnd + "/" + monthEnd + "/" + yearEnd;
						}
					}
				}	
				
				if(exclusionArray[5]==false && !type.equalsIgnoreCase("Blu-ray"))
				{
					type = dataAni.getString("type");
				}

				if (exclusionArray[0]==false)
				{
					String imageLink = dataAni.getString("image_url_lge");
					imageLink = imageLink.replaceAll("\\\\/", "/");
					String imageName = name.replaceAll("\\\\", "_");
					imageName = imageName.replaceAll("/", "_");
					imageName = imageName.replaceAll(":", "_");
					imageName = imageName.replaceAll("\\*", "_");
					imageName = imageName.replaceAll("\\?", "_");
					imageName = imageName.replaceAll("\"", "_");
					imageName = imageName.replaceAll(">", "_");
					imageName = imageName.replaceAll("<", "_");
					String list = AnimeIndex.getList();
					if (list.equalsIgnoreCase("anime completati"))
						FileManager.saveImage(imageLink, imageName, "Completed");
					if (list.equalsIgnoreCase("anime in corso"))
						FileManager.saveImage(imageLink, imageName, "Airing");
					if (list.equalsIgnoreCase("oav"))
						FileManager.saveImage(imageLink, imageName, "Ova");
					if (list.equalsIgnoreCase("film"))
						FileManager.saveImage(imageLink, imageName, "Film");
					if (list.equalsIgnoreCase("completi da vedere"))
						FileManager.saveImage(imageLink, imageName, "Completed to See");
					
					String path = oldData.getImagePath(list);
					File imgFile = new File(path);
					if(imgFile.exists())
						AnimeIndex.animeInformation.setImage(path);
					else
						AnimeIndex.animeInformation.setImage("default");
				}
				AnimeData newData = new AnimeData(oldData.getCurrentEpisode(), totalEp, oldData.getFansub(), 
					    oldData.getNote(), oldData.getImageName(), oldData.getDay(), oldData.getId(),
						oldData.getLinkName(), oldData.getLink(), type, startDate, 
						finishDate, duration, oldData.getBd());
				map.put(name, newData);
				
			}
			AnimeIndex.animeInformation.totalEpisodeText.setText(map.get(name).getTotalEpisode());
			AnimeIndex.animeInformation.durationField.setText(map.get(name).getDurationEp());
			AnimeIndex.animeInformation.releaseDateField.setText(map.get(name).getReleaseDate());
			AnimeIndex.animeInformation.finishDateField.setText(map.get(name).getFinishDate());
			AnimeIndex.animeInformation.typeComboBox.setSelectedItem(map.get(name).getAnimeType());	

			AnimeInformation.dial.dispose();
		}
		return null;
	}
}
