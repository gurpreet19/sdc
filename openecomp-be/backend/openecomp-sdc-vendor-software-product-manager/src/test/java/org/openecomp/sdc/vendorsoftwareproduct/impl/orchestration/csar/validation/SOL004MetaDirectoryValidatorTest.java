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

package org.openecomp.sdc.vendorsoftwareproduct.impl.orchestration.csar.validation;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.openecomp.core.utilities.file.FileContentHandler;
import org.openecomp.sdc.common.errors.Messages;
import org.openecomp.sdc.common.utils.SdcCommon;
import org.openecomp.sdc.datatypes.error.ErrorLevel;
import org.openecomp.sdc.datatypes.error.ErrorMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.openecomp.sdc.logging.api.Logger;
import org.openecomp.sdc.logging.api.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.openecomp.sdc.tosca.csar.CSARConstants.PNFD_NAME;
import static org.openecomp.sdc.tosca.csar.CSARConstants.PNFD_PROVIDER;
import static org.openecomp.sdc.tosca.csar.CSARConstants.PNFD_ARCHIVE_VERSION;
import static org.openecomp.sdc.tosca.csar.CSARConstants.PNFD_RELEASE_DATE_TIME;
import static org.openecomp.sdc.tosca.csar.CSARConstants.SEPARATOR_MF_ATTRIBUTE;
import static org.openecomp.sdc.tosca.csar.CSARConstants.TOSCA_META_ENTRY_DEFINITIONS;
import static org.openecomp.sdc.tosca.csar.CSARConstants.TOSCA_META_ETSI_ENTRY_CERTIFICATE;
import static org.openecomp.sdc.tosca.csar.CSARConstants.TOSCA_META_ETSI_ENTRY_CHANGE_LOG;
import static org.openecomp.sdc.tosca.csar.CSARConstants.TOSCA_META_ETSI_ENTRY_LICENSES;
import static org.openecomp.sdc.tosca.csar.CSARConstants.TOSCA_META_ETSI_ENTRY_MANIFEST;
import static org.openecomp.sdc.tosca.csar.CSARConstants.TOSCA_META_ETSI_ENTRY_TESTS;
import static org.openecomp.sdc.tosca.csar.CSARConstants.TOSCA_META_PATH_FILE_NAME;
import static org.openecomp.sdc.tosca.csar.CSARConstants.VNF_PRODUCT_NAME;
import static org.openecomp.sdc.tosca.csar.CSARConstants.VNF_PROVIDER_ID;
import static org.openecomp.sdc.tosca.csar.CSARConstants.VNF_PACKAGE_VERSION;
import static org.openecomp.sdc.tosca.csar.CSARConstants.VNF_RELEASE_DATE_TIME;

import static org.openecomp.sdc.vendorsoftwareproduct.impl.orchestration.csar.validation.TestConstants.*;

