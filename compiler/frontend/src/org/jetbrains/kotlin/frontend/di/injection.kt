/*
 * Copyright 2010-2016 JetBrains s.r.o.
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

package org.jetbrains.kotlin.frontend.di

import org.jetbrains.kotlin.config.LanguageFeatureSettings
import org.jetbrains.kotlin.container.*
import org.jetbrains.kotlin.context.LazyResolveToken
import org.jetbrains.kotlin.context.ModuleContext
import org.jetbrains.kotlin.extensions.StorageComponentContainerContributor
import org.jetbrains.kotlin.incremental.components.LookupTracker
import org.jetbrains.kotlin.resolve.*
import org.jetbrains.kotlin.resolve.lazy.*
import org.jetbrains.kotlin.resolve.lazy.declarations.DeclarationProviderFactory
import org.jetbrains.kotlin.types.expressions.DeclarationScopeProviderForLocalClassifierAnalyzer
import org.jetbrains.kotlin.types.expressions.LocalClassDescriptorHolder
import org.jetbrains.kotlin.types.expressions.LocalLazyDeclarationResolver

fun StorageComponentContainer.configureModule(
        moduleContext: ModuleContext, platform: TargetPlatform
) {
    useInstance(moduleContext)
    useInstance(moduleContext.module)
    useInstance(moduleContext.project)
    useInstance(moduleContext.storageManager)
    useInstance(moduleContext.builtIns)
    useInstance(moduleContext.platformToKotlinClassMap)

    useInstance(platform)

    platform.platformConfigurator.configure(this)

    for (extension in StorageComponentContainerContributor.getInstances(moduleContext.project)) {
        extension.addDeclarations(this, platform)
    }

    configurePlatformIndependentComponents()
}

private fun StorageComponentContainer.configurePlatformIndependentComponents() {
    useImpl<SupertypeLoopCheckerImpl>()
}

fun StorageComponentContainer.configureModule(
        moduleContext: ModuleContext, platform: TargetPlatform, trace: BindingTrace
) {
    configureModule(moduleContext, platform)
    useInstance(trace)
}

fun createContainerForBodyResolve(
        moduleContext: ModuleContext,
        bindingTrace: BindingTrace,
        platform: TargetPlatform,
        statementFilter: StatementFilter,
        languageFeatureSettings: LanguageFeatureSettings
): StorageComponentContainer = createContainer("BodyResolve") {
    configureModule(moduleContext, platform, bindingTrace)

    useInstance(statementFilter)

    useInstance(LookupTracker.DO_NOTHING)
    useInstance(BodyResolveCache.ThrowException)
    useInstance(languageFeatureSettings)

    useImpl<BodyResolver>()
}

fun createContainerForLazyBodyResolve(
        moduleContext: ModuleContext,
        kotlinCodeAnalyzer: KotlinCodeAnalyzer,
        bindingTrace: BindingTrace,
        platform: TargetPlatform,
        bodyResolveCache: BodyResolveCache,
        languageFeatureSettings: LanguageFeatureSettings
): StorageComponentContainer = createContainer("LazyBodyResolve") {
    configureModule(moduleContext, platform, bindingTrace)

    useInstance(LookupTracker.DO_NOTHING)
    useInstance(kotlinCodeAnalyzer)
    useInstance(kotlinCodeAnalyzer.fileScopeProvider)
    useInstance(bodyResolveCache)
    useInstance(languageFeatureSettings)
    useImpl<LazyTopDownAnalyzerForTopLevel>()
    useImpl<BasicAbsentDescriptorHandler>()
}

fun createContainerForLazyLocalClassifierAnalyzer(
        moduleContext: ModuleContext,
        bindingTrace: BindingTrace,
        platform: TargetPlatform,
        lookupTracker: LookupTracker,
        languageFeatureSettings: LanguageFeatureSettings,
        localClassDescriptorHolder: LocalClassDescriptorHolder
): StorageComponentContainer = createContainer("LocalClassifierAnalyzer") {
    configureModule(moduleContext, platform, bindingTrace)

    useInstance(localClassDescriptorHolder)
    useInstance(lookupTracker)

    useImpl<LazyTopDownAnalyzer>()

    useInstance(NoTopLevelDescriptorProvider)

    CompilerEnvironment.configure(this)

    useInstance(FileScopeProvider.ThrowException)

    useImpl<DeclarationScopeProviderForLocalClassifierAnalyzer>()
    useImpl<LocalLazyDeclarationResolver>()

    useInstance(languageFeatureSettings)
}

fun createContainerForLazyResolve(
        moduleContext: ModuleContext,
        declarationProviderFactory: DeclarationProviderFactory,
        bindingTrace: BindingTrace,
        platform: TargetPlatform,
        targetEnvironment: TargetEnvironment,
        languageFeatureSettings: LanguageFeatureSettings
): StorageComponentContainer = createContainer("LazyResolve") {
    configureModule(moduleContext, platform, bindingTrace)

    useInstance(declarationProviderFactory)
    useInstance(LookupTracker.DO_NOTHING)
    useInstance(languageFeatureSettings)

    useImpl<FileScopeProviderImpl>()
    targetEnvironment.configure(this)

    useImpl<LazyResolveToken>()
    useImpl<ResolveSession>()
}

@JvmOverloads
fun createLazyResolveSession(
        moduleContext: ModuleContext,
        declarationProviderFactory: DeclarationProviderFactory,
        bindingTrace: BindingTrace,
        platform: TargetPlatform,
        languageFeatureSettings: LanguageFeatureSettings,
        targetEnvironment: TargetEnvironment = CompilerEnvironment
): ResolveSession = createContainerForLazyResolve(
        moduleContext, declarationProviderFactory, bindingTrace, platform, targetEnvironment, languageFeatureSettings
).get<ResolveSession>()
