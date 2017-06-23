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

package org.uberfire.ext.security.management.client.widgets.management.editor.acl.node;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.uberfire.security.client.authz.tree.PermissionNode;
import org.uberfire.security.client.authz.tree.impl.PermissionLeafNode;

@ApplicationScoped
public class PermissionWidgetFactory {

    private ManagedInstance<LeafPermissionNodeEditor> leafNodeEditor;
    private ManagedInstance<MultiplePermissionNodeEditor> multipleNodeEditor;
    private ManagedInstance<LeafPermissionNodeViewer> leafNodeViewer;
    private ManagedInstance<MultiplePermissionNodeViewer> multipleNodeViewer;
    private ManagedInstance<PermissionSwitch> permissionSwitch;
    private ManagedInstance<PermissionExceptionSwitch> permissionExceptionSwitch;

    @Inject
    public PermissionWidgetFactory(ManagedInstance<LeafPermissionNodeEditor> leafNodeEditor,
                                   ManagedInstance<MultiplePermissionNodeEditor> multipleNodeEditor,
                                   ManagedInstance<LeafPermissionNodeViewer> leafNodeViewer,
                                   ManagedInstance<MultiplePermissionNodeViewer> multipleNodeViewer,
                                   ManagedInstance<PermissionSwitch> permissionSwitch,
                                   ManagedInstance<PermissionExceptionSwitch> permissionExceptionSwitch) {
                                    this.leafNodeEditor = leafNodeEditor;
                                    this.multipleNodeEditor = multipleNodeEditor;
                                    this.leafNodeViewer = leafNodeViewer;
                                    this.multipleNodeViewer = multipleNodeViewer;
                                    this.permissionSwitch = permissionSwitch;
                                    this.permissionExceptionSwitch = permissionExceptionSwitch;
    }

    public PermissionNodeEditor createEditor(PermissionNode node) {
        if (node instanceof PermissionLeafNode) {
            return leafNodeEditor.get();
        }
        return multipleNodeEditor.get();
    }

    public PermissionNodeViewer createViewer(PermissionNode node) {
        if (node instanceof PermissionLeafNode) {
            return leafNodeViewer.get();
        }
        return multipleNodeViewer.get();
    }

    public PermissionSwitch createSwitch() {
        return permissionSwitch.get();
    }

    public PermissionExceptionSwitch createExceptionSwitch() {
        return permissionExceptionSwitch.get();
    }
}
