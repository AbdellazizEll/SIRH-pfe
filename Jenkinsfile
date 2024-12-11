// pipeline {
//     agent any
//
//     environment {
//         DOCKERHUB_REPO = 'abdellazizell' // DockerHub repo username
//         SMTP_SMARTHOST = 'sandbox.smtp.mailtrap.io:2525'
//         SMTP_FROM = 'alertmanager@example.com'
//     }
//
//     tools {
//         maven 'Maven' // Ensure Maven is configured in Jenkins
//     }
//
//     stages {
//         stage('Checkout') {
//             steps {
//                 checkout scm
//             }
//         }
//
//         stage('Build Applications') {
//             parallel {
//                 stage('Build Backend') {
//                     steps {
//                         dir('sirh-backend') {
//                             echo 'Building Backend...'
//                             bat 'mvn clean package'
//                         }
//                     }
//                 }
//                 stage('Build Frontend') {
//                     steps {
//                         dir('sirh-frontend') {
//                             echo 'Building Frontend...'
//                             bat '''
//                             npm install --legacy-peer-deps
//                             npm run build
//                             '''
//                         }
//                     }
//                 }
//             }
//         }
//
//         stage('Test Backend') {
//             steps {
//                 dir('sirh-backend') {
//                     echo 'Testing Backend...'
//                     bat 'mvn test'
//                 }
//             }
//             post {
//                 failure {
//                     error('Backend tests failed! Stopping the pipeline.')
//                 }
//             }
//         }
//
//         stage('Build Docker Images') {
//             parallel {
//                 stage('Backend Docker Image') {
//                     steps {
//                         script {
//                             env.backendImage = "${DOCKERHUB_REPO}/backend:${env.BUILD_NUMBER}"
//                             echo "Building Backend Docker Image: ${env.backendImage}"
//                             bat "docker build -t ${backendImage} -f sirh-backend/Dockerfile sirh-backend"
//                         }
//                     }
//                 }
//                 stage('Frontend Docker Image') {
//                     steps {
//                         script {
//                             env.frontendImage = "${DOCKERHUB_REPO}/frontend:${env.BUILD_NUMBER}"
//                             echo "Building Frontend Docker Image: ${env.frontendImage}"
//                             bat "docker build -t ${frontendImage} -f sirh-frontend/Dockerfile sirh-frontend"
//                         }
//                     }
//                 }
//             }
//         }
//
//         stage('Push Docker Images') {
//             steps {
//                 script {
//                     withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKERHUB_USR', passwordVariable: 'DOCKERHUB_PSW')]) {
//                         echo 'Logging into Docker Hub...'
//                         bat "docker login -u ${DOCKERHUB_USR} -p ${DOCKERHUB_PSW}"
//
//                         echo "Pushing Backend Image: ${env.backendImage}"
//                         bat "docker push ${env.backendImage}"
//
//                         echo "Pushing Frontend Image: ${env.frontendImage}"
//                         bat "docker push ${env.frontendImage}"
//                     }
//                 }
//             }
//         }
//
//         stage('Deploy') {
//             steps {
//                 script {
//                     withCredentials([
//                         string(credentialsId: 'smtp-username', variable: 'SMTP_AUTH_USERNAME'),
//                         string(credentialsId: 'smtp-password', variable: 'SMTP_AUTH_PASSWORD')
//                     ]) {
//                         echo 'Deploying with Docker Compose...'
//                         bat """
//                         set DOCKERHUB_REPO=${env.DOCKERHUB_REPO}
//                         set BUILD_NUMBER=${env.BUILD_NUMBER}
//                         set SMTP_SMARTHOST=${env.SMTP_SMARTHOST}
//                         set SMTP_FROM=${env.SMTP_FROM}
//                         set SMTP_AUTH_USERNAME=%SMTP_AUTH_USERNAME%
//                         set SMTP_AUTH_PASSWORD=%SMTP_AUTH_PASSWORD%
//                         set SPRING_MAIL_HOST=%SMTP_SMARTHOST%
//                         set SPRING_MAIL_PORT=2525
//                         set SPRING_MAIL_USERNAME=%SMTP_AUTH_USERNAME%
//                         set SPRING_MAIL_PASSWORD=%SMTP_AUTH_PASSWORD%
//                         set SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH=true
//                         set SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE=true
//                         docker-compose pull
//                         docker-compose up -d
//                         """
//                     }
//                 }
//             }
//         }
//
//         stage('Verify Monitoring') {
//             steps {
//                 script {
//                     // Wait a few seconds for services to come up
//                     sleep time: 10, unit: 'SECONDS'
//
//                     echo 'Verifying Prometheus availability...'
//                     // Check Prometheus health endpoint
//                     bat 'curl --fail http://localhost:9090/-/healthy'
//                 }
//             }
//             post {
//                 success {
//                     echo 'Prometheus is up and healthy!'
//                 }
//                 failure {
//                     error('Failed to verify Prometheus. Check logs and configuration.')
//                 }
//             }
//         }
//
//         stage('Verify Grafana') {
//             steps {
//                 script {
//                     sleep time: 15, unit: 'SECONDS'
//                     bat 'curl --fail http://localhost:3001/login'
//                 }
//             }
//             post {
//                 success {
//                     echo 'Grafana is reachable!'
//                 }
//                 failure {
//                     error('Failed to verify Grafana. Check logs and configuration.')
//                 }
//             }
//         }
//     }
//
//     post {
//         always {
//             echo 'Pipeline terminé.'
//         }
//         success {
//             echo 'Build, test, push, and deployment succeeded!'
//         }
//         failure {
//             echo 'Build, test, push, or deployment failed.'
//         }
//     }
// }

