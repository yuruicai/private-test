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
import com.sinochem.yunlian.upm.tools.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @since 0.1
 */
public abstract class SecureTag extends TagSupport {

    //TODO - complete JavaDoc

    private static final Logger log = LoggerFactory.getLogger(SecureTag.class);

    public SecureTag() {
    }

    protected User getUser() {
        return UserUtils.getUser();
    }

    protected JspContext getContext(){
        return getContext();
    }

    protected void verifyAttributes() throws JspException {
    }

    public int doStartTag() throws JspException {

        verifyAttributes();

        return onDoStartTag();
    }

    public abstract int onDoStartTag() throws JspException;
}
