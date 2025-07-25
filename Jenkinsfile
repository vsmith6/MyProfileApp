pipeline {
    agent any

    environment {
        JAVA_HOME   = '/usr/lib/jvm/java-17-openjdk-amd64'
        MAVEN_OPTS  = '-Xmx1024m'
    }

    stages {
        stage('Clone') {
            steps {
                // ✅ Secure GitHub access via SSH and Jenkins credentials
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[
                        url: 'git@github.com:vsmith6/MyProfileApp.git',
                        credentialsId: 'aac201d4-bb2e-4dbc-b862-2c1101a55e09' // 🔐 Your SSH credential ID from Jenkins
                    ]]
                ])
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
                    while (parts.size() < 3) { parts << 0 }
                    parts[2]++ // Patch bump for non-main

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
                    // 🎯 Read current project version
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

                    // 🔧 Apply bumped version to pom.xml
                    sh "./mvnw versions:set -DnewVersion=${newVersion}"
                    sh "./mvnw versions:commit"

                    // 📝 Commit version change
                    sh """
                        git config user.name 'Jenkins CI'
                        git config user.email 'ci@jenkins'
                        git add pom.xml
                        git commit -m 'Release v${newVersion}'
                    """

                    // 🧪 Check for tag existence
                    def tagExists = sh(
                        returnStatus: true,
                        script: "git rev-parse -q --verify refs/tags/v${newVersion}"
                    ) == 0

                    // 🏷️ Tag and push if not already tagged
                    if (!tagExists) {
                        sh "git tag -a v${newVersion} -m 'Release v${newVersion}'"
                        sh "git push origin v${newVersion}"
                    } else {
                        echo "⚠️ Tag v${newVersion} already exists, skipping tag creation"
                    }

                    // 🚀 Push changes to main branch
                    sh "git push origin HEAD:main"
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