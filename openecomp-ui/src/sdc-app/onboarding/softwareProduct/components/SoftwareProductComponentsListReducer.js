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
import { actionTypes } from './SoftwareProductComponentsConstants.js';

export default (state = [], action) => {
    switch (action.type) {
        case actionTypes.COMPONENTS_LIST_UPDATE:
            return [...action.componentsList];
        case actionTypes.COMPONENTS_LIST_EDIT:
            const indexForEdit = state.findIndex(
                component => component.id === action.component.id
            );
            return [
                ...state.slice(0, indexForEdit),
                action.component,
                ...state.slice(indexForEdit + 1)
            ];
        case actionTypes.COMPONENT_DELETE:
            return state.filter(
                component => component.id !== action.componentId
            );
        default:
            return state;
    }
};
