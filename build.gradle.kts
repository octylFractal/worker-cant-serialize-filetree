abstract class SimpleWorkerAction : WorkAction<SimpleWorkerParameters> {
    override fun execute() {
        println(parameters.fileTree.files.joinToString("\n"))
    }
}

interface SimpleWorkerParameters : WorkParameters {
    val fileTree: ConfigurableFileTree
}

abstract class SimpleWorkerTask : DefaultTask() {
    @get:javax.inject.Inject
    abstract val workerExecutor: WorkerExecutor

    @org.gradle.api.tasks.TaskAction
    fun action() {
        val workQueue = workerExecutor.noIsolation()

        workQueue.submit(SimpleWorkerAction::class) {
            fileTree.from(project.projectDir.resolve("gradle"))
        }
    }
}

val simpleWorker by tasks.registering(SimpleWorkerTask::class)
