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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.subethamail.wiser.Wiser;


/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class ServiceUnitTest extends DbUnitTest {

	private static Wiser testSmtp;

    @BeforeClass
    public static void testSmtpInit() {
    	testSmtp = new Wiser();
    	testSmtp.setPort(2500);
        testSmtp.start();
    }

    @AfterClass
    public static void cleanup() {
        testSmtp.stop();
    }

    public Wiser getTestSmtp() {
		return testSmtp;
	}
}
