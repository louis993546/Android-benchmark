#!/usr/bin/env kotlin

@file:Repository("https://dl.bintray.com/jakubriegel/kotlin-shell")
@file:DependsOn("eu.jrie.jetbrains:kotlin-shell-core:0.2.1")
@file:DependsOn("org.slf4j:slf4j-simple:1.7.28")
@file:CompilerOptions("-Xopt-in=kotlin.RequiresOptIn")
@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

import eu.jrie.jetbrains.kotlinshell.shell.*

shell {
    val projects = this.file("projects.csv")
    val lines = projects.readLines().drop(1)
    lines.forEachIndexed { index, line ->
        val (gitUrl, task) = line.split(',').map { line.trim() }
        val (owner, project) = gitUrl.split('/').takeLast(2)

        "git clone --depth 1 $gitUrl repos/$owner/$project"()
        "gradle-profiler --benchmark --project-dir repos/$owner/$project --output-dir data/$owner/$project $task"()
        "rm -rf repos/$owner/$project"()
        "git add data"()
        "git commit -m \"Benchmark $gitUrl\""()

        println("$index/${lines.size}: $owner/$project is done")
    }
    println("It's all done. Please review the commit and push when everything looks ok")
}