public class SOL004MetaDirectoryValidatorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOL004MetaDirectoryValidatorTest.class);

    private SOL004MetaDirectoryValidator sol004MetaDirectoryValidator;
    private FileContentHandler handler;
    private String metaFile;

    @Before
    public void setUp() {
        sol004MetaDirectoryValidator = new SOL004MetaDirectoryValidator();
        handler = new FileContentHandler();
        metaFile =
                "TOSCA-Meta-File-Version: 1.0\n"+
                "CSAR-Version: 1.1\n"+
                "Created-By: Vendor\n"+
                TOSCA_META_ENTRY_DEFINITIONS + SEPARATOR_MF_ATTRIBUTE + "Definitions/MainServiceTemplate.yaml\n"+
                TOSCA_META_ETSI_ENTRY_MANIFEST + SEPARATOR_MF_ATTRIBUTE + "Definitions/MainServiceTemplate.mf\n"+
                TOSCA_META_ETSI_ENTRY_CHANGE_LOG + SEPARATOR_MF_ATTRIBUTE + "Artifacts/changeLog.text\n";
    }

    @Test
    public void testGivenTOSCAMetaFile_whenEntryHasNoValue_thenErrorIsReturned() {
        final String metaFileWithInvalidEntry = "TOSCA-Meta-File-Version: \n" +
                "Entry-Definitions: Definitions/MainServiceTemplate.yaml";

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFileWithInvalidEntry.getBytes(StandardCharsets.UTF_8));
        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(TestConstants.SAMPLE_DEFINITION_FILE_PATH));

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        assertExpectedErrors("TOSCA Meta file with no entries", errors, 1);
    }

    @Test
    public void testGivenTOSCAMeta_withAllSupportedEntries_thenNoErrorsReturned() {

        final String entryTestFilePath = "Files/Tests";
        final String entryLicenseFilePath = "Files/Licenses";

        final List<String> folderList = new ArrayList<>();
        folderList.add("Files/Tests/");
        folderList.add("Files/Licenses/");

        metaFile = metaFile +
                TOSCA_META_ETSI_ENTRY_TESTS + SEPARATOR_MF_ATTRIBUTE + entryTestFilePath + "\n" +
                TOSCA_META_ETSI_ENTRY_LICENSES + SEPARATOR_MF_ATTRIBUTE + entryLicenseFilePath +"\n";

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(SAMPLE_DEFINITION_FILE_PATH));
        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes(StandardCharsets.UTF_8));

        handler.addFile(SAMPLE_SOURCE, "".getBytes());
        handler.addFile(SAMPLE_DEFINITION_IMPORT_FILE_PATH, "".getBytes());
        handler.addFile(entryTestFilePath, "".getBytes());
        handler.addFile(entryLicenseFilePath, "".getBytes());

        final ManifestBuilder manifestBuilder = getVnfManifestSampleBuilder()
            .withSource(TOSCA_META_PATH_FILE_NAME)
            .withSource(TOSCA_DEFINITION_FILEPATH)
            .withSource(TOSCA_CHANGELOG_FILEPATH)
            .withSource(TOSCA_MANIFEST_FILEPATH).withSource(SAMPLE_SOURCE)
            .withSource(SAMPLE_DEFINITION_IMPORT_FILE_PATH)
            .withSource(entryTestFilePath)
            .withSource(entryLicenseFilePath);

        handler.addFile(TOSCA_MANIFEST_FILEPATH, manifestBuilder.build().getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, folderList);
        assertTrue(errors.size() == 0);
    }

    @Test
    public void testGivenTOSCAMeta_withUnsupportedEntry_thenWarningIsReturned() {
        metaFile = "Entry-Events: Definitions/events.log";

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        List<ErrorMessage> errorMessages = errors.get(SdcCommon.UPLOAD_FILE);
        assertTrue(errors.size() == 1 && errorMessages.size() == 1);
        assertTrue(errorMessages.get(0).getLevel() == ErrorLevel.ERROR);
    }

    /**
     * Tests if the meta file contains invalid versions in TOSCA-Meta-File-Version and CSAR-Version attributes.
     */
    @Test
    public void testGivenTOSCAMetaFile_withInvalidTOSCAMetaFileVersionAndCSARVersion_thenErrorIsReturned() {
        final String metaFile =
                "TOSCA-Meta-File-Version: " + Integer.MAX_VALUE +
                "\nCSAR-Version: " + Integer.MAX_VALUE  +
                "\nCreated-By: Bilal Iqbal\n" +
                TOSCA_META_ENTRY_DEFINITIONS+ SEPARATOR_MF_ATTRIBUTE + "Definitions/MainServiceTemplate.yaml\n" +
                TOSCA_META_ETSI_ENTRY_MANIFEST + SEPARATOR_MF_ATTRIBUTE + "Definitions/MainServiceTemplate.mf\n"+
                TOSCA_META_ETSI_ENTRY_CHANGE_LOG + SEPARATOR_MF_ATTRIBUTE + "Artifacts/changeLog.text";

        final ManifestBuilder manifestBuilder = getVnfManifestSampleBuilder();

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_META_PATH_FILE_NAME);

        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(TestConstants.SAMPLE_DEFINITION_FILE_PATH));
        manifestBuilder.withSource(TOSCA_DEFINITION_FILEPATH);

        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_CHANGELOG_FILEPATH);

        manifestBuilder.withSource(TOSCA_MANIFEST_FILEPATH);
        handler.addFile(TOSCA_MANIFEST_FILEPATH, manifestBuilder.build().getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        assertExpectedErrors("Invalid TOSCA-Meta-File-Version and CSAR-Version attributes", errors, 2);
    }

    @Test
    public void testGivenTOSCAMetaFile_withNonExistentFileReferenced_thenErrorsReturned() {
        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        List<ErrorMessage> errorMessages = errors.get(SdcCommon.UPLOAD_FILE);
        assertTrue(errors.size() == 1 && errorMessages.size() == 3);
    }


    @Test
    public void testGivenDefinitionFile_whenValidImportStatementExist_thenNoErrorsReturned() {
        final ManifestBuilder manifestBuilder = getPnfManifestSampleBuilder();

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_META_PATH_FILE_NAME);

        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_CHANGELOG_FILEPATH);

        handler.addFile(SAMPLE_SOURCE, "".getBytes());
        manifestBuilder.withSource(SAMPLE_SOURCE);

        handler.addFile("Definitions/etsi_nfv_sol001_pnfd_2_5_1_types.yaml", getResourceBytes(SAMPLE_DEFINITION_FILE_PATH));
        manifestBuilder.withSource("Definitions/etsi_nfv_sol001_pnfd_2_5_1_types.yaml");

        final String definitionFileWithValidImports = "/validation.files/definition/definitionFileWithValidImports.yaml";
        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(definitionFileWithValidImports));
        manifestBuilder.withSource(TOSCA_DEFINITION_FILEPATH);

        manifestBuilder.withSource(TOSCA_MANIFEST_FILEPATH);
        handler.addFile(TOSCA_MANIFEST_FILEPATH, manifestBuilder.build().getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        assertTrue(errors.size() == 0);
    }

    @Test
    public void testGivenDefinitionFile_whenMultipleDefinitionsImportStatementExist_thenNoErrorsReturned() {
        final ManifestBuilder manifestBuilder = getVnfManifestSampleBuilder();

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_META_PATH_FILE_NAME);

        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_CHANGELOG_FILEPATH);

        handler.addFile(SAMPLE_SOURCE, "".getBytes());
        manifestBuilder.withSource(SAMPLE_SOURCE);

        final byte [] sampleDefinitionFile1 = getResourceBytes("/validation.files/definition/sampleDefinitionFile1.yaml");
        handler.addFile(TOSCA_DEFINITION_FILEPATH, sampleDefinitionFile1);
        manifestBuilder.withSource(TOSCA_DEFINITION_FILEPATH);

        final byte [] sampleDefinitionFile2 = getResourceBytes("/validation.files/definition/sampleDefinitionFile2.yaml");
        handler.addFile("Definitions/etsi_nfv_sol001_pnfd_2_5_1_types.yaml", sampleDefinitionFile2);
        manifestBuilder.withSource("Definitions/etsi_nfv_sol001_pnfd_2_5_1_types.yaml");

        final byte [] sampleDefinitionFile3 = getResourceBytes("/validation.files/definition/sampleDefinitionFile1.yaml");
        handler.addFile("Definitions/etsi_nfv_sol001_pnfd_2_5_2_types.yaml", sampleDefinitionFile3);
        manifestBuilder.withSource("Definitions/etsi_nfv_sol001_pnfd_2_5_2_types.yaml");

        manifestBuilder.withSource(TOSCA_MANIFEST_FILEPATH);
        handler.addFile(TOSCA_MANIFEST_FILEPATH, manifestBuilder.build().getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        assertTrue(errors.size() == 0);
    }

    @Test
    public void testGivenDefinitionFile_whenInvalidImportStatementExist_thenErrorIsReturned() {
        final ManifestBuilder manifestBuilder = getVnfManifestSampleBuilder();

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_META_PATH_FILE_NAME);

        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_CHANGELOG_FILEPATH);

        handler.addFile(SAMPLE_SOURCE, "".getBytes());
        manifestBuilder.withSource(SAMPLE_SOURCE);

        final String definitionFileWithInvalidImports = "/validation.files/definition/definitionFileWithInvalidImport.yaml";
        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(definitionFileWithInvalidImports));
        manifestBuilder.withSource(TOSCA_DEFINITION_FILEPATH);

        manifestBuilder.withSource(TOSCA_MANIFEST_FILEPATH);
        String manifest = manifestBuilder.build();
        handler.addFile(TOSCA_MANIFEST_FILEPATH, manifest.getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        assertExpectedErrors("", errors, 1);
    }

    /**
     * Manifest referenced import file missing
     */
    @Test
    public void testGivenDefinitionFile_whenReferencedImportDoesNotExist_thenErrorIsReturned() {
        final ManifestBuilder manifestBuilder = getVnfManifestSampleBuilder();

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_META_PATH_FILE_NAME);

        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_CHANGELOG_FILEPATH);

        handler.addFile(SAMPLE_SOURCE, "".getBytes());
        manifestBuilder.withSource(SAMPLE_SOURCE);

        handler.addFile("Definitions/etsi_nfv_sol001_pnfd_2_5_1_types.yaml", "".getBytes());
        manifestBuilder.withSource("Definitions/etsi_nfv_sol001_pnfd_2_5_1_types.yaml");

        manifestBuilder.withSource(TOSCA_DEFINITION_FILEPATH);
        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes("/validation.files/definition/sampleDefinitionFile2.yaml"));

        manifestBuilder.withSource(TOSCA_MANIFEST_FILEPATH);
        handler.addFile(TOSCA_MANIFEST_FILEPATH, manifestBuilder.build().getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        assertExpectedErrors("Manifest referenced import file missing", errors, 1);
    }

    /**
     * Reference with invalid YAML format.
     */
    @Test
    public void testGivenDefinitionFile_withInvalidYAML_thenErrorIsReturned() {
        final ManifestBuilder manifestBuilder = getVnfManifestSampleBuilder();

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_META_PATH_FILE_NAME);

        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_CHANGELOG_FILEPATH);

        handler.addFile(SAMPLE_SOURCE, "".getBytes());
        manifestBuilder.withSource(SAMPLE_SOURCE);

        final String definitionFileWithInvalidYAML = "/validation.files/definition/invalidDefinitionFile.yaml";
        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(definitionFileWithInvalidYAML));
        manifestBuilder.withSource(TOSCA_DEFINITION_FILEPATH);

        manifestBuilder.withSource(TOSCA_MANIFEST_FILEPATH);
        handler.addFile(TOSCA_MANIFEST_FILEPATH, manifestBuilder.build().getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        assertExpectedErrors("Reference with invalid YAML format", errors, 1);
    }

    @Test
    public void testGivenManifestFile_withValidSourceAndNonManoSources_thenNoErrorIsReturned() {
        final ManifestBuilder manifestBuilder = getPnfManifestSampleBuilder();

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_META_PATH_FILE_NAME);

        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes());
        manifestBuilder.withSource(TOSCA_CHANGELOG_FILEPATH);

        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(SAMPLE_DEFINITION_FILE_PATH));
        manifestBuilder.withSource(TOSCA_DEFINITION_FILEPATH);

        handler.addFile(SAMPLE_SOURCE, "".getBytes());
        manifestBuilder.withSource(SAMPLE_SOURCE);

        handler.addFile(SAMPLE_DEFINITION_IMPORT_FILE_PATH, getResourceBytes(SAMPLE_DEFINITION_FILE_PATH));
        manifestBuilder.withSource(SAMPLE_DEFINITION_IMPORT_FILE_PATH);

        final String nonManoSource = "Artifacts/Deployment/Measurements/PM_Dictionary.yaml";
        handler.addFile(nonManoSource, "".getBytes());
        manifestBuilder.withNonManoArtifact("onap_pm_events", nonManoSource);

        manifestBuilder.withSource(TOSCA_MANIFEST_FILEPATH);
        handler.addFile(TOSCA_MANIFEST_FILEPATH, manifestBuilder.build().getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        assertTrue(errors.size() == 0);
    }

    /**
     * Manifest with non existent source files should return error.
     */
    @Test
    public void testGivenManifestFile_withNonExistentSourceFile_thenErrorIsReturned() {
        final ManifestBuilder manifestBuilder = getPnfManifestSampleBuilder();
        //non existent reference
        manifestBuilder.withSource("Artifacts/Deployment/Events/RadioNode_pnf_v1.yaml");

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_META_PATH_FILE_NAME);

        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes());
        manifestBuilder.withSource(TOSCA_CHANGELOG_FILEPATH);

        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(SAMPLE_DEFINITION_FILE_PATH));
        manifestBuilder.withSource(TOSCA_DEFINITION_FILEPATH);

        handler.addFile(SAMPLE_DEFINITION_IMPORT_FILE_PATH, "".getBytes());
        manifestBuilder.withSource(SAMPLE_DEFINITION_IMPORT_FILE_PATH);

        String nonManoSource = "Artifacts/Deployment/Measurements/PM_Dictionary.yaml";
        handler.addFile(nonManoSource, "".getBytes());
        manifestBuilder.withNonManoArtifact("onap_pm_events", nonManoSource);

        manifestBuilder.withSource(TOSCA_MANIFEST_FILEPATH);
        handler.addFile(TOSCA_MANIFEST_FILEPATH, manifestBuilder.build().getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        assertExpectedErrors("Manifest with non existent source files", errors, 1);
    }

    /**
     * Tests the validation for a TOSCA Manifest with invalid data.
     */
    @Test
    public void testGivenManifestFile_withInvalidData_thenErrorIsReturned() {
        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        handler.addFile(TOSCA_MANIFEST_FILEPATH, getResourceBytes("/validation.files/manifest/invalidManifest.mf"));
        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes());
        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(SAMPLE_DEFINITION_FILE_PATH));
        handler.addFile(SAMPLE_DEFINITION_IMPORT_FILE_PATH, "".getBytes());

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        assertExpectedErrors("TOSCA manifest with invalid data", errors, 1);
    }

    @Test
    public void testGivenManifestAndDefinitionFile_withSameNames_thenNoErrorReturned()  {
        final ManifestBuilder manifestBuilder = getVnfManifestSampleBuilder();

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_META_PATH_FILE_NAME);

        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes());
        manifestBuilder.withSource(TOSCA_CHANGELOG_FILEPATH);

        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(SAMPLE_DEFINITION_FILE_PATH));
        manifestBuilder.withSource(TOSCA_DEFINITION_FILEPATH);

        handler.addFile(SAMPLE_DEFINITION_IMPORT_FILE_PATH, "".getBytes());
        manifestBuilder.withSource(SAMPLE_DEFINITION_IMPORT_FILE_PATH);

        manifestBuilder.withSource(TOSCA_MANIFEST_FILEPATH);
        handler.addFile(TOSCA_MANIFEST_FILEPATH, manifestBuilder.build().getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        assertTrue(errors.size() == 0);
    }

    /**
     * Main TOSCA definitions file and Manifest file with different name should return error.
     */
    @Test
    public void testGivenManifestAndMainDefinitionFile_withDifferentNames_thenErrorIsReturned() {
        metaFile =
                "TOSCA-Meta-File-Version: 1.0\n"+
                "CSAR-Version: 1.1\n"+
                "Created-By: Vendor\n"+
                TOSCA_META_ENTRY_DEFINITIONS + SEPARATOR_MF_ATTRIBUTE + "Definitions/MainServiceTemplate.yaml\n"+
                TOSCA_META_ETSI_ENTRY_MANIFEST + SEPARATOR_MF_ATTRIBUTE +"Definitions/MainServiceTemplate2.mf\n"+
                TOSCA_META_ETSI_ENTRY_CHANGE_LOG + SEPARATOR_MF_ATTRIBUTE +"Artifacts/changeLog.text\n";

        final ManifestBuilder manifestBuilder = getVnfManifestSampleBuilder();

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_META_PATH_FILE_NAME);

        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes());
        manifestBuilder.withSource(TOSCA_CHANGELOG_FILEPATH);

        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(SAMPLE_DEFINITION_FILE_PATH));
        manifestBuilder.withSource(TOSCA_DEFINITION_FILEPATH);

        handler.addFile(SAMPLE_DEFINITION_IMPORT_FILE_PATH, "".getBytes());
        manifestBuilder.withSource(SAMPLE_DEFINITION_IMPORT_FILE_PATH);

        manifestBuilder.withSource("Definitions/MainServiceTemplate2.mf");
        handler.addFile("Definitions/MainServiceTemplate2.mf", manifestBuilder.build().getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        assertExpectedErrors("Main TOSCA definitions file and Manifest file with different name should return error",
               errors, 1);
    }

    @Test
    public void testGivenManifestFile_withDifferentExtension_thenErrorIsReturned() {
        metaFile =
                "TOSCA-Meta-File-Version: 1.0\n"+
                "CSAR-Version: 1.1\n"+
                "Created-By: Vendor\n"+
                "Entry-Definitions: Definitions/MainServiceTemplate.yaml\n"+
                TOSCA_META_ETSI_ENTRY_MANIFEST + SEPARATOR_MF_ATTRIBUTE +  "Definitions/MainServiceTemplate.txt\n"+
                TOSCA_META_ETSI_ENTRY_CHANGE_LOG + SEPARATOR_MF_ATTRIBUTE + "Artifacts/changeLog.text\n";

        final ManifestBuilder manifestBuilder = getVnfManifestSampleBuilder();

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_META_PATH_FILE_NAME);

        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes());
        manifestBuilder.withSource(TOSCA_CHANGELOG_FILEPATH);

        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(SAMPLE_DEFINITION_FILE_PATH));
        manifestBuilder.withSource(TOSCA_DEFINITION_FILEPATH);

        handler.addFile(SAMPLE_DEFINITION_IMPORT_FILE_PATH, "".getBytes());
        manifestBuilder.withSource(SAMPLE_DEFINITION_IMPORT_FILE_PATH);

        manifestBuilder.withSource("Definitions/MainServiceTemplate.txt");
        handler.addFile("Definitions/MainServiceTemplate.txt", manifestBuilder.build().getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        assertExpectedErrors("Manifest file with different extension than .mf should return error",
                errors, 1);
    }

    @Test
    public void testGivenManifestFile_withValidVnfMetadata_thenNoErrorsReturned() {
        final ManifestBuilder manifestBuilder = getVnfManifestSampleBuilder();

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_META_PATH_FILE_NAME);
        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes());
        manifestBuilder.withSource(TOSCA_CHANGELOG_FILEPATH);
        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(SAMPLE_DEFINITION_FILE_PATH));
        manifestBuilder.withSource(TOSCA_DEFINITION_FILEPATH);

        manifestBuilder.withSource(TOSCA_MANIFEST_FILEPATH);
        handler.addFile(TOSCA_MANIFEST_FILEPATH, manifestBuilder.build().getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        assertExpectedErrors("Manifest with valid vnf mandatory values should not return any errors", errors, 0);
    }

    @Test
    public void testGivenManifestFile_withValidPnfMetadata_thenNoErrorsReturned() {
        final ManifestBuilder manifestBuilder = getPnfManifestSampleBuilder();

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_META_PATH_FILE_NAME);

        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes());
        manifestBuilder.withSource(TOSCA_CHANGELOG_FILEPATH);

        manifestBuilder.withSignedSource(TOSCA_DEFINITION_FILEPATH
            , "SHA-abc", "09e5a788acb180162c51679ae4c998039fa6644505db2415e35107d1ee213943");
        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(SAMPLE_DEFINITION_FILE_PATH));

        manifestBuilder.withSource(TOSCA_MANIFEST_FILEPATH);
        handler.addFile(TOSCA_MANIFEST_FILEPATH, manifestBuilder.build().getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        assertExpectedErrors("Manifest with valid pnf mandatory values should not return any errors", errors, 0);
    }

    /**
     * Manifest with mixed metadata should return error.
     */
    @Test
    public void testGivenManifestFile_withMetadataContainingMixedPnfVnfMetadata_thenErrorIsReturned() {
        final ManifestBuilder manifestBuilder = new ManifestBuilder()
            .withMetaData(PNFD_NAME, "RadioNode")
            .withMetaData(VNF_PROVIDER_ID, "Bilal Iqbal")
            .withMetaData(PNFD_ARCHIVE_VERSION, "1.0")
            .withMetaData(VNF_RELEASE_DATE_TIME, "2019-12-14T11:25:00+00:00");

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_META_PATH_FILE_NAME);
        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes());
        manifestBuilder.withSource(TOSCA_CHANGELOG_FILEPATH);
        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(SAMPLE_DEFINITION_FILE_PATH));
        manifestBuilder.withSource(TOSCA_DEFINITION_FILEPATH);

        manifestBuilder.withSource(TOSCA_MANIFEST_FILEPATH);
        handler.addFile(TOSCA_MANIFEST_FILEPATH, manifestBuilder.build().getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        assertExpectedErrors("Manifest with mixed metadata should return error", errors, 1);
    }


    @Test
    public void testGivenManifestFile_withMetadataMissingPnfOrVnfMandatoryEntries_thenErrorIsReturned() {
        final ManifestBuilder manifestBuilder = new ManifestBuilder()
            .withMetaData("invalid_product_name", "RadioNode")
            .withMetaData("invalid_provider_id", "Bilal Iqbal")
            .withMetaData("invalid_package_version", "1.0")
            .withMetaData("invalid_release_date_time", "2019-12-14T11:25:00+00:00");

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_META_PATH_FILE_NAME);

        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes());
        manifestBuilder.withSource(TOSCA_CHANGELOG_FILEPATH);

        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(SAMPLE_DEFINITION_FILE_PATH));
        manifestBuilder.withSource(TOSCA_DEFINITION_FILEPATH);

        manifestBuilder.withSource(TOSCA_MANIFEST_FILEPATH);
        handler.addFile(TOSCA_MANIFEST_FILEPATH, manifestBuilder.build().getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        assertExpectedErrors("Manifest with missing vnf or pnf mandatory entries should return error", errors, 1);
    }

    @Test
    public void testGivenManifestFile_withMetadataMissingMandatoryPnfEntries_thenErrorIsReturned() {
        final ManifestBuilder manifestBuilder = new ManifestBuilder();

        manifestBuilder.withMetaData(PNFD_NAME, "RadioNode");
        manifestBuilder.withMetaData(PNFD_RELEASE_DATE_TIME, "2019-12-14T11:25:00+00:00");

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_META_PATH_FILE_NAME);

        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes());
        manifestBuilder.withSource(TOSCA_CHANGELOG_FILEPATH);

        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(SAMPLE_DEFINITION_FILE_PATH));
        manifestBuilder.withSource(TOSCA_DEFINITION_FILEPATH);

        manifestBuilder.withSource(TOSCA_MANIFEST_FILEPATH);
        handler.addFile(TOSCA_MANIFEST_FILEPATH, manifestBuilder.build().getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        assertExpectedErrors("Manifest with metadata missing pnf mandatory entries should return error", errors, 3);

    }

    @Test
    public void testGivenManifestFile_withMetadataMissingMandatoryVnfEntries_thenErrorIsReturned() {
        final ManifestBuilder manifestBuilder = new ManifestBuilder();

        manifestBuilder.withMetaData(VNF_PRODUCT_NAME, "RadioNode");

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_META_PATH_FILE_NAME);

        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes());
        manifestBuilder.withSource(TOSCA_CHANGELOG_FILEPATH);

        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(SAMPLE_DEFINITION_FILE_PATH));
        manifestBuilder.withSource(TOSCA_DEFINITION_FILEPATH);

        manifestBuilder.withSource(TOSCA_MANIFEST_FILEPATH);
        handler.addFile(TOSCA_MANIFEST_FILEPATH, manifestBuilder.build().getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        assertExpectedErrors("Manifest with metadata missing vnf mandatory entries should return error", errors, 4);

    }

    /**
     * Manifest with more than 4 metadata entries should return error.
     */
    @Test
    public void testGivenManifestFile_withMetadataEntriesExceedingTheLimit_thenErrorIsReturned() {
        final ManifestBuilder manifestBuilder = getVnfManifestSampleBuilder()
            .withMetaData(PNFD_NAME, "RadioNode")
            .withMetaData(PNFD_PROVIDER, "Bilal Iqbal")
            .withMetaData(PNFD_ARCHIVE_VERSION, "1.0")
            .withMetaData(PNFD_RELEASE_DATE_TIME, "2019-03-11T11:25:00+00:00");

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_META_PATH_FILE_NAME);

        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes());
        manifestBuilder.withSource(TOSCA_CHANGELOG_FILEPATH);

        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(SAMPLE_DEFINITION_FILE_PATH));
        manifestBuilder.withSource(TOSCA_DEFINITION_FILEPATH);

        manifestBuilder.withSource(TOSCA_MANIFEST_FILEPATH);
        handler.addFile(TOSCA_MANIFEST_FILEPATH, manifestBuilder.build().getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());
        assertExpectedErrors("Manifest with more than 4 metadata entries should return error", errors, 2);
    }

    @Test
    public void testGivenManifestFile_withPnfMetadataAndVfEntries_thenErrorIsReturned() {
        final ManifestBuilder manifestBuilder = getPnfManifestSampleBuilder();

        metaFile = metaFile +
            TOSCA_META_ETSI_ENTRY_TESTS + SEPARATOR_MF_ATTRIBUTE + "Files/Tests\n" +
            TOSCA_META_ETSI_ENTRY_LICENSES + SEPARATOR_MF_ATTRIBUTE + "Files/Licenses\n" +
            TOSCA_META_ETSI_ENTRY_CERTIFICATE + SEPARATOR_MF_ATTRIBUTE + "Files/Certificates";

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_META_PATH_FILE_NAME);

        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes());
        manifestBuilder.withSource(TOSCA_CHANGELOG_FILEPATH);

        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(SAMPLE_DEFINITION_FILE_PATH));
        manifestBuilder.withSource(TOSCA_DEFINITION_FILEPATH);

        manifestBuilder.withSource(TOSCA_MANIFEST_FILEPATH);
        handler.addFile(TOSCA_MANIFEST_FILEPATH, manifestBuilder.build().getBytes(StandardCharsets.UTF_8));

        final List<String> folderList = new ArrayList<>();
        folderList.add("Files/Certificates/");
        final Map<String, List<ErrorMessage>> errors = sol004MetaDirectoryValidator.validateContent(handler, folderList);
        assertExpectedErrors("Tosca.meta should not have entries applicable only to VF", errors, 2);

    }

    /**
     * Tests an imported descriptor with a missing imported file.
     */
    @Test
    public void testGivenDefinitionFileWithImportedDescriptor_whenImportedDescriptorImportsMissingFile_thenMissingImportErrorOccur() throws IOException {
        final ManifestBuilder manifestBuilder = getVnfManifestSampleBuilder();

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_META_PATH_FILE_NAME);

        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_CHANGELOG_FILEPATH);

        handler.addFile(SAMPLE_SOURCE, "".getBytes());
        manifestBuilder.withSource(SAMPLE_SOURCE);

        final String definitionImportOne = "Definitions/importOne.yaml";
        handler.addFile(definitionImportOne, getResourceBytes("/validation.files/definition/sampleDefinitionFile2.yaml"));
        manifestBuilder.withSource(definitionImportOne);

        final String definitionFileWithValidImports = "/validation.files/definition/definitionFileWithOneImport.yaml";
        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(definitionFileWithValidImports));
        manifestBuilder.withSource(TOSCA_DEFINITION_FILEPATH);

        manifestBuilder.withSource(TOSCA_MANIFEST_FILEPATH);
        handler.addFile(TOSCA_MANIFEST_FILEPATH, manifestBuilder.build().getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> actualErrorMap = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());

        final List<ErrorMessage> expectedErrorList = new ArrayList<>();
        expectedErrorList.add(new ErrorMessage(ErrorLevel.ERROR
            , Messages.MISSING_IMPORT_FILE.formatMessage("Definitions/etsi_nfv_sol001_pnfd_2_5_2_types.yaml"))
        );

        assertExpectedErrors(actualErrorMap.get(SdcCommon.UPLOAD_FILE), expectedErrorList);
    }

    /**
     * Tests an imported descriptor with invalid import statement.
     */
    @Test
    public void testGivenDefinitionFileWithImportedDescriptor_whenInvalidImportStatementExistInImportedDescriptor_thenInvalidImportErrorOccur() {
        final ManifestBuilder manifestBuilder = getVnfManifestSampleBuilder();

        handler.addFile(TOSCA_META_PATH_FILE_NAME, metaFile.getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_META_PATH_FILE_NAME);

        handler.addFile(TOSCA_CHANGELOG_FILEPATH, "".getBytes(StandardCharsets.UTF_8));
        manifestBuilder.withSource(TOSCA_CHANGELOG_FILEPATH);

        handler.addFile(SAMPLE_SOURCE, "".getBytes());
        manifestBuilder.withSource(SAMPLE_SOURCE);

        final String definitionImportOne = "Definitions/importOne.yaml";
        handler.addFile(definitionImportOne, getResourceBytes("/validation.files/definition/definitionFileWithInvalidImport.yaml"));
        manifestBuilder.withSource(definitionImportOne);

        final String definitionFileWithValidImports = "/validation.files/definition/definitionFileWithOneImport.yaml";
        handler.addFile(TOSCA_DEFINITION_FILEPATH, getResourceBytes(definitionFileWithValidImports));
        manifestBuilder.withSource(TOSCA_DEFINITION_FILEPATH);

        manifestBuilder.withSource(TOSCA_MANIFEST_FILEPATH);
        handler.addFile(TOSCA_MANIFEST_FILEPATH, manifestBuilder.build().getBytes(StandardCharsets.UTF_8));

        final Map<String, List<ErrorMessage>> actualErrorMap = sol004MetaDirectoryValidator.validateContent(handler, Collections.emptyList());

        final List<ErrorMessage> expectedErrorList = new ArrayList<>();
        expectedErrorList.add(new ErrorMessage(ErrorLevel.ERROR
            , Messages.INVALID_IMPORT_STATEMENT.formatMessage(definitionImportOne, "null"))
        );

        assertExpectedErrors(actualErrorMap.get(SdcCommon.UPLOAD_FILE), expectedErrorList);
    }

    private void assertExpectedErrors(final String testCase, final Map<String, List<ErrorMessage>> errors, final int expectedErrors){
        final List<ErrorMessage> errorMessages = errors.get(SdcCommon.UPLOAD_FILE);
        printErrorMessages(errorMessages);
        if (expectedErrors > 0) {
            assertEquals(testCase, errorMessages.size(), expectedErrors);
        } else {
            assertEquals(testCase, errors.size(), expectedErrors);
        }
    }

    private void printErrorMessages(final List<ErrorMessage> errorMessages) {
        if (CollectionUtils.isNotEmpty(errorMessages)) {
            errorMessages.forEach(errorMessage -> {
                System.out.println(String.format("%s: %s", errorMessage.getLevel(), errorMessage.getMessage()));
            });
        }
    }

    private byte[] getResourceBytes(final String resourcePath) {
        try {
            return ValidatorUtil.getFileResource(resourcePath);
        } catch (final IOException e) {
            final String errorMsg = String.format("Could not load resource '%s'", resourcePath);
            LOGGER.error(errorMsg, e);
            fail(errorMsg);
        }

        return null;
    }

    private ManifestBuilder getPnfManifestSampleBuilder() {
        return new ManifestBuilder()
            .withMetaData(PNFD_NAME, "myPnf")
            .withMetaData(PNFD_PROVIDER, "ACME")
            .withMetaData(PNFD_ARCHIVE_VERSION, "1.0")
            .withMetaData(PNFD_RELEASE_DATE_TIME, "2019-03-11T11:25:00+00:00");
    }

    private ManifestBuilder getVnfManifestSampleBuilder() {
        return new ManifestBuilder()
            .withMetaData(VNF_PRODUCT_NAME, "RadioNode")
            .withMetaData(VNF_PROVIDER_ID, "ACME")
            .withMetaData(VNF_PACKAGE_VERSION, "1.0")
            .withMetaData(VNF_RELEASE_DATE_TIME, "2019-03-11T11:25:00+00:00");
    }

    private void assertExpectedErrors(List<ErrorMessage> actualErrorList, final List<ErrorMessage> expectedErrorList) {
        if (actualErrorList == null) {
            actualErrorList = new ArrayList<>();
        }

        assertThat("The actual error list should have the same size as the expected error list"
            , actualErrorList, hasSize(expectedErrorList.size())
        );

        assertThat("The actual error and expected error lists should be the same"
            , actualErrorList, containsInAnyOrder(expectedErrorList.toArray(new ErrorMessage[0]))
        );
    }

}