// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds;

import java.io.Reader;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

import com.github.tasogare.json.ds.datatype.driver.DatatypeSchemaProcessorTestDriver;

/**
 * 
 * @author tasogare
 *
 * @param <M>
 */
public class TranslationUnit<M extends MetaObject<?>> {

    /**
     * static semantics errorが発生した時のためのリポーター
     * 
     * @author tasogare
     * @see TranslationUnit#getContext(MetaObject, StaticSemanticsErrorReporter,
     *      RuntimeSemanticsErrorReporter)
     *
     */
    @FunctionalInterface
    public static interface StaticSemanticsErrorReporter {
        void reportError(StaticSemanticsException error);
    }

    /**
     * runtime semantics errorが発生した時のためのリポーター
     * 
     * @author tasogare
     * @see TranslationUnit#getContext(MetaObject, StaticSemanticsErrorReporter,
     *      RuntimeSemanticsErrorReporter)
     *
     */
    @FunctionalInterface
    public static interface RuntimeSemanticsErrorReporter {
        void reportError(RuntimeSemanticsException error);
    }

    /**
     * {@value このクラスが利用するプロセッサのクラス名}
     */
    private static final String className = System
        .getProperty("com.github.tasogare.json.ds.translation_unit.processor",
                     "com.github.tasogare.json.ds.datatype.driver.JsonDsProcessorHoistableTestDriver");

    private final Reader jsds;
    private final URL sourceName;

    public TranslationUnit(Reader jsds, URL sourceName) {
        this.jsds = jsds;
        this.sourceName = sourceName;
    }

    public Optional<M> getContext(M global, StaticSemanticsErrorReporter sser, RuntimeSemanticsErrorReporter rser)
        throws NullPointerException, ReflectiveOperationException, IllegalArgumentException, SecurityException
    {
        sser = Objects.requireNonNull(sser);
        rser = Objects.requireNonNull(rser);

        @SuppressWarnings("unchecked")
        final Class<? extends DatatypeSchemaProcessorTestDriver> clazz = (Class<? extends DatatypeSchemaProcessorTestDriver>) Class
            .forName(className);
        final DatatypeSchemaProcessorTestDriver p = clazz.getConstructor(MetaObject.class).newInstance(global);
        try {
            p.process(jsds, sourceName);
        } catch (RuntimeSemanticsException e) {
            rser.reportError(e);
            return Optional.empty();
        } catch (StaticSemanticsException e) {
            sser.reportError(e);
            return Optional.empty();
        }
        @SuppressWarnings("unchecked")
        final Optional<M> metaObject = Optional.of((M) p.getMetaObjects());
        return metaObject;
    }
}
