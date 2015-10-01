package com.github.tasogare.json.ds;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tasogare.json.ds.datatype.driver.JsonMetaObjectTestDriver;
import com.github.tasogare.json.ds.tests.AllTest;

public class TranslationUnitTest {

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

    @SuppressWarnings("unchecked")
    @Test
    public void testCorrectTranslationUnit()
        throws IOException, NullPointerException, ReflectiveOperationException, SecurityException,
        IllegalArgumentException
    {
        final String str = "com/github/tasogare/json/ds/resources/testCorrectTranslationUnit.jsds";
        final URL name = getClass().getClassLoader().getResource(str);
        try (final BufferedReader r = AllTest.newReader(name)) {
            final TranslationUnit<JsonMetaObjectTestDriver> unit = new TranslationUnit<>(r, name);
            final Optional<JsonMetaObjectTestDriver> cx = unit.getContext(new JsonMetaObjectTestDriver(),
                                                                          AllTest::reportError, AllTest::reportError);

            final JsonMetaObjectTestDriver metaObjects = cx.orElseThrow(AssertionError::new);
            final Set<String> keys = metaObjects.registerKeys();
            assertNotNull(keys);
            assertThat(keys.size(), is(8));
            assertThat(keys, allOf(hasItem("string"), hasItem("number"), hasItem("boolean"), hasItem("null"),
                                   hasItem("*"), hasItem("*!"), hasItem("JSON"), hasItem("Person")));
        }
    }

    @Test
    public void testBadTranslationUnit()
        throws IOException, NullPointerException, ReflectiveOperationException, SecurityException,
        IllegalArgumentException
    {
        final String str = "com/github/tasogare/json/ds/resources/testBadTranslationUnit.jsds";
        final URL name = getClass().getClassLoader().getResource(str);
        try (final BufferedReader r = AllTest.newReader(name)) {
            final TranslationUnit<JsonMetaObjectTestDriver> unit = new TranslationUnit<>(r, name);
            final Optional<JsonMetaObjectTestDriver> cx = unit.getContext(new JsonMetaObjectTestDriver(),
                                                                          AllTest::reportError, AllTest::reportError);
            assertThat(cx.isPresent(), is(false));
        }
    }
}
