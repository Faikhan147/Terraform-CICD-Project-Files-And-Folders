pipelineJob('Frontend-Website') {
    description('This job builds the Real-Time Frontend Website on GitHub push.')

    parameters {
        stringParam('BRANCH', 'main', 'Branch to build')
        stringParam('SONAR_PROJECT_KEY', 'Website', 'SonarQube Project Key')
        stringParam('SONAR_PROJECT_NAME', 'Frontend-Website', 'SonarQube Project Name')
        stringParam('REPO_URL', 'https://github.com/Faikhan147/Real-Time-Frontend-Website-Repo.git', 'Git repo URL')
        choiceParam('ENVIRONMENT', ['qa', 'staging', 'prod'], 'Select the environment to deploy')
    }

    properties {
        githubProjectUrl('https://github.com/Faikhan147/Real-Time-Frontend-Website-Repo')
    }

    triggers {
        githubPush()
    }

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/Faikhan147/Real-Time-Frontend-Website-Repo.git')
                        credentials('github-credentials')
                    }
                    branches('*/main')
                }
            }
            scriptPath('Jenkinsfile')
        }
    }
}
