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

package org.openecomp.sdc.be.dao.api;

public enum ActionStatus {
    OK, ACCEPTED, CREATED, NO_CONTENT, GENERAL_ERROR, NOT_ALLOWED, MISSING_INFORMATION, RESTRICTED_OPERATION, RESTRICTED_ACCESS, INVALID_CONTENT,
    // User related
    USER_ALREADY_EXIST, USER_INACTIVE, USER_NOT_FOUND, USER_HAS_ACTIVE_ELEMENTS, INVALID_EMAIL_ADDRESS, INVALID_ROLE, DELETE_USER_ADMIN_CONFLICT, UPDATE_USER_ADMIN_CONFLICT, CANNOT_DELETE_USER_WITH_ACTIVE_ELEMENTS, CANNOT_UPDATE_USER_WITH_ACTIVE_ELEMENTS, INVALID_USER_ID, USER_DEFINED,
    // CapabilityType related
    CAPABILITY_TYPE_ALREADY_EXIST, MISSING_CAPABILITY_TYPE, MISSING_CAPABILITIES, REQ_CAP_NOT_SATISFIED_BEFORE_CERTIFICATION, IMPORT_DUPLICATE_REQ_CAP_NAME, IMPORT_REQ_CAP_NAME_EXISTS_IN_DERIVED,
    CAPABILITY_OF_INSTANCE_NOT_FOUND_ON_CONTAINER, REQUIREMENT_OF_INSTANCE_NOT_FOUND_ON_CONTAINER, RELATION_NOT_FOUND,
    // Resource related
    RESOURCE_NOT_FOUND, MISSING_DERIVED_FROM_TEMPLATE, PARENT_RESOURCE_NOT_FOUND, PARENT_RESOURCE_DOES_NOT_EXTEND, INVALID_DEFAULT_VALUE, INVALID_COMPLEX_DEFAULT_VALUE, MULTIPLE_PARENT_RESOURCE_FOUND, INVALID_RESOURCE_PAYLOAD, INVALID_TOSCA_FILE_EXTENSION, INVALID_YAML_FILE, INVALID_TOSCA_TEMPLATE, NOT_RESOURCE_TOSCA_TEMPLATE, NOT_SINGLE_RESOURCE, INVALID_RESOURCE_NAMESPACE, RESOURCE_ALREADY_EXISTS, INVALID_RESOURCE_CHECKSUM, RESOURCE_CANNOT_CONTAIN_RESOURCE_INSTANCES, NO_ASSETS_FOUND, GENERIC_TYPE_NOT_FOUND, INVALID_RESOURCE_TYPE, TOSCA_PARSE_ERROR,
    // Service related
    SERVICE_TYPE_EXCEEDS_LIMIT, INVALID_SERVICE_TYPE, SERVICE_ROLE_EXCEEDS_LIMIT, INVALID_SERVICE_ROLE, INVALID_INSTANTIATION_TYPE, 
    // Component name related
    COMPONENT_NAME_ALREADY_EXIST, COMPONENT_NAME_EXCEEDS_LIMIT, MISSING_COMPONENT_NAME, INVALID_COMPONENT_NAME,
    // Component description related
    COMPONENT_INVALID_DESCRIPTION, COMPONENT_MISSING_DESCRIPTION, COMPONENT_DESCRIPTION_EXCEEDS_LIMIT,
    // Component icon related
    COMPONENT_MISSING_ICON, COMPONENT_INVALID_ICON, COMPONENT_ICON_EXCEEDS_LIMIT,
    // Directives related
    DIRECTIVES_INVALID_VALUE,
    // Tag related
    COMPONENT_MISSING_TAGS, COMPONENT_TAGS_EXCEED_LIMIT, COMPONENT_SINGLE_TAG_EXCEED_LIMIT, INVALID_FIELD_FORMAT, COMPONENT_INVALID_TAGS_NO_COMP_NAME,
    // contactId related
    COMPONENT_MISSING_CONTACT, COMPONENT_INVALID_CONTACT,
    // Vendor related
    VENDOR_NAME_EXCEEDS_LIMIT, VENDOR_RELEASE_EXCEEDS_LIMIT, INVALID_VENDOR_NAME, INVALID_VENDOR_RELEASE, MISSING_VENDOR_NAME, MISSING_VENDOR_RELEASE, RESOURCE_VENDOR_MODEL_NUMBER_EXCEEDS_LIMIT, INVALID_RESOURCE_VENDOR_MODEL_NUMBER,
    // Category related
    COMPONENT_MISSING_CATEGORY, COMPONENT_INVALID_CATEGORY, COMPONENT_ELEMENT_INVALID_NAME_FORMAT, COMPONENT_ELEMENT_INVALID_NAME_LENGTH, COMPONENT_CATEGORY_ALREADY_EXISTS, COMPONENT_CATEGORY_NOT_FOUND, COMPONENT_SUB_CATEGORY_NOT_FOUND_FOR_CATEGORY, COMPONENT_SUB_CATEGORY_EXISTS_FOR_CATEGORY, COMPONENT_GROUPING_EXISTS_FOR_SUB_CATEGORY,
    // Service API URL
    INVALID_SERVICE_API_URL,
    // Property related
    PROPERTY_ALREADY_EXIST, PROPERTY_NAME_ALREADY_EXISTS, PROPERTY_NOT_FOUND, INVALID_PROPERTY, INVALID_PROPERTY_TYPE, INVALID_PROPERTY_INNER_TYPE,
    // Attribute related
    ATTRIBUTE_ALREADY_EXIST, ATTRIBUTE_NOT_FOUND,
    // State related
    INVALID_SERVICE_STATE, COMPONENT_IN_CHECKOUT_STATE, ILLEGAL_COMPONENT_STATE, COMPONENT_IN_CERT_IN_PROGRESS_STATE, COMPONENT_SENT_FOR_CERTIFICATION, COMPONENT_VERSION_ALREADY_EXIST, COMPONENT_ALREADY_CHECKED_IN, COMPONENT_CHECKOUT_BY_ANOTHER_USER, COMPONENT_IN_USE, COMPONENT_HAS_NEWER_VERSION, COMPONENT_ALREADY_CERTIFIED, COMPONENT_NOT_READY_FOR_CERTIFICATION, COMPONENT_ARTIFACT_NOT_FOUND, COMPONENT_INSTANCE_NOT_FOUND, COMPONENT_INSTANCE_NOT_FOUND_ON_CONTAINER, SERVICE_NOT_FOUND, SERVICE_CATEGORY_CANNOT_BE_CHANGED, SERVICE_NAME_CANNOT_BE_CHANGED, SERVICE_ICON_CANNOT_BE_CHANGED, COMPONENT_TOO_MUCH_CATEGORIES, SERVICE_CANNOT_CONTAIN_SUBCATEGORY, RESOURCE_CATEGORY_CANNOT_BE_CHANGED, RESOURCE_NAME_CANNOT_BE_CHANGED, RESOURCE_ICON_CANNOT_BE_CHANGED, RESOURCE_VENDOR_NAME_CANNOT_BE_CHANGED, RESOURCE_TOO_MUCH_SUBCATEGORIES, SERVICE_ICON_EXCEEDS_LIMIT, RESOURCE_INSTANCE_NOT_FOUND, RESOURCE_INSTANCE_BAD_REQUEST, RESOURCE_INSTANCE_MATCH_NOT_FOUND, RESOURCE_INSTANCE_ALREADY_EXIST, RESOURCE_INSTANCE_RELATION_NOT_FOUND, COMPONENT_MISSING_SUBCATEGORY, COMPONENT_INVALID_SUBCATEGORY, ARTIFACT_TYPE_NOT_SUPPORTED, MISSING_ARTIFACT_TYPE, ARTIFACT_LOGICAL_NAME_CANNOT_BE_CHANGED, ARTIFACT_EXIST, ARTIFACT_NOT_FOUND, ARTIFACT_INVALID_MD5, MISSING_ARTIFACT_NAME, MISSING_PROJECT_CODE, INVALID_PROJECT_CODE, COMPONENT_MISSING_MANDATORY_ARTIFACTS, LIFECYCLE_TYPE_ALREADY_EXIST, SERVICE_NOT_AVAILABLE_FOR_DISTRIBUTION, MISSING_LIFECYCLE_TYPE, RESOURCE_VFCMT_LIFECYCLE_STATE_NOT_VALID,
    // Distribution
    BAD_REQUEST_MISSING_RESOURCE, MISSING_USER_ID, MISSING_X_ECOMP_INSTANCE_ID, MISSING_PUBLIC_KEY, MISSING_ENV_NAME, DISTRIBUTION_ENV_DOES_NOT_EXIST, MISSING_BODY, ECOMP_RESEND_WITH_BASIC_AUTHENTICATION_CREDENTIALS, ECOMP_COMPONENT_NOT_AUTHORIZED, METHOD_NOT_ALLOWED_TO_DOWNLOAD_ARTIFACT, REGISTRATION_FAILED, DISTRIBUTION_ENVIRONMENT_NOT_AVAILABLE, DISTRIBUTION_ENVIRONMENT_NOT_FOUND, DISTRIBUTION_ENVIRONMENT_INVALID, DISTRIBUTION_REQUESTED_NOT_FOUND, DISTRIBUTION_REQUESTED_FAILED, DISTRIBUTION_NOT_FOUND, ADDITIONAL_INFORMATION_ALREADY_EXISTS, COMPONENT_VERSION_NOT_FOUND, ADDITIONAL_INFORMATION_MAX_NUMBER_REACHED, ADDITIONAL_INFORMATION_EMPTY_STRING_NOT_ALLOWED, ADDITIONAL_INFORMATION_EXCEEDS_LIMIT, ADDITIONAL_INFORMATION_KEY_NOT_ALLOWED_CHARACTERS, ADDITIONAL_INFORMATION_VALUE_NOT_ALLOWED_CHARACTERS, ADDITIONAL_INFORMATION_NOT_FOUND, ASDC_VERSION_NOT_FOUND, MISSING_DATA, EXCEEDS_LIMIT, UNSUPPORTED_ERROR, ARTIFACT_INVALID_TIMEOUT, SERVICE_IS_VNF_CANNOT_BE_CHANGED, RESOURCE_INSTANCE_NOT_FOUND_ON_SERVICE, WRONG_ARTIFACT_FILE_EXTENSION, INVALID_YAML, INVALID_XML, INVALID_JSON, INVALID_DEPLOYMENT_ARTIFACT_HEAT, INVALID_HEAT_PARAMETER_TYPE, INVALID_HEAT_PARAMETER_VALUE, DEPLOYMENT_ARTIFACT_OF_TYPE_ALREADY_EXISTS, DEPLOYMENT_ARTIFACT_NAME_ALREADY_EXISTS, MISSING_HEAT, MISMATCH_HEAT_VS_HEAT_ENV, CORRUPTED_FORMAT, MISMATCH_BETWEEN_ARTIFACT_TYPE_AND_COMPONENT_TYPE, INVALID_PARAMS_IN_HEAT_ENV_FILE, API_RESOURCE_NOT_FOUND,
    //UEB
    UNKNOWN_HOST, AUTHENTICATION_ERROR, CONNNECTION_ERROR, OBJECT_NOT_FOUND, INVALID_RESPONSE_FROM_PROXY,
    // auth
    AUTH_FAILED_INVALIDE_HEADER, AUTH_FAILED, AUTH_REQUIRED, CONSUMER_ALREADY_EXISTS, INVALID_LENGTH, ECOMP_USER_NOT_FOUND, INVALID_CONTENT_PARAM, INVALID_FILTER_KEY, SERVICE_DEPLOYMENT_ARTIFACT_NOT_FOUND, VALIDATED_RESOURCE_NOT_FOUND, FOUND_ALREADY_VALIDATED_RESOURCE, FOUND_LIST_VALIDATED_RESOURCES,

