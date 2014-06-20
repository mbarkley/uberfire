/**
 * Copyright (C) 2014 JBoss Inc
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
package org.dashbuilder.client.gallery;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;

/**
 * Base class for all gallery nodes.
 */
public abstract class GalleryNode  {

    protected String name = null;
    protected List<GalleryNode> children = null;
    protected Widget widget = null;

    public GalleryNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<GalleryNode> getChildren() {
        return children;
    }

    public Widget getWidget() {
        return createWidget();
    }

    protected abstract Widget createWidget();
}
