/*
 * Copyright 2010-2012 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.jet.codegen;

import org.jetbrains.jet.ConfigurationKind;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Stepan Koltsov
 * @author alex.tkachman
 */
public class EnumGenTest extends CodegenTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createEnvironmentWithMockJdkAndIdeaAnnotations(ConfigurationKind.JDK_ONLY);
    }

    public void testSuperclassIsEnum() throws NoSuchFieldException, IllegalAccessException {
        loadFile("enum/simple.kt");
        Class season = loadImplementationClass(generateClassesInFile(), "Season");
        assertEquals("java.lang.Enum", season.getSuperclass().getName());
    }

    public void testEnumClassModifiers() throws NoSuchFieldException, IllegalAccessException {
        loadFile("enum/simple.kt");
        Class season = loadImplementationClass(generateClassesInFile(), "Season");
        int modifiers = season.getModifiers();
        assertTrue((modifiers & 0x4000) != 0); // ACC_ENUM
        assertTrue((modifiers & Modifier.FINAL) != 0);
    }

    public void testEnumFieldModifiers() throws NoSuchFieldException, IllegalAccessException {
        loadFile("enum/simple.kt");
        Class season = loadImplementationClass(generateClassesInFile(), "Season");
        Field summer = season.getField("SUMMER");
        int modifiers = summer.getModifiers();
        assertTrue((modifiers & 0x4000) != 0); // ACC_ENUM
        assertTrue((modifiers & Modifier.FINAL) != 0);
        assertTrue((modifiers & Modifier.STATIC) != 0);
        assertTrue((modifiers & Modifier.PUBLIC) != 0);
    }

    public void testEnumConstantConstructors() throws Exception {
        loadText("enum class Color(val rgb: Int) { RED: Color(0xFF0000); GREEN: Color(0x00FF00); }");
        final Class colorClass = createClassLoader(generateClassesInFile()).loadClass("Color");
        final Field redField = colorClass.getField("RED");
        final Object redValue = redField.get(null);
        final Method rgbMethod = colorClass.getMethod("getRgb");
        assertEquals(0xFF0000, rgbMethod.invoke(redValue));
    }

    public void testSimple() throws NoSuchFieldException, IllegalAccessException {
        blackBoxFile("enum/simple.kt");
    }

    public void testSimpleEnumInPackage() throws NoSuchFieldException, IllegalAccessException {
        blackBoxFile("enum/inPackage.kt");
    }

    public void testAsReturnExpression() {
        blackBoxFile("enum/asReturnExpression.kt");
    }

    public void testToString() {
        blackBoxFile("enum/toString.kt");
    }

    public void testName() {
        blackBoxFile("enum/name.kt");
    }

    public void testOrdinal() {
        blackBoxFile("enum/ordinal.kt");
    }

    public void testValues() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        blackBoxFile("enum/valueof.kt");
    }
}
