// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.github.tasogare.json.ds.tests.AllTest.newReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tasogare.json.ds.MetaObject;
import com.github.tasogare.json.ds.RuntimeSemanticsException;
import com.github.tasogare.json.ds.StaticSemanticsException;
import com.github.tasogare.json.ds.datatype.driver.JsonDsProcessorTestDriver;

public class JsonDsProcessorTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws IOException, RuntimeSemanticsException, StaticSemanticsException {
        final String name = "com/github/tasogare/json/ds/datatype/resources/layer/layer.jsds";
        try (final BufferedReader r = newReader(name, getClass())) {
            final JsonDsProcessorTestDriver processor = new JsonDsProcessorTestDriver();
            processor.process(r, getClass().getClassLoader().getResource(name));
            final Type json = processor.getMetaObjects().getMetaObject("JSON");
            System.out.println(json);
        }
    }

    @Test
    public void test2() throws IOException, RuntimeSemanticsException, StaticSemanticsException {
        final String source = "use standard; type JSON = [number, string, boolean, ...number]";
        final JsonDsProcessorTestDriver processor = new JsonDsProcessorTestDriver();
        processor.process(source, null);

        final String name = "com/github/tasogare/json/ds/datatype/resources/mixed.json";
        try (final BufferedReader r = newReader(name, getClass())) {
            final JsonStructure json = Json.createReader(r).read();
            final MetaObject<JsonValue> typeSystem = processor.getMetaObjects();
            final Type jsonType = typeSystem.getMetaObject("JSON");

            assertTrue(typeSystem.is(json, jsonType));
        }
    }

    @Test
    public void test3() throws IOException, RuntimeSemanticsException, StaticSemanticsException {
        final String source = "use standard; type JSON = [...*!]";
        final JsonDsProcessorTestDriver processor = new JsonDsProcessorTestDriver();
        processor.process(source, null);

        try (final JsonReader r = Json.createReader(new BufferedReader(new StringReader("[null]")))) {
            final JsonStructure json = r.read();
            final MetaObject<JsonValue> typeSystem = processor.getMetaObjects();
            final Type jsonType = typeSystem.getMetaObject("JSON");

            assertFalse(typeSystem.is(json, jsonType));
        }
    }

    @Test
    public void testInvalid() throws IOException, RuntimeSemanticsException, StaticSemanticsException {
        final String source = "use standard; type JSON = {\"first\": string, \"last\": string, \"age\": number}";
        final JsonDsProcessorTestDriver processor = new JsonDsProcessorTestDriver();
        processor.process(source, null);

        final String name = "com/github/tasogare/json/ds/datatype/resources/Person.json";
        try (final JsonReader r = Json.createReader(newReader(name, getClass()))) {
            final JsonStructure json = r.read();
            final MetaObject<JsonValue> typeSystem = processor.getMetaObjects();
            final Type jsonType = typeSystem.getMetaObject("JSON");

            assertFalse(typeSystem.is(json, jsonType));
        }
    }

    @Test
    public void testLayerForLayerWithColor() throws IOException, RuntimeSemanticsException, StaticSemanticsException {
        final long old = System.nanoTime();

        final String jsdsFile = "com/github/tasogare/json/ds/datatype/resources/layer/layer.jsds";
        final URL url = getClass().getClassLoader().getResource(jsdsFile);
        try (BufferedReader r = newReader(url)) {
            final JsonDsProcessorTestDriver processor = new JsonDsProcessorTestDriver();
            processor.process(r, url);

            final String jsonFile = "com/github/tasogare/json/ds/datatype/resources/layer/LayerForLayerWithColor.json";
            try (final JsonReader jsonReader = Json.createReader(newReader(jsonFile, getClass()))) {
                final JsonStructure json = jsonReader.read();
                final MetaObject<JsonValue> typeSystem = processor.getMetaObjects();
                final Type jsonType = typeSystem.getMetaObject("JSON");

                assertTrue(typeSystem.is(json, jsonType));
            }
        }

        System.out.println("LayerForLayerWithColor: " + (System.nanoTime() - old) + " ns");
    }

    @Test
    public void testLayerIncluded() throws IOException, RuntimeSemanticsException, StaticSemanticsException {
        final long old = System.nanoTime();

        final String jsdsFile = "com/github/tasogare/json/ds/datatype/resources/layer/include/layerSplit.jsds";
        final URL url = getClass().getClassLoader().getResource(jsdsFile);
        try (final BufferedReader r = newReader(url)) {
            final JsonDsProcessorTestDriver processor = new JsonDsProcessorTestDriver();
            processor.process(r, url);
        }

        System.out.println("Layer + including test: " + (System.nanoTime() - old) + " ns");
    }

    @Test
    public void testLayerWithLinearGradient() throws IOException, RuntimeSemanticsException, StaticSemanticsException {
        final long old = System.nanoTime();

        final String jsdsFile = "com/github/tasogare/json/ds/datatype/resources/layer/layer.jsds";
        final URL url = getClass().getClassLoader().getResource(jsdsFile);
        try (final BufferedReader r = newReader(url)) {
            final JsonDsProcessorTestDriver processor = new JsonDsProcessorTestDriver();
            processor.process(r, url);

            final String jsonFile = "com/github/tasogare/json/ds/datatype/resources/layer/LayerWithLinearGradient.json";
            try (final JsonReader jsonReader = Json.createReader(newReader(jsonFile, getClass()))) {
                final JsonStructure json = jsonReader.read();
                final MetaObject<JsonValue> typeSystem = processor.getMetaObjects();
                final Type jsonType = typeSystem.getMetaObject("JSON");

                assertTrue(typeSystem.is(json, jsonType));
            }
        }

        System.out.println("LayerWithLinearGradient: " + (System.nanoTime() - old) + " ns");
    }

    @Test
    public void testLayerWithRadialGradient() throws IOException, RuntimeSemanticsException, StaticSemanticsException {
        final long old = System.nanoTime();

        final String jsdsFile = "com/github/tasogare/json/ds/datatype/resources/layer/layer.jsds";
        final URL url = getClass().getClassLoader().getResource(jsdsFile);
        try (final BufferedReader r = newReader(url)) {
            final JsonDsProcessorTestDriver processor = new JsonDsProcessorTestDriver();
            processor.process(r, url);

            final MetaObject<JsonValue> typeSystem = processor.getMetaObjects();
            final String jsonFile = "com/github/tasogare/json/ds/datatype/resources/layer/LayerWithRadialGradient.json";
            final InputStream is2 = getClass().getClassLoader().getResourceAsStream(jsonFile);
            try (final JsonReader jsonReader = Json.createReader(new InputStreamReader(is2, StandardCharsets.UTF_8))) {
                final JsonStructure json = jsonReader.read();
                final Type jsonType = typeSystem.getMetaObject("JSON");

                assertTrue(typeSystem.is(json, jsonType));
                System.out.println("LayerWithRadialGradient: " + (System.nanoTime() - old) + " ns");
            }
        }
    }

    @Test
    public void testValid() throws IOException, RuntimeSemanticsException, StaticSemanticsException {
        final String source = "use standard; type JSON = {\"first\": string?, \"last\": string, \"age\": number}";
        final JsonDsProcessorTestDriver processor = new JsonDsProcessorTestDriver();
        processor.process(source, null);

        final String name = "com/github/tasogare/json/ds/datatype/resources/Person.json";
        try (final JsonReader r = Json.createReader(newReader(name, getClass()))) {
            final JsonStructure json = r.read();
            final MetaObject<JsonValue> typeSystem = processor.getMetaObjects();
            final Type jsonType = typeSystem.getMetaObject("JSON");

            assertTrue(typeSystem.is(json, jsonType));
        }
    }
}
