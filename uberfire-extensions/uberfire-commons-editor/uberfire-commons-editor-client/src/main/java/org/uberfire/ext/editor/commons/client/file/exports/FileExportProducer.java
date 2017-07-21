/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uberfire.ext.editor.commons.client.file.exports;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.LoadAsync;
import org.uberfire.async.UberfireActivityFragment;
import org.uberfire.ext.editor.commons.client.file.exports.jso.FileExportScriptInjector;

/**
 * The FileExport bean factory.
 * Also ensures the sources are injected on demand.
 */
@ApplicationScoped
@LoadAsync(UberfireActivityFragment.class)
public class FileExportProducer {

    private final FileExportScriptInjector fsScriptInjector;

    protected FileExportProducer() {
        this(null);
    }

    @Inject
    public FileExportProducer(final FileExportScriptInjector fsScriptInjector) {
        this.fsScriptInjector = fsScriptInjector;
    }

    @PostConstruct
    public void init() {
        fsScriptInjector.inject();
    }

    @Produces
    @LoadAsync(UberfireActivityFragment.class)
    public TextFileExport forText() {
        return new TextFileExport();
    }

    @Produces
    @LoadAsync(UberfireActivityFragment.class)
    public PdfFileExport forPDF() {
        return new PdfFileExport();
    }

    @Produces
    @LoadAsync(UberfireActivityFragment.class)
    public ImageFileExport forImage() {
        return new ImageFileExport();
    }
}
