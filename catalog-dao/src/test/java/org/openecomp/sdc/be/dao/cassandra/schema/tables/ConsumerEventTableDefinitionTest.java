/*-
 * ============LICENSE_START=======================================================
 * SDC
 * ================================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.openecomp.sdc.be.dao.cassandra.schema.tables;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;

import com.datastax.driver.core.DataType;


public class ConsumerEventTableDefinitionTest {

	private ConsumerEventTableDefinition createTestSubject() {
		return new ConsumerEventTableDefinition();
	}

	
	@Test
	public void testPrimaryKeys() throws Exception {
		ConsumerEventTableDefinition testSubject;
		List<ImmutablePair<String, DataType>> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.primaryKeys();
	}

	
	@Test
	public void testClusteringKeys() throws Exception {
		ConsumerEventTableDefinition testSubject;
		List<ImmutablePair<String, DataType>> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.clusteringKeys();
	}

	
	@Test
	public void testGetColumnDescription() throws Exception {
		ConsumerEventTableDefinition testSubject;
		Map<String, ImmutablePair<DataType, Boolean>> result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getColumnDescription();
	}

	
	@Test
	public void testGetKeyspace() throws Exception {
		ConsumerEventTableDefinition testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getKeyspace();
	}

	
	@Test
	public void testGetTableName() throws Exception {
		ConsumerEventTableDefinition testSubject;
		String result;

		// default test
		testSubject = createTestSubject();
		result = testSubject.getTableName();
	}
}
