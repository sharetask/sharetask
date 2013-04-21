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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class WorkspaceControllerIT {

    private static final String URL_WORKSPACE = "http://localhost:8088/sharetask/api/workspace";
    
    @Test
    public void testFindWorkspaceByOwner() throws IOException {
        //given
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL_WORKSPACE);
		
        //when
        HttpResponse response = client.execute(httpGet);
 
        //then
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
        String responseData = EntityUtils.toString(response.getEntity());
        Assert.assertTrue(responseData.contains("\"title\":\"Workspace\""));
    }

    @Test
    public void testAddMemeber() throws IOException {
        //given
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL_WORKSPACE + "/1/member");
        httpPost.addHeader(new BasicHeader("Content-Type", "application/json"));
        StringEntity httpEntity = new StringEntity("{\"userId\":2}");
        System.out.println(EntityUtils.toString(httpEntity));
        httpPost.setEntity(httpEntity);
        
        //when
        HttpResponse response = client.execute(httpPost);
 
        //then
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
    }
    
    @Test
    public void testRemoveMemeber() throws IOException {
        //given
        HttpClient client = new DefaultHttpClient();
        HttpDelete httpDelete = new HttpDelete(URL_WORKSPACE + "/1/member/1");
        httpDelete.addHeader(new BasicHeader("Content-Type", "application/json"));
        
        //when
        HttpResponse response = client.execute(httpDelete);
 
        //then
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
    }
}
