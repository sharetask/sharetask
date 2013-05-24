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
package org.sharetask.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.sharetask.data.IntegrationTest;
import org.springframework.http.HttpStatus;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class WorkspaceControllerIT extends IntegrationTest {

    private static final String WORKSPACE_PATH = "/workspace";
    private static final String URL_WORKSPACE = BASE_URL + WORKSPACE_PATH;
    
    @Test
    public void testFindWorkspaceByOwner() throws IOException, URISyntaxException {
    	URIBuilder builder = new URIBuilder();
    	builder.setScheme(SCHEMA).setHost(HOST).setPath(BASE_PATH + WORKSPACE_PATH)
    	    .setParameter("type", "OWNER");
    	URI uri = builder.build();
        //given
        HttpGet httpGet = new HttpGet(uri);
		
        //when
        HttpResponse response = getClient().execute(httpGet);
 
        //then
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
        String responseData = EntityUtils.toString(response.getEntity());
        Assert.assertTrue(responseData.contains("\"title\":\"ABX Agency\""));
    }

    @Test
    public void testAddMember() throws IOException {
        //given
        HttpPost httpPost = new HttpPost(URL_WORKSPACE + "/1/member");
        httpPost.addHeader(new BasicHeader("Content-Type", "application/json"));
        StringEntity httpEntity = new StringEntity("{\"username\":\"dev3@shareta.sk\"}");
        httpPost.setEntity(httpEntity);
        
        //when
        HttpResponse response = getClient().execute(httpPost);

        //then
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
    }

    @Test
    public void testUpdateWorkspace() throws IOException {
        //given
    	HttpPut httpPut = new HttpPut(URL_WORKSPACE);
        httpPut.addHeader(new BasicHeader("Content-Type", "application/json"));
        StringEntity httpEntity = new StringEntity("{\"id\":1," +
                                                    "\"title\":\"Test Title\"," +
                                                    "\"owner\":{\"username\":\"dev1@shareta.sk\"}" +
                                                    "}");
        httpPut.setEntity(httpEntity);
        
        //when
        HttpResponse response = getClient().execute(httpPut);

        //then
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
        String responseData = EntityUtils.toString(response.getEntity());
        Assert.assertTrue(responseData.contains("\"title\":\"Test Title\""));
    }
    
    
    @Test
    public void testDelete() throws IOException {
        //given
    	HttpDelete httpDelete = new HttpDelete(URL_WORKSPACE + "/2");
        httpDelete.addHeader(new BasicHeader("Content-Type", "application/json"));
        
        //when
        HttpResponse response = getClient().execute(httpDelete);
 
        //then
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
    }
}