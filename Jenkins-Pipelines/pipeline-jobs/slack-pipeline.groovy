pipeline {
    agent any
    parameters {
        string(name: 'STATUS', defaultValue: '', description: 'Deployment status message')
        string(name: 'ENV', defaultValue: '', description: 'Environment')
    }
    environment {
        SLACK_WEBHOOK_URL = credentials('slack-webhook')
    }
    stages {
        stage('Send Slack Notification') {
            steps {
                script {
                    def message = "${params.STATUS} for *${params.ENV}*"
                    sh """
                        curl -X POST -H 'Content-type: application/json' \
                        --data '{"text":"${message}"}' \
                        '${SLACK_WEBHOOK_URL}'
                    """
                }
            }
        }
    }
}
