pipeline {
    agent any

    environment {
        STUDENT    = 'Akerke Tairabek'
        GROUP      = 'IT2-2307'
        IMAGE_NAME = 'tairabek-akerke-student-api'
        IMAGE_TAG  = 'v1.0-tairabek-akerke'
    }

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    triggers {
        // Polling fallback: checks GitHub every ~2 minutes (use a webhook if Jenkins is public)
        pollSCM('H/2 * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                echo "=== Pipeline of student: ${STUDENT} (${GROUP}) ==="
                checkout scm
                sh 'git log -1 --oneline'
            }
        }

        stage('Build') {
            steps {
                echo "Building Spring Boot app for ${STUDENT}"
                sh 'chmod +x gradlew'
                sh './gradlew clean bootJar -x test --no-daemon'
            }
        }

        stage('Test') {
            steps {
                echo "Running tests for ${STUDENT}"
                sh './gradlew test --no-daemon'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'build/test-results/test/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo "Building image ${IMAGE_NAME}:${IMAGE_TAG}"
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -t ${IMAGE_NAME}:build-${BUILD_NUMBER} ."
            }
        }

        stage('Verify') {
            steps {
                sh "docker image inspect ${IMAGE_NAME}:${IMAGE_TAG} --format 'Image OK: {{.RepoTags}} size={{.Size}} created={{.Created}}'"
                sh "docker images ${IMAGE_NAME}"
            }
        }
    }

    post {
        success { echo "SUCCESS: pipeline of ${STUDENT} finished, image ${IMAGE_NAME}:${IMAGE_TAG} is ready" }
        failure { echo "FAILURE: pipeline of ${STUDENT} failed - check the stage logs above" }
    }
}
