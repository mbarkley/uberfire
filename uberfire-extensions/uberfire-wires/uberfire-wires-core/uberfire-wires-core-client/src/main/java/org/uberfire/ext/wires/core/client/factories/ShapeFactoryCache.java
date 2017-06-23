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
package org.uberfire.ext.wires.core.client.factories;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.uberfire.commons.validation.PortablePreconditions;
import org.uberfire.ext.wires.core.api.factories.ShapeFactory;

/**
 * A cache of Factories
 */
@ApplicationScoped
public class ShapeFactoryCache {

    @Inject
    @Any
    private ManagedInstance<ShapeFactory<?>> shapeFactoryProvider;

    private Set<ShapeFactory> factories = new HashSet<>();

    @PostConstruct
    private void setup() {
        this.factories = getAvailableFactories();
    }

    public Set<ShapeFactory> getShapeFactories() {
        return Collections.unmodifiableSet(factories);
    }

    public void addShapeFactory(final ShapeFactory factory) {
        factories.add(PortablePreconditions.checkNotNull("factory",
                                                         factory));
    }

    private Set<ShapeFactory> getAvailableFactories() {
        final Set<ShapeFactory> factories = new HashSet<>();
        for (ShapeFactory bean : shapeFactoryProvider) {
            factories.add(bean);
        }
        return factories;
    }
}
