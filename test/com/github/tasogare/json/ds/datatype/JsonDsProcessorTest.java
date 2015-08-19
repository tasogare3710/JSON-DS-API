// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonStructure;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tasogare.json.ds.JsonDsException;
import com.github.tasogare.json.ds.datatype.driver.JsonDsProcessorTestDriver;
import com.github.tasogare.json.ds.datatype.driver.JsonMetaObjectTestDriver;

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
    public void test() throws IOException, JsonDsException {
        String name = "com/github/tasogare/json/ds/datatype/resources/layer/layer.js";
        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        try (BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            final JsonDsProcessorTestDriver processor = new JsonDsProcessorTestDriver();
            processor.process(r, name);
            final Type json = processor.getMetaObjects().getMetaObject("JSON");
            System.out.println(json);
        }
    }

    @Test
    public void test2() throws IOException, JsonDsException {
        final String source = "use standard; type JSON = [number, string, boolean, ...number]";
        final JsonDsProcessorTestDriver processor = new JsonDsProcessorTestDriver();
        processor.process(source, "<string buffer>");
        final String name = "com/github/tasogare/json/ds/datatype/resources/mixed.json";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        try (final BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            final JsonStructure json = Json.createReader(r).read();
            final JsonMetaObjectTestDriver typeSystem = processor.getMetaObjects();
            final Type jsonType = typeSystem.getMetaObject("JSON");

            assertTrue(typeSystem.is(json, jsonType));
        }
    }

    @Test
    public void test3() throws IOException, JsonDsException {
        final String source = "use standard; type JSON = [...*!]";
        final JsonDsProcessorTestDriver processor = new JsonDsProcessorTestDriver();
        processor.process(source, "<string buffer>");
        try (final BufferedReader r = new BufferedReader(new StringReader("[null]"))) {
            final JsonStructure json = Json.createReader(r).read();
            final JsonMetaObjectTestDriver typeSystem = processor.getMetaObjects();
            final Type jsonType = typeSystem.getMetaObject("JSON");

            assertFalse(typeSystem.is(json, jsonType));
        }
    }

    @Test
    public void testInvalid() throws IOException, JsonDsException {
        final String source = "use standard; type JSON = {\"first\": string, \"last\": string, \"age\": number}";
        final JsonDsProcessorTestDriver processor = new JsonDsProcessorTestDriver();
        processor.process(source, "<string-buffer>");

        final String name = "com/github/tasogare/json/ds/datatype/resources/Person.json";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        try(final BufferedReader r2 = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final JsonMetaObjectTestDriver typeSystem = processor.getMetaObjects();

            final Type jsonType = typeSystem.getMetaObject("JSON");
            final JsonStructure json = Json.createReader(r2).read();

            assertFalse(typeSystem.is(json, jsonType));
        }
    }

    @Test
    public void testValid() throws IOException, JsonDsException {
        final String source = "use standard; type JSON = {\"first\": string?, \"last\": string, \"age\": number}";
        final JsonDsProcessorTestDriver processor = new JsonDsProcessorTestDriver();
        processor.process(source, "<string-buffer>");

        final String name = "com/github/tasogare/json/ds/datatype/resources/Person.json";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        try(final BufferedReader r2 = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final JsonMetaObjectTestDriver typeSystem = processor.getMetaObjects();

            final Type jsonType = typeSystem.getMetaObject("JSON");
            final JsonStructure json = Json.createReader(r2).read();

            assertTrue(typeSystem.is(json, jsonType));
        }
    }

    @Test
    public void testLayerForLayerWithColor() throws IOException, JsonDsException {
        final long old = System.nanoTime();

        final String jsdsFile = "com/github/tasogare/json/ds/datatype/resources/layer/layer.js";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(jsdsFile);
        try (BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            final JsonDsProcessorTestDriver processor = new JsonDsProcessorTestDriver();
            processor.process(r, jsdsFile);

            final JsonMetaObjectTestDriver typeSystem = processor.getMetaObjects();
            final String jsonFile = "com/github/tasogare/json/ds/datatype/resources/layer/LayerForLayerWithColor.json";
            final InputStream is2 = getClass().getClassLoader().getResourceAsStream(jsonFile);
            try (final JsonReader jsonReader = Json.createReader(new InputStreamReader(is2, StandardCharsets.UTF_8))) {
                final JsonStructure json = jsonReader.read();
                final Type jsonType = typeSystem.getMetaObject("JSON");

                assertTrue(typeSystem.is(json, jsonType));
                System.out.println("LayerForLayerWithColor: " + (System.nanoTime() - old) + " ns");
            }
        }
    }

    @Test
    public void testLayerWithLinearGradient()  throws IOException, JsonDsException {
        final long old = System.nanoTime();

        final String jsdsFile = "com/github/tasogare/json/ds/datatype/resources/layer/layer.js";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(jsdsFile);
        try (final BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            final JsonDsProcessorTestDriver processor = new JsonDsProcessorTestDriver();
            processor.process(r, jsdsFile);

            final JsonMetaObjectTestDriver typeSystem = processor.getMetaObjects();
            final String jsonFile = "com/github/tasogare/json/ds/datatype/resources/layer/LayerWithLinearGradient.json";
            final InputStream is2 = getClass().getClassLoader().getResourceAsStream(jsonFile);
            try (final JsonReader jsonReader = Json.createReader(new InputStreamReader(is2, StandardCharsets.UTF_8))) {
                final JsonStructure json = jsonReader.read();
                final Type jsonType = typeSystem.getMetaObject("JSON");

                assertTrue(typeSystem.is(json, jsonType));
                System.out.println("LayerWithLinearGradient: " + (System.nanoTime() - old) + " ns");
            }
        }
    }

    @Test
    public void testLayerWithRadialGradient()  throws IOException, JsonDsException {
        final long old = System.nanoTime();

        final String jsdsFile = "com/github/tasogare/json/ds/datatype/resources/layer/layer.js";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(jsdsFile);
        try (final BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            final JsonDsProcessorTestDriver processor = new JsonDsProcessorTestDriver();
            processor.process(r, jsdsFile);

            final JsonMetaObjectTestDriver typeSystem = processor.getMetaObjects();
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
}
