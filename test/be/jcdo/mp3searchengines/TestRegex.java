package be.jcdo.mp3searchengines;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex
{
	public static void main(String[] args)
	{
		String url = "http://breakbeat.wrzuta.pl/audio/005bHvFxqIb/sigurt_-_colourful_indivision_feat_jett_remix";
		
		Pattern pattern = Pattern.compile("(audio/)([\\w]+)(/)");
        Matcher matcher = pattern.matcher(url);

        if(matcher.find()) {
        	String match = matcher.group();
        	int end = match.length();
        	
        	String wrzutaID = match.substring(6, end-1);
        	System.out.println(wrzutaID);
        }
	}
}
