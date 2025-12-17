pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK25'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'refs/heads/*',
                    url: 'https://github.com/lvallone3315/WordlePrep.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Deploy') {
            steps {
                echo "Deploying JAR..."
                sh 'nohup java -jar target/*.jar > app.log 2>&1 &'
            }
        }
    }
}