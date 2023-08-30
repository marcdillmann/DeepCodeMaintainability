/* 
 * JSweet - http://www.jsweet.org
 * Copyright (C) 2015 CINCHEO SAS <renaud.pawlak@cincheo.fr>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jsweet.test.transpiler;

import java.io.File;

import org.jsweet.transpiler.ModuleKind;
import org.jsweet.transpiler.SourceFile;
import org.junit.Test;

import source.candies.Angular;
import source.candies.Babylonjs;
import source.candies.BackboneCandy;
import source.candies.ExpressLib;
import source.candies.GlobalsImport;
import source.candies.JQuery;
import source.candies.Mixins;
import source.candies.QualifiedNames;
import source.candies.ReactLib;
import source.candies.SocketIOLib;
import source.candies.Threejs;

public class CandiesTests extends AbstractTest {

	@Test
	public void testGlobalsImport() {
		transpile(ModuleKind.commonjs, logHandler -> {
			logHandler.assertNoProblems();
		}, getSourceFile(GlobalsImport.class));
	}

	@Test
	public void testQualifiedNames() {
		transpile(ModuleKind.commonjs, TestTranspilationHandler::assertNoProblems, getSourceFile(QualifiedNames.class));
	}

	@Test
	public void testAngular() {
		transpile(TestTranspilationHandler::assertNoProblems, getSourceFile(Angular.class));
	}

	@Test
	public void testJQuery() {
		transpile(TestTranspilationHandler::assertNoProblems, getSourceFile(JQuery.class));
	}

	@Test
	public void testBackbone() {
		transpile(TestTranspilationHandler::assertNoProblems, getSourceFile(BackboneCandy.class));
	}

	@Test
	public void testExpressLib() {
		transpile(ModuleKind.commonjs, TestTranspilationHandler::assertNoProblems, getSourceFile(ExpressLib.class));
	}

	@Test
	public void testThreejs() {
		transpile(TestTranspilationHandler::assertNoProblems, getSourceFile(Threejs.class));
	}

	@Test
	public void testReactLib() {
		transpile(ModuleKind.none, TestTranspilationHandler::assertNoProblems, getSourceFile(ReactLib.class));
	}

	@Test
	public void testSocketIOLib() {
		transpile(ModuleKind.commonjs, TestTranspilationHandler::assertNoProblems, getSourceFile(SocketIOLib.class));
	}

	@Test
	public void testBabylonjs() {
		transpile(TestTranspilationHandler::assertNoProblems, getSourceFile(Babylonjs.class), new SourceFile(new File(TEST_DIRECTORY_NAME + "/source/candies/module_defs.java")));
	}

	@Test
	public void testMixins() {
		transpile(TestTranspilationHandler::assertNoProblems, getSourceFile(Mixins.class));
	}

}
