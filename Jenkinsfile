pipeline {
    agent {
        docker {
            image 'maven:3.9.5-openjdk-21'
            args '-v $HOME/.m2:/root/.m2'
        }
    }

    environment {
        SONAR_TOKEN = credentials('sonar-token')
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test (CI profile)') {
            steps {
                sh '''
                  mvn clean verify \
                    -Pci \
                    -Dspring.profiles.active=ci
                '''
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    script {
                        def sonarCmd = "mvn sonar:sonar -Pci -Dsonar.login=${env.SONAR_TOKEN}"

                        if (env.CHANGE_ID) {
                            sonarCmd += " -Dsonar.pullrequest.key=${env.CHANGE_ID} " +
                                        "-Dsonar.pullrequest.branch=${env.CHANGE_BRANCH} " +
                                        "-Dsonar.pullrequest.base=${env.CHANGE_TARGET}"
                        } else if (env.BRANCH_NAME == 'dev') {
                            sonarCmd += " -Dsonar.branch.name=dev"
                        } else {
                            echo "Skipping SonarQube analysis for branch ${env.BRANCH_NAME}"
                            sonarCmd = null
                        }

                        if (sonarCmd) {
                            sh sonarCmd
                        }
                    }
                }
            }
        }

        stage('Quality Gate') {
            when {
                anyOf {
                    expression { env.CHANGE_ID }
                    branch 'dev'
                }
            }
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        failure {
            echo "❌ CI failed — PR cannot be merged into dev"
        }
        success {
            echo "✅ CI passed — PR can be merged into dev"
        }
    }
}
