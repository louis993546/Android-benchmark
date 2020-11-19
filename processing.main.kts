#!/usr/bin/env kotlin

@file:Repository("https://dl.bintray.com/jakubriegel/kotlin-shell")
@file:DependsOn("eu.jrie.jetbrains:kotlin-shell-core:0.2.1")
@file:DependsOn("org.slf4j:slf4j-simple:1.7.28")
@file:CompilerOptions("-Xopt-in=kotlin.RequiresOptIn")
@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

import eu.jrie.jetbrains.kotlinshell.shell.*
import java.nio.file.Files

shell {
    val outputFile = this.file("data/output.csv")
    outputFile.appendText("Project, Task, Scenario, Version, Task, Value, Warm-up build #1, Warm-up build #2, Warm-up build #3, Warm-up build #4, Warm-up build #5, Warm-up build #6, Measured build #1, Measured build #2, Measured build #3, Measured build #4, Measured build #5, Measured build #6, Measured build #7, Measured build #8, Measured build #9, Measured build #10\n")

    val dataDir = this.file("data")
    Files.find(dataDir.toPath(), 999, { p, bfa -> bfa.isRegularFile && p.fileName.toString() == "benchmark.csv" })
        .forEach {
            val file = it.toFile()
            val test = listOf(it.parent.parent.fileName.toString(), it.parent.fileName.toString())
            val result = file.readLines().map { it.split(',').takeLast(1) }.flatten()
            val output = (test + result).joinToString(",") + "\n"
            outputFile.appendText(output)
        }
}