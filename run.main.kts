#!/usr/bin/env kotlin

@file:Repository("https://dl.bintray.com/jakubriegel/kotlin-shell")
@file:DependsOn("eu.jrie.jetbrains:kotlin-shell-core:0.2.1")
@file:DependsOn("org.slf4j:slf4j-simple:1.7.28")
@file:CompilerOptions("-Xopt-in=kotlin.RequiresOptIn")
@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

import eu.jrie.jetbrains.kotlinshell.shell.*

shell {
    val nameOfMachine = args[0]
    val waitTime = args[1].toLong()

    suspend fun run(file: String, jdkHome: String) {
        export("JAVA_HOME" to jdkHome)

        val projects = this.file(file)
        val lines = projects.readLines().drop(1)
        lines.forEachIndexed { index, line ->
            val row = line.split(',').map { it.trim() }.take(3)
            val gitUrl = row[0]
            val commit = row[1]
            val task = row[2]
            val (owner, projectGit) = gitUrl.split('/').takeLast(2)
            val project = projectGit.split('.')[0]

            val id = "$owner/$project/$task"
            println("Starting $id")

            "git clone $gitUrl repos/$id"()
            cd("repos/$id")
            "git checkout $commit"()
            repeat(4) { cd(up) }

            "gradle-profiler --benchmark --project-dir repos/$id --output-dir data/$nameOfMachine/$id $task"()
            "rm -rf repos/$id"()

            println("${index + 1}/${lines.size}: $id is done.")
            val isLast = (index + 1) == lines.size
            if (!isLast) {
                println("Next one will start in about ${waitTime/1000} seconds / ${waitTime/1000/60} minutes.")
                Thread.sleep(waitTime)
            }
        }
    }

    run("java11.csv", "/usr/lib/jvm/java-11-openjdk-amd64")
    run("java8.csv", "/usr/lib/jvm/java-8-openjdk-amd64")

    println("It's all done. Please commit and push when everything looks ok")
}