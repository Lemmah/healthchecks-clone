#!groovy
// This deployment script assumes that there is only a single Jenkins server (master) and there are no agents.

node {
    // It's often recommended to run a django project from a virtual environment.
    // This way you can manage all of your depedencies without affecting the rest of your system.
    stage("Ensure that python environment is set up.") {
        sh '''
           sudo apt-get install python-virtualenv
           sudo apt-get install python3-pip 
           sudo apt-get install python3-venv
           '''
    }
    stage("Install Python Virtual Enviroment") {
        sh 'python3 -m venv .'
    }  
    
    // The stage below is attempting to get the latest version of our application code.
    stage ("Get Latest Code") {
        git url: 'https://github.com/Lemmah/healthchecks-clone.git'
    }
    
    // Then we install our requirements
    stage ("Install Application Dependencies") {
        sh '''
            ls
            . bin/activate
            sudo pip install -r requirements.txt
            deactivate
            '''
    }
    
    // Typically, django recommends that all the static assets such as images and css are to be collected to a single folder and
    // served separately outside the django application via apache or a CDN. This command will gather up all the static assets and
    // ready them for deployment.
    stage ("Collect Static files") {
        sh '''
            . bin/activate
            python -V
            sudo pip install django==1.11.6 --upgrade
            ./manage.py collectstatic --noinput
            deactivate
            '''
    }
  
    // After all of the dependencies are installed, we now start running our tests.
    stage ("Run Unit/Integration Tests") {
        def testsError = null
        try {
            sh '''
                . ../bin/activate
                ./manage.py test
                deactivate
               '''
        }
        catch(err) {
            testsError = err
            currentBuild.result = 'FAILURE'
        }
        finally {
            junit 'reports/junit.xml'

            if (testsError) {
                throw testsError
            }
        }
    }


    stage("Create Artifact") {

    }
}

node {
    stage("Deploy Artifact") {

    }
}

node {
    stage("Run End-To-End Tests") {

    }
}