pipeline {
    agent any

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk'
        MAVEN_OPTS = '-Xmx1024m'
        BUILD_TAG = "v1.0.${env.BUILD_NUMBER}"
    }

    stages {
        stage('Clone') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'main') {
                        sh './mvnw clean verify -Dspring.profiles.active=prod'
                    } else if (env.BRANCH_NAME == 'develop') {
                        sh './mvnw clean verify -Dspring.profiles.active=test'
                    } else {
                        sh './mvnw clean verify -Dspring.profiles.active=dev'
                    }
                }
            }
        }

        stage('Tag Release') {
            when {
                branch 'main'
            }
            steps {
                script {
                    sh "git config user.name 'Jenkins CI'"
                    sh "git config user.email 'ci@jenkins'"
                    sh "git tag -a ${BUILD_TAG} -m 'Build from ${env.BRANCH_NAME}: ${BUILD_TAG}'"
                    sh "git push origin ${BUILD_TAG}"
                }
            }
        }

        stage('Archive JAR') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        success {
            echo "✅ Build complete for branch ${env.BRANCH_NAME}"
        }
        failure {
            echo "❌ Build failed for branch ${env.BRANCH_NAME}"
        }
    }
}