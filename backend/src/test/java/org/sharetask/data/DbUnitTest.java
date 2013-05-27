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

import java.io.File;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.internal.SessionImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/template.xml", "classpath:spring/mail.xml", "classpath:spring/transaction.xml",
		"classpath:spring/security.xml", "classpath:spring/applicationConfig.xml" })
public class DbUnitTest {

	@PersistenceContext(name = "entityManagerFactory")
	private EntityManager entityManager;

	@Inject
	private AuthenticationManager authenticationManager;
	
	protected boolean enableSecurity = true;
	
	@Before
	public void init() throws DatabaseUnitException, SQLException, MalformedURLException {
		// insert data into database
		DatabaseOperation.DELETE.execute(getConnection(), getDataSet());
		DatabaseOperation.INSERT.execute(getConnection(), getDataSet());
		// login
		if (this.enableSecurity) {
		    final Authentication authentication = new UsernamePasswordAuthenticationToken("test1@test.com", "password");
	    	final Authentication authenticate = this.authenticationManager.authenticate(authentication);
	    	SecurityContextHolder.getContext().setAuthentication(authenticate);
		}
	}

	@After
	public void after() throws DatabaseUnitException, SQLException, MalformedURLException {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	private IDatabaseConnection getConnection() throws DatabaseUnitException {
		// get connection
		final SessionImpl session = (SessionImpl) this.entityManager.getDelegate();
		final Connection con = session.connection(); // NOPMD
		//DatabaseMetaData databaseMetaData = con.getMetaData();
		final IDatabaseConnection connection = new DatabaseConnection(con);
		final DatabaseConfig config = connection.getConfig();
		config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
		return connection;
	}

	private IDataSet getDataSet() throws MalformedURLException, DataSetException {
		final FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		builder.setColumnSensing(true);
		return builder.build(new File("src/test/resources/dataset.xml"));
	}
	
	protected EntityManager getEntityManager() {
		return this.entityManager;
	}
}
