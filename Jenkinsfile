pipeline {
    agent any

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk' // Update if needed
        MAVEN_OPTS = '-Xmx1024m'
    }

    stages {
        stage('Clone Repository') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                sh './mvnw clean verify -Dspring.profiles.active=test'
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        // Optional deployment
        // stage('Deploy') {
        //     steps {
        //         sh './mvnw spring-boot:run -Dspring.profiles.active=s3'
        //     }
        // }
    }
}