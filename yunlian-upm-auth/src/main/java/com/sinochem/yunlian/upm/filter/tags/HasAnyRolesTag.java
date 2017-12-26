/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.sinochem.yunlian.upm.filter.tags;

import com.sinochem.yunlian.upm.filter.util.UserUtils;
import org.springframework.util.CollectionUtils;

import java.util.Map;


/**
 * Displays body content if the current user has any of the roles specified.
 *
 * @since 0.2
 */
public class HasAnyRolesTag extends RoleTag {

    //TODO - complete JavaDoc

    // Delimeter that separates role names in tag attribute
    private static final String ROLE_NAMES_DELIMETER = ",";

    public HasAnyRolesTag() {
    }

    protected boolean showTagBody(String roleNames) {
        Map<String,String> roles = UserUtils.getRolesAsMap();
        if(CollectionUtils.isEmpty(roles)){
            return false;
        }

        for (String role : roleNames.split(ROLE_NAMES_DELIMETER)) {
            if(roles.containsKey(role)) {
                return true;
            }

        }

        return false;
    }

}
