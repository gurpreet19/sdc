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

package org.openecomp.sdc.be.model.catalog;

import com.google.common.collect.ImmutableList;
import org.openecomp.sdc.be.datatypes.enums.ComponentTypeEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class CatalogComponent {

    private String version;
    private ComponentTypeEnum componentType;
    private String icon;
    private String uniqueId;
    private String lifecycleState;
    private long lastUpdateDate;
    private String name;
    private String resourceType;
    private String categoryNormalizedName;
    private String subCategoryNormalizedName;
    private String distributionStatus;
    private List<String> tags;

    public String getCategoryNormalizedName() {
        return categoryNormalizedName;
    }

    public void setCategoryNormalizedName(String categoryNormalizedName) {
        this.categoryNormalizedName = categoryNormalizedName;
    }

    public String getSubCategoryNormalizedName() {
        return subCategoryNormalizedName;
    }

    public void setSubCategoryNormalizedName(String subCategoryNormalizedName) {
        this.subCategoryNormalizedName = subCategoryNormalizedName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(long lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setComponentType(ComponentTypeEnum componentType) {
        this.componentType = componentType;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getVersion() {
        return version;
    }

    public ComponentTypeEnum getComponentType() {
        return componentType;
    }

    public String getIcon() {
        return icon;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getLifecycleState() {
        return lifecycleState;
    }

    public void setLifecycleState(String lifecycleState) {
        this.lifecycleState = lifecycleState;
    }

    public String getDistributionStatus() {
        return distributionStatus;
    }

    public void setDistributionStatus(String distributionStatus) {
        this.distributionStatus = distributionStatus;
    }

    public List<String> getTags() {
        return tags == null ? Collections.emptyList() : ImmutableList.copyOf(tags);
    }

    public void setTags(List<String> tags) {
        requireNonNull(tags);
        this.tags = new ArrayList<>(tags);
    }
}
