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
        val (gitUrl, task) = line.split(',').map { it.trim() }.take(2)
        val (owner, project) = gitUrl.split('/').takeLast(2)

        val id = "$owner/$project"
        println("Starting $id")

        "git clone --depth 1 $gitUrl repos/$id"()
        "gradle-profiler --benchmark --project-dir repos/$id --output-dir data/$id $task"()
        "rm -rf repos/$id"()

        println("${index + 1}/${lines.size}: $id is done")
    }
    println("It's all done. Please commit and push when everything looks ok")
}