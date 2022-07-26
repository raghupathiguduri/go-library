def call(mode = "") {
	if(!params.BRANCH) {
		error "Branch parameter is missing"
	}
	def ApprunTests = utils.loadPipelineProps("runTests")
	def Appartifact_name = utils.loadPipelineProps("appName")
	def AppVersion = utils.loadPipelineProps("version")
	def AppPublish = utils.loadPipelineProps("publish")
	if(AppPublish != false ) {
	sh "dotnet publish"
	}
	steps.sh "mkdir ${Appartifact_name}-${AppVersion}"
	if(ApprunTests != false ) { 
	sh "dotnet test"
	sh "dotnet build --output ${Appartifact_name}-${AppVersion}"
	}
	else {
	sh "dotnet build --output ${Appartifact_name}-${AppVersion}"
	}
	steps.sh "echo zipping artifact"
	steps.sh "zip -r '${Appartifact_name}-${AppVersion}'.zip '${Appartifact_name}-${AppVersion}'"
}