    // product
    PRODUCT_NOT_FOUND, INVALID_GROUP_ASSOCIATION, EMPTY_PRODUCT_CONTACTS_LIST, INVALID_PRODUCT_CONTACT, MISSING_ONE_OF_COMPONENT_NAMES, COMPONENT_PARAMETER_CANNOT_BE_CHANGED,

    // occurrences
    EMPTY_OCCURRENCES_LIST, INVALID_OCCURRENCES, NOT_TOPOLOGY_TOSCA_TEMPLATE, INVALID_NODE_TEMPLATE,

    // Data type related
    DATA_TYPE_ALREADY_EXIST, DATA_TYPE_NOR_PROPERTIES_NEITHER_DERIVED_FROM, DATA_TYPE_PROPERTIES_CANNOT_BE_EMPTY, DATA_TYPE_DERIVED_IS_MISSING, DATA_TYPE_PROPERTY_ALREADY_DEFINED_IN_ANCESTOR, DATA_TYPE_DUPLICATE_PROPERTY, DATA_TYPE_PROEPRTY_CANNOT_HAVE_SAME_TYPE_OF_DATA_TYPE, DATA_TYPE_CANNOT_HAVE_PROPERTIES, DATA_TYPE_CANNOT_BE_EMPTY, DATA_TYPE_CANNOT_BE_UPDATED_BAD_REQUEST,

