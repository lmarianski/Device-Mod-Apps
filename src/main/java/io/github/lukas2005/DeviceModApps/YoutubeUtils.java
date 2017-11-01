package io.github.lukas2005.DeviceModApps;

import java.io.IOException;
import java.math.BigInteger;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.Thumbnail;

public class YoutubeUtils {

	private final static String API_KEY = "AIzaSyDR7ghkuYPkCiCuVHddGaXPVUtc9olTHBw"; //Shh! Don't tell anyone! :P
	
    public static YouTube youtube = new YouTube.Builder(
            new NetHttpTransport(),
            new JacksonFactory(),
            request -> {
    }).setApplicationName("lukas2005's Device Mod Apps").setYouTubeRequestInitializer(new YouTubeRequestInitializer(API_KEY)).build();
	
	public static BigInteger getSubscriberCount(String channelId) throws IOException {
		YouTube.Channels.List channels = youtube.channels().list("snippet, statistics");
		channels.setId(channelId);

		ChannelListResponse channelResponse = channels.execute();
		
		return channelResponse.getItems().get(0).getStatistics().getSubscriberCount();
	}
	
	public static String getChannelIcon(String channelId) throws IOException {
		YouTube.Channels.List channels = youtube.channels().list("snippet, statistics");
		channels.setId(channelId);

		ChannelListResponse channelResponse = channels.execute();
		
		Thumbnail thumbnail = (Thumbnail) channelResponse.getItems().get(0).getSnippet().getThumbnails().get("default");

		return thumbnail.getUrl();
	}
	
}
