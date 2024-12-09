pipeline {
    agent any

    environment {
        // DockerHub Credentials
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials') // Jenkins DockerHub credentials
        DOCKERHUB_REPO = 'abdellazizell' // DockerHub repo username

        // SMTP Credentials
        SMTP_SMARTHOST = 'sandbox.smtp.mailtrap.io:2525'
        SMTP_FROM = 'alertmanager@example.com'
        SMTP_AUTH_USERNAME = credentials('smtp-username') // Jenkins SMTP Username
        SMTP_AUTH_PASSWORD = credentials('smtp-password') // Jenkins SMTP Password
    }

    tools {
        maven 'Maven' // Ensure Maven is configured in Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Applications') {
            parallel {
                stage('Build Backend') {
                    steps {
                        dir('sirh-backend') {
                            echo 'Building Backend...'
                            bat 'mvn clean package'
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
            }
        }

        stage('Test Backend') {
            steps {
                dir('sirh-backend') {
                    echo 'Testing Backend...'
                    bat 'mvn test'
                }
            }
            post {
                failure {
                    error('Backend tests failed! Stopping the pipeline.')
                }
            }
        }

        stage('Build Docker Images') {
            parallel {
                stage('Backend Docker Image') {
                    steps {
                        script {
                            env.backendImage = "${DOCKERHUB_REPO}/backend:${env.BUILD_NUMBER}"
                            echo "Building Backend Docker Image: ${env.backendImage}"
                            bat "docker build -t ${backendImage} -f sirh-backend/Dockerfile sirh-backend"
                        }
                    }
                }
                stage('Frontend Docker Image') {
                    steps {
                        script {
                            env.frontendImage = "${DOCKERHUB_REPO}/frontend:${env.BUILD_NUMBER}"
                            echo "Building Frontend Docker Image: ${env.frontendImage}"
                            bat "docker build -t ${frontendImage} -f sirh-frontend/Dockerfile sirh-frontend"
                        }
                    }
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                script {
                    echo 'Logging into Docker Hub...'
                    bat "docker login -u ${DOCKERHUB_CREDENTIALS_USR} -p ${DOCKERHUB_CREDENTIALS_PSW}"

                    echo "Pushing Backend Image: ${env.backendImage}"
                    bat "docker push ${backendImage}"

                    echo "Pushing Frontend Image: ${env.frontendImage}"
                    bat "docker push ${frontendImage}"
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
                    set SMTP_SMARTHOST=${env.SMTP_SMARTHOST}
                    set SMTP_FROM=${env.SMTP_FROM}
                    set SMTP_AUTH_USERNAME=${env.SMTP_AUTH_USERNAME}
                    set SMTP_AUTH_PASSWORD=${env.SMTP_AUTH_PASSWORD}
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