    // Policy Type related
    TARGETS_EMPTY, TARGETS_NON_VALID, POLICY_TYPE_ALREADY_EXIST,

    // Group Type related
    GROUP_MEMBER_EMPTY, GROUP_TYPE_ALREADY_EXIST, GROUP_TYPE_ILLEGAL_PER_COMPONENT,

    // Annotation Type related
    ANNOTATION_TYPE_ALREADY_EXIST,

    // CSAR
    MISSING_CSAR_UUID, CSAR_INVALID, CSAR_INVALID_FORMAT, CSAR_NOT_FOUND, YAML_NOT_FOUND_IN_CSAR, VSP_ALREADY_EXISTS, RESOURCE_LINKED_TO_DIFFERENT_VSP, RESOURCE_FROM_CSAR_NOT_FOUND, AAI_ARTIFACT_GENERATION_FAILED, ASSET_NOT_FOUND_DURING_CSAR_CREATION, ARTIFACT_PAYLOAD_NOT_FOUND_DURING_CSAR_CREATION, TOSCA_SCHEMA_FILES_NOT_FOUND, ARTIFACT_NAME_INVALID, ARTIFACT_PAYLOAD_EMPTY,ERROR_DURING_CSAR_CREATION,

    // Group
    GROUP_HAS_CYCLIC_DEPENDENCY, GROUP_ALREADY_EXIST, GROUP_TYPE_IS_INVALID, GROUP_MISSING_GROUP_TYPE, GROUP_INVALID_COMPONENT_INSTANCE, GROUP_INVALID_TOSCA_NAME_OF_COMPONENT_INSTANCE, GROUP_IS_MISSING, GROUP_ARTIFACT_ALREADY_ASSOCIATED, GROUP_ARTIFACT_ALREADY_DISSOCIATED, GROUP_PROPERTY_NOT_FOUND, INVALID_VF_MODULE_NAME, INVALID_VF_MODULE_NAME_MODIFICATION, INVALID_VF_MODULE_TYPE,

