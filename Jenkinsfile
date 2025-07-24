pipeline {
    agent any

    environment {
        JAVA_HOME   = '/usr/lib/jvm/java-17-openjdk-amd64'
        MAVEN_OPTS  = '-Xmx1024m'
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
                    def profile = env.BRANCH_NAME == 'main' ? 'prod' :
                                  env.BRANCH_NAME == 'develop' ? 'test' : 'dev'

                    sh "./mvnw clean verify -Dspring.profiles.active=${profile}"
                }
            }
        }

        stage('Bump Version for Branch') {
            when {
                not {
                    branch 'main'
                }
            }
            steps {
                script {
                    def currentVersion = sh(
                        returnStdout: true,
                        script: "./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout"
                    ).trim()

                    def cleanVersion = currentVersion.replace('-SNAPSHOT', '')
                    def parts = cleanVersion.tokenize('.').collect { it.toInteger() }

                    while (parts.size() < 3) { parts << 0 } // Ensure major.minor.patch
                    parts[2]++ // Always bump patch

                    def newVersion = parts.join('.') + '-SNAPSHOT'
                    echo "🔧 Branch build using version: ${newVersion}"

                    sh "./mvnw versions:set -DnewVersion=${newVersion}"
                    sh "./mvnw versions:commit"
                }
            }
        }

        stage('Tag and Release on Main') {
            when {
                branch 'main'
            }
            steps {
                script {
                    def currentVersion = sh(
                        returnStdout: true,
                        script: "./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout"
                    ).trim()

                    def cleanVersion = currentVersion.replace('-SNAPSHOT', '')
                    def parts = cleanVersion.tokenize('.').collect { it.toInteger() }

                    while (parts.size() < 3) { parts << 0 }
                    parts[2]++ // Patch bump for release
                    def newVersion = parts.join('.')

                    echo "🏷️ Finalizing release version: v${newVersion}"

                    sh "./mvnw versions:set -DnewVersion=${newVersion}"
                    sh "./mvnw versions:commit"

                    sh """
                        git config user.name 'Jenkins CI'
                        git config user.email 'ci@jenkins'
                        git add pom.xml
                        git commit -m 'Release v${newVersion}'
                        git tag -a v${newVersion} -m 'Release v${newVersion}'
                        git push origin HEAD:main
                        git push origin v${newVersion}
                    """
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
            echo "✅ Build completed for ${env.BRANCH_NAME}"
        }
        failure {
            echo "❌ Build failed for ${env.BRANCH_NAME}"
        }
    }
}