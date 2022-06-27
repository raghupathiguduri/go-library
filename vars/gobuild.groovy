def call(mode = "") {
    goTool = tool type: 'go' , name: 'go-1.9.2'
    String goPath = sh(returnStdout: true, script: 'pwd').trim()
    steps.sh "echo ${goPath}"
    println "[INFO] GOPATH : $goPath"
    String outputFolder = "${goPath}/bin"
    sh "mkdir -p ${outputFolder}"
    def outputs = [[
                                   OS: 'darwin',
                                   architecture: 'amd64',
                                   postfix: '-darwin'
                           ],  [
                                   OS: 'windows',
                                   architecture: 'amd64',
                                   postfix: '.exe'
                           ],  [
                                   OS: 'linux',
                                   architecture: 'amd64',
                                   postfix: '-linux'
                           ]]
    for(output in outputs) {
                String file = "${outputFolder}/${output.OS}${output.architecture}"
                String OS = "${output.OS}"
                String architecture = "${output.architecture}"
                println "[INFO] OS : ${OS}"
                println "[INFO] Architecture : ${architecture}"
        steps.withEnv(["GOROOT=${goTool}", "PATH+GO=${goTool}/bin" ]) {
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
