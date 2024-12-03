pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials') // Assurez-vous d'avoir ce credential configuré
        DOCKERHUB_REPO = 'votre-nom-utilisateur-dockerhub' // Remplacez par votre nom d'utilisateur Docker Hub
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Backend') {
            steps {
                dir('backend') {
                    bat 'mvn clean package' // Utilisez 'sh' si vous êtes sur Linux/macOS
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    bat '''
                    npm install --legacy-peer-deps
                    npm run build
                    ''' // Utilisez 'sh' si vous êtes sur Linux/macOS
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    def backendImage = "${DOCKERHUB_REPO}/backend:${env.BUILD_NUMBER}"
                    def frontendImage = "${DOCKERHUB_REPO}/frontend:${env.BUILD_NUMBER}"

                    bat """
                    docker build -t ${backendImage} -f backend/Dockerfile backend
                    docker build -t ${frontendImage} -f frontend/Dockerfile frontend
                    """ // Utilisez 'sh' si vous êtes sur Linux/macOS
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                script {
                    bat 'docker login -u %DOCKERHUB_CREDENTIALS_USR% -p %DOCKERHUB_CREDENTIALS_PSW%' // Utilisez 'sh' pour les systèmes Unix
                    bat """
                    docker push ${backendImage}
                    docker push ${frontendImage}
                    """ // Utilisez 'sh' si vous êtes sur Linux/macOS
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline terminé.'
        }
        success {
            echo 'Build et push réussis!'
        }
        failure {
            echo 'Build ou push échoué.'
        }
    }
}
