/*
 * Copyright 2010-2015 JetBrains s.r.o.
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

package org.jetbrains.kotlin.js.test.semantics;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.js.JavaScript;
import org.jetbrains.kotlin.js.config.EcmaVersion;
import org.jetbrains.kotlin.js.test.SingleFileTranslationTest;

import java.io.File;
import java.util.List;

public final class NativeInteropTest extends SingleFileTranslationTest {

    @NotNull
    private static final String NATIVE = "native/";

    public NativeInteropTest() {
        super(NATIVE);
    }

    @NotNull
    @Override
    protected List<String> additionalJsFiles(@NotNull EcmaVersion ecmaVersion) {
        List<String> result = Lists.newArrayList(super.additionalJsFiles(ecmaVersion));

        String jsFilePath = pathToTestDir() + NATIVE + "/" + getTestName(true) + JavaScript.DOT_EXTENSION;
        File jsFile = new File(jsFilePath);
        if (jsFile.exists() && jsFile.isFile()) {
            result.add(jsFilePath);
        }

        return result;
    }

    public void testSimple() throws Exception {
        fooBoxTest();
    }

    public void testInheritanceFromNativeClass() throws Exception {
        checkFooBoxIsOk();
    }

    public void testInheritanceFromNativeTrait() throws Exception {
        checkFooBoxIsOk();
    }

    public void testClass() throws Exception {
        fooBoxTest();
    }

    public void testVararg() throws Exception {
        checkFooBoxIsOk();
    }

    public void testKt1519() throws Exception {
        fooBoxTest();
    }

    public void testClassObject() throws Exception {
        fooBoxTest();
    }

    public void testSimpleUndefined() throws Exception {
        fooBoxTest();
    }

    public void testLibrary() throws Exception {
        fooBoxTest();
    }

    public void testKt2209() throws Exception {
        fooBoxTest();
    }

    public void testKt2323() throws Exception {
        fooBoxTest();
    }

    public void testNativePropertyWithCustomName() throws Exception {
        checkFooBoxIsOk();
    }

    public void testNativeExtensionLikeMember() throws Exception {
        checkFooBoxIsOk();
    }

    public void testPassExtLambdaToNative() throws Exception {
        checkFooBoxIsOk();
    }

    public void testPassMemberOrExtToNative() throws Exception {
        checkFooBoxIsOk();
    }

    public void testPassExtLambdaFromNative() throws Exception {
        checkFooBoxIsOk();
    }

    public void testPassMemberOrExtFromNative() throws Exception {
        checkFooBoxIsOk();
    }

    public void testPassTopLevelOrLocalFunctionToNative() throws Exception {
        checkFooBoxIsOk();
    }

    public void testPassTopLevelFunctionFromNative() throws Exception {
        checkFooBoxIsOk();
    }

    public void testNestedElements() throws Exception {
        checkFooBoxIsOk();
    }

    public void testPrint() throws Exception {
        checkFooBoxIsOk();
    }

    public void testNativeInvoke() throws Exception {
        checkFooBoxIsOk();
    }

    public void testNativeGetterAndNativeSetter() throws Exception {
        checkFooBoxIsOk();
    }

    public void testEval() throws Exception {
        checkFooBoxIsOk();
    }

    public void testUndefined() throws Exception {
        checkFooBoxIsOk();
    }

    public void testTypeof() throws Exception {
        checkFooBoxIsOk();
    }

    public void testSecondaryConstructor() throws Exception {
        checkFooBoxIsOk();
    }
}
