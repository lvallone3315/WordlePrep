pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
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

                // Copy JAR to deployment folder - note changed owner of /opt/wordle-app to jenkins
                sh 'cp target/WordlePrep-1.0-SNAPSHOT.jar /opt/wordle-app/WordlePrep.jar'

                // Stop currently running app (ignore errors if not running)
                // sh 'pkill -f WordlePrep.jar || true'

                // Start the new app in background and log output
                // sh 'nohup java -jar /opt/wordle-app/WordlePrep.jar --server.port=8081 > /opt/wordle-app/wordle.log 2>&1 &'
            }
        }
    }
}