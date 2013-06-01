/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.sharetask.data;

import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.http.HttpStatus;


/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class IntegrationTest {

	public static final String SCHEMA = "http";
	public static final String HOST = "localhost:8088";
	public static final String BASE_PATH = "/sharetask/api";
	
	public static final String BASE_URL = SCHEMA + "://" + HOST + BASE_PATH;
	
	private static String SESSIONID;
	private static String DOMAIN;
	
	private DefaultHttpClient client;

	@BeforeClass
	public static void login() throws Exception {
		final DefaultHttpClient client = new DefaultHttpClient();
        final HttpPost httpPost = new HttpPost(BASE_URL + "/user/login");
        httpPost.addHeader(new BasicHeader("Content-Type", "application/json"));
        final StringEntity httpEntity = new StringEntity("{\"username\":\"dev1@shareta.sk\"," +
        		"\"password\":\"password\"}");
        System.out.println(EntityUtils.toString(httpEntity));
        httpPost.setEntity(httpEntity);
        
        //when
        final HttpResponse response = client.execute(httpPost);

        //then
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
        client.getCookieStore().getCookies();
        for (final Cookie cookie : client.getCookieStore().getCookies()) {
			if (cookie.getName().equals("JSESSIONID")) {
				DOMAIN = cookie.getDomain();
				SESSIONID = cookie.getValue();
			}
		}
	}
	
	@BeforeClass
	public static void showInfo() {
    	// show timezone
        System.out.println("******************************************************************************");  
        System.out.println("Default time zone name: " + TimeZone.getDefault().getDisplayName());  
        System.out.println("Default time zone id: " + TimeZone.getDefault().getID());  
        System.out.println("******************************************************************************");  
	}
	
	@Before
	public void setCookie() {
		this.client = new DefaultHttpClient();
		final BasicClientCookie basicClientCookie = new BasicClientCookie("JSESSIONID", SESSIONID);
		basicClientCookie.setDomain(DOMAIN);
		this.client.getCookieStore().addCookie(basicClientCookie);
	}
	
	public HttpClient getClient() {
		return this.client;
	}
}
