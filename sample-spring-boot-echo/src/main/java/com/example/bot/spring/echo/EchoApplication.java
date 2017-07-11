/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 * 
 * Last updated: July 10, 2017 09:24 PM
 * 
 */

package com.example.bot.spring.echo;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@SpringBootApplication
@LineMessageHandler
public class EchoApplication 
{
	boolean digital_check;
	boolean character_check;
	// Pattern expression
	private static Pattern p;
	private static Matcher m;
	private static String space_pattern = "^[0-9]";
	// Stock info
//	private Vector code = new Vector();
//	private Vector name = new Vector();
	Vector<String> code = new Vector<String>();
	Vector<String> name = new Vector<String>();
	
    public static void main(String[] args) 
    {    	
    	SpringApplication.run(EchoApplication.class, args);
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) 
    {
    	Read_Taiwan_StockID();
    	
    	System.out.println("event: " + event);
        
        String get_return;
		// CJKV check
//		String get_return = CJKV_check(event.getMessage().getText());
//		return new TextMessage(get_return);
        //return new TextMessage("Auto:  "+event.getMessage().getText());
        
        // Digital check
        //get_return = Regular_Expression_Digital(event.getMessage().getText());
        digital_check = Regular_Expression_Digital(event.getMessage().getText());
        if(digital_check == true){
        	if(event.getMessage().getText().length() == 4){
        		get_return = "4 digital";
        		GetStockInfor(get_return);
        	}else{
        		get_return = "Please input 4 digital code or Stock name";
        	}        	
        }else{
        	get_return = "illegal";
        }
        
        //return new TextMessage(get_return);
        return new TextMessage(code.size()+"	"+name.size());        
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
	
    private boolean Regular_Expression_Digital(String input_str)
    {
    	//Regular_Expression
    	String num_pattern = "[0-9]{4}";    	

    	// Number check
    	Pattern p = Pattern.compile(num_pattern);
    	Matcher  m = p.matcher(input_str);
        
    	boolean digital_temp;
    	if(m.find()){        	
    		digital_temp = true;
        }else{
        	digital_temp = false;
        }
    	
    	return digital_temp;
    }
    
//    private boolean Regular_Expression_Chines(String input_str)
//    {
//    	boolean check;
//		String return_str = "";
//		check = input_str.codePoints().anyMatch(codepoint ->
//	            Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN);
//		
//		if(check == true){
//			//return_str = "Non English";
//			check = true;
//		}else{
//			//return_str = "English";
//			check = false;
//		}		
//		
//		return check;
//    }
    
    private void GetStockInfor(String input)
    {
    	
    }
    
    private void Read_Taiwan_StockID() throws Exception
	{		
		String url = "http://isin.twse.com.tw/isin/C_public.jsp?strMode=2";
		// JSoup Example 2 - Reading HTML page from URL
		Document doc = Jsoup.connect(url).timeout(5000).get();
		doc.outputSettings().charset("UTF-8");
		
		// body
		Elements iframe_ele = doc.select("body");
		if((iframe_ele.size() > 0)){
			//System.out.println(iframe_ele);
		}
		
		String temp_str;
		
		// div embed
		Elements div_embed_ele = doc.select("tr");
		if ((div_embed_ele.size() > 0)) {
			//System.out.println(div_embed_ele);
		}
		//System.out.println(div_embed_ele.size());
		
		boolean dig_check;
		for(int i=0; i<div_embed_ele.size(); i++)
		{
			// childNode()
			if(div_embed_ele.get(i).childNodeSize() == 7){
				//System.out.println(i+"	"+div_embed_ele.get(i).child(0));
				temp_str = div_embed_ele.get(i).child(0).toString().substring(22, div_embed_ele.get(i).child(0).toString().indexOf("</td>"));
				dig_check = Pattern_expression_digital(temp_str);
				if(dig_check == true){
					Separation(temp_str);
				}			
			}	
		}
	}

    private static boolean Pattern_expression_digital(String input)
	{
		boolean check = false;
		String temp;
		if(input.trim().length() < 5){
			temp = input.trim();
		}else{
			temp = input.substring(4, 5).trim();
		}
		//System.out.println(temp);
        p = Pattern.compile(space_pattern);
        m = p.matcher(temp);
        if(m.find()){
        	//System.out.println("Not Digital");
        	check = false;
        }else{
        	check = true;
        }
		
		return check;
	}
    
    private void Separation(String input)
	{
		String code_temp = input.substring(0, 4);
		String name_temp = input.substring(5, input.length());
		
		code.add(code_temp);
		name.add(name_temp);
	}
    
//	private String CJKV_check(String input_str)
//	{
//		boolean check;
//		String return_str = "";
//		check = input_str.codePoints().anyMatch(codepoint ->
//	            Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN);
//		
//		//System.out.println(check);
//		if(check == true){
//			return_str = "Non English";
//		}else{
//			return_str = "English";
//		}
//		
//		return return_str;
//	}
}
