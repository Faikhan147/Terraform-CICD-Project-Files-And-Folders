import jenkins.model.*
import javaposse.jobdsl.plugin.*
import javaposse.jobdsl.dsl.*
import javaposse.jobdsl.plugin.JenkinsJobManagement
import javaposse.jobdsl.dsl.DslScriptLoader

def jobFiles = [
    "Frontend-Website": "frontend-website-pipeline.groovy",
    "Frontend-App"    : "frontend-app-pipeline.groovy",
    "Backend-App"    : "backend-app-pipeline.groovy",
    "Backend-Jar-App"    : "backend-jar-app-pipeline.groovy",
    "Backend-War-App"    : "backend-war-app-pipeline.groovy",
    "Slack-Notifier"  : "slack-pipeline.groovy"
]

def localScriptPath = "/var/lib/jenkins/dsl_scripts"
def s3BasePath = "s3://terraform-backend-all-environments/Jenkins-Pipelines/pipeline-jobs"

jobFiles.each { jobName, fileName ->
    def s3ScriptPath = "${s3BasePath}/${fileName}"
    def localFilePath = "${localScriptPath}/${fileName}"

    println "Downloading ${fileName} from S3..."

    def downloadCmd = "aws s3 cp ${s3ScriptPath} ${localFilePath}"
    def proc = downloadCmd.execute()
    proc.waitFor()

    println "stdout: ${proc.in.text}"
    println "stderr: ${proc.err.text}"

    if (proc.exitValue() == 0) {
        println "✅ ${fileName} downloaded from S3 to ${localFilePath}"

        def pipelineScript = new File(localFilePath).text
        def jobDslScript = ""

        if (jobName == "Slack-Notifier") {
            jobDslScript = """
                pipelineJob('${jobName}') {
                    definition {
                        cps {
                            script('''${pipelineScript}''')
                            sandbox(true)
                        }
                    }
                }
            """
        } else {
            jobDslScript = pipelineScript
        }

        def jobManagement = new JenkinsJobManagement(System.out, [:], new File('.'))
        def dslScriptLoader = new DslScriptLoader(jobManagement)
        dslScriptLoader.runScript(jobDslScript)

        println "✅ Jenkins job '${jobName}' created successfully!"
    } else {
        println "❌ Failed to download ${fileName} from S3"
    }
}


println "🎯 All jobs processed!"
