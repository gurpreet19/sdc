/*!
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import { connect } from 'react-redux';
import SoftwareProductComponentsGeneralView from './SoftwareProductComponentsGeneralView.jsx';
import SoftwareProductComponentsActionHelper from '../SoftwareProductComponentsActionHelper.js';
import ValidationHelper from 'sdc-app/common/helpers/ValidationHelper.js';
import SoftwareProductActionHelper from 'sdc-app/onboarding/softwareProduct/SoftwareProductActionHelper.js';

import {
    forms,
    COMPONENTS_QUESTIONNAIRE
} from '../SoftwareProductComponentsConstants.js';
import { onboardingMethod } from '../../SoftwareProductConstants.js';

export const mapStateToProps = ({ softwareProduct }) => {
    let {
        softwareProductEditor: { data: currentVSP },
        softwareProductComponents
    } = softwareProduct;
    let {
        componentEditor: {
            data: componentData = {},
            qdata,
            qgenericFieldInfo: qGenericFieldInfo,
            dataMap,
            genericFieldInfo
        }
    } = softwareProductComponents;
    let isFormValid = ValidationHelper.checkFormValid(genericFieldInfo);

    return {
        componentData,
        qdata,
        isManual: currentVSP.onboardingMethod === onboardingMethod.MANUAL,
        genericFieldInfo,
        qGenericFieldInfo,
        dataMap,
        isFormValid
    };
};

const mapActionsToProps = (
    dispatch,
    { softwareProductId, version, componentId }
) => {
    return {
        onDataChanged: deltaData =>
            ValidationHelper.dataChanged(dispatch, {
                deltaData,
                formName: forms.ALL_SPC_FORMS
            }),
        onQDataChanged: deltaData =>
            ValidationHelper.qDataChanged(dispatch, {
                deltaData,
                qName: COMPONENTS_QUESTIONNAIRE
            }),
        onSubmit: ({ componentData, qdata }) => {
            return SoftwareProductComponentsActionHelper.updateSoftwareProductComponent(
                dispatch,
                {
                    softwareProductId,
                    version,
                    vspComponentId: componentId,
                    componentData,
                    qdata
                }
            );
        },
        onValidityChanged: isValidityData =>
            SoftwareProductActionHelper.setIsValidityData(dispatch, {
                isValidityData
            })
    };
};

export default connect(mapStateToProps, mapActionsToProps, null, {
    withRef: true
})(SoftwareProductComponentsGeneralView);
