/*
 * Copyright 2015 JBoss, by Red Hat, Inc
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

package org.uberfire.ext.editor.commons.client.file;

import javax.enterprise.context.Dependent;

import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.uberfire.backend.vfs.ObservablePath;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.PathFactory;

@Dependent
public class RestoreUtil {

    private ManagedInstance<ObservablePath> pathProvider;

    public RestoreUtil(ManagedInstance<ObservablePath> pathProvider) {
        this.pathProvider = pathProvider;
    }

    public ObservablePath createObservablePath(Path path,
                                               String uri) {
        return pathProvider.get().wrap(
                PathFactory.newPathBasedOn(path.getFileName(),
                                           uri,
                                           path));
    }
}
