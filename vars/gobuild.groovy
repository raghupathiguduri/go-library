def call(mode = "") {
    if(fileExists("*.go")) {
    goTool = tool type: 'go' , name: 'go-1.9.2'
    String goPath = env.WORKSPAC
    println "[INFO] GOPATH : $goPath"
    String outputFolder = "${env.WORKSPACE}/bin"
    sh "mkdir -p ${outputFolder}"
    def outputs = [[
                                   OS: 'darwin',
                                   architecture: 'amd64',
                                   postfix: '-darwin'
                           ], [
                                   OS: 'darwin',
                                   architecture: '386',
                                   postfix: '-darwin-x86'
                           ], [
                                   OS: 'windows',
                                   architecture: 'amd64',
                                   postfix: '.exe'
                           ], [
                                   OS: 'windows',
                                   architecture: '386',
                                   postfix: '-32.exe'
                           ] , [
                                   OS: 'linux',
                                   architecture: 'amd64',
                                   postfix: '-linux'
                           ], [
                                   OS: 'linux',
                                   architecture: '386',
                                   postfix: '-linux-x86'
                           ]]
    for(output in outputs) {
                String file = "${outputFolder}/${output.OS}${output.architecture}"
                String OS = "${output.OS}"
                String architecture = "${output.architecture}"
                println "[INFO] OS : ${OS}"
                println "[INFO] Architecture : ${architecture}"
        steps.withEnv(["GOROOT=${goTool}", "PATH+GO=${goTool}/bin", "GOPATH=${goPath}"]) {
            steps.dir(goPath) {
                steps.withEnv(["GOOS=$OS", "GOARCH=$architecture"]) {
                    steps.sh "go build -o $file"
                }
            }           
            }
    }
    zipFile = "${file}.zip"
            if(fileExists(zipFile)) {
                sh "rm $zipFile"
            }
    else {
        error "there is no file with .go extension to build"
    }
}
}
