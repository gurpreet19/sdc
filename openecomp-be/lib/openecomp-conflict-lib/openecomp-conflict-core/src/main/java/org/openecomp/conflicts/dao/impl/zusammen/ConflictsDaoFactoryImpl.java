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

package org.openecomp.conflicts.dao.impl.zusammen;


import org.openecomp.conflicts.dao.ConflictsDao;
import org.openecomp.conflicts.dao.ConflictsDaoFactory;
import org.openecomp.core.zusammen.api.ZusammenAdaptorFactory;

public class ConflictsDaoFactoryImpl extends ConflictsDaoFactory {

  private static final ConflictsDao INSTANCE = new
      ConflictsDaoImpl(ZusammenAdaptorFactory.getInstance().createInterface());

  @Override
  public ConflictsDao createInterface() {
    return INSTANCE;
  }
}