pipeline {
    agent any

    environment {
        DOCKERHUB_REPO = 'abdellazizell' // Your Docker Hub username/repository
        SMTP_SMARTHOST = 'sandbox.smtp.mailtrap.io:2525'
        SMTP_FROM = 'alertmanager@example.com'
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

        stage('Build & Test') {
            parallel {
                stage('Build Backend') {
                    steps {
                        dir('sirh-backend') {
                            echo 'Building and Testing Backend...'
                            bat 'mvn clean package'
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
            }
        }

        stage('Build Docker Images') {
            parallel {
                stage('Backend Image') {
                    steps {
                        script {
                            env.backendImage = "${DOCKERHUB_REPO}/backend:${env.BUILD_NUMBER}"
                            echo "Building Backend Docker Image: ${env.backendImage}"
                            bat "docker build -t ${backendImage} -f sirh-backend/Dockerfile sirh-backend"
                        }
                    }
                }
                stage('Frontend Image') {
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
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKERHUB_USR', passwordVariable: 'DOCKERHUB_PSW')]) {
                        echo 'Logging into Docker Hub...'
                        bat "docker login -u %DOCKERHUB_USR% -p %DOCKERHUB_PSW%"

                        echo "Pushing Backend Image: ${env.backendImage}"
                        bat "docker push ${env.backendImage}"

                        echo "Pushing Frontend Image: ${env.frontendImage}"
                        bat "docker push ${env.frontendImage}"
                    }
                }
            }
        }

        stage('Deploy to Staging') {
            steps {
                script {
                    echo "Deploying to Staging environment..."
                    bat """
                    docker-compose -f docker-compose.staging.yml pull
                    docker-compose -f docker-compose.staging.yml up -d
                    """
                }
            }
        }

        stage('Verify Staging') {
            steps {
                script {
                    echo "Verifying staging environment..."
                    // Adjust the endpoint/port according to your staging setup
                    bat 'curl --fail http://localhost:8086/actuator/health'
                }
            }
            post {
                success {
                    echo 'Staging environment is up and healthy!'
                }
                failure {
                    error('Failed to verify staging environment.')
                }
            }
        }

        stage('Approval for Production') {
            steps {
                input message: 'Deploy to Production?', ok: 'Yes'
            }
        }

        stage('Deploy to Production') {
            steps {
                script {
                    echo "Deploying to Production environment..."
                    bat """
                    docker-compose -f docker-compose.prod.yml pull
                    docker-compose -f docker-compose.prod.yml up -d
                    """
                }
            }
        }

        stage('Verify Monitoring (Production)') {
            steps {
                script {
                    echo 'Verifying Prometheus in Production...'
                    // Wait for services to start
                    sleep time: 10, unit: 'SECONDS'
                    // Check Prometheus in production (adjust if different port)
                    bat 'curl --fail http://localhost:9090/-/healthy'

                    echo 'Verifying Grafana in Production...'
                    sleep time: 10, unit: 'SECONDS'
                    // Check Grafana login page (adjust if different port)
                    bat 'curl --fail http://localhost:3000/login'
                }
            }
            post {
                success {
                    echo 'Prometheus and Grafana are reachable in Production!'
                }
                failure {
                    error('Failed to verify Monitoring in Production.')
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline ended.'
        }
        success {
            echo 'All steps succeeded! Code is built, tested, and deployed to production.'
        }
        failure {
            echo 'Something failed. Check the logs for details.'
        }
    }
}
