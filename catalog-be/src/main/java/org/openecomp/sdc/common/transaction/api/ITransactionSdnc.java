/*-
 * ============LICENSE_START=======================================================
 * SDC
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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

package org.openecomp.sdc.common.transaction.api;

import org.openecomp.sdc.be.resources.data.ESArtifactData;
import org.openecomp.sdc.common.transaction.api.TransactionUtils.DBActionCodeEnum;
import org.openecomp.sdc.common.transaction.api.TransactionUtils.DBTypeEnum;
import org.openecomp.sdc.common.transaction.api.TransactionUtils.ESActionTypeEnum;
import org.openecomp.sdc.common.transaction.api.TransactionUtils.TransactionCodeEnum;

import fj.data.Either;

public interface ITransactionSdnc {
    TransactionCodeEnum finishTransaction();

    Either<DBActionCodeEnum, TransactionCodeEnum> invokeESAction(boolean isLastAction, ESActionTypeEnum esActiontype, ESArtifactData artifactData);

    <T> Either<T, TransactionCodeEnum> invokeGeneralDBAction(boolean isLastAction, DBTypeEnum dbType, IDBAction dbAction, IDBAction dbRollbackAction);

    <T> Either<T, TransactionCodeEnum> invokeTitanAction(boolean isLastAction, IDBAction dbAction);

}
