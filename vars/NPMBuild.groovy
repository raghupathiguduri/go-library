def call(mode = "") {
    if(!params.BRANCH) {
        error "Branch parameter is missing"
    }
    def skipCI = utils.validateCommitMsg()
    if(skipCI) {
        echo "*********************** Skip CI found in Commig msg *************************"
        return
    }    
    sh script: "npm install -g yarn && \
        yarn install"
    if(mode != "prod") {
        sh script: "yarn build"
    } else {
        sh script: "yarn build --production=true"
        sh script: "ls && tar -cf build.tar build"
        stash name: "build", includes: "build.tar"
        stash name: "Dockerfile", includes: "Dockerfile"    
    }
}
