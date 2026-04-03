package com.vellebue.churiservicesv2.articles.architecture

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import com.tngtech.archunit.lang.ArchRule

@AnalyzeClasses(packages = ["com.vellebue.churiservicesv2.articles"])
class CleanArchitectureTest {

    @ArchTest
    val layerDependenciesAreRespected: ArchRule = layeredArchitecture()
        .consideringAllDependencies()
        .withOptionalLayers(true)
        .layer("Domain").definedBy("..domain..")
        .layer("Application").definedBy("..application..")
        .layer("Infrastructure").definedBy("..infrastructure..")
        .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure")
        .whereLayer("Application").mayOnlyBeAccessedByLayers("Infrastructure")
        .whereLayer("Infrastructure").mayNotBeAccessedByAnyLayer()

    @ArchTest
    val domainMustNotDependOnOtherLayers: ArchRule = noClasses()
        .that().resideInAPackage("..domain..")
        .should().dependOnClassesThat().resideInAPackage("..application..")
        .orShould().dependOnClassesThat().resideInAPackage("..infrastructure..")
        .allowEmptyShould(true)
}