    // Group instance
    GROUP_INSTANCE_NOT_FOUND_ON_COMPONENT_INSTANCE, INVALID_GROUP_MIN_MAX_INSTANCES_PROPERTY_VALUE, INVALID_GROUP_INITIAL_COUNT_PROPERTY_VALUE, INVALID_GROUP_PROPERTY_VALUE_LOWER_HIGHER,

    ARTIFACT_NOT_FOUND_IN_CSAR, ARTIFACT_ALRADY_EXIST_IN_DIFFERENT_TYPE_IN_CSAR, FAILED_RETRIVE_ARTIFACTS_TYPES, ARTIFACT_ALRADY_EXIST_IN_MASTER_IN_CSAR, ARTIFACT_NOT_VALID_IN_MASTER, ARTIFACT_NOT_VALID_ENV,

    // cache
    CONVERT_COMPONENT_ERROR, COMPONENT_NOT_FOUND,

    // Inputs
    INPUT_IS_NOT_CHILD_OF_COMPONENT,
    CFVC_LOOP_DETECTED,
    //Forwarding Path related
    FORWARDING_PATH_NAME_MAXIMUM_LENGTH, FORWARDING_PATH_NAME_ALREADY_IN_USE, FORWARDING_PATH_NAME_EMPTY,
    FORWARDING_PATH_PROTOCOL_MAXIMUM_LENGTH, FORWARDING_PATH_DESTINATION_PORT_MAXIMUM_LENGTH,
    MISSING_OLD_COMPONENT_INSTANCE, MISSING_NEW_COMPONENT_INSTANCE,
    //policies
    RESOURCE_CANNOT_CONTAIN_POLICIES,
    POLICY_NOT_FOUND_ON_CONTAINER,
    INVALID_POLICY_NAME,
    POLICY_NAME_ALREADY_EXIST, EXCLUDED_POLICY_TYPE,

