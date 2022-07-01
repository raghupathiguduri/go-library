def call() {
    if(!params.BRANCH) {
        error "Branch parameter is missing"
    }
    if(params.BRANCH != "develop") {
        error "Docker build will be executed only on develop branch"
    }
    def skipCI = utils.validateCommitMsg()
    if(skipCI) {
        echo "*********************** Skip CI found in Commig msg *************************"
        return
    }    
    sh "rm -rf *"
    unstash name: "build"
    unstash name: "Dockerfile"
    unstash name: "imageTag"
    unstash name: "workflowProperties"
    def dockerImage = sh script: "cat image.txt", returnStdout: true 
    println "Docker image: " + dockerImage

    sh "tar -xf build.tar && cp Dockerfile build && ls build"
    sh "cd build && docker build . -t ${dockerImage}"
    sh "docker push ${dockerImage}"
}
