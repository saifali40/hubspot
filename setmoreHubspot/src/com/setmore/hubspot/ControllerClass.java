package com.setmore.hubspot;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Controller
@RequestMapping("")
public class ControllerClass {
	
	
	
	@RequestMapping("/OAuth")
	public ModelAndView test2() {
		return new ModelAndView("redirect:https://app.hubspot.com/oauth/authorize?client_id="+Constants.ClientID+"&"
				+ "scope=contacts&"
				+ "redirect_uri="+Constants.SAVE);
	}
	
	
	
	@RequestMapping("/read")
	@ResponseBody
	public String read(@RequestParam(value = "code",required=false) String code, HttpServletRequest req,
			HttpServletResponse resp,HttpSession session) throws IOException,NullPointerException, Exception{
		
		String url 				= 	"https://api.hubapi.com/oauth/v1/token?";
		URL obj 				= 	new URL(url);
		HttpURLConnection con 	= 	(HttpURLConnection) obj.openConnection();
		
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setConnectTimeout(60000);
		con.setReadTimeout(60000);
		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
		
		String urlParameters = "grant_type=authorization_code"
				+ "&client_id="+Constants.ClientID
				+ "&client_secret="+Constants.ClientS
				+ "&redirect_uri="+Constants.READ
				+ "&code="+code;
		
		OutputStreamWriter writers = new OutputStreamWriter(con.getOutputStream());
		writers.write(urlParameters);
		writers.flush();

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		String response = "";
		while ((inputLine = in.readLine()) != null) {
			response += inputLine;
		}
		in.close();
		JSONParser parser = new JSONParser();
		JSONObject json_access_token = null;
		json_access_token = (JSONObject) parser.parse(response);
		String access_token = (String) json_access_token.get("access_token");
		
		URL obj1 = new URL("https://api.hubapi.com/contacts/v1/lists/all/contacts/all?");
		HttpURLConnection conn = (HttpURLConnection) obj1.openConnection();
		
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Authorization","Bearer "+access_token);
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
		BufferedReader in1 = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine1;
		String responsee = "";
		while ((inputLine1 = in1.readLine()) != null) {
			responsee += inputLine1;
		}
		in1.close();

		return responsee;
	}
	
	
	@RequestMapping("/save")
	@ResponseBody
	public String save(@RequestParam(value = "code",required=false) String code, HttpServletRequest req,
			HttpServletResponse resp,HttpSession session) throws IOException,NullPointerException, Exception{
		
		String url 				= 	"https://api.hubapi.com/oauth/v1/token?";
		URL obj 				= 	new URL(url);
		HttpURLConnection con 	= 	(HttpURLConnection) obj.openConnection();
		
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setConnectTimeout(60000);
		con.setReadTimeout(60000);
		con.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
		
		String urlParameters = "grant_type=authorization_code"
				+ "&client_id="+Constants.ClientID
				+ "&client_secret="+Constants.ClientS
				+ "&redirect_uri="+Constants.SAVE
				+ "&code="+code;
		
		OutputStreamWriter writers = new OutputStreamWriter(con.getOutputStream());
		writers.write(urlParameters);
		writers.flush();

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		String response = "";
		while ((inputLine = in.readLine()) != null) {
			response += inputLine;
		}
		in.close();
		JSONParser parser = new JSONParser();
		JSONObject json_access_token = null;
		json_access_token = (JSONObject) parser.parse(response);
		String access_token = (String) json_access_token.get("access_token");
		

		URL obj1 = new URL("https://api.hubapi.com/contacts/v1/contact/?");
		HttpURLConnection conn = (HttpURLConnection) obj1.openConnection();
		conn.setRequestProperty("Authorization","Bearer "+access_token);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestMethod("POST");
		
		JSONObject out=new JSONObject();
		JSONObject in1=new JSONObject();
		
		JSONArray arr = new JSONArray();  
		
		Map mapA = new HashMap();

		mapA.put("property", "email");
		mapA.put("value", "testingapis@hubspot.com");
		
		in1.putAll(mapA);
		arr.put(in1);
		out.put("properties",arr);
		
		System.out.print(out);    
        
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(out.toString());
		wr.flush();
		
		

		return conn.getResponseMessage()+" --- () --- " + access_token;
	}
	
	
	
	@RequestMapping("/test")
	@ResponseBody
	public String test() throws ParseException, JSONException,ClassCastException{
		
		
		JSONObject out=new JSONObject();
		JSONObject in=new JSONObject();
		
		JSONArray arr = new JSONArray();  
		
		Map mapA = new HashMap();

		mapA.put("property", "email");
		mapA.put("value", "testingapis@hubspot.com");
		
		in.putAll(mapA);
		arr.put(in);
		out.put("properties",arr);
		
		System.out.print(out);    
        return out.toString();
	}
	
}
