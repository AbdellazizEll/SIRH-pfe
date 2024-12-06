pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials') // Ensure this credential is set up in Jenkins
        DOCKERHUB_REPO = 'abdellazizell' // Replace with your Docker Hub username
    }

    tools {
        maven 'Maven' // Ensure 'Maven' is configured in Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Backend') {
            steps {
                dir('sirh-backend') {
                    echo 'Building Backend...'
                    bat 'mvn clean package'
                }
            }
        }

        stage('Test Backend') {
            steps {
                dir('sirh-backend') {
                    echo 'Testing Backend...'
                    bat 'mvn test'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('sirh-frontend') {
                    echo 'Building Frontend...'
                    bat '''
                    npm install --legacy-peer-deps
                    npm run build
                    '''
                }
            }
        }

        // Optional: If you have frontend tests, you could add a Test Frontend stage here.

        stage('Build Docker Images') {
            steps {
                script {
                    // Define image tags
                    env.backendImage = "${DOCKERHUB_REPO}/backend:${env.BUILD_NUMBER}"
                    env.frontendImage = "${DOCKERHUB_REPO}/frontend:${env.BUILD_NUMBER}"

                    echo "Building Backend Docker Image: ${env.backendImage}"
                    bat "docker build -t ${backendImage} -f sirh-backend/Dockerfile sirh-backend"

                    echo "Building Frontend Docker Image: ${env.frontendImage}"
                    bat "docker build -t ${frontendImage} -f sirh-frontend/Dockerfile sirh-frontend"
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                script {
                    echo 'Logging into Docker Hub...'
                    bat 'docker login -u %DOCKERHUB_CREDENTIALS_USR% -p %DOCKERHUB_CREDENTIALS_PSW%'

                    echo "Pushing Backend Image: ${env.backendImage}"
                    bat "docker push ${env.backendImage}"

                    echo "Pushing Frontend Image: ${env.frontendImage}"
                    bat "docker push ${env.frontendImage}"
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    echo 'Deploying with Docker Compose...'
                    bat """
                    set DOCKERHUB_REPO=${env.DOCKERHUB_REPO}
                    set BUILD_NUMBER=${env.BUILD_NUMBER}
                    docker-compose pull
                    docker-compose up -d
                    """
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline terminé.'
        }
        success {
            echo 'Build, test, et push réussis!'
        }
        failure {
            echo 'Build, test, ou push échoué.'
        }
    }
}
