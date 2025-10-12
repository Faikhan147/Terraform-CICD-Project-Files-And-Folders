pipelineJob('Backend-App') {
    description('This job builds the Real-Time Backend App on GitHub push.')

    parameters {
        stringParam('BRANCH', 'main', 'Branch to build')
        stringParam('SONAR_PROJECT_KEY', 'Database', 'SonarQube Project Key')
        stringParam('SONAR_PROJECT_NAME', 'Backend-Database', 'SonarQube Project Name')
        stringParam('REPO_URL', 'https://github.com/Faikhan147/Real-Time-Backend-App-Repo.git', 'Git repo URL')
        choiceParam('ENVIRONMENT', ['qa', 'staging', 'prod'], 'Select the environment to deploy')
    }

    properties {
        githubProjectUrl('https://github.com/Faikhan147/Real-Time-Backend-App-Repo')
    }

    triggers {
        githubPush()
    }

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/Faikhan147/Real-Time-Backend-App-Repo.git')
                        credentials('github-credentials')
                    }
                    branches('*/main')
                }
            }
            scriptPath('Jenkinsfile')
        }
    }
}