    //External References Statuses
    EXT_REF_ALREADY_EXIST,
    EXT_REF_NOT_FOUND,

    POLICY_TARGET_DOES_NOT_EXIST, POLICY_TARGET_TYPE_DOES_NOT_EXIST, INPUTS_NOT_FOUND,
    RESOURCE_LIFECYCLE_STATE_NOT_VALID,
    COMPONENT_IS_ARCHIVED,
    //Automated upgrade
    COMPONENT_IS_NOT_HIHGEST_CERTIFIED,
    NO_INSTANCES_TO_UPGRADE,
    ARCHIVED_ORIGINS_FOUND,

    //Interface
    INTERFACE_NOT_FOUND_IN_COMPONENT,

    //InterfaceOperation
    INTERFACE_OPERATION_NOT_FOUND, INTERFACE_OPERATION_NAME_ALREADY_IN_USE, INTERFACE_OPERATION_NAME_MANDATORY,
    INTERFACE_OPERATION_NAME_INVALID, INTERFACE_OPERATION_DESCRIPTION_MAX_LENGTH, INTERFACE_OPERATION_INPUT_NAME_ALREADY_IN_USE,
    INTERFACE_OPERATION_OUTPUT_NAME_ALREADY_IN_USE, INTERFACE_OPERATION_NOT_DELETED,
    INTERFACE_OPERATION_MAPPED_OUTPUT_MODIFIED,
    INTERFACE_OPERATION_INPUT_NAME_MANDATORY, INTERFACE_OPERATION_OUTPUT_NAME_MANDATORY,
    INTERFACE_OPERATION_INPUT_PROPERTY_NOT_FOUND_IN_COMPONENT, INTERFACE_OPERATION_INVALID_FOR_LOCAL_TYPE,
    INTERFACE_OPERATION_INVALID_FOR_GLOBAL_TYPE,
    PROPERTY_USED_BY_OPERATION, DECLARED_INPUT_USED_BY_OPERATION,

    //NodeFilter
    NODE_FILTER_NOT_FOUND,
    UNSUPPORTED_VALUE_PROVIDED,
    SELECTED_PROPERTY_NOT_PRESENT,
    MAPPED_PROPERTY_NOT_FOUND,
    UNSUPPORTED_OPERATOR_PROVIDED,
    CONSTRAINT_FORMAT_INCORRECT,
    SOURCE_TARGET_PROPERTY_TYPE_MISMATCH,
    SOURCE_TARGET_SCHEMA_MISMATCH,
    UNSUPPORTED_PROPERTY_TYPE,


    //InterfaceLifeCycleType
    INTERFACE_LIFECYCLE_TYPES_NOT_FOUND
}
