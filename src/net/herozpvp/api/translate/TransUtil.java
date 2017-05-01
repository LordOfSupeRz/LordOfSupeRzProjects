package net.herozpvp.api.translate;
	
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransUtil {

    public static String readURL(String url) {
        String response = "";
        try {
            URL toRead = new URL(url);
            URLConnection yc = toRead.openConnection();
   
            yc.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(yc
                    .getInputStream(), "UTF-8"));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response = response + inputLine;
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @SuppressWarnings("deprecation")
	public static String getTranslation(String text, String lang) {
        HashMap<String, String> hm = new HashMap<String, String>();
        Pattern p = Pattern
                .compile("(?i)\\b((?:https?://|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:'\".,<>?������]))");
        Matcher m = p.matcher(text);
        StringBuffer sb = new StringBuffer();
        String urlTmp = "";
        
        while (m.find()) {
            urlTmp = m.group(1);
            String uuid = UUID.randomUUID().toString().replace("-", "");
            hm.put(uuid, urlTmp);
            text = text.replace(urlTmp, uuid);
            m.appendReplacement(sb, "");
            sb.append(urlTmp);
        }
        m.appendTail(sb);
        text = sb.toString();
       
        text = URLEncoder.encode(text);
        String response = readURL("http://translate.google.com/translate_a/t?q=" + text + "&client=p&text=&sl=auto&tl=" + lang + "&ie=UTF-8&oe=UTF-8");
        response = parse(response);

     
        Set<Map.Entry<String,String>> set = hm.entrySet();
        for (Map.Entry<String, String> me : set) {
            response = response.replace(me.getKey(), me.getValue());
        }
    
        response = postProcess(response, lang);
        return response;
    }

    public static String postProcess(String response, String lang) {
      
        response = response.replace(" :", ":");
        response = response.replace(" ,", ",");
        response = response.replace(". / ", "./");

        if (response.startsWith("\u00BF") && StringUtils.countMatches(response, "?") == 0) {
            response = response + "?";
        }
        if (response.startsWith("\u00A1") && StringUtils.countMatches(response, "!") == 0) {
            response = response + "!";
        }
        if (lang.equals("en") && response.startsWith("'re")) {
            response = "You" + response;
        }
        return response;
    }

    public static String parse(String response) {
        JSONParser parser = new JSONParser();
        JSONObject obj = new JSONObject();
        try {
            obj = (JSONObject) parser.parse(response);
        } catch (ParseException ignored) {
        }
        JSONArray sentences = (JSONArray) obj.get("sentences");
        String finalResponse = "";
        for (Object sentence : sentences) {
            String line = "" + sentence;
            String trans = getTrans(line);
            finalResponse = finalResponse + trans;
        }
        return finalResponse;
    }

    public static String getTrans(String sentence) {
        JSONParser parser = new JSONParser();
        JSONObject obj = new JSONObject();
        try {
            obj = (JSONObject) parser.parse(sentence);
        } catch (ParseException ignored) {
        }
        sentence = (String) obj.get("trans");
        return sentence;
    }

}
