/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ext.uberfire.social.activities.client.widgets.item.bundle;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;

import org.jboss.errai.ioc.client.api.ManagedInstance;

@Dependent
public class SocialBundleHelper {

    @Inject
    private ManagedInstance<SocialBundleService> bundleServiceProvider;

    public String getItemDescription(final String key) {
        String value = null;

        for (SocialBundleService service : bundleServiceProvider) {
            try {
                value = getTranslationFromService(key,
                                                  value,
                                                  service);
            } catch (DuplicatedTranslationException e) {
                GWT.log(e.getMessage());
                break;
            }
            bundleServiceProvider.destroy(service);
        }

        return value != null ? value : key;
    }

    static String getTranslationFromService(final String key,
                                            final String currentValue,
                                            final SocialBundleService service) throws DuplicatedTranslationException {
        final String translation = service.getTranslation(key);
        String value;

        if (translation != null) {
            if (currentValue == null) {
                value = translation;
            } else {
                throw new DuplicatedTranslationException(key);
            }
        } else {
            value = currentValue;
        }

        return value;
    }
}
