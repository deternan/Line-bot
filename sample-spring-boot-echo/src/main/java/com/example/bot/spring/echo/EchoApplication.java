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
import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;

@SpringBootApplication
@LineMessageHandler
public class EchoApplication 
{
    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        System.out.println("event: " + event);
        
        String get_return;
		// CJKV check
//		String get_return = CJKV_check(event.getMessage().getText());
//		return new TextMessage(get_return);
        //return new TextMessage("Auto:  "+event.getMessage().getText());
        
        // Digital check
        get_return = Regular_Expression_Digital(event.getMessage().getText());
        return new TextMessage(get_return);
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
	
    private String Regular_Expression_Digital(String temp_str)
    {
    	//Regular_Expression
    	String num_pattern = "[0-9]{4}";    	

    	// Number check
    	Pattern p = Pattern.compile(num_pattern);
    	Matcher  m = p.matcher(temp_str);
        
    	boolean digital_check;
    	if(m.find()){
        	//System.out.println(m.group());
    		digital_check = true;
        }else{
        	digital_check = false;
        }
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
