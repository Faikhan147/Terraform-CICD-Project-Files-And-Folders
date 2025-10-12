import jenkins.model.*
import jenkins.install.InstallState

Jenkins.instance.setInstallState(InstallState.INITIAL_SETUP_COMPLETED)

def plugins = [
    "git-parameter",          
    "github-oauth",           
    "pipeline-github",        
    "generic-webhook-trigger",
    "git-push",               
    "sonar",                  
    "slack",
    "configuration-as-code",
    "job-dsl",
    "aws-credentials",
    "aws-global-configuration",
    "workflow-aggregator",
    "pipeline-aws",
    "pipeline-stage-view",
    "blueocean"
]

def jenkinsInstance = jenkins.model.Jenkins.getInstance()
def pluginManager = jenkinsInstance.getPluginManager()
def updateCenter = jenkinsInstance.getUpdateCenter()

plugins.each {
    def plugin = pluginManager.getPlugin(it)
    if (!plugin) {
        def pluginToInstall = updateCenter.getPlugin(it)
        if (pluginToInstall) {
            pluginToInstall.deploy()
            println("Plugin \${it} has been installed.")
        } else {
            println("Plugin \${it} not found.")
        }
    } else {
        println("Plugin \${it} is already installed.")
    }
}
jenkinsInstance.save()