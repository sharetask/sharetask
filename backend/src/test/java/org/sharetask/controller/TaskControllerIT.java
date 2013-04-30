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

import org.apache.http.HttpResponse;
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
public class TaskControllerIT extends IntegrationTest {

    private static final String URL_WORKSPACE = BASE_URL + "/workspace/1/task";
 
    @Test
    public void testAddComment() throws IOException {
        //given
        HttpPost httpPost = new HttpPost(URL_WORKSPACE + "/1/comment");
        httpPost.addHeader(new BasicHeader("Content-Type", "application/json"));
        StringEntity httpEntity = new StringEntity("{\"comment\":\"test comment\"}");
        System.out.println(EntityUtils.toString(httpEntity));
        httpPost.setEntity(httpEntity);
        
        //when
        HttpResponse response = getClient().execute(httpPost);
 
        //then
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
        httpEntity.getContent().close();
    }

    @Test
    public void testCreateTask() throws IOException {
        //given
        HttpPost httpPost = new HttpPost(URL_WORKSPACE );
        httpPost.addHeader(new BasicHeader("Content-Type", "application/json"));
        StringEntity httpEntity = new StringEntity("{\"title\":\"TestTask\"," +
        		                                     "\"priority\":\"MEDIUM\"," +
        		                                     "\"dueDate\":\"2013-01-01\"}");
        httpPost.setEntity(httpEntity);
        
        //when
        HttpResponse response = getClient().execute(httpPost);
 
        //then
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
    }
}
