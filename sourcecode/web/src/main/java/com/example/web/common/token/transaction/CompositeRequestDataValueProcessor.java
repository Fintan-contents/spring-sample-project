/*
 * Originally distributed by NTT DATA Corporation under Apache License, Version 2.0.
 *    Library: TERASOLUNA Server Framework for Java (5.x) Common Library
 *    Source: https://github.com/terasolunaorg/terasoluna-gfw/blob/5.7.1.SP1.RELEASE/terasoluna-gfw-common-libraries/terasoluna-gfw-web/src/main/java/org/terasoluna/gfw/web/mvc/support/CompositeRequestDataValueProcessor.java
 *
 * Modified by TIS Inc.
 */
/*
 * Copyright(c) 2013 NTT DATA Corporation.
 * Copyright(c) 2022 TIS Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.example.web.common.token.transaction;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.support.RequestDataValueProcessor;

/**
 * Concrete implementation class for {@link RequestDataValueProcessor}, used when <br>
 * multiple implementations of {@link RequestDataValueProcessor} are to be applied.<br>
 * <p>
 * This class is like a list of other {@link RequestDataValueProcessor} implementations.
 * </p>
 */
public class CompositeRequestDataValueProcessor implements
        RequestDataValueProcessor {

    /**
     * List of {@link RequestDataValueProcessor}
     */
    private final List<RequestDataValueProcessor> processors;

    /**
     * Reversed list of {@link RequestDataValueProcessor}
     */
    private final List<RequestDataValueProcessor> reversedProcessors;

    /**
     * Constructor<br>
     * <p>
     * Sets and initializes a list of {@link RequestDataValueProcessor}
     * </p>
     * @param processors List of {@link RequestDataValueProcessor}
     */
    public CompositeRequestDataValueProcessor(
            RequestDataValueProcessor... processors) {

        this.processors = Collections
                .unmodifiableList(Arrays
                        .asList(
                                processors));
        List<RequestDataValueProcessor> reverse = Arrays.asList(processors);
        Collections.reverse(reverse);
        this.reversedProcessors = Collections.unmodifiableList(reverse);
    }

    /**
     * Calls the {@code processAction()} method of all the {@link RequestDataValueProcessor} implementations <br>
     * this class holds.
     * @param request the current request
     * @param action action of form tag. must not be null.
     * @param method http method of form tag.
     * @see org.springframework.web.servlet.support.RequestDataValueProcessor#processAction(jakarta.servlet.http.HttpServletRequest,
     *      java.lang.String, java.lang.String)
     * @since 1.0.2 for Spring 4 or higher
     */
    @Override
    public String processAction(HttpServletRequest request, String action,
            String method) {

        String result = action;
        for (RequestDataValueProcessor processor : processors) {
            result = processor.processAction(request, action, method);
            if (!action.equals(result)) {
                break;
            }
        }

        return result;
    }

    /**
     * Calls the {@code processFormFieldValue()} method of all the {@link RequestDataValueProcessor} implementations <br>
     * this class holds.
     * @param request the current request
     * @param name the form field name
     * @param value the form field value.must not be null.
     * @param type the form field type ("text", "hidden", etc.)
     * @see org.springframework.web.servlet.support.RequestDataValueProcessor#processFormFieldValue(jakarta.servlet.http.HttpServletRequest,
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String processFormFieldValue(HttpServletRequest request, String name,
            String value, String type) {

        String result = value;
        for (RequestDataValueProcessor processor : processors) {
            result = processor
                    .processFormFieldValue(request, name, value,
                            type);
            if (!value.equals(result)) {
                break;
            }
        }

        return result;
    }

    /**
     * Calls the {@code getExtraHiddenFields()} method of all the {@link RequestDataValueProcessor} implementations <br>
     * this class holds.
     * @param request the current request
     * @see org.springframework.web.servlet.support.RequestDataValueProcessor#getExtraHiddenFields(jakarta.servlet.http.HttpServletRequest)
     */
    @Override
    public Map<String, String> getExtraHiddenFields(
            HttpServletRequest request) {
        Map<String, String> result = new LinkedHashMap<>();
        for (RequestDataValueProcessor processor : reversedProcessors) {
            Map<String, String> map = processor.getExtraHiddenFields(request);
            if (map != null) {
                result.putAll(map);
            }
        }
        return result;
    }

    /**
     * Calls the {@code processUrl()} method of all the {@link RequestDataValueProcessor} implementations <br>
     * this class holds.
     * @param request the current request
     * @param url the URL value.must not be null.
     * @see org.springframework.web.servlet.support.RequestDataValueProcessor#processUrl(jakarta.servlet.http.HttpServletRequest,
     *      java.lang.String)
     */
    @Override
    public String processUrl(HttpServletRequest request, String url) {
        String result = url;
        for (RequestDataValueProcessor processor : processors) {
            result = processor.processUrl(request, url);
            if (!url.equals(result)) {
                break;
            }
        }
        return result;
    }
}
