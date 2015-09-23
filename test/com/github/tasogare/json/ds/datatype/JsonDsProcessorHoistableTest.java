// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.github.tasogare.json.ds.tests.AllTest.newReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonStructure;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tasogare.json.ds.JsonDsException;
import com.github.tasogare.json.ds.datatype.driver.JsonDsProcessorHoistableTestDriver;
import com.github.tasogare.json.ds.datatype.driver.JsonMetaObjectTestDriver;

public class JsonDsProcessorHoistableTest {

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
        final String name = "com/github/tasogare/json/ds/datatype/resources/layer/layerHoisted.jsds";
        final URL url = getClass().getClassLoader().getResource(name);
        try (BufferedReader r = newReader(url)) {
            final JsonDsProcessorHoistableTestDriver processor = new JsonDsProcessorHoistableTestDriver();
            processor.process(r, url);
            final Type jsonType = processor.getMetaObjects().getMetaObject("JSON");
            System.out.println(jsonType);
        }
    }

    @Test
    public void test2() throws IOException, JsonDsException {
        final String source = "use standard; type JSON = [number, string, boolean, ...number]";
        final JsonDsProcessorHoistableTestDriver processor = new JsonDsProcessorHoistableTestDriver();
        processor.process(source, null);

        final String name = "com/github/tasogare/json/ds/datatype/resources/mixed.json";
        try(final JsonReader r = Json.createReader(newReader(name, getClass()))){
            final JsonStructure json = r.read();
            final JsonMetaObjectTestDriver typeSystem = processor.getMetaObjects();
            final Type jsonType = typeSystem.getMetaObject("JSON");

            assertTrue(typeSystem.is(json, jsonType));
        }
    }

    @Test
    public void testInvalid() throws IOException, JsonDsException {
        final String source = "use standard; type JSON = {\"first\": string, \"last\": string, \"age\": number}";
        final JsonDsProcessorHoistableTestDriver processor = new JsonDsProcessorHoistableTestDriver();
        processor.process(source, null);

        final String name = "com/github/tasogare/json/ds/datatype/resources/Person.json";
        try(final JsonReader r = Json.createReader(newReader(name, getClass()))){
            final JsonStructure json = r.read();
            final JsonMetaObjectTestDriver typeSystem = processor.getMetaObjects();
            final Type jsonType = typeSystem.getMetaObject("JSON");

            assertFalse(typeSystem.is(json, jsonType));
        }
    }

    @Test
    public void testValid() throws IOException, JsonDsException {
        final String source = "use standard; type JSON = {\"first\": string?, \"last\": string, \"age\": number}";
        final JsonDsProcessorHoistableTestDriver processor = new JsonDsProcessorHoistableTestDriver();
        processor.process(source, null);

        final String name = "com/github/tasogare/json/ds/datatype/resources/Person.json";
        try(final JsonReader r = Json.createReader(newReader(name, getClass()))){
            final JsonStructure json = r.read();
            final JsonMetaObjectTestDriver typeSystem = processor.getMetaObjects();
            final Type jsonType = typeSystem.getMetaObject("JSON");

            assertTrue(typeSystem.is(json, jsonType));
        }
    }

    @Test
    public void testLayerForLayerWithColor() throws IOException, JsonDsException {
        final long old = System.nanoTime();

        final String jsdsFile = "com/github/tasogare/json/ds/datatype/resources/layer/layer.jsds";
        final URL url = getClass().getClassLoader().getResource(jsdsFile);
        try (final BufferedReader r = newReader(url)) {
            final JsonDsProcessorHoistableTestDriver processor = new JsonDsProcessorHoistableTestDriver();
            processor.process(r, url);

            final String jsonFile = "com/github/tasogare/json/ds/datatype/resources/layer/LayerForLayerWithColor.json";
            try (final JsonReader r2 = Json.createReader(newReader(jsonFile, getClass()))) {
                final JsonStructure json = r2.read();
                final JsonMetaObjectTestDriver typeSystem = processor.getMetaObjects();
                final Type jsonType = typeSystem.getMetaObject("JSON");

                assertTrue(typeSystem.is(json, jsonType));
            }
        }

        System.out.println("LayerForLayerWithColor: " + (System.nanoTime() - old) + " ns");
    }

    @Test
    public void testLayerWithLinearGradient() throws IOException, JsonDsException {
        final long old = System.nanoTime();

        final String jsdsFile = "com/github/tasogare/json/ds/datatype/resources/layer/layer.jsds";
        final URL url = getClass().getClassLoader().getResource(jsdsFile);
        try (final BufferedReader r = newReader(url)) {
            final JsonDsProcessorHoistableTestDriver processor = new JsonDsProcessorHoistableTestDriver();
            processor.process(r, url);

            final String jsonFile = "com/github/tasogare/json/ds/datatype/resources/layer/LayerWithLinearGradient.json";
            try (final JsonReader r2 = Json.createReader(newReader(jsonFile, getClass()))) {
                final JsonStructure json = r2.read();
                final JsonMetaObjectTestDriver typeSystem = processor.getMetaObjects();
                final Type jsonType = typeSystem.getMetaObject("JSON");

                assertTrue(typeSystem.is(json, jsonType));
            }
        }

        System.out.println("LayerWithLinearGradient: " + (System.nanoTime() - old) + " ns");
    }

    @Test
    public void testLayerWithRadialGradient() throws IOException, JsonDsException {
        final long old = System.nanoTime();

        final String jsdsFile = "com/github/tasogare/json/ds/datatype/resources/layer/layer.jsds";
        final URL url = getClass().getClassLoader().getResource(jsdsFile);
        try (final BufferedReader r = newReader(url)) {
            final JsonDsProcessorHoistableTestDriver processor = new JsonDsProcessorHoistableTestDriver();
            processor.process(r, url);

            final String jsonFile = "com/github/tasogare/json/ds/datatype/resources/layer/LayerWithRadialGradient.json";
            try (final JsonReader r2 = Json.createReader(newReader(jsonFile, getClass()))) {
                final JsonStructure json = r2.read();
                final JsonMetaObjectTestDriver typeSystem = processor.getMetaObjects();
                final Type jsonType = typeSystem.getMetaObject("JSON");

                assertTrue(typeSystem.is(json, jsonType));
            }
        }

        System.out.println("LayerWithRadialGradient: " + (System.nanoTime() - old) + " ns");
    }

    @Test
    public void testLayerForLayerWithColorHoisted() throws IOException, JsonDsException {
        final long old = System.nanoTime();

        final String jsdsFile = "com/github/tasogare/json/ds/datatype/resources/layer/layerHoisted.jsds";
        final URL url = getClass().getClassLoader().getResource(jsdsFile);
        try (final BufferedReader r = newReader(url)) {
            final JsonDsProcessorHoistableTestDriver processor = new JsonDsProcessorHoistableTestDriver();
            processor.process(r, url);

            final String jsonFile = "com/github/tasogare/json/ds/datatype/resources/layer/LayerForLayerWithColor.json";
            try (final JsonReader r2 = Json.createReader(newReader(jsonFile, getClass()))) {
                final JsonStructure json = r2.read();
                final JsonMetaObjectTestDriver typeSystem = processor.getMetaObjects();
                final Type jsonType = typeSystem.getMetaObject("JSON");

                assertTrue(typeSystem.is(json, jsonType));
            }
        }

        System.out.println("LayerForLayerWithColorHoisted: " + (System.nanoTime() - old) + " ns");
    }

    @Test
    public void testLayerWithLinearGradientHoisted() throws IOException, JsonDsException {
        final long old = System.nanoTime();

        final String jsdsFile = "com/github/tasogare/json/ds/datatype/resources/layer/layerHoisted.jsds";
        final URL url = getClass().getClassLoader().getResource(jsdsFile);
        try (final BufferedReader r = newReader(url)) {
            final JsonDsProcessorHoistableTestDriver processor = new JsonDsProcessorHoistableTestDriver();
            processor.process(r, url);

            final String jsonFile = "com/github/tasogare/json/ds/datatype/resources/layer/LayerWithLinearGradient.json";
            try (final JsonReader r2 = Json.createReader(newReader(jsonFile, getClass()))) {
                final JsonStructure json = r2.read();
                final JsonMetaObjectTestDriver typeSystem = processor.getMetaObjects();
                final Type jsonType = typeSystem.getMetaObject("JSON");

                assertTrue(typeSystem.is(json, jsonType));
            }
        }

        System.out.println("LayerWithLinearGradientHoisted: " + (System.nanoTime() - old) + " ns");
    }

    @Test
    public void testLayerWithRadialGradientHoisted() throws IOException, JsonDsException {
        final long old = System.nanoTime();

        final String jsdsFile = "com/github/tasogare/json/ds/datatype/resources/layer/layerHoisted.jsds";
        final URL url = getClass().getClassLoader().getResource(jsdsFile);
        try (final BufferedReader r = newReader(url)) {
            final JsonDsProcessorHoistableTestDriver processor = new JsonDsProcessorHoistableTestDriver();
            processor.process(r, url);

            final String jsonFile = "com/github/tasogare/json/ds/datatype/resources/layer/LayerWithRadialGradient.json";
            try (final JsonReader r2 = Json.createReader(newReader(jsonFile, getClass()))) {
                final JsonStructure json = r2.read();
                final JsonMetaObjectTestDriver typeSystem = processor.getMetaObjects();
                final Type jsonType = typeSystem.getMetaObject("JSON");

                assertTrue(typeSystem.is(json, jsonType));
            }
        }

        System.out.println("LayerWithRadialGradientHoisted: " + (System.nanoTime() - old) + " ns");
    }

    @Test
    public void testLayerIncluded()  throws IOException, JsonDsException {
        final long old = System.nanoTime();

        final String jsdsFile = "com/github/tasogare/json/ds/datatype/resources/layer/include/layerHoistedSplit.jsds";
        final URL url = getClass().getClassLoader().getResource(jsdsFile);
        try (final BufferedReader r = newReader(url)) {
            final JsonDsProcessorHoistableTestDriver processor = new JsonDsProcessorHoistableTestDriver();
            processor.process(r, url);
        }

        System.out.println("LayerHoisted + including test: " + (System.nanoTime() - old) + " ns");
    }

}
