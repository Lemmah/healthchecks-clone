#!groovy
// This deployment script assumes that there is only a single Jenkins server (master) and there are no agents.

node {

    // It's often recommended to run a django project from a virtual environment.
    // This way you can manage all of your depedencies without affecting the rest of your system.
    stage("Setup Project Env (venv)") {
        sh '''
            chmod 777 setup-scripts/*
            ./setup-scripts/setup-env.sh
            '''
    }  
    
    // The stage below is attempting to get the latest version of our application code.
    stage ("Get Latest Code") {
        git url: 'https://github.com/Lemmah/healthchecks-clone.git'
    }
    
    // Then we install our requirements
    stage ("Install Application Dependencies") {
        sh '''
            ./setup-scripts/setup-project.sh
            '''
    }
    
    // Typically, django recommends that all the static assets such as images and css are to be collected to a single folder and
    // served separately outside the django application via apache or a CDN. This command will gather up all the static assets and
    // ready them for deployment.
    stage ("Setup Project database") {
        sh '''
            ./setup-scripts/setup-db.sh
            '''
    }
  
    // After all of the dependencies are installed, we now start running our tests.
    stage ("Run Unit/Integration Tests") {
        def testsError = null
        try {
            sh '''
                cd ~/healthchecksapp/healthchecks-clone
                ./manage.py test
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