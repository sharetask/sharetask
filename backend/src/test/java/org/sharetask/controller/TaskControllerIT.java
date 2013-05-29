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

import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
@Slf4j
public class TaskControllerIT extends IntegrationTest {

	private static final String TASK_PATH = "/workspace/1/task";
    private static final String URL_TASK = BASE_URL + TASK_PATH;
 
    @Test
    public void testAddComment() throws IOException {
        //given
        final HttpPost httpPost = new HttpPost(URL_TASK + "/1/comment");
        httpPost.addHeader(new BasicHeader("Content-Type", "application/json"));
        final StringEntity httpEntity = new StringEntity("{\"comment\":\"test comment\"}");
        httpPost.setEntity(httpEntity);
        
        //when
        final HttpResponse response = getClient().execute(httpPost);
 
        //then
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetComments() throws IOException {
        //given
        final HttpGet httpGet = new HttpGet(URL_TASK + "/1/comment");
        httpGet.addHeader(new BasicHeader("Content-Type", "application/json"));
        
        //when
        final HttpResponse response = getClient().execute(httpGet);
 
        //then
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
        final String responseData = EntityUtils.toString(response.getEntity());
        Assert.assertTrue(responseData.contains("\"message\":\"Vivamus diam "));
    }
    
    @Test
    public void testGetEvents() throws IOException {
        //given
        final HttpGet httpGet = new HttpGet(URL_TASK + "/1/event");
        httpGet.addHeader(new BasicHeader("Content-Type", "application/json"));
        
        //when
        final HttpResponse response = getClient().execute(httpGet);
 
        //then
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
        final String responseData = EntityUtils.toString(response.getEntity());
        Assert.assertTrue(responseData.contains("\"message\":\"TASK_CREATED\""));
    }
    
    @Test
    public void testCreateTask() throws IOException {
        //given
        final HttpPost httpPost = new HttpPost(URL_TASK );
        httpPost.addHeader(new BasicHeader("Content-Type", "application/json"));
        final StringEntity httpEntity = new StringEntity("{\"title\":\"TestTask\"," +
        		                                     "\"priority\":\"MEDIUM\"," +
        		                                     "\"dueDate\":\"2013-01-01T01:10:53Z\"}");
        httpPost.setEntity(httpEntity);
        
        //when
        final HttpResponse response = getClient().execute(httpPost);
 
        //then
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
    }
    
    @Test
    public void testCompleteTask() throws IOException {
        //given
        final HttpPost httpPost = new HttpPost(URL_TASK + "/1/complete");
        httpPost.addHeader(new BasicHeader("Content-Type", "application/json"));
        
        //when
        final HttpResponse response = getClient().execute(httpPost);
 
        //then
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
    }
    
    @Test
    public void testDelete() throws IOException {
        //given
    	final HttpDelete httpDelete = new HttpDelete(URL_TASK + "/4");
        httpDelete.addHeader(new BasicHeader("Content-Type", "application/json"));
        
        //when
        final HttpResponse response = getClient().execute(httpDelete);
 
        //then
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
    }
}
