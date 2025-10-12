pipelineJob('Frontend-App') {
    description('This job builds the Real-Time Frontend App on GitHub push.')

    parameters {
        stringParam('BRANCH', 'main', 'Branch to build')
        stringParam('SONAR_PROJECT_KEY', 'App', 'SonarQube Project Key')
        stringParam('SONAR_PROJECT_NAME', 'Frontend-App', 'SonarQube Project Name')
        stringParam('REPO_URL', 'https://github.com/Faikhan147/Real-Time-Frontend-App-Repo.git', 'Git repo URL')
        choiceParam('ENVIRONMENT', ['qa', 'staging', 'prod'], 'Select the environment to deploy')
    }

    properties {
        githubProjectUrl('https://github.com/Faikhan147/Real-Time-Frontend-App-Repo')
    }

    triggers {
        githubPush()
    }

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/Faikhan147/Real-Time-Frontend-App-Repo.git')
                        credentials('github-credentials')
                    }
                    branches('*/main')
                }
            }
            scriptPath('Jenkinsfile')
        }
    }
}
