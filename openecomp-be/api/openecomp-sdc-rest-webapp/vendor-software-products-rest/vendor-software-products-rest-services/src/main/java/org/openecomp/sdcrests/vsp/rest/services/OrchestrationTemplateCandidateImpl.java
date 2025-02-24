/*
 * Copyright © 2016-2018 European Support Limited
 *
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
 * Modifications copyright (c) 2019 Nokia
 * ================================================================================
 */

package org.openecomp.sdcrests.vsp.rest.services;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.openecomp.sdc.activitylog.ActivityLogManager;
import org.openecomp.sdc.activitylog.ActivityLogManagerFactory;
import org.openecomp.sdc.activitylog.dao.type.ActivityLogEntity;
import org.openecomp.sdc.activitylog.dao.type.ActivityType;
import org.openecomp.sdc.common.errors.Messages;
import org.openecomp.sdc.common.utils.SdcCommon;
import org.openecomp.sdc.datatypes.error.ErrorLevel;
import org.openecomp.sdc.datatypes.error.ErrorMessage;
import org.openecomp.sdc.logging.api.Logger;
import org.openecomp.sdc.logging.api.LoggerFactory;
import org.openecomp.sdc.vendorsoftwareproduct.OrchestrationTemplateCandidateManager;
import org.openecomp.sdc.vendorsoftwareproduct.OrchestrationTemplateCandidateManagerFactory;
import org.openecomp.sdc.vendorsoftwareproduct.VendorSoftwareProductManager;
import org.openecomp.sdc.vendorsoftwareproduct.VspManagerFactory;
import org.openecomp.sdc.vendorsoftwareproduct.security.SecurityManagerException;
import org.openecomp.sdc.vendorsoftwareproduct.types.OrchestrationTemplateActionResponse;
import org.openecomp.sdc.vendorsoftwareproduct.types.UploadFileResponse;
import org.openecomp.sdc.vendorsoftwareproduct.types.ValidationResponse;
import org.openecomp.sdc.vendorsoftwareproduct.types.candidateheat.FilesDataStructure;
import org.openecomp.sdc.versioning.dao.types.Version;
import org.openecomp.sdcrests.vendorsoftwareproducts.types.FileDataStructureDto;
import org.openecomp.sdcrests.vendorsoftwareproducts.types.OrchestrationTemplateActionResponseDto;
import org.openecomp.sdcrests.vendorsoftwareproducts.types.UploadFileResponseDto;
import org.openecomp.sdcrests.vendorsoftwareproducts.types.ValidationResponseDto;
import org.openecomp.sdcrests.vsp.rest.OrchestrationTemplateCandidate;
import org.openecomp.sdcrests.vsp.rest.data.PackageArchive;
import org.openecomp.sdcrests.vsp.rest.mapping.MapFilesDataStructureToDto;
import org.openecomp.sdcrests.vsp.rest.mapping.MapUploadFileResponseToUploadFileResponseDto;
import org.openecomp.sdcrests.vsp.rest.mapping.MapValidationResponseToDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.inject.Named;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.openecomp.core.utilities.file.FileUtils.getFileExtension;
import static org.openecomp.core.utilities.file.FileUtils.getNetworkPackageName;
import static org.openecomp.core.validation.errors.ErrorMessagesFormatBuilder.getErrorWithParameters;

@Named
@Service("orchestrationTemplateCandidate")
@Scope(value = "prototype")
public class OrchestrationTemplateCandidateImpl implements OrchestrationTemplateCandidate {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(OrchestrationTemplateCandidateImpl.class);
  private OrchestrationTemplateCandidateManager candidateManager =
      OrchestrationTemplateCandidateManagerFactory.getInstance().createInterface();
  private VendorSoftwareProductManager vendorSoftwareProductManager = VspManagerFactory
      .getInstance().createInterface();
  private ActivityLogManager activityLogManager =
          ActivityLogManagerFactory.getInstance().createInterface();

  @Override
  public Response upload(String vspId, String versionId, Attachment fileToUpload, String user) {
    PackageArchive archive = new PackageArchive(fileToUpload.getObject(byte[].class));
    UploadFileResponseDto uploadFileResponseDto;
    try {
      if (archive.isSigned() && !archive.isSignatureValid()) {
        ErrorMessage errorMessage = new ErrorMessage(ErrorLevel.ERROR,
                getErrorWithParameters(Messages.FAILED_TO_VERIFY_SIGNATURE.getErrorMessage(), ""));
        LOGGER.error(errorMessage.getMessage());
        uploadFileResponseDto = buildUploadResponseWithError(errorMessage);
        //returning OK as SDC UI won't show error message if NOT OK error code.
        return Response.ok(uploadFileResponseDto).build();
      }

      String filename = archive.getArchiveFileName().orElse(fileToUpload.getContentDisposition().getFilename());
      UploadFileResponse uploadFileResponse = candidateManager
              .upload(vspId, new Version(versionId), new ByteArrayInputStream(archive.getPackageFileContents()),
                      getFileExtension(filename), getNetworkPackageName(filename));

      uploadFileResponseDto = new MapUploadFileResponseToUploadFileResponseDto()
              .applyMapping(uploadFileResponse, UploadFileResponseDto.class);
    } catch (SecurityManagerException e) {
      ErrorMessage errorMessage = new ErrorMessage(ErrorLevel.ERROR,
              getErrorWithParameters(e.getMessage(), ""));
      LOGGER.error(errorMessage.getMessage(), e);
      uploadFileResponseDto = buildUploadResponseWithError(errorMessage);
      //returning OK as SDC UI won't show error message if NOT OK error code.
      return Response.ok(uploadFileResponseDto).build();
    }
    return Response.ok(uploadFileResponseDto).build();
  }

