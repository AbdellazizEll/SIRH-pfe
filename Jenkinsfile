pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials') // Assurez-vous d'avoir ce credential configuré
        DOCKERHUB_REPO = 'abdellazizell' // Remplacez par votre nom d'utilisateur Docker Hub
    }

    tools {
        maven 'Maven' // Utilisez le nom configuré dans Jenkins pour Maven
        // Assurez-vous que "Maven" correspond au "Name" défini dans la configuration globale des outils
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Backend') {
            steps {
                dir('sirh-backend') { // Assurez-vous que le chemin est correct
                    bat 'mvn clean package' // Utilisez 'sh' si vous êtes sur Linux/macOS
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('sirh-frontend') { // Assurez-vous que le chemin est correct
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
                    docker build -t ${backendImage} -f sirh-backend/Dockerfile sirh-backend
                    docker build -t ${frontendImage} -f sirh-frontend/Dockerfile sirh-frontend
                    """ // Utilisez 'sh' si vous êtes sur Linux/macOS
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                script {
                    bat 'docker login -u %DOCKERHUB_CREDENTIALS_USR% -p %DOCKERHUB_CREDENTIALS_PSW%'
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
