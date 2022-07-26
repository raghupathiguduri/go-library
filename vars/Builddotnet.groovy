def call(String dotnetcmd) 
{
    if(!params.BRANCH) 
    {
        error "Branch parameter is missing"
    }
	try 
		export PATH=/usr/local/share/dotnet:$PATH
    {	
		sh "dotnet ${dotnetcmd}"
    }
    catch (Exception ex) 
    {
        throw ex
    }
}