  private UploadFileResponseDto buildUploadResponseWithError(ErrorMessage errorMessage) {
    UploadFileResponseDto uploadFileResponseDto = new UploadFileResponseDto();
    Map<String, List<ErrorMessage>> errorMap = new HashMap<>();
    List<ErrorMessage> errorMessages = new ArrayList<>();
    errorMessages.add(errorMessage);
    errorMap.put(SdcCommon.UPLOAD_FILE, errorMessages);
    uploadFileResponseDto.setErrors(errorMap);
    return uploadFileResponseDto;
  }

  @Override
  public Response get(String vspId, String versionId, String user) throws IOException {
    Optional<Pair<String, byte[]>> zipFile = candidateManager.get(vspId, new Version(versionId));
    String fileName;
    if (zipFile.isPresent()) {
      fileName = "Candidate." + zipFile.get().getLeft();
    } else {
      zipFile = vendorSoftwareProductManager.get(vspId, new Version((versionId)));

      if (!zipFile.isPresent()) {
        ErrorMessage errorMessage = new ErrorMessage(ErrorLevel.ERROR,
            getErrorWithParameters(
                Messages.NO_FILE_WAS_UPLOADED_OR_FILE_NOT_EXIST.getErrorMessage(),
                ""));
        LOGGER.error(errorMessage.getMessage());
        return Response.status(Response.Status.NOT_FOUND).build();
      }
      fileName = "Processed." + zipFile.get().getLeft();
    }
    Response.ResponseBuilder response = Response.ok(zipFile.get().getRight());
    response.header("Content-Disposition", "attachment; filename=" + fileName);
    return response.build();
  }

  @Override
  public Response abort(String vspId, String versionId) {
    candidateManager.abort(vspId, new Version(versionId));
    return Response.ok().build();
  }

  @Override
  public Response process(String vspId, String versionId, String user) {

    Version version = new Version(versionId);
    OrchestrationTemplateActionResponse response = candidateManager.process(vspId, version);

    activityLogManager.logActivity(new ActivityLogEntity(vspId, version,
            ActivityType.Upload_Network_Package, user, true, "", ""));

    OrchestrationTemplateActionResponseDto responseDto = copyOrchestrationTemplateActionResponseToDto(response);

    return Response.ok(responseDto).build();
  }

  @Override
  public Response updateFilesDataStructure(
      String vspId, String versionId, FileDataStructureDto fileDataStructureDto, String user) {

    FilesDataStructure fileDataStructure = copyFilesDataStructureDtoToFilesDataStructure(fileDataStructureDto);

    ValidationResponse response = candidateManager
        .updateFilesDataStructure(vspId, new Version(versionId), fileDataStructure);

    if (!response.isValid()) {
      return Response.status(Response.Status.EXPECTATION_FAILED).entity(
          new MapValidationResponseToDto()
              .applyMapping(response, ValidationResponseDto.class)).build();
    }
    return Response.ok(fileDataStructureDto).build();
  }

  @Override
  public Response getFilesDataStructure(String vspId, String versionId, String user) {
    Optional<FilesDataStructure> filesDataStructure =
        candidateManager.getFilesDataStructure(vspId, new Version(versionId));
    if (!filesDataStructure.isPresent()) {
      filesDataStructure = vendorSoftwareProductManager.getOrchestrationTemplateStructure(vspId,
          new Version(versionId));
    }

    FileDataStructureDto fileDataStructureDto =
        filesDataStructure.map(dataStructure -> new MapFilesDataStructureToDto()
            .applyMapping(dataStructure, FileDataStructureDto.class))
            .orElse(new FileDataStructureDto());
    return Response.ok(fileDataStructureDto).build();
  }

  private OrchestrationTemplateActionResponseDto copyOrchestrationTemplateActionResponseToDto(OrchestrationTemplateActionResponse response){
    OrchestrationTemplateActionResponseDto result = new OrchestrationTemplateActionResponseDto();
    result.setErrors(response.getErrors());
    result.setFileNames(response.getFileNames());
    result.setStatus(response.getStatus());
    return result;
  }

  private FilesDataStructure copyFilesDataStructureDtoToFilesDataStructure(FileDataStructureDto fileDataStructureDto){
    FilesDataStructure filesDataStructure = new FilesDataStructure();
    filesDataStructure.setArtifacts(fileDataStructureDto.getArtifacts());
    filesDataStructure.setModules(fileDataStructureDto.getModules());
    filesDataStructure.setNested(fileDataStructureDto.getNested());
    filesDataStructure.setUnassigned(fileDataStructureDto.getUnassigned());
    return filesDataStructure;
  }

}
